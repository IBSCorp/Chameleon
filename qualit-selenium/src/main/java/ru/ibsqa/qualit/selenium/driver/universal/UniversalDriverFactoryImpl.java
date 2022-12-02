package ru.ibsqa.qualit.selenium.driver.universal;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;
import ru.ibsqa.qualit.selenium.driver.AbstractDriverFactory;
import ru.ibsqa.qualit.selenium.driver.DriverFactory;
import ru.ibsqa.qualit.selenium.driver.configuration.IDriverConfiguration;
import ru.ibsqa.qualit.selenium.driver.configuration.IDriverConfigurationProvider;

import java.lang.reflect.ParameterizedType;
import java.util.*;

/***
 * Универсальная фабрика для создания драйвера. Автоматически подбирает подходящую фабрику в зависимости от типа драйвера.
 * Использует по умолчанию единственную универсальную конфигурацию драйвера. В случае наличия в контексте нескольких конфигураций
 * необходимо самостоятельно создавать бин этой фабрики, передавая нужную конфигурацию в ее конструктор.
 */
@Component
@NoArgsConstructor
@Slf4j
public class UniversalDriverFactoryImpl implements IUniversalDriverFactory {

    private List<AbstractDriverFactory> driverFactories;

    @Autowired
    private void collectDriverFactories(List<AbstractDriverFactory> driverFactories) {
        this.driverFactories = driverFactories;
    }

    // Здесь содержится явно переданная в конструкторе конфигурация.
    // У экземпляра этой фабрики, созданного в результате автоскана, данный атрибут равен null.
    private IDriverConfiguration configuration;

    public UniversalDriverFactoryImpl(IDriverConfiguration configuration){
        this.configuration = configuration;
    }

    @Autowired
    private IDriverConfigurationProvider driverConfigurationProvider;

    @Override
    public IDriverConfiguration getConfiguration() {
        return Optional.ofNullable(configuration)
                .orElseGet(() -> driverConfigurationProvider.getConfiguration());
    }

    @Override
    public WebDriver newInstance(String driverId) {
        return driverFactories.stream()
                .filter(f -> getConfiguration()
                        .getDriverType()
                        .getClass()
                        .equals(
                                ((ParameterizedType) f.getClass().getGenericSuperclass())
                                        .getActualTypeArguments()[0]
                        )
                )
                .sorted(Comparator.comparing(this::getDriverFactoryPriority))
                .map(f -> {
                    WebDriver driver = f.newInstance(driverId);
                    if (Objects.nonNull(driver)) {
                        return driver;
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    private ConfigurationPriority getDriverFactoryPriority(AbstractDriverFactory candidate) {
        return Optional.ofNullable(candidate.getClass().getAnnotation(DriverFactory.class))
                .map(DriverFactory::priority)
                .orElse(ConfigurationPriority.NORMAL);
    }

}
