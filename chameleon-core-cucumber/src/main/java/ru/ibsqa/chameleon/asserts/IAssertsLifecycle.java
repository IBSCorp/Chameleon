package ru.ibsqa.chameleon.asserts;

import io.cucumber.java.Scenario;

public interface IAssertsLifecycle {
    void beforeScenario(Scenario scenario);
    void afterScenario(Scenario scenario);
}
