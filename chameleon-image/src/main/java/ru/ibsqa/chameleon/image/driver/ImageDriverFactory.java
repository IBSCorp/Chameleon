package ru.ibsqa.chameleon.image.driver;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;
import ru.ibsqa.chameleon.i18n.ILocaleManager;
import ru.ibsqa.chameleon.selenium.driver.DriverFactory;
import ru.ibsqa.chameleon.selenium.driver.AbstractDriverFactory;
import ru.ibsqa.chameleon.selenium.driver.configuration.IDriverConfiguration;
import ru.ibsqa.chameleon.selenium.driver.exceptions.NoSupportedDriverException;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.lang.reflect.Constructor;

@DriverFactory(priority = ConfigurationPriority.LOW)
@NoArgsConstructor
@Slf4j
@Component
public class ImageDriverFactory extends AbstractDriverFactory<ImageSupportedDriver> {

    public ImageDriverFactory(IDriverConfiguration configuration) {
        super(configuration);
    }

    // TODO реализовать сохранение драйвера в файл по аналогии с SeleniumDriverFactory
    @Override
    public WebDriver newInstance(String driverId) {
        ImageDriver driver;
        try {
            Class<? extends WebDriver> aClass = getConfiguration().getDriverType().getAsClass().asSubclass(ImageDriver.class);
            Constructor<? extends WebDriver> constructor = aClass.getConstructor(DesiredCapabilities.class);
            driver = (ImageDriver) constructor.newInstance(getConfiguration().getDesiredCapabilities());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new NoSupportedDriverException(ILocaleManager.message("noSupportedDriverException", getConfiguration().getDriverType().name()));
        }
        driver.setImplicitlyWait(getConfiguration().getImplicitlyWait());
        driver.setConfiguration(getConfiguration());
    //    driver.setImplicitlywait(getConfiguration().getImplicitlyWait());
        return driver;
    }

}