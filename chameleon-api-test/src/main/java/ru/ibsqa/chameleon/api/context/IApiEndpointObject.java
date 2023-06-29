package ru.ibsqa.chameleon.api.context;

import ru.ibsqa.chameleon.context.IContextObject;
import ru.ibsqa.chameleon.definitions.repository.IRepositoryElement;
import ru.ibsqa.chameleon.elements.IFacade;

import java.util.List;

public interface IApiEndpointObject extends IContextObject {
    List<? extends IRepositoryElement> getRequests();
    List<? extends IRepositoryElement> getResponses();
    default <T extends IFacade> T getField(String fieldName) {
        return null;
    }
}
