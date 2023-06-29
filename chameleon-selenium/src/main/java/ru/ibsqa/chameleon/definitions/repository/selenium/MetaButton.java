package ru.ibsqa.chameleon.definitions.repository.selenium;

import lombok.ToString;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;
import ru.ibsqa.chameleon.definitions.repository.MetaPriority;
import ru.ibsqa.chameleon.definitions.repository.selenium.adapters.AdapterButton;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@ToString(callSuper = true)
@XmlRootElement(name = "Button")
@XmlJavaTypeAdapter(AdapterButton.class)
@MetaPriority(ConfigurationPriority.LOW)
public class MetaButton extends AbstractMetaField {
}
