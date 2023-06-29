package ru.ibsqa.chameleon.steps;

import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;
import ru.ibsqa.chameleon.selenium.driver.ISupportedDriver;

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
