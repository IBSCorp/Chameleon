package ru.ibsqa.chameleon.elements.winium;

import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;
import ru.ibsqa.chameleon.definitions.repository.winium.MetaWinCheckBox;
import ru.ibsqa.chameleon.elements.MetaElement;
import ru.ibsqa.chameleon.selenium.driver.WiniumSupportedDriver;

@MetaElement(value = MetaWinCheckBox.class, supportedDriver = WiniumSupportedDriver.class, priority = ConfigurationPriority.LOW)
public class WinCheckBox extends WinElementFacade {

    @Override
    public boolean isEditable() {
        return false;
    }

    @Override
    public void type(String value) {
        if (Boolean.valueOf(value) == isSelected()){
            return;
        }
        super.click();
    }

    @Override
    public String getFieldValue() {
        return  isSelected()? "true" : "false";
    }

}
