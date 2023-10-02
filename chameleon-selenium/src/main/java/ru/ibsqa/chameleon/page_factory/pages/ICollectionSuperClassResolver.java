package ru.ibsqa.chameleon.page_factory.pages;

import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;
import ru.ibsqa.chameleon.definitions.repository.selenium.IMetaCollection;
import ru.ibsqa.chameleon.selenium.driver.IDriverFacade;

public interface ICollectionSuperClassResolver {

    <T extends IPageObject> Class<T> getSuperClass(IMetaCollection metaCollection, IDriverFacade driverFacade);
    default ConfigurationPriority getPriority() {
        return ConfigurationPriority.HIGH;
    }

}
