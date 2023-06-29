package ru.ibsqa.chameleon.api.context;

import ru.ibsqa.chameleon.context.IContextManager;

public interface IContextManagerApiEndpoint extends IContextManager<IApiEndpointObject> {
    void setCurrentEndpoint(IApiEndpointObject endpoint);
    IApiEndpointObject getCurrentEndpoint();
}
