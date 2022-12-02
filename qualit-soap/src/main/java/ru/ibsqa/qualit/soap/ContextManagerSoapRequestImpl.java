package ru.ibsqa.qualit.soap;

import ru.ibsqa.qualit.api.context.IApiRequestObject;
import ru.ibsqa.qualit.api.context.IContextManagerApiEndpoint;
import ru.ibsqa.qualit.api.context.IContextManagerApiRequest;
import ru.ibsqa.qualit.context.IContextRegistrator;
import ru.ibsqa.qualit.i18n.ILocaleManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ContextManagerSoapRequestImpl implements IContextManagerSoapRequest, IContextRegistrator {

    @Autowired
    IContextManagerApiRequest iContextManagerRequest;

    @Override
    public String getContextName() {
        return localeManager.getMessage("soapRequestContextName");
    }

    @Autowired
    private ILocaleManager localeManager;

    @Autowired
    private IContextManagerApiEndpoint contextManagerRestEndpoint;



    @Override
    public IApiRequestObject createRequestFromWSDL(String requestName, String template) {
        IApiRequestObject request = new DefaultSoapRequestObject();
        request.initialize(contextManagerRestEndpoint.getCurrentEndpoint(), requestName, template);
        iContextManagerRequest.setCurrentRequest(request);
        return request;
    }

    @Override
    public IApiRequestObject createRequest(String requestName, String template) {
        return iContextManagerRequest.createRequest(requestName, template);
    }

    @Override
    public IApiRequestObject getCurrentRequest() {
        return iContextManagerRequest.getCurrentRequest();
    }


}
