package ru.ibsqa.qualit.elements.web;

import org.openqa.selenium.support.ui.Select;
import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;
import ru.ibsqa.qualit.definitions.repository.selenium.MetaComboBox;
import ru.ibsqa.qualit.elements.MetaElement;
import ru.ibsqa.qualit.elements.selenium.WebElementFacade;
import ru.ibsqa.qualit.selenium.driver.WebSupportedDriver;

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
