package ru.ibsqa.chameleon.definitions.repository.winium;

import lombok.ToString;
import ru.ibsqa.chameleon.definitions.repository.MetaPriority;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;

import javax.xml.bind.annotation.XmlRootElement;

@ToString(callSuper = true)
@XmlRootElement(name = "WinTextInput")
@MetaPriority(ConfigurationPriority.LOW)
public class MetaWinTextInput extends AbstractMetaWiniumField {
}
