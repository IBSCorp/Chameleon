package ru.ibsqa.qualit.soap;

import ru.ibsqa.qualit.api.context.IApiRequestObject;
import ru.ibsqa.qualit.context.IContextManager;

public interface IContextManagerSoapRequest  extends IContextManager<IApiRequestObject> {
    IApiRequestObject createRequestFromWSDL(String requestName, String template);
    IApiRequestObject createRequest(String requestName, String template);
    IApiRequestObject getCurrentRequest();

}
