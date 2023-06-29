package ru.ibsqa.chameleon.page_factory.pages;

import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;
import ru.ibsqa.chameleon.definitions.repository.selenium.IMetaPage;
import org.springframework.stereotype.Component;
import ru.ibsqa.chameleon.selenium.driver.WebDriverFacade;

@Component
public class DefaultPageSuperClassResolver implements IPageSuperClassResolver {

    @Override
    @SuppressWarnings("unchecked")
    public <T extends IPageObject> Class<T> getSuperClass(IMetaPage metaPage, WebDriverFacade webDriverFacade) {
        return (Class<T>) DefaultPageObject.class;
    }

    @Override
    public ConfigurationPriority getPriority() {
        return ConfigurationPriority.LOW;
    }

}
