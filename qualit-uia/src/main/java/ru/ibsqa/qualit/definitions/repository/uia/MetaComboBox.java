package ru.ibsqa.qualit.definitions.repository.uia;

import lombok.ToString;
import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;
import ru.ibsqa.qualit.definitions.repository.MetaPriority;
import ru.ibsqa.qualit.definitions.repository.selenium.AbstractMetaField;

import javax.xml.bind.annotation.XmlRootElement;

@ToString(callSuper = true)
@XmlRootElement(name = "ComboBox")
@MetaPriority(ConfigurationPriority.LOW)
public class MetaComboBox extends AbstractMetaField {
}