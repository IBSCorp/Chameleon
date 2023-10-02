package ru.ibsqa.chameleon.storage;

import io.cucumber.java.Scenario;

public interface IStorageLifecycle {
    void beforeScenario(Scenario scenario);
    void afterScenario(Scenario scenario);
}
