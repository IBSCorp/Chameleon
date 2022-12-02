package ru.ibsqa.qualit.api.context;

import ru.ibsqa.qualit.context.IContextManager;

public interface IContextManagerApiResponse extends IContextManager<IApiResponseObject> {
    IApiResponseObject receiveResponse(String responseName);
    IApiResponseObject getCurrentResponse();
}
