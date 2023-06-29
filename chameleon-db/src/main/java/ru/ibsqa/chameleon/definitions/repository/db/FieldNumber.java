package ru.ibsqa.chameleon.definitions.repository.db;

import lombok.ToString;
import ru.ibsqa.chameleon.definitions.repository.MetaPriority;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;

import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@ToString(callSuper = true)
@XmlRootElement(name="FieldNumber")
@MetaPriority(ConfigurationPriority.LOW)
public class FieldNumber extends AbstractField { // TODO реализовать проверку на соответствие типу при чтении
    @Override
    public Class getPrimitiveType() {
        return BigDecimal.class;
    }
}
