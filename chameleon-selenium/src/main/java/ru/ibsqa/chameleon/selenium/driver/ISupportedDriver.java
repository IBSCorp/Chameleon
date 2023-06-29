package ru.ibsqa.chameleon.selenium.driver;

import org.openqa.selenium.WebDriver;
import ru.ibsqa.chameleon.selenium.driver.configuration.IDriverConfiguration;

public interface ISupportedDriver {

	void initDriver(IDriverConfiguration configuration);

	Class<? extends WebDriver> getAsClass();

	String name();
}
