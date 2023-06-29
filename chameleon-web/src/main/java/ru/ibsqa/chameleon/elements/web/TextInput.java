package ru.ibsqa.chameleon.elements.web;

import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;
import ru.ibsqa.chameleon.definitions.repository.web.MetaTextInput;
import ru.ibsqa.chameleon.elements.MetaElement;
import ru.ibsqa.chameleon.elements.selenium.WebElementFacade;
import ru.ibsqa.chameleon.selenium.driver.WebSupportedDriver;

@MetaElement(value = MetaTextInput.class, supportedDriver = WebSupportedDriver.class, priority = ConfigurationPriority.LOW)
public class TextInput extends WebElementFacade {
}
