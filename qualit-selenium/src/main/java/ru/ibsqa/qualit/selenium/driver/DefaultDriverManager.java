package ru.ibsqa.qualit.selenium.driver;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.ibsqa.qualit.i18n.ILocaleManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Component
public class DefaultDriverManager implements IDriverManager {

    @Autowired
    private ApplicationContext appContext;

    @Autowired
    private ILocaleManager localeManager;

    private final ThreadLocal<WebDriverFacade> lastDriver = new ThreadLocal<>();

    @Setter
    private String currentDefaultDriverId;

    @Getter
    private List<WebDriverFacade> drivers;

    @Autowired
    private void collectDrivers(List<WebDriverFacade> drivers) {
        this.drivers = drivers;
    }

    /**
     * Получить драйвер по идентификатору бина
     *
     * @param driverId
     * @return
     */
    @Override
    public WebDriverFacade getDriver(String driverId) {
        if (null == driverId || driverId.isEmpty()) {
            lastDriver.set(getDefaultDriver());
            return lastDriver.get();
        }
        lastDriver.set((WebDriverFacade) appContext.getBean(driverId));
        return lastDriver.get();
    }

    /**
     * Получить последний используемый драйвер
     *
     * @return lastDriver
     */
    @Override
    public WebDriverFacade getLastDriver(){
        if (lastDriver.get() == null) {
            if (drivers.size() == 1) {
                return drivers.get(0);
            } else {
                return drivers.stream()
                        .filter(WebDriverFacade::isDefaultDriver)
                        .findFirst()
                        .orElse(null);
            }
        }
        return lastDriver.get();
    }

    @Override
    public void closeDriver(String driverId) {
        WebDriverFacade driver = getDriver(driverId);
        if (driver != null) {
            driver.quit();
        }
    }

    @Override
    public void closeAllDrivers() {
        drivers.forEach(WebDriverFacade::quit);
    }

    @Override
    public void closeLastDriver() {
        WebDriverFacade driver = getLastDriver();
        if (driver != null) {
            driver.quit();
        }
    }

    private WebDriverFacade getDefaultDriver() {
        if (drivers.size() == 1) {
            return drivers.get(0);
        }

        WebDriverFacade driver = drivers.stream().filter(item -> currentDefaultDriverId == null ? item.isDefaultDriver() : item.getId().equals(currentDefaultDriverId)).findFirst().orElse(null);

        assertNotNull(driver, localeManager.getMessage("noDefaultDriverAssertMessage"));

        return driver;
    }

}

