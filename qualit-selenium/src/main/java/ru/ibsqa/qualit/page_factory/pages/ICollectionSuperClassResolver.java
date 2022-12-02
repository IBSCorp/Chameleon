package ru.ibsqa.qualit.page_factory.pages;

import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;
import ru.ibsqa.qualit.definitions.repository.selenium.IMetaCollection;
import ru.ibsqa.qualit.selenium.driver.WebDriverFacade;

public interface ICollectionSuperClassResolver {

    <T extends IPageObject> Class<T> getSuperClass(IMetaCollection metaCollection, WebDriverFacade webDriverFacade);
    default ConfigurationPriority getPriority() {
        return ConfigurationPriority.HIGH;
    }

}
