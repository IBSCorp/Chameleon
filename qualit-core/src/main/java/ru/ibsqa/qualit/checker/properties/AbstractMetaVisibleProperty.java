package ru.ibsqa.qualit.checker.properties;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.annotation.XmlAttribute;
import java.util.Optional;

@Slf4j
public abstract class AbstractMetaVisibleProperty extends AbstractMetaProperty {

    public void validate(ValidateResult validateResult, String value) {
        if (isRequired() && StringUtils.isEmpty(value)) {
            validateResult.appendError(String.format("Не заполнен обязательный входной параметр %s", getLabel()));
        }
    }

    @XmlAttribute
    private String label;

    public String getLabel() {
        return Optional.ofNullable(label).map(l -> String.format("%s (%s)", getName(), l)).orElse(getName());
    }

    private Boolean required;

    public boolean isRequired() {
        return Optional.ofNullable(required).orElse(false);
    }

    @XmlAttribute
    private Boolean environment;

    public boolean isEnvironment() {
        return Optional.ofNullable(environment).orElse(false);
    }

    @Getter
    @XmlAttribute
    private Integer priority;

    public Integer getPriority() {
        return Optional.ofNullable(priority).orElse(0);
    }

}
