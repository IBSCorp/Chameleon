package ru.ibsqa.qualit.page_factory.pages;

import ru.ibsqa.qualit.definitions.repository.selenium.IMetaCollection;
import ru.ibsqa.qualit.selenium.driver.WebDriverFacade;

public interface ICollectionSuperClassManager {
    <T extends IPageObject> Class<T> getSuperClass(IMetaCollection metaCollection, WebDriverFacade webDriverFacade);
}
