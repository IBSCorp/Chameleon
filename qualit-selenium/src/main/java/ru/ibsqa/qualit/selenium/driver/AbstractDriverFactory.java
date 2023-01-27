package ru.ibsqa.qualit.selenium.driver;

import org.springframework.beans.factory.annotation.Autowired;
import ru.ibsqa.qualit.selenium.driver.configuration.IDriverConfiguration;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.openqa.selenium.WebDriver;
import ru.ibsqa.qualit.selenium.driver.configuration.IDriverConfigurationProvider;

@NoArgsConstructor
public abstract class AbstractDriverFactory<T extends ISupportedDriver> implements IDriverFactory {

    @Getter(AccessLevel.PUBLIC)
    private IDriverConfiguration configuration;

    @Autowired
    private IDriverConfigurationProvider driverConfigurationProvider;

    public AbstractDriverFactory(IDriverConfiguration configuration) {
        this.configuration = configuration;
    }

    public abstract WebDriver newInstance(String driverId);

    public WebDriver newInstance(String driverId, IDriverConfiguration configuration) {
        this.configuration = configuration;
        return newInstance(driverId);
    }

}
