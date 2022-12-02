package ru.ibsqa.qualit.selenium.driver;

import org.springframework.beans.factory.annotation.Autowired;
import ru.ibsqa.qualit.selenium.driver.configuration.IDriverConfiguration;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.openqa.selenium.WebDriver;
import ru.ibsqa.qualit.selenium.driver.configuration.IDriverConfigurationProvider;

import javax.annotation.PostConstruct;
import java.util.Objects;

@NoArgsConstructor
public abstract class AbstractDriverFactory<T extends ISupportedDriver> implements IDriverFactory {

    @Getter(AccessLevel.PUBLIC)
    private IDriverConfiguration configuration;

    @Autowired
    private IDriverConfigurationProvider driverConfigurationProvider;

    @PostConstruct
    private void init() {
        if (Objects.isNull(configuration)) {
            configuration = driverConfigurationProvider.getConfiguration();
        }
    }

    public AbstractDriverFactory(IDriverConfiguration configuration) {
        this.configuration = configuration;
    }

    public abstract WebDriver newInstance(String driverId);
}
