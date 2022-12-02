package ru.ibsqa.qualit.steps;

import io.cucumber.core.gherkin.Step;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.fail;

@Component
@Slf4j
public class AllureFeatureSteps implements IFeatureSteps {
    @Override
    public void beforeStep(Step step) {
        final StepResult stepResult = new StepResult()
                .setName(step.getText())
                .setParameters(new ArrayList<>());
        AllureLifecycle lifecycle = Allure.getLifecycle();
        lifecycle.startStep(step.getId(), stepResult);
    }

    @Override
    public void afterStep(Step step) {
        AllureLifecycle lifecycle = Allure.getLifecycle();
        lifecycle.updateStep(step.getId(), s -> s.setStatus(Status.PASSED));
    }

    @Override
    public void afterException(Step step) {
        AllureLifecycle lifecycle = Allure.getLifecycle();
        lifecycle.updateStep(step.getId(), s -> s.setStatus(Status.FAILED));
    }

    @Override
    public void finallyStep(Step step) {
        AllureLifecycle lifecycle = Allure.getLifecycle();
        lifecycle.stopStep(step.getId());
    }
}