package ru.ibsqa.chameleon.page_factory.pages;

import ru.ibsqa.chameleon.definitions.repository.selenium.IMetaPage;
import ru.ibsqa.chameleon.selenium.driver.WebDriverFacade;

public interface IPageSuperClassManager {
    <T extends IPageObject> Class<T> getSuperClass(IMetaPage metaPage, WebDriverFacade webDriverFacade);
}
