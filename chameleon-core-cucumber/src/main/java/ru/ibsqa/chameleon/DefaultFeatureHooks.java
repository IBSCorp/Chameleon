package ru.ibsqa.chameleon;

import io.cucumber.java.After;
import ru.ibsqa.chameleon.asserts.IAssertsLifecycle;
import ru.ibsqa.chameleon.steps.HiddenStep;
import ru.ibsqa.chameleon.storage.IStorageLifecycle;
import ru.ibsqa.chameleon.utils.spring.SpringUtils;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultFeatureHooks {

    @Before(order = 1)
    @HiddenStep
    public void lifecycleBefore(Scenario scenario) {
        SpringUtils.getBean(IAssertsLifecycle.class).beforeScenario(scenario);
        SpringUtils.getBean(IStorageLifecycle.class).beforeScenario(scenario);
    }

    @After(order = 1)
    @HiddenStep
    public void lifecycleAfter(Scenario scenario) {
        SpringUtils.getBean(IStorageLifecycle.class).afterScenario(scenario);
        SpringUtils.getBean(IAssertsLifecycle.class).afterScenario(scenario);
    }

}