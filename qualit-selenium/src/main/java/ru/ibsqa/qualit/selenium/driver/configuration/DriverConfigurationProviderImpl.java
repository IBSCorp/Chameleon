package ru.ibsqa.qualit.selenium.driver.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DriverConfigurationProviderImpl implements IDriverConfigurationProvider {
    // Коллекция всех конфигураций
    private List<IDriverConfiguration> configurations;

    @Autowired
    private void collectDriverConfigurations(List<IDriverConfiguration> configurations) {
        this.configurations = configurations;
    }

    @Override
    public IDriverConfiguration getConfiguration() {
        if (configurations.size() > 1) {
            throw new RuntimeException("Too many driver configurations (see IDriverConfiguration beans)");
        }
        // Вернуть или явно указанную конфигурацию или единственную в контексте
        return configurations.size() == 1
                ? configurations.get(0)
                : null;
    }
}
