package ru.ibsqa.qualit.selenium.driver.configuration;

import ru.ibsqa.qualit.reporter.ScreenshotConfiguration;
import ru.ibsqa.qualit.selenium.driver.ISupportedDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public interface IDriverConfiguration {

    ISupportedDriver getDriverType();

    String getDriverPath();

    int getImplicitlyWait();

    int getDefaultWaitTimeOut();

    boolean isMaximizeWindow();

    DesiredCapabilities getDesiredCapabilities();

    String getApplicationUrl();

    boolean isConnectToRunningApp();

    boolean isCloseDriverAfterTest();

    boolean isHighlightElements();

    void initDriver();

    boolean isAdvancedScreenshotMethod();

    boolean isFullScreenshot();

    ScreenshotConfiguration getScreenshotConfiguration();
}
