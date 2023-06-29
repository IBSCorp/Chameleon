package ru.ibsqa.chameleon.definitions.repository.selenium.elements;

import ru.ibsqa.chameleon.definitions.repository.MetaPriority;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;
import ru.ibsqa.chameleon.definitions.repository.selenium.AbstractMetaElement;
import ru.ibsqa.chameleon.definitions.repository.selenium.IMetaBlock;
import lombok.ToString;

import javax.xml.bind.annotation.XmlRootElement;

@ToString(callSuper = true)
@XmlRootElement(name = "Block")
@MetaPriority(ConfigurationPriority.LOW)
public class MetaBlock extends AbstractMetaElement implements IMetaBlock {

}
