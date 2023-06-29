package ru.ibsqa.chameleon.checker.properties;

import ru.ibsqa.chameleon.definitions.repository.MetaPriority;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "CheckBox")
@MetaPriority(ConfigurationPriority.LOW)
public class MetaCheckBox extends AbstractMetaVisibleProperty {
    @Override
    public PropertyType getType() {
        return PropertyType.CheckBox;
    }
}
