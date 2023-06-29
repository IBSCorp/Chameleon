package ru.ibsqa.chameleon.selenium.driver;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.ChromiumDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariOptions;
import ru.ibsqa.chameleon.selenium.driver.configuration.IDriverConfiguration;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.safari.SafariDriver;

@RequiredArgsConstructor
public enum WebSupportedDriver implements ISupportedDriver {
    CHROME(ChromeDriver.class, ChromeOptions.class, "webdriver.chrome.driver"),
    FIREFOX(FirefoxDriver.class, FirefoxOptions.class, "webdriver.gecko.driver"),
    SAFARI(SafariDriver.class, SafariOptions.class, null),
    IE(InternetExplorerDriver.class, InternetExplorerOptions.class, "webdriver.ie.driver"),
    EDGE(EdgeDriver.class, EdgeOptions.class, "webdriver.edge.drive"),
    CHROMIUM(ChromiumDriver.class, DesiredCapabilities.class, null),
    REMOTEDRIVER(RemoteWebDriver.class, DesiredCapabilities.class, null),
    ;
    // Смотрите в CHANGELOG по слову OPERA для подробностей
    // OPERA(OperaDriver.class)
    // Смотрите в CHANGELOG по слову MARIONETTE для подробностей
    // MARIONETTE(FirefoxDriver.class)

    private final Class<? extends WebDriver> driverClass;
    private final Class<? extends Capabilities> capabilitiesClass;
    private final String driverPathProperty;

    @Override
    public Class<? extends WebDriver> getAsClass() {
        return driverClass;
    }

    @Override
    public void initDriver(IDriverConfiguration configuration) {
        if (StringUtils.isNotEmpty(configuration.getDriverPath()) && StringUtils.isNotEmpty(driverPathProperty)) {
            System.setProperty(driverPathProperty, System.getProperty("user.dir") + System.getProperty("file.separator") + configuration.getDriverPath());
        }
    }

}
