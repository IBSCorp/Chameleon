package ru.ibsqa.qualit.page_factory.pages;

import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;
import ru.ibsqa.qualit.definitions.repository.selenium.IMetaPage;
import ru.ibsqa.qualit.selenium.driver.WebDriverFacade;

public interface IPageSuperClassResolver {

    <T extends IPageObject> Class<T> getSuperClass(IMetaPage metaPage, WebDriverFacade webDriverFacade);
    default ConfigurationPriority getPriority() {
        return ConfigurationPriority.HIGH;
    }

}
