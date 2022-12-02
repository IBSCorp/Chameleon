package ru.ibsqa.qualit.page_factory.decorator;

import lombok.NonNull;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.ibsqa.qualit.page_factory.locator.AbstractElementLocatorFactory;
import ru.ibsqa.qualit.selenium.driver.IDriverManager;
import ru.ibsqa.qualit.selenium.driver.ISupportedDriver;

import java.util.Objects;
import java.util.Optional;

public abstract class AbstractDecorateExecutor implements IDecorateExecutor {

    @Autowired
    private IDriverManager driverManager;

    protected String getDriverId(@NonNull ElementLocatorFactory elementLocatorFactory) {
        if (null != elementLocatorFactory && elementLocatorFactory instanceof AbstractElementLocatorFactory){
            return ((AbstractElementLocatorFactory) elementLocatorFactory).getDriverId();
        }
        return null;
    }

    protected ISupportedDriver getSupportedDriver(@NonNull ElementLocatorFactory elementLocatorFactory) {
        return Optional.ofNullable(driverManager.getDriver(getDriverId(elementLocatorFactory)))
                .filter(d -> Objects.nonNull(d.getConfiguration()))
                .map(d -> d.getConfiguration().getDriverType())
                .orElse(null);
    }

    protected boolean isSupportedDriver(@NonNull ElementLocatorFactory elementLocatorFactory, @NonNull ISupportedDriver supportedDriver) {
        return supportedDriver.equals(getSupportedDriver(elementLocatorFactory));
    }

}
