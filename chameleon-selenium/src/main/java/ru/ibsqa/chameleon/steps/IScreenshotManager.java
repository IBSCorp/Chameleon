package ru.ibsqa.chameleon.steps;

public interface IScreenshotManager {

    void takeScreenshotToReport(String name, IScreenshotSteps.SeverityLevel level);

    default void takeScreenshotToReport(String name) {
        takeScreenshotToReport(name, IScreenshotSteps.SeverityLevel.INFO);
    }

    default void takeScreenshotToReport() {
        takeScreenshotToReport(null, IScreenshotSteps.SeverityLevel.ERROR);
    }

}
