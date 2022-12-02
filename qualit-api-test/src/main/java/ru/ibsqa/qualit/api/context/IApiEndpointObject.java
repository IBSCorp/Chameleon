package ru.ibsqa.qualit.api.context;

import ru.ibsqa.qualit.context.IContextObject;
import ru.ibsqa.qualit.definitions.repository.IRepositoryElement;
import ru.ibsqa.qualit.elements.IFacade;

import java.util.List;

public interface IApiEndpointObject extends IContextObject {
    List<? extends IRepositoryElement> getRequests();
    List<? extends IRepositoryElement> getResponses();
    default <T extends IFacade> T getField(String fieldName) {
        return null;
    }
}
