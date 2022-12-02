package ru.ibsqa.qualit.api.context;

import ru.ibsqa.qualit.context.IContextManager;

public interface IContextManagerApiEndpoint extends IContextManager<IApiEndpointObject> {
    void setCurrentEndpoint(IApiEndpointObject endpoint);
    IApiEndpointObject getCurrentEndpoint();
}
