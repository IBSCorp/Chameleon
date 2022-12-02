package ru.ibsqa.qualit.uia.driver;

import org.springframework.stereotype.Component;
import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;
import ru.ibsqa.qualit.selenium.driver.ISupportedDriver;
import ru.ibsqa.qualit.steps.IScreenshotSteps;
import ru.ibsqa.qualit.steps.DefaultScreenshotSteps;

import java.util.Collections;
import java.util.List;

@Component
public class UiaScreenshotSteps extends DefaultScreenshotSteps implements IScreenshotSteps {

    @Override
    public void takeScreenshotToReport(String name, SeverityLevel level) {
        this.addScreenshot(name, this::getFullScreenshot);
    }

    @Override
    public ConfigurationPriority getPriority() {
        return ConfigurationPriority.NORMAL;
    }

    @Override
    public List<ISupportedDriver> getSupportedDrivers() {
        return Collections.singletonList(UiaSupportedDriver.UI_AUTOMATION);
    }

}
