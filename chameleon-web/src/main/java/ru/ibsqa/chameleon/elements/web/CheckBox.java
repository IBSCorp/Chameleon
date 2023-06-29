package ru.ibsqa.chameleon.elements.web;

import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;
import ru.ibsqa.chameleon.definitions.repository.selenium.MetaCheckBox;
import ru.ibsqa.chameleon.elements.MetaElement;
import ru.ibsqa.chameleon.elements.selenium.WebElementFacade;
import ru.ibsqa.chameleon.selenium.driver.WebSupportedDriver;

@MetaElement(value = MetaCheckBox.class, supportedDriver = WebSupportedDriver.class, priority = ConfigurationPriority.LOW)
public class CheckBox extends WebElementFacade {

    @Override
    public void type(String value) {
        if (Boolean.valueOf(value) == isSelected()){
            return;
        }
        super.click();
    }

    @Override
    public String getFieldValue() {
        return String.valueOf(isSelected());
    }
}
