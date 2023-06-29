package ru.ibsqa.chameleon;

import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.EventHandler;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.PickleStepTestStep;
import io.cucumber.plugin.event.TestStepStarted;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultCucumberListener implements ConcurrentEventListener {

    private final EventHandler<TestStepStarted> stepStartedLoggerHandler;

    public DefaultCucumberListener() {
        super();
        this.stepStartedLoggerHandler = this::handleTestStepStartedLogger;
    }

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestStepStarted.class, this.stepStartedLoggerHandler);
    }

    protected void handleTestStepStartedLogger(TestStepStarted event) {
        if (event.getTestStep() instanceof PickleStepTestStep) {
            PickleStepTestStep pickleStep = (PickleStepTestStep) event.getTestStep();
            log.info(pickleStep.getStep().getText());
        }
    }

}
