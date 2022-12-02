package ru.ibsqa.qualit.elements.web;

import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;
import ru.ibsqa.qualit.definitions.repository.selenium.MetaCheckBox;
import ru.ibsqa.qualit.elements.MetaElement;
import ru.ibsqa.qualit.elements.selenium.WebElementFacade;
import ru.ibsqa.qualit.selenium.driver.WebSupportedDriver;

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
