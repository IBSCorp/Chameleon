package ru.ibsqa.qualit.checker.properties;

import lombok.Getter;

import javax.xml.bind.annotation.XmlAttribute;
import java.util.Optional;

public class MetaItem {

    @XmlAttribute
    @Getter
    private String value;

    @XmlAttribute
    private String label;

    public String getLabel() {
        return Optional.ofNullable(label).map(l -> String.format("%s (%s)", value, l)).orElse(value);
    }
}
