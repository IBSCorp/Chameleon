package ru.ibsqa.qualit.sap.elements;

import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;
import ru.ibsqa.qualit.elements.MetaElement;
import ru.ibsqa.qualit.sap.definitions.repository.MetaSapButton;
import ru.ibsqa.qualit.sap.driver.SapSupportedDriver;

@MetaElement(value = MetaSapButton.class, supportedDriver = SapSupportedDriver.class, priority = ConfigurationPriority.LOW)
public class SapButton extends SapElementFacade {
}
