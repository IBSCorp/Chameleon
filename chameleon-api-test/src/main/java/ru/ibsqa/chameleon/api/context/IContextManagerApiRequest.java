package ru.ibsqa.chameleon.api.context;

import ru.ibsqa.chameleon.context.IContextManager;

public interface IContextManagerApiRequest extends IContextManager<IApiRequestObject> {
    IApiRequestObject createRequest(String requestName, String template);
    IApiRequestObject getCurrentRequest();
    void setCurrentRequest(IApiRequestObject currentRequest);

}
