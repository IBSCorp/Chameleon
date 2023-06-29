package ru.ibsqa.chameleon.elements.web;

import org.openqa.selenium.support.ui.Select;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;
import ru.ibsqa.chameleon.definitions.repository.selenium.MetaComboBox;
import ru.ibsqa.chameleon.elements.MetaElement;
import ru.ibsqa.chameleon.elements.selenium.WebElementFacade;
import ru.ibsqa.chameleon.selenium.driver.WebSupportedDriver;

@MetaElement(value = MetaComboBox.class, supportedDriver = WebSupportedDriver.class, priority = ConfigurationPriority.LOW)
public class ComboBox extends WebElementFacade {

    @Override
    public void type(String value){
        final Select selectBox = new Select(getWrappedElement());
        selectBox.selectByVisibleText(value);
    }

    @Override
    public String getFieldValue() {
        final Select selectBox = new Select(getWrappedElement());
        return selectBox.getFirstSelectedOption().getText();
    }
}
