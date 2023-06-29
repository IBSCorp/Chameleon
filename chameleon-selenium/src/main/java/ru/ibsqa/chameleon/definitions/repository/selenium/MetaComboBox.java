package ru.ibsqa.chameleon.definitions.repository.selenium;

import lombok.ToString;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;
import ru.ibsqa.chameleon.definitions.repository.MetaPriority;

import javax.xml.bind.annotation.XmlRootElement;

@ToString(callSuper = true)
@XmlRootElement(name = "ComboBox")
@MetaPriority(ConfigurationPriority.LOW)
public class MetaComboBox extends AbstractMetaField {
}
