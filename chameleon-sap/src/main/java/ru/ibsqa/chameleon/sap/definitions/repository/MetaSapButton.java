package ru.ibsqa.chameleon.sap.definitions.repository;

import ru.ibsqa.chameleon.definitions.repository.MetaPriority;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;
import ru.ibsqa.chameleon.definitions.repository.selenium.AbstractMetaField;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "SapButton")
@MetaPriority(ConfigurationPriority.LOW)
public class MetaSapButton extends AbstractMetaField {

    public String toString() {
        return "MetaSapButton(super=" + super.toString() + ")";
    }
}