package ru.ibsqa.qualit.steps;

import io.cucumber.core.gherkin.Step;

public interface IFeatureSteps {
    void beforeStep(Step step);

    void afterStep(Step step);

    void afterException(Step step);

    void finallyStep(Step step);
}