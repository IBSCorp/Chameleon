package ru.ibsqa.chameleon.page_factory.pages;

import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;
import ru.ibsqa.chameleon.definitions.repository.selenium.IMetaCollection;
import ru.ibsqa.chameleon.selenium.driver.WebDriverFacade;

public interface ICollectionSuperClassResolver {

    <T extends IPageObject> Class<T> getSuperClass(IMetaCollection metaCollection, WebDriverFacade webDriverFacade);
    default ConfigurationPriority getPriority() {
        return ConfigurationPriority.HIGH;
    }

}
