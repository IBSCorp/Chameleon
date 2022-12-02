package ru.ibsqa.qualit.reporter;

import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jbehave.core.model.Meta;
import org.jbehave.core.model.Story;
import org.jbehave.core.reporters.NullStoryReporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ibsqa.qualit.evaluate.IEvaluateManager;
import ru.ibsqa.qualit.integration.ITasktrackerAdapter;
import ru.ibsqa.qualit.integration.TaskStatus;
import ru.ibsqa.qualit.storage.IVariableStorage;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.qameta.allure.util.ResultsUtils.*;

@Slf4j
@Component
public class AllureJBehaveReporter extends NullStoryReporter {

    @Autowired
    private IVariableStorage variableStorage;

    @Autowired
    private IEvaluateManager evaluateManager;

    @Autowired
    private IScenarioManager scenarioManager;

    @Autowired
    private ITasktrackerAdapter tasktrackerAdapter;

    private List<IReportInformer> scenarioInformers = new ArrayList<>();

    @Autowired
    private void collectScenarioInformers(List<IReportInformer> scenarioInformers) {
        this.scenarioInformers.addAll(scenarioInformers);
    }

    private static final String MD_5 = "md5";

    private final AllureLifecycle lifecycle;

    private String stepUID ;

    private final ThreadLocal<Story> parentStories = new InheritableThreadLocal<>();
    private final ThreadLocal<Story> stories = new InheritableThreadLocal<>();

    private final ThreadLocal<String> scenarios = InheritableThreadLocal.withInitial(() -> UUID.randomUUID().toString());

    private final Map<String, Status> scenarioStatusStorage = new ConcurrentHashMap<>();

    public AllureJBehaveReporter() {
        this(Allure.getLifecycle());
    }

    public AllureJBehaveReporter(final AllureLifecycle lifecycle) {
        this.lifecycle = lifecycle;
    }

    @Override
    public void beforeStory(final Story story, final boolean givenStory) {
        variableStorage.setDefaultScope(variableStorage.getRootScope().createChild());
        setStoryVariables(story);
        String storyName = story.getName().replaceAll("\\.story", "");
        story.namedAs(storyName);
        if (givenStory) {
            parentStories.set(stories.get());
        }
        stories.set(story);
    }

    @Override
    public void afterStory(final boolean givenStory) {
        if (givenStory) {
            stories.set(parentStories.get());
            parentStories.remove();
        } else {
            stories.remove();
        }
        variableStorage.setDefaultScope(variableStorage.getDefaultScope().getParent());
    }

    private void startScenarioReport() {

    }

    private void stopScenarioReport() {

    }

    @Override
    public void beforeScenario(final String title) {
        final Story story = stories.get();
        final Story parentStory = parentStories.get();
        final String uuid = scenarios.get();
        String fullTitle = title;

        if (null == parentStory) {

            //String fullName = String.format("%s: %s", story.getName(), title);
            String fullName = scenarioManager.getScenarioName(story, parentStory, title);
            log.debug("=====================" + fullName);

            String reportDescription = String.join("", scenarioInformers.stream().map(item -> item.renderBefore()).collect(Collectors.toList()));

            final TestResult result = new TestResult()
                .setUuid(uuid)
                .setName(fullTitle)
                .setFullName(fullName)
                .setStage(Stage.SCHEDULED)
                .setLabels(List.of(createStoryLabel(story.getName()), createHostLabel(), createThreadLabel()))
                .setDescription(reportDescription)
                .setHistoryId(md5(fullName));

            getLifecycle().scheduleTestCase(result);
            getLifecycle().startTestCase(result.getUuid());
            tasktrackerAdapter.changeTestCase(TaskStatus.IN_PROGRESS);
        }
    }

    Map<String, String> commentPool = new HashMap<>();

    @Override
    public void comment(String step){
        if (step.contains("start time")){
            final String stepUid = UUID.randomUUID().toString();
            String comment = getCommentName(step);
            commentPool.put(comment, stepUid);
            getLifecycle().startStep(stepUid, new StepResult().setName(comment));
        }
        if (step.contains("end time")){
            getLifecycle().stopStep(commentPool.get(getCommentName(step)));
        }
    }

    private String getCommentName(String step) {
        return step.replace("!--", "").replace(" start time ", "")
                .replace(" end time ", "");
    }


    @Override
    public void afterScenario() {
        final Story parentStory = parentStories.get();
        if (null == parentStory) {
            final String uuid = scenarios.get();
            final Status status = scenarioStatusStorage.getOrDefault(uuid, Status.PASSED);
            getLifecycle().updateTestCase(uuid, testResult -> {
                testResult.setStatus(status);
                String reportDescription = testResult.getDescription();
                reportDescription += String.join("", scenarioInformers.stream().map(item -> item.renderAfter()).collect(Collectors.toList()));
                testResult.setDescription(reportDescription);
            });
            getLifecycle().stopTestCase(uuid);
            getLifecycle().writeTestCase(uuid);

            if (status == Status.PASSED) {
                tasktrackerAdapter.changeTestCase(TaskStatus.PASSED);
            }

            scenarios.remove();
        }

    }

