package ru.ibsqa.chameleon.definitions.repository.data;

import lombok.ToString;
import ru.ibsqa.chameleon.definitions.repository.MetaPriority;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;

import javax.xml.bind.annotation.XmlRootElement;

@ToString(callSuper = true)
@XmlRootElement(name="FieldString")
@MetaPriority(ConfigurationPriority.LOW)
public class FieldString extends AbstractDataMetaField { // TODO реализовать проверку на соответствие типу при чтении
    @Override
    protected Class getPrimitiveType() {
        return String.class;
    }
}
