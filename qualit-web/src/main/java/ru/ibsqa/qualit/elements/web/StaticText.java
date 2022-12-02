package ru.ibsqa.qualit.elements.web;

import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;
import ru.ibsqa.qualit.definitions.repository.web.MetaStaticText;
import ru.ibsqa.qualit.elements.MetaElement;
import ru.ibsqa.qualit.elements.selenium.WebElementFacade;
import ru.ibsqa.qualit.i18n.ILocaleManager;
import ru.ibsqa.qualit.selenium.driver.WebSupportedDriver;

@MetaElement(value = MetaStaticText.class, supportedDriver = WebSupportedDriver.class, priority = ConfigurationPriority.LOW)
public class StaticText extends WebElementFacade {

    @Override
    public boolean isEditable() {
        return false;
    }

    @Override
    public void type(String value){
        throw new UnsupportedOperationException(ILocaleManager.message("elementIsReadOnlyErrorMessage"));
    }
}
