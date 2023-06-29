package ru.ibsqa.chameleon.integration;

import ru.ibsqa.chameleon.evaluate.IEvaluateManager;
import ru.ibsqa.chameleon.integration.model.TestCase;
import ru.ibsqa.chameleon.integration.model.TestCycle;
import ru.ibsqa.chameleon.integration.model.TestProject;
import ru.ibsqa.chameleon.utils.file.FileUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Должен быть сконфигурирован как bean в xml с primary="true" определены свойства:
 * url, путь к интеграционному сервису, например "http://10.46.5.68:8080"
 * linkMeta, имя параметра, хранящего ссылку на задачи в jira, например "jira_link"
 * storiesFolder, имя папки с тестами BDD, например "target\test-classes\stories"
 * projectName, обозначение проекта, например "EASLITE"
 * cycleProperty, параметр содержащий выбранный тестовый цикл, например "jira.cycle"
 * versionProperty, параметр содержащий выбранную версию, например "jira.plugin.version"
 */
@Slf4j
public class TasktrackerAdapterJiraImpl implements ITasktrackerAdapter {

    @Autowired
    private IEvaluateManager evaluateManager;

    private RestTemplate restTemplate = new RestTemplate();

    @Getter @Setter
    private String url;

    @Getter @Setter
    private String linkMeta;

    @Getter @Setter
    private String storiesFolder;

    @Getter @Setter
    private String projectName;

    @Getter @Setter
    private String cycleProperty;

    @Getter @Setter
    private String versionProperty;

    @Autowired
    private FileUtils fileUtils;

    private final String WORKING_DIR = System.getProperty("user.dir");

    private String getStoriesPath() {
        return WORKING_DIR + "\\" + storiesFolder + "\\";
    }

    @Override
    public String getSuite() {

        final String linkMetaPattern = String.format("@%s", linkMeta);

        Optional<TestProject> testProject = Optional.ofNullable(createTestProject());
        if (testProject.isPresent() && isTestCycle()) {
            testProject = Optional.ofNullable(getTestCaseList(testProject.get()));
            if (testProject.isPresent()
                    && Optional.ofNullable(testProject.get().getTestCycle()).isPresent()
                    && Optional.ofNullable(testProject.get().getTestCycle().getTestCases()).isPresent()
                    && testProject.get().getTestCycle().getTestCases().size() > 0) {

                log.debug("test cases:");
                testProject.get().getTestCycle().getTestCases().stream().forEach(testCase -> log.debug(testCase.getName()));

                String suite = "";

                List<File> files = fileUtils.getFilesInDirectory(getStoriesPath());
                for (File file: files){
                    BufferedReader br = null;
                    try {
                        br = new BufferedReader(new FileReader(file));
                        String line = br.readLine();
                        while (line != null) {
                            if (line.contains(linkMetaPattern)){
                                for (TestCase testCase: testProject.get().getTestCycle().getTestCases()){
                                    if (line.replace(linkMetaPattern, "").replaceAll("\\s+", "").equals(testCase.getName())){
                                        if (suite.isEmpty())
                                            suite = file.getAbsolutePath().replace(getStoriesPath(), "**/");
                                        else
                                            suite = suite + "," + file.getAbsolutePath().replace(getStoriesPath(), "**/");
                                    }
                                }
                                br.close();
                                break;
                            }

                            line = br.readLine();

                        }

                    } catch (Exception e) {

                    } finally {
                        if (br != null)
                            try {
                                br.close();
                            } catch (IOException e) {
                                log.error(e.getMessage(), e);
                            }
                    }
                }


                return suite;
            }
        }
        return null;
    }

    private boolean isTestCycle(){
        return (System.getProperty(cycleProperty) != null
                && System.getProperty(cycleProperty) != null
                && !System.getProperty(cycleProperty).isEmpty()
                && !System.getProperty(versionProperty).isEmpty());
    }

    @Override
    public void changeTestCase(TaskStatus status, final List<String> attachments, String comment) {
        if (isTestCycle()) {
            try {

                String testCase = evaluateManager.evalVariable(String.format("#{%s}", linkMeta));

                Optional<List<String>> optionalAttachments = Optional.ofNullable(attachments);
                if (optionalAttachments.isPresent()) {
                    optionalAttachments.get().forEach(path ->
                            fileUploadOnServer(path)
                    );
                }

                Optional<TestProject> testProject = Optional.ofNullable(createTestProject());
                if (testProject.isPresent()) {
                    testProject.get().getTestCycle().setTestCases(Arrays.asList(
                            TestCase.builder()
                                    .name(testCase)
                                    .status(status.getValue())
                                    .attachments(optionalAttachments.orElse(new ArrayList<>()))
                                    .comment(comment)
                                    .build()));
                    restTemplate.postForObject(url + "/change", testProject.get(), TestProject.class);
                }
            } catch (HttpServerErrorException e1) {
                JiraError jiraError = JiraError.parse(e1.getResponseBodyAsString());
                fail(jiraError.getMessage());
            } catch (ResourceAccessException e2) {
                fail(e2.getMessage());
            }
        }
    }

    private TestProject createTestProject() {
        TestCycle testCycle = TestCycle.builder().name(System.getProperty(cycleProperty)).version(System.getProperty(versionProperty)).build();
        TestProject testProject = TestProject.builder().name(projectName).testCycle(testCycle).build();
        return testProject;
    }

    private TestProject getTestCaseList(TestProject testProject) {
        try {
            return restTemplate.postForObject(url + "/testlist", testProject, TestProject.class);
        } catch (HttpServerErrorException e1) {
            JiraError jiraError = JiraError.parse(e1.getResponseBodyAsString());
            fail(jiraError.getMessage());
        } catch (ResourceAccessException e2) {
            fail(e2.getMessage());
        }
        return null;
    }

    private void fileUploadOnServer(String path) {
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost(url + "/fileupload");
            FileBody fileBody = new FileBody(new File(path), ContentType.MULTIPART_FORM_DATA);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            builder.setCharset(Charset.forName("UTF-8"));
            builder.addPart("file", fileBody);
            HttpEntity entity = builder.build();

            post.setEntity(entity);
            HttpResponse response = client.execute(post);
            log.debug("\nSending 'POST' request to URL : " + url);
            log.debug("Post parameters : " + post.getEntity());
            log.debug("Response Code : " + response.getStatusLine().getStatusCode());

            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
