package ru.ibsqa.qualit;

import io.cucumber.plugin.event.EventHandler;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.PickleStepTestStep;
import io.cucumber.plugin.event.TestStepStarted;
import io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultCucumberListener extends AllureCucumber7Jvm {

    private final EventHandler<TestStepStarted> stepStartedLoggerHandler;

    public DefaultCucumberListener() {
        super();
        this.stepStartedLoggerHandler = this::handleTestStepStartedLogger;
    }

    public void setStepStartedLoggerHandler(EventPublisher publisher) {
        super.setEventPublisher(publisher);
        publisher.registerHandlerFor(TestStepStarted.class, this.stepStartedLoggerHandler);
    }

    protected void handleTestStepStartedLogger(TestStepStarted event) {
        if (event.getTestStep() instanceof PickleStepTestStep) {
            PickleStepTestStep pickleStep = (PickleStepTestStep) event.getTestStep();
            log.info(pickleStep.getStep().getText());
        }
    }

}
