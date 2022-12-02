package ru.ibsqa.qualit.page_factory.pages;

import ru.ibsqa.qualit.definitions.repository.selenium.IMetaPage;
import ru.ibsqa.qualit.selenium.driver.WebDriverFacade;

public interface IPageSuperClassManager {
    <T extends IPageObject> Class<T> getSuperClass(IMetaPage metaPage, WebDriverFacade webDriverFacade);
}
