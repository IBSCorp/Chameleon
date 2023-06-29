package ru.ibsqa.chameleon.elements.web;

import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;
import ru.ibsqa.chameleon.definitions.repository.web.MetaLink;
import ru.ibsqa.chameleon.elements.MetaElement;
import ru.ibsqa.chameleon.elements.selenium.WebElementFacade;
import ru.ibsqa.chameleon.i18n.ILocaleManager;
import ru.ibsqa.chameleon.selenium.driver.WebSupportedDriver;

@MetaElement(value = MetaLink.class, supportedDriver = WebSupportedDriver.class, priority = ConfigurationPriority.LOW)
public class Link extends WebElementFacade {

    @Override
    public boolean isEditable() {
        return false;
    }

    @Override
    public void type(String value){
        throw new UnsupportedOperationException(ILocaleManager.message("elementIsReadOnlyErrorMessage"));
    }
}
