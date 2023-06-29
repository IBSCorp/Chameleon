package ru.ibsqa.chameleon.elements.winium;

import org.openqa.selenium.WebDriverException;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;
import ru.ibsqa.chameleon.definitions.repository.winium.MetaWinTableCell;
import ru.ibsqa.chameleon.elements.MetaElement;
import ru.ibsqa.chameleon.i18n.ILocaleManager;
import ru.ibsqa.chameleon.selenium.driver.WiniumSupportedDriver;

@MetaElement(value = MetaWinTableCell.class, supportedDriver = WiniumSupportedDriver.class, priority = ConfigurationPriority.LOW)
public class WinTableCell extends WinElementFacade {

    @Override
    public boolean isEditable() {
        return false;
    }

    @Override
    public String getFieldValue() {
        try {
            return getWrappedElement().getText().replaceAll("\\u00A0", " ");
        }catch (WebDriverException e){
        }
        return "";
    }

    @Override
    public void type(String value){
        throw new UnsupportedOperationException(ILocaleManager.message("elementIsReadOnlyErrorMessage"));
    }
}
