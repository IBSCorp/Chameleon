package ru.ibsqa.chameleon.definitions.repository.db;

import ru.ibsqa.chameleon.definitions.repository.IRepositoryData;
import ru.ibsqa.chameleon.definitions.repository.IRepositoryElement;
import lombok.Getter;
import lombok.ToString;
import ru.ibsqa.chameleon.definitions.repository.MetaPriority;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ToString
@XmlRootElement(name = "DbGears")
@MetaPriority(ConfigurationPriority.LOW)
public class MetaDbGears implements IRepositoryData, IRepositoryElementDb {

    @Getter
    @XmlElement(name = "Connection")
    private List<MetaConnection> connections;

    @Getter
    @XmlElement(name = "Query")
    private List<MetaQuery> queries;

    @Override
    public <ELEMENT extends IRepositoryElement> ELEMENT pickElement(String name, Class<ELEMENT> elementType) {
        if (MetaConnection.class == elementType) {
            return pickElement(name, elementType, connections);
        } else if (MetaQuery.class == elementType) {
            return pickElement(name, elementType, queries);
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<IRepositoryElement> pickAllElements() {
        return Stream.concat(connections.stream(), queries.stream()).collect(Collectors.toList());
    }

}
