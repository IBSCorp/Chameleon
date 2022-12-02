package ru.ibsqa.qualit.selenium.driver;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chromium.ChromiumDriver;
import ru.ibsqa.qualit.selenium.driver.configuration.IDriverConfiguration;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.safari.SafariDriver;

public enum WebSupportedDriver implements ISupportedDriver {
    CHROME(ChromeDriver.class),
    FIREFOX(FirefoxDriver.class),
    SAFARI(SafariDriver.class),
    IE(InternetExplorerDriver.class),
    EDGE(EdgeDriver.class),
    OPERA(OperaDriver.class),
    CHROMIUM(ChromiumDriver.class),
    ;
//    REMOTEDRIVER(RemoteWebDriver.class),
    // Смотрите в CHANGELOG по слову MARIONETTE для подробностей
    // MARIONETTE(FirefoxDriver.class),
//    OTHER(WiniumDriver.class);

    private final Class<? extends WebDriver> _class;
    private final String _item;

    WebSupportedDriver(Class<? extends WebDriver> __class, String item) {
        this._class = __class;
        this._item = item;
    }

    WebSupportedDriver(Class<? extends WebDriver> __class) {
        this._class = __class;
        this._item = null;
    }

    @Override
    public Class<? extends WebDriver> getAsClass() {
        return _class;
    }

    public String getItem() {
        return _item;
    }

    @Override
    public void initDriver(IDriverConfiguration configuration) {
        switch (this) {
            case FIREFOX:
                if (!StringUtils.isEmpty(configuration.getDriverPath())) {
                    System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + System.getProperty("file.separator") + configuration.getDriverPath());
                }
                break;
            case CHROME:
                if (!StringUtils.isEmpty(configuration.getDriverPath())) {
                    System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + System.getProperty("file.separator") + configuration.getDriverPath());
                }
                break;
            case IE:
                if (!StringUtils.isEmpty(configuration.getDriverPath())) {
                    System.setProperty("webdriver.ie.driver", System.getProperty("user.dir") + System.getProperty("file.separator") + configuration.getDriverPath());
                }
                break;
            case EDGE:
                if (!StringUtils.isEmpty(configuration.getDriverPath())) {
                    System.setProperty("webdriver.edge.drive", System.getProperty("user.dir") + System.getProperty("file.separator") + configuration.getDriverPath());
                }
                break;
            case CHROMIUM:
            case OPERA:
            case SAFARI:
                break;
            // Смотрите в CHANGELOG по слову MARIONETTE для подробностей
            // case MARIONETTE:
            //     //для FireFox версий < 47.0
            //     System.setProperty("webdriver.firefox.marionette", System.getProperty("user.dir") + System.getProperty("file.separator") + configuration.getDriverPath());
            //     break;
//            case REMOTEDRIVER:
//            case OTHER:
        }
    }
}
