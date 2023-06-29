package ru.ibsqa.chameleon.uia.driver;

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
import java.util.Objects;

@DriverFactory(priority = ConfigurationPriority.LOW)
@NoArgsConstructor
@Slf4j
@Component
public class UiaDriverFactory extends AbstractDriverFactory<UiaSupportedDriver> {

    public UiaDriverFactory(IDriverConfiguration configuration) {
        super(configuration);
    }

    // TODO реализовать сохранение состояния драйвера в файл
    @Override
    public WebDriver newInstance(String driverId) {
        UiaDriver driver;

        try {
            Class<? extends WebDriver> aClass = getConfiguration().getDriverType().getAsClass();
            Constructor<? extends WebDriver> constructor = aClass.getConstructor(DesiredCapabilities.class);
            driver = (UiaDriver) constructor.newInstance(getConfiguration().getDesiredCapabilities());
        } catch (Exception e) {
            log.error(Objects.nonNull(e.getMessage()) ? e.getMessage() : e.toString(), e);
            throw new NoSupportedDriverException(ILocaleManager.message("noSupportedDriverException", getConfiguration().getDriverType().name()));
        }

        return driver;
    }
}
