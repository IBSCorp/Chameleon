package ru.ibsqa.chameleon.definitions.repository.api;

import lombok.ToString;
import ru.ibsqa.chameleon.definitions.repository.MetaPriority;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;

import javax.xml.bind.annotation.XmlRootElement;

@ToString(callSuper = true)
@XmlRootElement(name="Body")
@MetaPriority(ConfigurationPriority.LOW)
public class MetaBody extends IteractionFieldList {
}
