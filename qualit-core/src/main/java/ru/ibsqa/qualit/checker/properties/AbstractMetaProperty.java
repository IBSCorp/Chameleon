package ru.ibsqa.qualit.checker.properties;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.annotation.XmlAttribute;

@Slf4j
public abstract class AbstractMetaProperty {

    public abstract PropertyType getType();

    @Getter
    @XmlAttribute
    private String name;

    public Integer getPriority() {
        return 0;
    };

    @Getter
    @XmlAttribute
    private String defaultValue;

}
