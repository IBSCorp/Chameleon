package ru.ibsqa.chameleon.definitions.repository.db;

import lombok.ToString;
import ru.ibsqa.chameleon.definitions.repository.MetaPriority;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;

import javax.xml.bind.annotation.XmlRootElement;

@ToString(callSuper = true)
@XmlRootElement(name="Params")
@MetaPriority(ConfigurationPriority.LOW)
public class MetaParams extends AbstractFieldList {
}
