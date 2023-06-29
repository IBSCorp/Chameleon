package ru.ibsqa.chameleon.sap.definitions.repository;

import ru.ibsqa.chameleon.definitions.repository.MetaPriority;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;
import ru.ibsqa.chameleon.definitions.repository.selenium.AbstractMetaField;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "SapTab")
@MetaPriority(ConfigurationPriority.LOW)
public class MetaSapTab extends AbstractMetaField {

    public String toString() {
        return "MetaSapTab(super=" + super.toString() + ")";
    }
}