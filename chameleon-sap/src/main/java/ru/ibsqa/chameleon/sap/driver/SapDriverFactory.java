package ru.ibsqa.chameleon.sap.driver;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;
import ru.ibsqa.chameleon.i18n.ILocaleManager;
import ru.ibsqa.chameleon.selenium.driver.DriverFactory;
import ru.ibsqa.chameleon.selenium.driver.AbstractDriverFactory;
import ru.ibsqa.chameleon.selenium.driver.IDriverSerialization;
import ru.ibsqa.chameleon.selenium.driver.configuration.IDriverConfiguration;
import ru.ibsqa.chameleon.selenium.driver.exceptions.NoSupportedDriverException;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Constructor;

@DriverFactory(priority = ConfigurationPriority.LOW)
@NoArgsConstructor
@Slf4j
@Component
public class SapDriverFactory extends AbstractDriverFactory<SapSupportedDriver> {
    @Autowired
    private IDriverSerialization driverSerialization;

    public SapDriverFactory(IDriverConfiguration configuration) {
        super(configuration);
    }

    // TODO реализовать сохранение драйвера в файл по аналогии с SeleniumDriverFactory
    @Override
    public WebDriver newInstance(String driverId) {
        SapDriver driver;


        try {
            System.setProperty(com.jacob.com.LibraryLoader.JACOB_DLL_PATH, System.getProperty("user.dir") + "\\" + getConfiguration().getDriverPath());
            Class<? extends WebDriver> aClass = getConfiguration().getDriverType().getAsClass();

            // Если подключаемся к существующей сессии браузера
            if (getConfiguration().isConnectToRunningApp()) {
                return driverSerialization.createDriver(driverId, aClass, getConfiguration());
            }

            Constructor<? extends WebDriver> constructor = aClass.getConstructor(DesiredCapabilities.class);
            driver = (SapDriver) constructor.newInstance(getConfiguration().getDesiredCapabilities());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new NoSupportedDriverException(ILocaleManager.message("noSupportedDriverException", getConfiguration().getDriverType().name()));
        }
        driver.manage().window().maximize();


        // Если после завершения работы требуется оставить драйвер, то сохраним его сессию в файл
        if (!getConfiguration().isCloseDriverAfterTest()) {
            driverSerialization.saveDriver(driver, driverId);
        }
        // driver.setConfiguration(getConfiguration());
        driver.setDriverFactory(new SapDriverFactory(getConfiguration()));
        return driver;
    }

}
