package ru.ibsqa.qualit.definitions.repository.api;

import ru.ibsqa.qualit.definitions.repository.INamedRepositoryElement;
import ru.ibsqa.qualit.definitions.repository.IRepositoryElement;
import lombok.Getter;
import lombok.ToString;
import ru.ibsqa.qualit.definitions.repository.MetaPriority;
import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@ToString(exclude="apiGears")
@XmlRootElement(name = "Credential")
@MetaPriority(ConfigurationPriority.LOW)
public class MetaCredential implements IRepositoryElement, INamedRepositoryElement {

    @Getter
    @XmlAttribute
    private String name;

    @Getter
    @XmlAttribute
    private String user;

    @Getter
    @XmlAttribute
    private String password;

    @Getter
    @XmlAttribute
    private String token;

    @Getter
    @XmlAttribute
    private String csrfFieldName;

    @Getter
    @XmlAttribute
    private AuthenticationEnum authentication;

    @Getter
    private MetaApiGears apiGears;

    void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
        this.apiGears = (MetaApiGears)parent;
    }
}
