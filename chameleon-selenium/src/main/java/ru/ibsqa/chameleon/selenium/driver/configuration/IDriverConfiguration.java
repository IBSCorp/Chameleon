package ru.ibsqa.chameleon.selenium.driver.configuration;

import org.openqa.selenium.Capabilities;
import ru.ibsqa.chameleon.reporter.ScreenshotConfiguration;
import ru.ibsqa.chameleon.selenium.driver.ISupportedDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Optional;

public interface IDriverConfiguration {

    ISupportedDriver getDriverType();

    String getDriverPath();

    int getImplicitlyWait();

    int getDefaultWaitTimeOut();

    boolean isMaximizeWindow();

    void setOptions(Object options);
    Object getOptions();

    default Capabilities getCapabilities() {
        return (Capabilities) Optional.ofNullable(getOptions())
                .filter(capabilities -> capabilities instanceof Capabilities)
                .orElse(null);
    }

    default DesiredCapabilities getDesiredCapabilities() {
        return (DesiredCapabilities) Optional.ofNullable(getOptions())
                .filter(capabilities -> capabilities instanceof DesiredCapabilities)
                .orElse(null);
    }

    String getApplicationUrl();

    boolean isConnectToRunningApp();

    boolean isCloseDriverAfterTest();

    boolean isHighlightElements();

    void initDriver();

    boolean isAdvancedScreenshotMethod();

    boolean isFullScreenshot();

    ScreenshotConfiguration getScreenshotConfiguration();
}
