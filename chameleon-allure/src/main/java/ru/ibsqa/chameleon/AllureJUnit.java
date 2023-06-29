package ru.ibsqa.chameleon;

import io.qameta.allure.junitplatform.AllureJunitPlatform;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.reporting.ReportEntry;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;
import org.springframework.core.io.Resource;
import ru.ibsqa.chameleon.utils.spring.SpringUtils;

import java.util.Optional;

public class AllureJUnit extends AllureJunitPlatform {
    private final boolean allureEnabled = Boolean.parseBoolean(System.getProperty("allure.enabled", "true"));
    private final Resource[] resources = SpringUtils.getResources("classpath:/cucumber.properties");
    private final boolean cucumberEnabled = Optional.ofNullable(resources)
            .map(resource -> resource.length > 0 && resource[0].exists())
            .orElse(false);

    @Override
    public void testPlanExecutionStarted(TestPlan testPlan) {
        if (!allureEnabled || cucumberEnabled) {
            return;
        }

        super.testPlanExecutionStarted(testPlan);
    }

    @Override
    public void testPlanExecutionFinished(TestPlan testPlan) {
        if (!allureEnabled || cucumberEnabled) {
            return;
        }

        super.testPlanExecutionFinished(testPlan);
    }

    @Override
    public void executionStarted(TestIdentifier testIdentifier) {
        if (!allureEnabled || cucumberEnabled) {
            return;
        }

        super.executionStarted(testIdentifier);
    }

    @Override
    public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
        if (!allureEnabled || cucumberEnabled) {
            return;
        }

        super.executionFinished(testIdentifier, testExecutionResult);
    }

    @Override
    public void executionSkipped(TestIdentifier testIdentifier, String reason) {
        if (!allureEnabled || cucumberEnabled) {
            return;
        }

        super.executionSkipped(testIdentifier, reason);
    }

    @Override
    public void reportingEntryPublished(TestIdentifier testIdentifier, ReportEntry entry) {
        if (!allureEnabled || cucumberEnabled) {
            return;
        }

        super.reportingEntryPublished(testIdentifier, entry);
    }
}
