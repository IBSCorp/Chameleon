package ru.ibsqa.qualit.steps;

import io.cucumber.java.ru.Дано;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Тогда;
import org.springframework.beans.factory.annotation.Autowired;
import ru.ibsqa.qualit.api.context.IContextManagerApiRequest;
import ru.ibsqa.qualit.api.context.IContextManagerApiResponse;
import ru.ibsqa.qualit.compare.ICompareManager;
import ru.ibsqa.qualit.context.Context;
import ru.ibsqa.qualit.context.ContextChange;
import ru.ibsqa.qualit.context.ContextType;
import ru.ibsqa.qualit.converters.FieldValueTable;
import ru.ibsqa.qualit.json.utils.DataUtils;
import ru.ibsqa.qualit.steps.roles.Value;
import ru.ibsqa.qualit.steps.roles.Write;

import java.util.Arrays;
import java.util.List;

public class ApiStorySteps extends AbstractSteps {
    @Autowired
    private ApiSteps apiSteps;

    @Autowired
    private CoreFieldSteps fieldSteps;

    @Autowired
    private IContextManagerApiRequest contextManagerRequest;

    @Autowired
    private IContextManagerApiResponse contextManagerResponse;

    @Autowired
    private ICompareManager compareManager;

    @Тогда("^установлен адрес сервиса \"(.*)\"$")
    @StepDescription(action = "API->Действия->Установить адрес сервиса"
            , subAction = "Установить адрес сервиса"
            , parameters = {"baseURI - адрес сервиса"})
    public void setBaseURI(String baseURI) {
        flow(() ->
                apiSteps.setBaseURI(baseURI)
        );
    }

    @Дано("^установлен порт сервиса \"(.*)\"$")
    @StepDescription(action = "API->Действия->Установить порт сервиса"
            , subAction = "Установить порт сервиса"
            , parameters = {"port - порт сервиса"})
    public void setPort(String port) {
        flow(() ->
                apiSteps.setPort(port)
        );
    }

    @Дано("^установлена схема авторизации \"(.*)\"$")
    @StepDescription(action = "API->Действия->Установить схему авторизации"
            , subAction = "Установить схему авторизации"
            , parameters = {"credential - схема авторизации"})
    public void setAuth(String credential) {
        flow(() ->
                apiSteps.setAuth(credential)
        );
    }

    @Дано("^установлена спецификация прокси \"(.*)\"$")
    @StepDescription(action = "API->Действия->Установить спецификацию прокси"
            , subAction = "Установить спецификацию прокси"
            , parameters = {"proxy - спецификация прокси"})
    public void setProxy(String proxy) {
        flow(() ->
                apiSteps.setProxy(proxy)
        );
    }

    @Тогда("^установлена точка подключения \"(.*)\"$")
    @StepDescription(action = "API->Действия->Установить точку подключения"
            , subAction = "Установить точку подключения"
            , parameters = {"endpointName - точка подключения"})
    @Context(type = ContextType.ENDPOINT, change = ContextChange.AFTER, parameter = "endpointName")
    public void setCurrentEndpoint(String endpointName) {
        flow(() ->
                apiSteps.setCurrentEndpoint(endpointName)
        );
    }

    @Тогда("^создан запрос \"(.*)\"$")
    @StepDescription(action = "API->Действия->Создать запрос"
            , subAction = "Создать запрос"
            , parameters = {"field - наименование запроса"})
    @Context(type = ContextType.REQUEST, change = ContextChange.AFTER, parameter = "requestName", delete = ContextType.RESPONSE)
    public void createRequest(String requestName) {
        flow(() ->
                apiSteps.createRequest(requestName)
        );
    }

    @Дано("^создан запрос на основе шаблона \"([^\"]*)\"$")
    @StepDescription(action = "API->Действия->Создать запрос на основе шаблона"
            , subAction = "Создать запрос на основе шаблона"
            , parameters = {"template - шаблон"})
    public void createRequestFromTemplate(String template) {
        flow(() ->
                apiSteps.createRequestFromTemplate(DataUtils.getDataAsString(template))
        );
    }

