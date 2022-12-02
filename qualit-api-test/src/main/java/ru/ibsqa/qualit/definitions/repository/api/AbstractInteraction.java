package ru.ibsqa.qualit.definitions.repository.api;

import ru.ibsqa.qualit.definitions.repository.IRepositoryElement;
import lombok.Getter;
import lombok.ToString;

import javax.xml.bind.Unmarshaller;

@ToString(exclude="endpoint")
public abstract class AbstractInteraction implements IRepositoryElement {

    @Getter
    private MetaEndpoint endpoint;

    void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
        this.endpoint = (MetaEndpoint)parent;
    }

    public abstract boolean isRequest();

    public abstract boolean isResponse();

}
