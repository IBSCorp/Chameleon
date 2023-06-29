package ru.ibsqa.chameleon.definitions.repository.db;

import ru.ibsqa.chameleon.definitions.repository.INamedRepositoryElement;
import ru.ibsqa.chameleon.definitions.repository.IRepositoryElement;
import lombok.Getter;
import lombok.ToString;
import ru.ibsqa.chameleon.definitions.repository.MetaPriority;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@ToString(exclude="dbGears")
@XmlRootElement(name = "Connection")
@MetaPriority(ConfigurationPriority.LOW)
public class MetaConnection implements IRepositoryElement, INamedRepositoryElement {

    @Getter
    @XmlAttribute
    private String name;

    @Getter
    @XmlAttribute
    private String driver;

    @Getter
    @XmlAttribute
    private String url;

    @Getter
    @XmlAttribute
    private boolean autoCommit;

    @Getter
    @XmlElement(name = "Params")
    private MetaParams params;

    @Getter
    private MetaDbGears dbGears;

    void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
        this.dbGears = (MetaDbGears)parent;
    }
}
