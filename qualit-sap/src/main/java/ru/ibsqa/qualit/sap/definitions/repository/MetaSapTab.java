package ru.ibsqa.qualit.sap.definitions.repository;

import ru.ibsqa.qualit.definitions.repository.MetaPriority;
import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;
import ru.ibsqa.qualit.definitions.repository.selenium.AbstractMetaField;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "SapTab")
@MetaPriority(ConfigurationPriority.LOW)
public class MetaSapTab extends AbstractMetaField {

    public String toString() {
        return "MetaSapTab(super=" + super.toString() + ")";
    }
}