package ru.ibsqa.qualit.definitions.repository.api;

import ru.ibsqa.qualit.definitions.repository.IRepositoryData;
import ru.ibsqa.qualit.definitions.repository.IRepositoryElement;
import lombok.Getter;
import lombok.ToString;
import ru.ibsqa.qualit.definitions.repository.MetaPriority;
import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ToString
@XmlRootElement(name = "ApiGears")
@MetaPriority(ConfigurationPriority.LOW)
public class MetaApiGears implements IRepositoryData, IRepositoryElementApi {

    @Getter
    @XmlElement(name = "Endpoint")
    private List<MetaEndpoint> endpoints;

    @Getter
    @XmlElement(name = "Credential")
    private List<MetaCredential> credentials;

    @Getter
    @XmlElement(name = "Proxy")
    private List<MetaProxy> proxies;

    @Override
    public <ELEMENT extends IRepositoryElement> ELEMENT pickElement(String name, Class<ELEMENT> elementType) {
        if (MetaEndpoint.class == elementType) {
            return pickElement(name, elementType, endpoints);
        } else if (MetaCredential.class == elementType) {
            return pickElement(name, elementType, credentials);
        } else if (MetaProxy.class == elementType) {
            return pickElement(name, elementType, proxies);
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E extends IRepositoryElement> List<E> pickAllElements() {
        return (List<E>)
                Stream.of(endpoints.stream(), credentials.stream(), proxies.stream())
                .flatMap(i -> i)
                .collect(Collectors.toList());
    }

}
