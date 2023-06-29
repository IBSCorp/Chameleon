package ru.ibsqa.chameleon.page_factory.pages;

import ru.ibsqa.chameleon.definitions.repository.selenium.IMetaCollection;
import ru.ibsqa.chameleon.selenium.driver.WebDriverFacade;

public interface ICollectionSuperClassManager {
    <T extends IPageObject> Class<T> getSuperClass(IMetaCollection metaCollection, WebDriverFacade webDriverFacade);
}
