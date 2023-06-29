package ru.ibsqa.chameleon.elements.winium;

import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;
import ru.ibsqa.chameleon.definitions.repository.winium.MetaWinTextInput;
import ru.ibsqa.chameleon.elements.MetaElement;
import ru.ibsqa.chameleon.selenium.driver.WiniumSupportedDriver;

@MetaElement(value = MetaWinTextInput.class, supportedDriver = WiniumSupportedDriver.class, priority = ConfigurationPriority.LOW)
public class WinTextInput extends WinElementFacade {
}
