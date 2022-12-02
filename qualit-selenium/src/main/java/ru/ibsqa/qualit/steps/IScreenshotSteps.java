package ru.ibsqa.qualit.steps;

import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;
import ru.ibsqa.qualit.selenium.driver.ISupportedDriver;

import java.util.List;

public interface IScreenshotSteps {

    enum SeverityLevel {
        INFO,
        ERROR
    }

    void takeScreenshotToReport(String name, SeverityLevel level);

    default ConfigurationPriority getPriority() {
        return ConfigurationPriority.HIGH;
    }

    List<ISupportedDriver> getSupportedDrivers();

}
