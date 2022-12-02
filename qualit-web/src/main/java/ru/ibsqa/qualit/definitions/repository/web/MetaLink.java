package ru.ibsqa.qualit.definitions.repository.web;

import ru.ibsqa.qualit.definitions.repository.MetaPriority;
import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;
import ru.ibsqa.qualit.definitions.repository.selenium.AbstractMetaField;
import lombok.ToString;

import javax.xml.bind.annotation.XmlRootElement;

@ToString(callSuper = true)
@XmlRootElement(name = "Link")
@MetaPriority(ConfigurationPriority.LOW)
public class MetaLink extends AbstractMetaField {
}
