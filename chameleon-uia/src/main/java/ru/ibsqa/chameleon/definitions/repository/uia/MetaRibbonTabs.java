package ru.ibsqa.chameleon.definitions.repository.uia;

import ru.ibsqa.chameleon.definitions.repository.MetaPriority;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;
import ru.ibsqa.chameleon.definitions.repository.selenium.AbstractMetaField;
import lombok.ToString;

import javax.xml.bind.annotation.XmlRootElement;

@ToString(callSuper = true)
@XmlRootElement(name = "RibbonTabs")
@MetaPriority(ConfigurationPriority.LOW)
public class MetaRibbonTabs extends AbstractMetaField {
}
