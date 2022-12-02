package ru.ibsqa.qualit.elements.winium;

import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;
import ru.ibsqa.qualit.definitions.repository.winium.MetaWinTextInput;
import ru.ibsqa.qualit.elements.MetaElement;
import ru.ibsqa.qualit.selenium.driver.WiniumSupportedDriver;

@MetaElement(value = MetaWinTextInput.class, supportedDriver = WiniumSupportedDriver.class, priority = ConfigurationPriority.LOW)
public class WinTextInput extends WinElementFacade {
}
