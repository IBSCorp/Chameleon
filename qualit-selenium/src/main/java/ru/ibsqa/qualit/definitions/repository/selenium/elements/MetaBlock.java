package ru.ibsqa.qualit.definitions.repository.selenium.elements;

import ru.ibsqa.qualit.definitions.repository.MetaPriority;
import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;
import ru.ibsqa.qualit.definitions.repository.selenium.AbstractMetaElement;
import ru.ibsqa.qualit.definitions.repository.selenium.IMetaBlock;
import lombok.ToString;

import javax.xml.bind.annotation.XmlRootElement;

@ToString(callSuper = true)
@XmlRootElement(name = "Block")
@MetaPriority(ConfigurationPriority.LOW)
public class MetaBlock extends AbstractMetaElement implements IMetaBlock {

}
