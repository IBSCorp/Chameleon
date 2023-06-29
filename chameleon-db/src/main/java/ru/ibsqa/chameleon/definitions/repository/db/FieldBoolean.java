package ru.ibsqa.chameleon.definitions.repository.db;

import lombok.ToString;
import ru.ibsqa.chameleon.definitions.repository.MetaPriority;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;

import javax.xml.bind.annotation.XmlRootElement;

@ToString(callSuper = true)
@XmlRootElement(name="FieldBoolean")
@MetaPriority(ConfigurationPriority.LOW)
public class FieldBoolean extends AbstractField { // TODO реализовать проверку на соответствие типу при чтении
    @Override
    public Class getPrimitiveType() {
        return Boolean.class;
    }
}
