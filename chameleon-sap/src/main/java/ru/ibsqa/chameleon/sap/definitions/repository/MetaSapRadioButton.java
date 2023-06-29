package ru.ibsqa.chameleon.sap.definitions.repository;

import ru.ibsqa.chameleon.definitions.repository.MetaPriority;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;
import ru.ibsqa.chameleon.definitions.repository.selenium.AbstractMetaField;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "SapRadioButton")
@MetaPriority(ConfigurationPriority.LOW)
public class MetaSapRadioButton extends AbstractMetaField {

    public String toString() {
        return "MetaSapRadioButton(super=" + super.toString() + ")";
    }
}