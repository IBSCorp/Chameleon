package ru.ibsqa.chameleon.steps;

import ru.ibsqa.chameleon.api.context.IApiRequestObject;
import ru.ibsqa.chameleon.soap.IContextManagerSoapRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Шаги для работы с SOAP-сервисами
 */
@Component
public class SoapSteps extends AbstractSteps {

    @Autowired
    private IContextManagerSoapRequest contextManagerRequest;

    @TestStep("создать SOAP-запрос \"${requestName}\" из WSDL-схемы \"${wsdl}\"")
    public void createRequestFromWSDL(String requestName, String wsdl) {
        IApiRequestObject request = contextManagerRequest.createRequestFromWSDL(requestName, wsdl);
    }
}
