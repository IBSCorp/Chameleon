package ru.ibsqa.chameleon.appium.driver;

import lombok.RequiredArgsConstructor;
import org.openqa.selenium.WebDriver;
import ru.ibsqa.chameleon.selenium.driver.ISupportedDriver;
import ru.ibsqa.chameleon.selenium.driver.configuration.IDriverConfiguration;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

@RequiredArgsConstructor
public enum AppiumSupportedDriver implements ISupportedDriver {

    IOS(IOSDriver.class),
    ANDROID(AndroidDriver.class);

    private final Class<? extends WebDriver> driverClass;

    @Override
    public Class<? extends WebDriver> getAsClass() {
        return driverClass;
    }

    @Override
    public void initDriver(IDriverConfiguration configuration) {
        switch (this) {
            case ANDROID:
                break;
            case IOS:
                break;
        }
    }
}
