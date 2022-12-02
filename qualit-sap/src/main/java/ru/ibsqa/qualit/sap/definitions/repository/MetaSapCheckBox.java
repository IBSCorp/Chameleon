package ru.ibsqa.qualit.sap.definitions.repository;

import ru.ibsqa.qualit.definitions.repository.MetaPriority;
import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;
import ru.ibsqa.qualit.definitions.repository.selenium.AbstractMetaField;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "SapCheckBox")
@MetaPriority(ConfigurationPriority.LOW)
public class MetaSapCheckBox extends AbstractMetaField {

    public String toString() {
        return "MetaSapCheckBox(super=" + super.toString() + ")";
    }
}