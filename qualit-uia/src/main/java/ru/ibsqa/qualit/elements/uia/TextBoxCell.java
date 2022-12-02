package ru.ibsqa.qualit.elements.uia;

import lombok.extern.slf4j.Slf4j;
import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;
import ru.ibsqa.qualit.elements.MetaElement;
import ru.ibsqa.qualit.definitions.repository.uia.MetaTextBoxCell;
import ru.ibsqa.qualit.uia.driver.UiaSupportedDriver;

@Slf4j
@MetaElement(value = MetaTextBoxCell.class, supportedDriver = UiaSupportedDriver.class, priority = ConfigurationPriority.LOW)
public class TextBoxCell extends AbstractCell {
}
