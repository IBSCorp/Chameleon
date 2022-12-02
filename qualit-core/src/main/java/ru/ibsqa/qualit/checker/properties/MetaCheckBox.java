package ru.ibsqa.qualit.checker.properties;

import ru.ibsqa.qualit.definitions.repository.MetaPriority;
import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "CheckBox")
@MetaPriority(ConfigurationPriority.LOW)
public class MetaCheckBox extends AbstractMetaVisibleProperty {
    @Override
    public PropertyType getType() {
        return PropertyType.CheckBox;
    }
}
