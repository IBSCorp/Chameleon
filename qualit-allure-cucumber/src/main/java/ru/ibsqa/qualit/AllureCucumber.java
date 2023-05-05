package ru.ibsqa.qualit;

import io.cucumber.plugin.event.*;
import io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm;
import ru.ibsqa.qualit.asserts.AssertLayer;
import ru.ibsqa.qualit.asserts.IAssertManager;
import ru.ibsqa.qualit.steps.HiddenStep;
import ru.ibsqa.qualit.utils.reflection.ReflectionUtils;
import ru.ibsqa.qualit.utils.spring.SpringUtils;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;

public class AllureCucumber extends AllureCucumber7Jvm {

    private IAssertManager assertManager;
    private AllureConfiguration allureConfiguration;

    protected EventHandler<TestSourceRead> featureStartedHandlerWrapped;
    protected EventHandler<TestCaseStarted> caseStartedHandlerWrapped;
    protected EventHandler<TestCaseFinished> caseFinishedHandlerWrapped;
    protected EventHandler<TestStepStarted> stepStartedHandlerWrapped;
    protected EventHandler<TestStepFinished> stepFinishedHandlerWrapped;
    protected EventHandler<WriteEvent> writeEventHandlerWrapped;
    protected EventHandler<EmbedEvent> embedEventHandlerWrapped;

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        featureStartedHandlerWrapped = wrapHandlerAndRegister(publisher, "featureStartedHandler", TestSourceRead.class, this::handleFeatureStarted);
        caseStartedHandlerWrapped = wrapHandlerAndRegister(publisher, "caseStartedHandler", TestCaseStarted.class, this::handleTestCaseStarted);
        caseFinishedHandlerWrapped = wrapHandlerAndRegister(publisher, "caseFinishedHandler", TestCaseFinished.class, this::handleTestCaseFinished);
        stepStartedHandlerWrapped = wrapHandlerAndRegister(publisher, "stepStartedHandler", TestStepStarted.class, this::handleTestStepStarted);
        stepFinishedHandlerWrapped = wrapHandlerAndRegister(publisher, "stepFinishedHandler", TestStepFinished.class, this::handleTestStepFinished);
        writeEventHandlerWrapped = wrapHandlerAndRegister(publisher, "writeEventHandler", WriteEvent.class, this::handleWriteEvent);
        embedEventHandlerWrapped = wrapHandlerAndRegister(publisher, "embedEventHandler", EmbedEvent.class, this::handleEmbedEvent);
    }

    @SuppressWarnings("unchecked")
    private <T> EventHandler<T> wrapHandlerAndRegister(EventPublisher publisher, String fieldName, Class<T> event, EventHandler<T> handler) {
        EventHandler<T> wrappedHandler;
        try {
            Field field = AllureCucumber7Jvm.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            wrappedHandler = (EventHandler<T>) field.get(this);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        publisher.registerHandlerFor(event, handler);
        return wrappedHandler;
    }

    protected IAssertManager getAssertManager() {
        if (Objects.isNull(assertManager)) {
            assertManager = SpringUtils.getBean(IAssertManager.class);
        }
        return assertManager;
    }

    protected AllureConfiguration getAllureConfiguration() {
        if (Objects.isNull(allureConfiguration)) {
            allureConfiguration = SpringUtils.getBean(AllureConfiguration.class);
        }
        return allureConfiguration;
    }

    protected boolean isHidden(TestStep testStep) {
        return ReflectionUtils.getMethodBySignature(testStep.getCodeLocation())
                .map(m -> Objects.nonNull(m.getAnnotation(HiddenStep.class)))
                .orElse(false);
    }

    protected void handleFeatureStarted(TestSourceRead event) {
        featureStartedHandlerWrapped.receive(event);
    }

    protected void handleTestCaseStarted(TestCaseStarted event) {
        if (!getAllureConfiguration().isEnabled()) {
            return;
        }

        caseStartedHandlerWrapped.receive(event);
    }

    protected void handleTestCaseFinished(TestCaseFinished event) {
        if (!getAllureConfiguration().isEnabled()) {
            return;
        }

        caseFinishedHandlerWrapped.receive(event);
    }

    protected void handleTestStepStarted(TestStepStarted event) {
        if (!getAllureConfiguration().isEnabled()) {
            return;
        }
        if (isHidden(event.getTestStep())) {
            return;
        }

        stepStartedHandlerWrapped.receive(event);
    }

    protected void handleTestStepFinished(final TestStepFinished event) {
        if (!getAllureConfiguration().isEnabled()) {
            return;
        }
        if (isHidden(event.getTestStep())) {
            return;
        }

        boolean fail = !(event.getTestStep() instanceof HookTestStep)
                && Optional.ofNullable(getAssertManager().getLastLayer())
                .map(AssertLayer::hasErrors)
                .orElse(false);
        if (fail) {
            Result result = event.getResult();
            Result modifiedResult = new Result(Status.FAILED, result.getDuration(), result.getError());
            TestStepFinished modifiedEvent = new TestStepFinished(event.getInstant(), event.getTestCase(), event.getTestStep(), modifiedResult);
            stepFinishedHandlerWrapped.receive(modifiedEvent);
        } else {
            stepFinishedHandlerWrapped.receive(event);
        }
    }

    protected void handleWriteEvent(WriteEvent event) {
        if (!getAllureConfiguration().isEnabled()) {
            return;
        }

        writeEventHandlerWrapped.receive(event);
    }

    protected void handleEmbedEvent(EmbedEvent event) {
        if (!getAllureConfiguration().isEnabled()) {
            return;
        }

        embedEventHandlerWrapped.receive(event);
    }

}
