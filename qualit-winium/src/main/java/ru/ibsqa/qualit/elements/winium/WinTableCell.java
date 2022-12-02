package ru.ibsqa.qualit.elements.winium;

import org.openqa.selenium.WebDriverException;
import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;
import ru.ibsqa.qualit.definitions.repository.winium.MetaWinTableCell;
import ru.ibsqa.qualit.elements.MetaElement;
import ru.ibsqa.qualit.i18n.ILocaleManager;
import ru.ibsqa.qualit.selenium.driver.WiniumSupportedDriver;

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
