package ru.ibsqa.chameleon.checker.properties;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

@XmlRootElement(name = "Text")
@Slf4j
public class MetaText extends AbstractMetaVisibleProperty {

    @Getter
    @XmlAttribute
    private String regexp;

    @Override
    public PropertyType getType() {
        return PropertyType.Text;
    }

    @Override
    public void validate(ValidateResult validateResult, String value) {
        super.validate(validateResult, value);
        if (validateResult.isValid() && Objects.nonNull(value) && Objects.nonNull(regexp) && !value.matches(regexp)) {
            validateResult.appendError(String.format("Значение [%s] входного параметра %s не соответствует правилу [%s]", value, getLabel(), regexp));
        }
    }

}
