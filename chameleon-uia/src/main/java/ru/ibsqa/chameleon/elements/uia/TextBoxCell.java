package ru.ibsqa.chameleon.elements.uia;

import lombok.extern.slf4j.Slf4j;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;
import ru.ibsqa.chameleon.elements.MetaElement;
import ru.ibsqa.chameleon.definitions.repository.uia.MetaTextBoxCell;
import ru.ibsqa.chameleon.uia.driver.UiaSupportedDriver;

@Slf4j
@MetaElement(value = MetaTextBoxCell.class, supportedDriver = UiaSupportedDriver.class, priority = ConfigurationPriority.LOW)
public class TextBoxCell extends AbstractCell {
}
