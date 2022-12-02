package ru.ibsqa.qualit.checker.properties;

import lombok.Getter;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "Properties")
public class MetaProperties {
    @XmlElementRef
    @Getter
    private List<AbstractMetaProperty> properties;
}
