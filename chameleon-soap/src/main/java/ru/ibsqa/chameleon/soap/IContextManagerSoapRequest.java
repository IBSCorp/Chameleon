package ru.ibsqa.chameleon.soap;

import ru.ibsqa.chameleon.api.context.IApiRequestObject;
import ru.ibsqa.chameleon.context.IContextManager;

public interface IContextManagerSoapRequest  extends IContextManager<IApiRequestObject> {
    IApiRequestObject createRequestFromWSDL(String requestName, String template);
    IApiRequestObject createRequest(String requestName, String template);
    IApiRequestObject getCurrentRequest();

}
