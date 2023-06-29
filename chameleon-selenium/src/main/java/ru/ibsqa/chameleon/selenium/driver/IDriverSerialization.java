package ru.ibsqa.chameleon.selenium.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CommandExecutor;
import ru.ibsqa.chameleon.selenium.driver.configuration.IDriverConfiguration;

public interface IDriverSerialization {
    void saveDriver(final WebDriver driver, final String driverId);

    WebDriver createDriver(final String driverId, final Class<? extends WebDriver> aClass, final IDriverConfiguration configuration);

    CommandExecutor createExecutor(final String driverId, final Class<? extends WebDriver> aClass, final DriverSession driverSession);
}
