package ru.ibsqa.chameleon.sap.elements;

import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;
import ru.ibsqa.chameleon.elements.MetaElement;
import ru.ibsqa.chameleon.sap.definitions.repository.MetaSapButton;
import ru.ibsqa.chameleon.sap.driver.SapSupportedDriver;

@MetaElement(value = MetaSapButton.class, supportedDriver = SapSupportedDriver.class, priority = ConfigurationPriority.LOW)
public class SapButton extends SapElementFacade {
}
