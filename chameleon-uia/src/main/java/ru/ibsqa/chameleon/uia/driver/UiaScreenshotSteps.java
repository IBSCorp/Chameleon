package ru.ibsqa.chameleon.uia.driver;

import org.springframework.stereotype.Component;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;
import ru.ibsqa.chameleon.selenium.driver.ISupportedDriver;
import ru.ibsqa.chameleon.steps.IScreenshotSteps;
import ru.ibsqa.chameleon.steps.DefaultScreenshotSteps;

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
