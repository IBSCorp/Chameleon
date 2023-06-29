package ru.ibsqa.chameleon.uia.pages;

import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;
import ru.ibsqa.chameleon.definitions.repository.selenium.IMetaCollection;
import ru.ibsqa.chameleon.page_factory.pages.ICollectionSuperClassResolver;
import ru.ibsqa.chameleon.page_factory.pages.IPageObject;
import org.springframework.stereotype.Component;
import ru.ibsqa.chameleon.selenium.driver.WebDriverFacade;
import ru.ibsqa.chameleon.selenium.driver.configuration.IDriverConfiguration;
import ru.ibsqa.chameleon.uia.driver.UiaSupportedDriver;

import java.util.Objects;
import java.util.Optional;

@Component
public class UiaCollectionSuperClassResolver implements ICollectionSuperClassResolver {

    @Override
    @SuppressWarnings("unchecked")
    public <T extends IPageObject> Class<T> getSuperClass(IMetaCollection iMetaCollection, WebDriverFacade webDriverFacade) {
        return Optional.ofNullable(webDriverFacade)
                .filter(d-> Objects.nonNull(d.getConfiguration()))
                .map(WebDriverFacade::getConfiguration)
                .filter(c-> Objects.nonNull(c.getDriverType()))
                .map(IDriverConfiguration::getDriverType)
                .filter(UiaSupportedDriver.UI_AUTOMATION::equals)
                .map(t -> (Class<T>) UiaCollectionObject.class)
                .orElse(null);
    }

    @Override
    public ConfigurationPriority getPriority() {
        return ConfigurationPriority.NORMAL;
    }

}
