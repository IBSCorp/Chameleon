package ru.ibsqa.chameleon.definitions.repository.api;

import lombok.ToString;
import ru.ibsqa.chameleon.definitions.repository.INamedRepositoryElement;
import ru.ibsqa.chameleon.definitions.repository.IRepositoryElement;
import lombok.Getter;
import ru.ibsqa.chameleon.definitions.repository.MetaPriority;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@ToString(exclude="apiGears")
@XmlRootElement(name = "Proxy")
@MetaPriority(ConfigurationPriority.LOW)
public class MetaProxy implements IRepositoryElement, INamedRepositoryElement {

    @Getter
    @XmlAttribute
    private String name;

    @Getter
    @XmlAttribute
    private String host;

    @Getter
    @XmlAttribute
    private Integer port;

    @Getter
    @XmlAttribute
    private String scheme;

    @Getter
    @XmlAttribute
    private String user;

    @Getter
    @XmlAttribute
    private String password;

    @Getter
    private MetaApiGears apiGears;

    void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
        this.apiGears = (MetaApiGears)parent;
    }

}
