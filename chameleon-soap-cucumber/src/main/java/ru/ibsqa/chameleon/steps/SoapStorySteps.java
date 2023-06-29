package ru.ibsqa.chameleon.steps;

import io.cucumber.java.ru.Дано;
import org.springframework.beans.factory.annotation.Autowired;
import ru.ibsqa.chameleon.context.Context;
import ru.ibsqa.chameleon.context.ContextChange;
import ru.ibsqa.chameleon.element.ElementTypeRequest;

public class SoapStorySteps extends AbstractSteps {
    @Autowired
    private SoapSteps soapSteps;


    @Дано("^создан SOAP запрос \"([^\"]*)\" на основе WSDL \"([^\"]*)\"$")
    @StepDescription(action = "API->Действия->Создать SOAP - запрос"
            , subAction = "Создать SOAP - запрос на основе WSDL"
            , parameters = {"requestName - наименование запроса", "params - наименование полей"})
    @Context(type = ElementTypeRequest.class, change = ContextChange.BEFORE, parameter = "requestName")
    public void createRequestFromWSDL(String requestName, String wsdl) {
        flow(() -> {
            soapSteps.createRequestFromWSDL(requestName, wsdl);
        });
    }

}
