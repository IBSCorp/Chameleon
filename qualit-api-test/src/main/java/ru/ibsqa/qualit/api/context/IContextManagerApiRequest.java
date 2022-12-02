package ru.ibsqa.qualit.api.context;

import ru.ibsqa.qualit.context.IContextManager;

public interface IContextManagerApiRequest extends IContextManager<IApiRequestObject> {
    IApiRequestObject createRequest(String requestName, String template);
    IApiRequestObject getCurrentRequest();
    void setCurrentRequest(IApiRequestObject currentRequest);

}
