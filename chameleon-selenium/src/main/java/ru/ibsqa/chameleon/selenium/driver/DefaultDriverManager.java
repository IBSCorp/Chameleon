package ru.ibsqa.chameleon.selenium.driver;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.ibsqa.chameleon.i18n.ILocaleManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Component
public class DefaultDriverManager implements IDriverManager {

    @Autowired
    private ApplicationContext appContext;

    @Autowired
    private ILocaleManager localeManager;

    private final ThreadLocal<IDriverFacade> lastDriver = new InheritableThreadLocal<>();

    @Setter
    private String currentDefaultDriverId;

    @Getter
    private List<IDriverFacade> drivers;

    @Autowired
    private void collectDrivers(List<IDriverFacade> drivers) {
        this.drivers = drivers;
    }

    /**
     * Получить драйвер по идентификатору бина
     *
     * @param driverId
     * @return
     */
    @Override
    public IDriverFacade getDriver(String driverId) {
        if (null == driverId || driverId.isEmpty()) {
            lastDriver.set(getDefaultDriver());
            return lastDriver.get();
        }
        lastDriver.set((IDriverFacade) appContext.getBean(driverId));
        return lastDriver.get();
    }

    /**
     * Получить последний используемый драйвер
     *
     * @return lastDriver
     */
    @Override
    public IDriverFacade getLastDriver(){
        if (lastDriver.get() == null) {
            if (drivers.size() == 1) {
                return drivers.get(0);
            } else {
                return drivers.stream()
                        .filter(IDriverFacade::isDefaultDriver)
                        .findFirst()
                        .orElse(null);
            }
        }
        return lastDriver.get();
    }

    @Override
    public void closeDriver(String driverId) {
        IDriverFacade driver = getDriver(driverId);
        if (driver != null) {
            driver.quit();
        }
    }

    @Override
    public void closeAllDrivers() {
        drivers.forEach(IDriverFacade::quit);
    }

    @Override
    public void closeLastDriver() {
        IDriverFacade driver = getLastDriver();
        if (driver != null) {
            driver.quit();
        }
    }

    private IDriverFacade getDefaultDriver() {
        if (drivers.size() == 1) {
            return drivers.get(0);
        }

        IDriverFacade driver = drivers.stream()
                .filter(item -> currentDefaultDriverId == null ? item.isDefaultDriver() : item.getId().equals(currentDefaultDriverId))
                .findFirst().orElse(null);

        assertNotNull(driver, localeManager.getMessage("noDefaultDriverAssertMessage"));

        return driver;
    }

}