    @Дано("^создан запрос \"([^\"]*)\" с параметрами:$")
    @StepDescription(action = "API->Действия->Создать запрос"
            , subAction = "Создать запрос с параметрами"
            , parameters = {"requestName - наименование запроса", "params - параметры"})
    @Context(type = ContextType.REQUEST, change = ContextChange.BEFORE, parameter = "requestName", delete = ContextType.RESPONSE)
    public void createRequest(String requestName, @Write("field") @Value("value") List<FieldValueTable> params) {
        flow(() -> {
            apiSteps.createRequest(requestName);
            doParams(false, params);
        });
    }

    @Дано("^создан запрос на основе шаблона \"([^\"]*)\" с параметрами:$")
    @StepDescription(action = "API->Действия->Создать запрос на основе шаблона"
            , subAction = "Создать запрос на основе шаблона с параметрами"
            , parameters = {"template - наименование наименование шаблона", "field - параметры"})
    public void createRequestFromTemplate(String template, List<FieldValueTable> params) {
        flow(() -> {
            apiSteps.createRequestFromTemplate(DataUtils.getDataAsString(template));
            doParams(false, params);
        });
    }

    @Дано(value = "^создан запрос \"([^\"]*)\" на основе шаблона \"([^\"]*)\"")
    @StepDescription(action = "API->Действия->Создать запрос на основе шаблона"
            , subAction = "Создать запрос на основе шаблона"
            , parameters = {"requestName - наименование запроса", "template - наименование шаблона"})
    public void createRequestFromTemplate(String requestName, String template) {
        flow(() ->
                apiSteps.createRequestFromTemplate(requestName, DataUtils.getDataAsString(template))
        );
    }

    @Дано(value = "создан запрос \"([^\"]*)\" на основе шаблона \"([^\"]*)\" с параметрами:$")
    @StepDescription(action = "API->Действия->Создать запрос на основе шаблона"
            , subAction = "Создать запрос на основе шаблона"
            , parameters = {"requestName - наименование запроса", "params - параметры"})
    @Context(type = ContextType.REQUEST, change = ContextChange.BEFORE, parameter = "requestName", delete = ContextType.RESPONSE)
    public void createRequestFromTemplate(String requestName, String template, List<FieldValueTable> params) {
        flow(() -> {
            apiSteps.createRequestFromTemplate(requestName, DataUtils.getDataAsString(template));
            doParams(false, params);
        });
    }

    /**
     * Проверить, что тело ответа пусто
     */
    @Тогда("^ответ пуст")
    @StepDescription(action = "API->Проверки->Проверить ответ"
            , subAction = "Ответ пуст")
    public void emptyResponse() {
        flow(() ->
                apiSteps.emptyResponse()
        );
    }

    private void doParams(boolean check, List<FieldValueTable> params) {
        for (FieldValueTable fieldValue : params
        ) {
            String field = (check ? contextManagerResponse.getContextName() : contextManagerRequest.getContextName()) + "::" + fieldValue.getField();
            String value = fieldValue.getValue();
            value = evalVariable(value);
            if (check) {
                fieldSteps.checkFieldValue(field, compareManager.defaultOperator(), value);
            } else {
                fieldSteps.fillField(field, value);
            }
        }
    }

    @Когда("^выполняется запрос$")
    @StepDescription(action = "API->Действия->Выполнить запрос"
            , subAction = "Выполнить запрос")
    @Context(type = ContextType.REQUEST, change = ContextChange.BEFORE)
    public void sendRequest() {
        flow(() ->
                apiSteps.sendRequest()
        );
    }

    @Тогда("^очистить хранилище куков")
    @StepDescription(action = "API->Действия->Очистить хранилище куков"
            , subAction = "Очистить хранилище куков")
    public void clearCookiesRepo() {
        flow(() -> apiSteps.clearCookiesRepo());
    }

