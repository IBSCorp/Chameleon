package ru.ibsqa.chameleon.selenium.driver;

import org.openqa.selenium.WebDriver;
import ru.ibsqa.chameleon.selenium.driver.configuration.IDriverConfiguration;

public interface IDriverFactory {
    IDriverConfiguration getConfiguration();
    WebDriver newInstance(String driverId);
}
