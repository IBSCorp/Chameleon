package ru.ibsqa.qualit.sap.elements;

import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;
import ru.ibsqa.qualit.elements.MetaElement;
import ru.ibsqa.qualit.sap.definitions.repository.MetaSapTextInput;
import ru.ibsqa.qualit.sap.driver.SapSupportedDriver;

@MetaElement(value = MetaSapTextInput.class, supportedDriver = SapSupportedDriver.class, priority = ConfigurationPriority.LOW)
public class SapTextInput extends SapElementFacade {
}
