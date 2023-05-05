package ru.ibsqa.qualit;

import io.qameta.allure.junitplatform.AllureJunitPlatform;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.reporting.ReportEntry;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;

public class AllureJUnit extends AllureJunitPlatform {
    private final boolean allureEnabled = Boolean.parseBoolean(System.getProperty("allure.enabled", "true"));

    @Override
    public void testPlanExecutionStarted(TestPlan testPlan) {
        if (!allureEnabled) {
            return;
        }

        super.testPlanExecutionStarted(testPlan);
    }

    @Override
    public void testPlanExecutionFinished(TestPlan testPlan) {
        if (!allureEnabled) {
            return;
        }

        super.testPlanExecutionFinished(testPlan);
    }

    @Override
    public void executionStarted(TestIdentifier testIdentifier) {
        if (!allureEnabled) {
            return;
        }

        super.executionStarted(testIdentifier);
    }

    @Override
    public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
        if (!allureEnabled) {
            return;
        }

        super.executionFinished(testIdentifier, testExecutionResult);
    }

    @Override
    public void executionSkipped(TestIdentifier testIdentifier, String reason) {
        if (!allureEnabled) {
            return;
        }

        super.executionSkipped(testIdentifier, reason);
    }

    @Override
    public void reportingEntryPublished(TestIdentifier testIdentifier, ReportEntry entry) {
        if (!allureEnabled) {
            return;
        }

        super.reportingEntryPublished(testIdentifier, entry);
    }
}
