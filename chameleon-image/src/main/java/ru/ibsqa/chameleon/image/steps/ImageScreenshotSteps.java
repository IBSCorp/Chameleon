package ru.ibsqa.chameleon.image.steps;

import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;
import ru.ibsqa.chameleon.image.driver.ImageSupportedDriver;
import ru.ibsqa.chameleon.selenium.driver.ISupportedDriver;
import ru.ibsqa.chameleon.steps.IScreenshotSteps;
import ru.ibsqa.chameleon.steps.DefaultScreenshotSteps;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

@Slf4j
public class ImageScreenshotSteps extends DefaultScreenshotSteps implements IScreenshotSteps {

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
		return Collections.singletonList(ImageSupportedDriver.IMAGE_DRIVER);
	}

}