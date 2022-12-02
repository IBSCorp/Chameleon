package ru.ibsqa.qualit.definitions.repository.winium;

import lombok.ToString;
import ru.ibsqa.qualit.definitions.repository.MetaPriority;
import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;

import javax.xml.bind.annotation.XmlRootElement;

@ToString(callSuper = true)
@XmlRootElement(name = "WinTextInput")
@MetaPriority(ConfigurationPriority.LOW)
public class MetaWinTextInput extends AbstractMetaWiniumField {
}
