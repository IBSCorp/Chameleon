package ru.ibsqa.qualit.selenium.driver;

import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;
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
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;

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

    /**
     * Создание Web-драйвера по его идентификатору
     * @param driverId
     * @return
     */
    @Override
    public WebDriver newInstance(String driverId) {
        System.setProperty("java.net.preferIPv4Stack", "true");
        getConfiguration().initDriver();
        Class<? extends WebDriver> aClass = getConfiguration().getDriverType().getAsClass().asSubclass(WebDriver.class);

        // Если подключаемся к существующей сессии браузера
        if (getConfiguration().isConnectToRunningApp()) {
            return driverSerialization.createDriver(driverId, aClass, getConfiguration());
        }

        // Создание драйвера
        WebDriver driver = newInstance(aClass, getConfiguration().getCapabilities());

        if (Objects.nonNull(driver)) {
            String url = getConfiguration().getApplicationUrl();
            if (StringUtils.isNotEmpty(url)) {
                driver.get(url);
            }
            if (getConfiguration().isMaximizeWindow()) {
                driver.manage().window().maximize();
            }
            // Если после завершения работы требуется оставить драйвер, то сохраним его сессию в файл
            if (!getConfiguration().isCloseDriverAfterTest()) {
                driverSerialization.saveDriver(driver, driverId);
            }
        }

        return driver;
    }

    /**
     * Создание Web-драйвера
     * @param driverClass класс драйвера (тип браузера)
     * @param options опции драйвера
     * @param <T> класс опцций
     * @return
     */
    public <T extends Capabilities> WebDriver newInstance(@NonNull Class<? extends WebDriver> driverClass, @Nullable T options) {
        WebDriver driver;
        try {
            driver = Objects.nonNull(options)
                    ? driverClass.getConstructor(options.getClass()).newInstance(getConfiguration().getCapabilities())
                    : driverClass.getConstructor().newInstance();

            if (getConfiguration().isHighlightElements()) {
                driver = new EventFiringWebDriver(driver).register(new Highlighter());
            }

            return driver;
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            log.error(e.getMessage(), e);
            throw new NoSupportedDriverException(ILocaleManager.message("noSupportedDriverException", getConfiguration().getDriverType().name()));
        }
    }

}
