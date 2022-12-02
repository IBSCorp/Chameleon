package ru.ibsqa.qualit.sap.pages;

import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;
import ru.ibsqa.qualit.definitions.repository.selenium.IMetaCollection;
import ru.ibsqa.qualit.page_factory.pages.ICollectionSuperClassResolver;
import ru.ibsqa.qualit.page_factory.pages.IPageObject;
import org.springframework.stereotype.Component;
import ru.ibsqa.qualit.sap.driver.SapSupportedDriver;
import ru.ibsqa.qualit.selenium.driver.WebDriverFacade;
import ru.ibsqa.qualit.selenium.driver.configuration.IDriverConfiguration;

import java.util.Objects;
import java.util.Optional;

@Component
public class SapCollectionSuperClassResolver implements ICollectionSuperClassResolver {

    @Override
    @SuppressWarnings("unchecked")
    public <T extends IPageObject> Class<T> getSuperClass(IMetaCollection iMetaCollection, WebDriverFacade webDriverFacade) {
        return Optional.ofNullable(webDriverFacade)
                .filter(d-> Objects.nonNull(d.getConfiguration()))
                .map(WebDriverFacade::getConfiguration)
                .filter(c-> Objects.nonNull(c.getDriverType()))
                .map(IDriverConfiguration::getDriverType)
                .filter(SapSupportedDriver.SAP::equals)
                .map(t -> (Class<T>) SapCollectionObject.class)
                .orElse(null);
    }

    @Override
    public ConfigurationPriority getPriority() {
        return ConfigurationPriority.NORMAL;
    }

}
