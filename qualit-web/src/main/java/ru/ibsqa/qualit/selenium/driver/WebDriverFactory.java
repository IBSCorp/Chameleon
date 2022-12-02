package ru.ibsqa.qualit.selenium.driver;

import org.springframework.stereotype.Component;
import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;
import ru.ibsqa.qualit.i18n.ILocaleManager;
import ru.ibsqa.qualit.selenium.driver.configuration.IDriverConfiguration;
import ru.ibsqa.qualit.selenium.driver.exceptions.NoSupportedDriverException;
import ru.ibsqa.qualit.selenium.events.Highlighter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Constructor;
import java.net.URI;
import java.net.URL;

@DriverFactory(priority = ConfigurationPriority.LOW)
@NoArgsConstructor
@Slf4j
@Component
public class WebDriverFactory extends AbstractDriverFactory<WebSupportedDriver> {

    @Autowired
    private IDriverSerialization driverSerialization;

    public WebDriverFactory(IDriverConfiguration configuration) {
        super(configuration);
    }

    // TODO надо переделать
    public WebDriver newInstance(String driverId) {
        System.setProperty("java.net.preferIPv4Stack", "true");
        getConfiguration().initDriver();
        Class<? extends WebDriver> aClass = getConfiguration().getDriverType().getAsClass().asSubclass(WebDriver.class);

        // Если подключаемся к существующей сессии браузера
        if (getConfiguration().isConnectToRunningApp()) {
            return driverSerialization.createDriver(driverId, aClass, getConfiguration());
        }

        WebDriver driver = null;
        try {
            if (aClass.isAssignableFrom(RemoteWebDriver.class)) {
                driver = createInstanceRemoteDriver(aClass);
            } else {
                if (getConfiguration().getDesiredCapabilities() == null) {
                    driver = aClass.newInstance();
                } else {
                    Constructor<? extends WebDriver> constructor = aClass.getConstructor(Capabilities.class);
                    if (getConfiguration().isHighlightElements()){
                        driver = new EventFiringWebDriver(constructor.newInstance(getConfiguration().getDesiredCapabilities()))
                            .register(new Highlighter());
                    }else{
                        driver = constructor.newInstance(getConfiguration().getDesiredCapabilities());
                    }

                }
            }
            String url = getConfiguration().getApplicationUrl();
            if (null != url && !url.isEmpty()) {
                driver.get(getConfiguration().getApplicationUrl());
            }
            if (getConfiguration().isMaximizeWindow()) {
                driver.manage().window().maximize();
            }


        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new NoSupportedDriverException(ILocaleManager.message("noSupportedDriverException", getConfiguration().getDriverType().name()));
        }

        // Если после завершения работы требуется оставить драйвер, то сохраним его сессию в файл
        if (!getConfiguration().isCloseDriverAfterTest()) {
            driverSerialization.saveDriver(driver, driverId);
        }

        return driver;
    }

    private WebDriver createInstanceRemoteDriver(Class<? extends WebDriver> aClass) {
        try {
            Constructor<? extends WebDriver> constructor = aClass.getConstructor(URL.class, Capabilities.class);
            if (getConfiguration().isHighlightElements()){
                return new EventFiringWebDriver(new RemoteWebDriver(URI.create(getConfiguration().getDriverPath()).toURL(), getConfiguration().getDesiredCapabilities()))
                    .register(new Highlighter());
            }else{
                return constructor.newInstance(URI.create(getConfiguration().getDriverPath()).toURL(), getConfiguration().getDesiredCapabilities());

            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

}
