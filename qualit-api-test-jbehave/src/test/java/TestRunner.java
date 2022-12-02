//import ru.ibsqa.qualit.definitions.repository.IRepositoryManager;
//import ru.ibsqa.qualit.definitions.repository.api.Endpoint;
import ru.ibsqa.qualit.runners.JBehaveRunner;
//import ru.ibsqa.qualit.steps.*;
//import ru.ibsqa.qualit.storage.IVariableStorage;
//import ru.ibsqa.qualit.utils.commandpromt.CommandPromptImpl;
//import ru.ibsqa.qualit.utils.commandpromt.ICommandPrompt;
//import com.google.common.io.Resources;
import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.io.FileUtils;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.HttpClients;
//import org.junit.AfterClass;
//import org.junit.BeforeClass;
//import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.OutputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring.xml")
@TestExecutionListeners(inheritListeners = false, listeners =
        {DependencyInjectionTestExecutionListener.class})
public class TestRunner extends JBehaveRunner {

//    @Autowired
//    private IRepositoryManager repositoryManager;
//
//    @Autowired
//    private IVariableStorage variableStorage;
//
//    @Autowired
//    private ApiSteps apiSteps;
//
//    @Autowired
//    private JsonFileSteps jsonFileSteps;
//
//    @Autowired
//    private JsonMutateSteps jsonMutateSteps;
//
//    @Autowired
//    private CoreFieldSteps coreFieldSteps;
//
//    private static ICommandPrompt commandPrompt = new CommandPromptImpl();

//    @Test
//    public void restApiTest() {
//        log.info("-= Тест REST-API =-");
//
//        variableStorage.setVariable("склад", "735001");
//        log.info(repositoryManager.pickElement("Метрика", Endpoint.class).toString());
//        log.info(repositoryManager.pickElement("Начало подготовки данных", Endpoint.class).toString());
//
//        apiSteps.setBaseURI(baseUri);
//
//        apiSteps.setCurrentEndpoint("Метрика");
//        apiSteps.createRequest();
//        coreFieldSteps.fillField("Наименование метрики", "system.cpu.count" );
//        apiSteps.sendRequest();
//        apiSteps.receiveResponse();
//        coreFieldSteps.checkFieldValue("Наименование метрики", CompareOperatorEnum.EQUALS,  "system.cpu.count");
//        //coreFieldSteps.checkFieldValue("Значение метрики", "8.0");
//
//        apiSteps.setCurrentEndpoint("Начало подготовки данных");
//        apiSteps.createRequest();
//        coreFieldSteps.fillField("Наименование теста", "04_Поиск отправлений по фамилии получателя" );
//        coreFieldSteps.fillField("Индекс ОПС", "153996" );
//        apiSteps.sendRequest();
//        apiSteps.receiveResponse();
//    }

//    @Test
//    public void jsonFileTest() {
//        log.info("-= Тест JSON file =-");
//
//        String path = directory.getAbsolutePath()+"\\";
//        jsonFileSteps.openJsonFromFile("Файл конфигурации", path+"config.json", "windows-1251");
//        coreFieldSteps.fillField("Пользователь{идентификатор_db=>pgmain}", "10008" );
//        coreFieldSteps.fillField("Пароль{идентификатор_db=>pgmain}", "2000" );
//        coreFieldSteps.fillField("Пользователь{идентификатор_db=>mssqlmain}", "300" );
//        coreFieldSteps.fillField("Пароль{идентификатор_db=>mssqlmain}", "400" );
//        jsonFileSteps.saveJsonToFile(path+"config1.json", "windows-1251");
//    }

//    private static File directory;
//
//    private static String baseUri = "http://localhost:8081";

//    @BeforeClass
//    public static void prepareTest() {
//        stopServer(true);
//
//        // Create temp folder
//        directory = new File(System.getProperty("java.io.tmpdir"), "QualIT.Test");
//        if (!directory.exists()) {
//            directory.mkdirs();
//        }
//        try {
//            FileUtils.cleanDirectory(directory);
//        } catch (IOException e) {
//            log.error(e.getMessage(), e);
//        }
//        System.setProperty("test.temp.directory", directory.getAbsolutePath());
//        //directory.deleteOnExit();
//
//        copyResourceToTemp("materials/config.json", "config.json");
//        String jarFileName = copyResourceToTemp("materials/postestagent.jar", "postestagent.jar");
//
//        log.info("Start server");
//        Thread thread = commandPrompt.runCommandThread("java -jar "+jarFileName);
//        try {
//            boolean started = false;
//            while (!started) {
//                Thread.sleep(2000);
//                try {
//                    URL url = new URL(baseUri);
//                    HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
//                    urlConn.connect();
//                    if (HttpURLConnection.HTTP_OK == urlConn.getResponseCode()) {
//                        started = true;
//                    }
//                } catch (IOException e) {
//                    log.info("Waiting for server...");
//                }
//            }
//        } catch (InterruptedException e) {}
//        log.info("Server was started");
//    }

//    private static void stopServer(boolean hide) {
//
//        if (!hide) {
//            log.info("Stop server");
//        }
//
//        boolean stopped = false;
//        String shutdownUrl = baseUri+"/actuator/shutdown";
//        try {
//            HttpClient httpclient = HttpClients.createDefault();
//            HttpPost httppost = new HttpPost(shutdownUrl);
//            httpclient.execute(httppost);
//            Thread.sleep(2000);
//            stopped = true;
//        } catch (InterruptedException | IOException e) {
//            if (!hide) {
//                log.error(e.getMessage(), e);
//            }
//        }
//
//        if (stopped) {
//            log.info("Server was stopped");
//        }
//    }

//    @AfterClass
//    public static void finishTest() {
//        stopServer(false);
//    }

//    private static String copyResourceToTemp(String resourceName, String targetFileName) {
//        log.info(String.format("Copy resource [%s] to temp folder, file [%s]", resourceName, targetFileName));
//        URL url = Resources.getResource(resourceName);
//        OutputStream os = null;
//        File file = new File(directory, targetFileName);
//        try {
//            os = new FileOutputStream(file);
//            Resources.copy(url, os);
//            os.flush();
//        } catch (IOException e) {
//            log.error(e.getMessage(), e);
//        } finally {
//            if (null != os) {
//                try {
//                    os.close();
//                } catch (IOException e) {
//                    log.error(e.getMessage(), e);
//                }
//            }
//        }
//        return file.getAbsolutePath();
//    }

}
