package ru.ibsqa.qualit.selenium.driver;

import org.openqa.selenium.WebDriver;
import ru.ibsqa.qualit.selenium.driver.configuration.IDriverConfiguration;

public interface IDriverFactory {
    IDriverConfiguration getConfiguration();
    WebDriver newInstance(String driverId);
}
