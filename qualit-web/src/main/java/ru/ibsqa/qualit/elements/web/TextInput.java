package ru.ibsqa.qualit.elements.web;

import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;
import ru.ibsqa.qualit.definitions.repository.web.MetaTextInput;
import ru.ibsqa.qualit.elements.MetaElement;
import ru.ibsqa.qualit.elements.selenium.WebElementFacade;
import ru.ibsqa.qualit.selenium.driver.WebSupportedDriver;

@MetaElement(value = MetaTextInput.class, supportedDriver = WebSupportedDriver.class, priority = ConfigurationPriority.LOW)
public class TextInput extends WebElementFacade {
}
