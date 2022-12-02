package ru.ibsqa.qualit.steps;

import ru.ibsqa.qualit.context.Context;
import ru.ibsqa.qualit.context.ContextChange;
import ru.ibsqa.qualit.context.ContextType;
import io.cucumber.java.ru.Дано;
import org.springframework.beans.factory.annotation.Autowired;

public class SoapStorySteps extends AbstractSteps {
    @Autowired
    private SoapSteps soapSteps;


    @Дано("^создан SOAP запрос \"([^\"]*)\" на основе WSDL \"([^\"]*)\"$")
    @StepDescription(action = "API->Действия->Создать SOAP - запрос"
            , subAction = "Создать SOAP - запрос на основе WSDL"
            , parameters = {"requestName - наименование запроса", "params - наименование полей"})
    @Context(type = ContextType.REQUEST, change = ContextChange.BEFORE, parameter = "requestName")
    public void createRequestFromWSDL(String requestName, String wsdl) {
        flow(() -> {
            soapSteps.createRequestFromWSDL(requestName, wsdl);
        });
    }

}
