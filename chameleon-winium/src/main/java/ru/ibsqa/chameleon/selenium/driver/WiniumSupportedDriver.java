package ru.ibsqa.chameleon.selenium.driver;

import lombok.RequiredArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.winium.WiniumDriver;
import ru.ibsqa.chameleon.selenium.driver.configuration.IDriverConfiguration;

@RequiredArgsConstructor
public enum WiniumSupportedDriver implements ISupportedDriver {

    WINIUM(WiniumDriver.class);

    private final Class<? extends WebDriver> driverClass;

    @Override
    public Class<? extends WebDriver> getAsClass() {
        return driverClass;
    }

    @Override
    public void initDriver(IDriverConfiguration configuration) {
    }

}
