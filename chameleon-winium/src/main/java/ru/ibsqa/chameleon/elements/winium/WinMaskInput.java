package ru.ibsqa.chameleon.elements.winium;

import org.openqa.selenium.interactions.Actions;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;
import ru.ibsqa.chameleon.definitions.repository.winium.MetaWinMaskInput;
import ru.ibsqa.chameleon.elements.MetaElement;
import ru.ibsqa.chameleon.selenium.driver.WiniumSupportedDriver;
import ru.ibsqa.chameleon.selenium.enums.KeyEnum;

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
