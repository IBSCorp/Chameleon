package ru.ibsqa.chameleon.sap.definitions.repository;

import ru.ibsqa.chameleon.definitions.repository.MetaPriority;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;
import ru.ibsqa.chameleon.definitions.repository.selenium.AbstractMetaField;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "SapTextInput")
@MetaPriority(ConfigurationPriority.LOW)
public class MetaSapTextInput extends AbstractMetaField {

    public String toString() {
        return "MetaTextInput(super=" + super.toString() + ")";
    }
}