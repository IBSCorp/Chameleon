package ru.ibsqa.qualit.definitions.repository.data;

import lombok.ToString;
import ru.ibsqa.qualit.definitions.repository.MetaPriority;
import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;

import javax.xml.bind.annotation.XmlRootElement;

@ToString(callSuper = true)
@XmlRootElement(name="FieldInteger")
@MetaPriority(ConfigurationPriority.LOW)
public class FieldInteger extends AbstractDataMetaField { // TODO реализовать проверку на соответствие типу при чтении
    @Override
    protected Class getPrimitiveType() {
        return Long.class;
    }
}
