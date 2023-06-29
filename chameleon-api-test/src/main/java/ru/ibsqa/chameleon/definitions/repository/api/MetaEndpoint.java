package ru.ibsqa.chameleon.definitions.repository.api;

import ru.ibsqa.chameleon.api.context.IApiEndpointObject;
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
import java.util.List;

@ToString(exclude="apiGears")
@XmlRootElement(name = "Endpoint")
@MetaPriority(ConfigurationPriority.LOW)
public class MetaEndpoint implements IRepositoryElement, INamedRepositoryElement, IApiEndpointObject {

    @Getter
    @XmlAttribute
    private String name;

    @Getter
    @XmlAttribute
    private String path;

    @Getter
    @XmlElement(name = "Request")
    private List<MetaRequest> requests;

    @Getter
    @XmlElement(name = "Response")
    private List<MetaResponse> responses;

    @Getter
    private MetaApiGears apiGears;

    void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
        this.apiGears = (MetaApiGears)parent;
    }
}
