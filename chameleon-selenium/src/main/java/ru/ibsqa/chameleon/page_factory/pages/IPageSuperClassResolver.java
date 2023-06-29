package ru.ibsqa.chameleon.page_factory.pages;

import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;
import ru.ibsqa.chameleon.definitions.repository.selenium.IMetaPage;
import ru.ibsqa.chameleon.selenium.driver.WebDriverFacade;

public interface IPageSuperClassResolver {

    <T extends IPageObject> Class<T> getSuperClass(IMetaPage metaPage, WebDriverFacade webDriverFacade);
    default ConfigurationPriority getPriority() {
        return ConfigurationPriority.HIGH;
    }

}
