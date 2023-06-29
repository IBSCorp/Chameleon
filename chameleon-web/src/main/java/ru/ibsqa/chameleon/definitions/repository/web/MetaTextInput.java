package ru.ibsqa.chameleon.definitions.repository.web;

import ru.ibsqa.chameleon.definitions.repository.MetaPriority;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;
import ru.ibsqa.chameleon.definitions.repository.selenium.AbstractMetaField;
import lombok.ToString;

import javax.xml.bind.annotation.XmlRootElement;

@ToString(callSuper = true)
@XmlRootElement(name = "TextInput")
@MetaPriority(ConfigurationPriority.LOW)
public class MetaTextInput extends AbstractMetaField {
}