    @Override
    public void beforeStep(final String step) {
        startStep(step);
    }

    @Override
    public void successful(final String step) {
        getLifecycle().updateStep(result -> result.setStatus(Status.PASSED));
        getLifecycle().stopStep();
        updateScenarioStatus(Status.PASSED);
    }

    private void startStep(final String step){
        final String stepUuid = UUID.randomUUID().toString();
        stepUID = stepUuid;
        getLifecycle().startStep(stepUuid, new StepResult().setName(getNormalizeStep(step)));
    }

    @Override
    public void ignorable(final String step) {
        //возможно тут когда - то будет ошибка с дублированием шагов в тесте,
        //если будет, то надо будет думать что с этим делать
        startStep(step);
        getLifecycle().updateStep(result -> result.setStatus(Status.SKIPPED));
        getLifecycle().stopStep();
    }

    @Override
    public void pending(final String step) {
        //возможно тут когда - то будет ошибка с дублированием шагов в тесте,
        //если будет, то надо будет думать что с этим делать
        startStep(step);
        getLifecycle().updateStep(result -> result.setStatus(Status.SKIPPED));
        getLifecycle().stopStep();
        updateScenarioStatus(Status.SKIPPED);
    }

    @Override
    public void failed(final String step, final Throwable cause) {
        getLifecycle().updateStep(stepUID, s -> s
                .setStatus(getStatus(cause).orElse(Status.BROKEN))
                .setStatusDetails(getStatusDetails(cause).orElse(null)));

        // TODO addScreenshot();

        // TODO getJBehaveStoryReport()
        /*
        String reportPath = getJBehaveStoryReport();
        try {
            convertUtils.encoding(reportPath);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        */

        final String scenarioUuid = scenarios.get();
        getLifecycle().updateTestCase(scenarioUuid, testResult -> testResult.setStatusDetails(new StatusDetails().setMessage(cause.getCause().getMessage())
                .setTrace(ExceptionUtils.getStackTrace(cause))));
        //screenshotSteps.takeScreenshotToReport();
        getLifecycle().stopStep(stepUID);
        updateScenarioStatus(Status.FAILED);
        tasktrackerAdapter.changeTestCase(TaskStatus.FAILED/*, Arrays.asList(new File(screenshotPath).getName(), new File(reportPath).getName())*/);
    }

    public AllureLifecycle getLifecycle() {
        return lifecycle;
    }

    private String getJBehaveStoryReport(){
        /* TODO
        String name = this.stories.get().getName();
        File reportFolder = new File(System.getProperty("user.dir") + "\\target\\jbehave");
        for (final File fileEntry : reportFolder.listFiles()) {
            if (fileEntry.getName().contains(name + ".txt" )){
                return fileEntry.getAbsolutePath();
            }
        }
        */
        return null;
    }

    protected void updateScenarioStatus(final Status passed) {
        final String scenarioUuid = scenarios.get();
        max(scenarioStatusStorage.get(scenarioUuid), passed)
                .ifPresent(status -> scenarioStatusStorage.put(scenarioUuid, status));
    }

    private String md5(final String string) {
        return DatatypeConverter.printHexBinary(getMessageDigest()
                        .digest(string.getBytes(StandardCharsets.UTF_8))
        );
    }

    private MessageDigest getMessageDigest() {
        try {
            return MessageDigest.getInstance(MD_5);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Could not find md5 hashing algorithm", e);
        }
    }

    private Optional<Status> max(final Status first, final Status second) {
        return Stream.of(first, second)
                .filter(Objects::nonNull)
                .min(Status::compareTo);
    }

    private void setStoryVariables(Story story){
        Meta meta = story.getMeta();
        for (String metaProp : meta.getPropertyNames()) {
            String value = (String) evaluateManager.evalVariable(meta.getProperty(metaProp));
            variableStorage.setVariable(variableStorage.getRootScope(), metaProp, value);
        }
    }

    private String getNormalizeStep(String step){
        if (step.indexOf("\r") > 0) {
            step = step.substring(0, step.indexOf("\r"));
        }
        if (!step.startsWith("!--")) {
            step = step.replaceAll("Given ", "").replaceAll("When ", "")
                    .replaceAll("Then ", "").replaceAll("And ", "");
            try {
                return evaluateManager.evalVariable(step);
            } catch (AssertionError e) {
                log.error(e.getMessage(), e);

            }
        }
        return step;
    }
}