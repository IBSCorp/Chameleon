package ru.ibsqa.qualit.page_factory.pages;

import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;
import ru.ibsqa.qualit.definitions.repository.selenium.IMetaCollection;
import org.springframework.stereotype.Component;
import ru.ibsqa.qualit.selenium.driver.WebDriverFacade;

@Component
public class DefaultCollectionSuperClassResolver implements ICollectionSuperClassResolver {

    @Override
    @SuppressWarnings("unchecked")
    public <T extends IPageObject> Class<T> getSuperClass(IMetaCollection metaCollection, WebDriverFacade webDriverFacade) {
        return (Class<T>) DefaultCollectionObject.class;
    }

    @Override
    public ConfigurationPriority getPriority() {
        return ConfigurationPriority.LOW;
    }

}
