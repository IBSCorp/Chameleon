package ru.ibsqa.chameleon.definitions.repository.uia;

import lombok.ToString;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;
import ru.ibsqa.chameleon.definitions.repository.MetaPriority;
import ru.ibsqa.chameleon.definitions.repository.selenium.AbstractMetaField;

import javax.xml.bind.annotation.XmlRootElement;

@ToString(callSuper = true)
@XmlRootElement(name = "Button")
@MetaPriority(ConfigurationPriority.LOW)
public class MetaButton extends AbstractMetaField {
}