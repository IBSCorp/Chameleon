package ru.ibsqa.qualit.elements.winium;

import org.openqa.selenium.interactions.Actions;
import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;
import ru.ibsqa.qualit.definitions.repository.winium.MetaWinMaskInput;
import ru.ibsqa.qualit.elements.MetaElement;
import ru.ibsqa.qualit.selenium.driver.WiniumSupportedDriver;
import ru.ibsqa.qualit.selenium.enums.KeyEnum;

@MetaElement(value = MetaWinMaskInput.class, supportedDriver = WiniumSupportedDriver.class, priority = ConfigurationPriority.LOW)
public class WinMaskInput extends WinElementFacade {

    @Override
    public void type(String value) {
        click();
        pressKey(KeyEnum.HOME);
        Actions action = new Actions(getDriver());
        char [] myCharArray = value.toCharArray ();

        for (int i = 0; i < myCharArray.length; i++) {
            action.sendKeys(String.valueOf(myCharArray[i]));
            action.perform();
        }
    }

}
