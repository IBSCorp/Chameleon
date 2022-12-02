package ru.ibsqa.qualit.elements.winium;

import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;
import ru.ibsqa.qualit.definitions.repository.winium.MetaWinCheckBox;
import ru.ibsqa.qualit.elements.MetaElement;
import ru.ibsqa.qualit.selenium.driver.WiniumSupportedDriver;

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