    @Тогда("^получен ответ$")
    @StepDescription(action = "API->Проверки->Проверить ответ"
            , subAction = "Получен ответ")
    @Context(type = ContextType.RESPONSE, change = ContextChange.AFTER, value = "currentResponse")
    public void validateResponse() {
        flow(() ->
                apiSteps.receiveResponse()
        );
    }

    @Тогда("^получен ответ с параметрами:$")
    @StepDescription(action = "API->Проверки->Проверить ответ"
            , subAction = "Получен ответ с параметрами"
            , parameters = {"params - параметры"})
    @Context(type = ContextType.RESPONSE, change = ContextChange.BEFORE)
    public void validateResponse(List<FieldValueTable> params) {
        flow(() -> {
            apiSteps.receiveResponse();
            doParams(true, params);
        });
    }

    @Тогда("^получен ответ \"([^\"]*)\"$")
    @StepDescription(action = "API->Проверки->Проверить ответ"
            , subAction = "Получен ответ с именем"
            , parameters = {"responseName - наименование ответа"})
    @Context(type = ContextType.RESPONSE, change = ContextChange.AFTER, parameter = "responseName", onlyStepContext = true)
    public void validateResponse(String responseName) {
        flow(() ->
                apiSteps.receiveResponse(responseName)
        );
    }

    @Тогда("^получен ответ \"([^\"]*)\" с параметрами:$")
    @StepDescription(action = "API->Проверки->Проверить ответ"
            , subAction = "Получен ответ с именем и с параметрами"
            , parameters = {"responseName - наименование ответа", "params - параметры"})
    @Context(type = ContextType.RESPONSE, change = ContextChange.BEFORE, parameter = "responseName")
    public void validateResponse(String responseName, List<FieldValueTable> params) {
        flow(() -> {
            apiSteps.receiveResponse(responseName);
            doParams(true, params);
        });
    }

    /**
     * Проверить код статуса ответа. Используется если код статуса не задан в метаданных ответа.
     *
     * @param expectedStatusCode
     */
    @Тогда("^код статуса ответа равен \"([^\"]*)\"$")
    @StepDescription(action = "API->Проверки->Проверить ответ"
            , subAction = "Проверить код статуса ответа"
            , parameters = {"expectedStatusCode - код статуса"})
    public void validateResponseStatusCode(String expectedStatusCode) {
        flow(() ->
                apiSteps.validateResponseStatusCode(Integer.parseInt(expectedStatusCode))
        );
    }

    /**
     * Проверить код статуса ответа. Используется если код статуса не задан в метаданных ответа.
     *
     * @param expectedStatusCodes
     */
    @Тогда("^код статуса ответа в списке \"([^\"]*)\"$")
    @StepDescription(action = "API->Проверки->Проверить ответ"
            , subAction = "Проверить код статуса ответа в списке"
            , parameters = {"expectedStatusCode - код статуса"})
    public void validateResponseStatusCodes(String expectedStatusCodes) {
        flow(() -> {
            int[] expectedStatusCodesInt = Arrays
                    .asList(expectedStatusCodes.split("\\s*,\\s*"))
                    .stream()
                    .mapToInt((s) -> Integer.parseInt(s))
                    .toArray();
            apiSteps.validateResponseStatusCodes(expectedStatusCodesInt);
        });
    }

    /**
     * Проверить тело ответа на соответствие схеме. Используется если схема не задана в метаданных ответа.
     *
     * @param schema
     */
    @Тогда("^ответ соответствует схеме \"([^\"]*)\"$")
    @StepDescription(action = "API->Проверки->Проверить ответ"
            , subAction = "Ответ соответствует схеме"
            , parameters = {"schema - схема"})
    public void validateResponseSchema(String schema) {
        flow(() ->
                apiSteps.validateResponseSchema(DataUtils.getDataAsString(schema))
        );
    }
}
