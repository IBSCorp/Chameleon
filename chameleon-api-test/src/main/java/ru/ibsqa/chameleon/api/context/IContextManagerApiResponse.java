package ru.ibsqa.chameleon.api.context;

import ru.ibsqa.chameleon.context.IContextManager;

public interface IContextManagerApiResponse extends IContextManager<IApiResponseObject> {
    IApiResponseObject receiveResponse(String responseName);
    IApiResponseObject getCurrentResponse();
}
