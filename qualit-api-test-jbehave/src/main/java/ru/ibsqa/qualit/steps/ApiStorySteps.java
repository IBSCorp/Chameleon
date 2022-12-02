package ru.ibsqa.qualit.steps;

import ru.ibsqa.qualit.api.context.IContextManagerApiRequest;
import ru.ibsqa.qualit.api.context.IContextManagerApiResponse;
import ru.ibsqa.qualit.converters.Variable;
import ru.ibsqa.qualit.json.utils.DataUtils;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;

@Component
public class ApiStorySteps extends AbstractSteps {

    @Autowired
    private ApiSteps apiSteps;

    @Autowired
    private CoreFieldSteps fieldSteps;

    @Autowired
    private IContextManagerApiRequest contextManagerRequest;

    @Autowired
    private IContextManagerApiResponse contextManagerResponse;

    @Given("установлен адрес сервиса \"$baseURI\"")
    public void setBaseURI(Variable baseURI) {
        flow(()->
                apiSteps.setBaseURI(baseURI.getValue())
        );
    }

    @Given("установлен порт сервиса \"$port\"")
    public void setPort(Variable port) {
        flow(()->
                apiSteps.setPort(port.getValue())
        );
    }

    @Given("установлена схема авторизации \"$credential\"")
    public void setAuth(Variable credential) {
        flow(()->
                apiSteps.setAuth(credential.getValue())
        );
    }

    @Given("установлена спецификация прокси \"$proxy\"")
    public void setProxy(String proxy) {
        flow(() ->
                apiSteps.setProxy(proxy)
        );
    }

    @Given("установлена точка подключения \"$endpointName\"")
    public void setCurrentEndpoint(String endpointName) {
        flow(()->
                apiSteps.setCurrentEndpoint(endpointName)
        );
    }

    @Given("создан запрос")
    public void createRequest() {
        flow(()->
                apiSteps.createRequest()
        );
    }

    @Given("создан запрос на основе шаблона \"$template\"")
    public void createRequestFromTemplate(String template) {
        flow(()->
                apiSteps.createRequestFromTemplate(DataUtils.getDataAsString(template))
        );
    }

    @Given("создан запрос с параметрами: $params")
    public void createRequest(ExamplesTable params) {
        flow(()-> {
            apiSteps.createRequest();
            doParams(false, params);
        });
    }

    @Given("создан запрос на основе шаблона \"$template\" с параметрами: $params")
    public void createRequestFromTemplate(String template, ExamplesTable params) {
        flow(()-> {
            apiSteps.createRequestFromTemplate(DataUtils.getDataAsString(template));
            doParams(false, params);
        });
    }

    @Given("создан запрос \"$requestName\"")
    public void createRequest(String requestName) {
        flow(()->
                apiSteps.createRequest(requestName)
        );
    }

    @Given(value = "создан запрос \"$requestName\" на основе шаблона \"$template\"", priority = 1)
    public void createRequestFromTemplate(String requestName, String template) {
        flow(()->
                apiSteps.createRequestFromTemplate(requestName, DataUtils.getDataAsString(template))
        );
    }

    @Given("создан запрос \"$requestName\" с параметрами: $params")
    public void createRequest(String requestName, ExamplesTable params) {
        flow(()-> {
            apiSteps.createRequest(requestName);
            doParams(false, params);
        });
    }

    @Given(value = "создан запрос \"$requestName\" на основе шаблона \"$template\" с параметрами: $params", priority = 1)
    public void createRequestFromTemplate(String requestName, String template, ExamplesTable params) {
        flow(()-> {
            apiSteps.createRequestFromTemplate(requestName, DataUtils.getDataAsString(template));
            doParams(false, params);
        });
    }

    @When("выполняется запрос")
    public void sendRequest() {
        flow(()->
                apiSteps.sendRequest()
        );
    }

    @Then("получен ответ")
    public void validateResponse() {
        flow(()->
                apiSteps.receiveResponse()
        );
    }

    @Then("получен ответ с параметрами: $params")
    public void validateResponse(ExamplesTable params) {
        flow(()-> {
            apiSteps.receiveResponse();
            doParams(true, params);
        });
    }

    @Then("получен ответ \"$responseName\"")
    public void validateResponse(String responseName) {
        flow(()->
                apiSteps.receiveResponse(responseName)
        );
    }

    @Then("получен ответ \"$responseName\" с параметрами: $params")
    public void validateResponse(String responseName, ExamplesTable params) {
        flow(()-> {
            apiSteps.receiveResponse(responseName);
            doParams(true, params);
        });
    }

    /**
     * Проверить код статуса ответа. Используется если код статуса не задан в метаданных ответа.
     * @param expectedStatusCode
     */
    @Then("код статуса ответа равен \"$expectedStatusCode\"")
    public void validateResponseStatusCode(Variable expectedStatusCode) {
        flow(()->
                apiSteps.validateResponseStatusCode(Integer.parseInt(expectedStatusCode.getValue()))
        );
    }

    /**
     * Проверить код статуса ответа. Используется если код статуса не задан в метаданных ответа.
     * @param expectedStatusCodes
     */
    @Then("код статуса ответа в списке \"$expectedStatusCodes\"")
    public void validateResponseStatusCodes(Variable expectedStatusCodes) {
        flow(()-> {
            int[] expectedStatusCodesInt = Arrays
                    .asList(expectedStatusCodes.getValue().split("\\s*,\\s*"))
                    .stream()
                    .mapToInt((s) -> Integer.parseInt(s))
                    .toArray();
            apiSteps.validateResponseStatusCodes(expectedStatusCodesInt);
        });
    }

    /**
     * Проверить тело ответа на соответствие схеме. Используется если схема не задана в метаданных ответа.
     * @param schema
     */
    @Then("ответ соответствует схеме \"$schema\"")
    public void validateResponseSchema(String schema) {
        flow(()->
                apiSteps.validateResponseSchema(DataUtils.getDataAsString(schema))
        );
    }

    private void doParams(boolean check, ExamplesTable params) {
        for (Map<String, String> row : params.getRows()) {
            String field = ( check ? contextManagerResponse.getContextName() : contextManagerRequest.getContextName() ) + "::" + row.get("field");
            String value = row.get("value");
            value = evalVariable(value);
            if (check) {
                fieldSteps.checkFieldValue(field, CompareOperatorEnum.EQUALS, value);
            } else {
                fieldSteps.fillField(field, value);
            }

        }
    }

    /**
     * Проверить, что тело ответа пусто
     */
    @Then("ответ пуст")
    public void emptyResponse() {
        flow(()->
                apiSteps.emptyResponse()
        );
    }

    @When("очистить хранилище куков")
    public void clearCookiesRepo() {
        flow(() ->
                apiSteps.clearCookiesRepo()
        );
    }
}
