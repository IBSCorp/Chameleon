package ru.ibsqa.qualit.steps;

import ru.ibsqa.qualit.api.authentication.IApiAuthentication;
import ru.ibsqa.qualit.api.context.*;
import ru.ibsqa.qualit.api.cookie.IApiCookie;
import ru.ibsqa.qualit.api.proxy.IApiProxy;
import ru.ibsqa.qualit.definitions.repository.IRepositoryManager;
import ru.ibsqa.qualit.definitions.repository.api.MetaEndpoint;
import ru.ibsqa.qualit.i18n.ILocaleManager;
import ru.ibsqa.qualit.json.utils.DataUtils;
import io.restassured.RestAssured;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ibsqa.qualit.reporter.TestAttachment;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Шаги для работы с веб-сервисами
 */
@Component
public class ApiSteps extends AbstractSteps {

    private final boolean attachMeta = Boolean.parseBoolean(System.getProperty("api.attachMeta", "true"));

    @Autowired
    private IApiCookie cookie;

    @Autowired
    private IContextManagerApiEndpoint contextManagerEndpoint;

    @Autowired
    private IContextManagerApiRequest contextManagerRequest;

    @Autowired
    private IContextManagerApiResponse contextManagerResponse;

    @Autowired
    private IRepositoryManager repositoryManager;

    @Autowired
    private IApiAuthentication authentication;

    @Autowired
    private IApiProxy proxy;

    @Autowired
    private ILocaleManager localeManager;

    @TestStep("установлен адрес сервиса \"${baseURI}\"")
    public void setBaseURI(String baseURI) {
        RestAssured.baseURI = baseURI;
    }

    @TestStep("установлен порт сервиса \"${port}\"")
    public void setPort(String port) {
        assertTrue(DataUtils.isInteger(port), message("portIsNotIntegerErrorMessage", port));
        RestAssured.port = Integer.parseInt(port);
    }

    @TestStep("установлена схема авторизации \"${credential}\"")
    public void setAuth(String credential) {
        RestAssured.authentication = authentication.getSchemeByCredentialName(credential);
    }

    @TestStep("установлена спецификация прокси \"${proxyName}\"")
    public void setProxy(String proxyName) {
        RestAssured.proxy = proxy.getSpecificationByProxy(proxyName);
    }

    /**
     * Установить текущую точку подключения
     * @param endpointName
     */
    @TestStep("установлена точка подключения \"${endpointName}\"")
    public void setCurrentEndpoint(String endpointName) {
        IApiEndpointObject endpoint = repositoryManager.pickElement(endpointName, MetaEndpoint.class);
        if (attachMeta) {
            attachEndpointMeta((MetaEndpoint) endpoint);
        }
        contextManagerEndpoint.setCurrentEndpoint(endpoint);
    }

    @TestAttachment(value = "Метаданные точки подключения", mimeType = "application/xml")
    private String attachEndpointMeta(MetaEndpoint endpoint) {
        return endpoint.marshallXML();
    }

    /**
     * Создать новый запрос
     */
    @TestStep("создан запрос")
    public void createRequest() {
        IApiRequestObject request = contextManagerRequest.createRequest(null, null);
    }

    /**
     * Создать новый запрос на основе шаблона
     * @param template
     */
    @TestStep("создан запрос на основе шаблона \"${template}\"")
    public void createRequestFromTemplate(String template) {
        IApiRequestObject request = contextManagerRequest.createRequest(null, template);
    }

    /**
     * Создать новый запрос
     * @param requestName
     */
    @TestStep("создан запрос \"${requestName}\"")
    public void createRequest(String requestName) {
        IApiRequestObject request = contextManagerRequest.createRequest(requestName, null);
    }

    /**
     * Создать новый запрос на основе шаблона
     * @param requestName
     * @param template
     */
    @TestStep("создан запрос \"${requestName}\" на основе шаблона \"${template}\"")
    public void createRequestFromTemplate(String requestName, String template) {
        IApiRequestObject request = contextManagerRequest.createRequest(requestName, template);
    }

    /**
     * Выполнить запрос
     */
    @TestStep("выполняется запрос")
    public void sendRequest() {
        contextManagerRequest.getCurrentRequest().send();
    }

    /**
     * Установить метаданные ответа и проверить его корректность
     */
    @TestStep("получен ответ")
    public void receiveResponse() {
        contextManagerResponse.receiveResponse(null);
    }

    /**
     * Установить метаданные ответа и проверить его корректность
     * @param responseName
     */
    @TestStep("получен ответ \"${responseName}\"")
    public void receiveResponse(String responseName) {
        contextManagerResponse.receiveResponse(responseName);
    }

    /**
     * Проверить код статуса ответа. Используется если код статуса не задан в метаданных ответа.
     * @param expectedStatusCode
     */
    @TestStep("код статуса ответа равен \"${expectedStatusCode}\"")
    public void validateResponseStatusCode(int expectedStatusCode) {
        contextManagerResponse.getCurrentResponse().validateStatusCode(expectedStatusCode);
    }

    /**
     * Проверить что код статуса ответа находится в указанном списке. Используется если код статуса не задан в метаданных ответа.
     * @param expectedStatusCodes
     */
    @TestStep("код статуса ответа в списке \"${expectedStatusCodes}\"")
    public void validateResponseStatusCodes(int... expectedStatusCodes) {
        contextManagerResponse.getCurrentResponse().validateStatusCodes(expectedStatusCodes);
    }

    /**
     * Проверить тело ответа на соответствие схеме. Используется если схема не задана в метаданных ответа.
     * @param schema
     */
    @TestStep("ответ соответствует схеме \"${schema}\"")
    public void validateResponseSchema(String schema) {
        contextManagerResponse.getCurrentResponse().validateSchema(schema);
    }

    /**
     * Проверить, что тело ответа пусто
     */
    @TestStep("ответ пуст")
    public void emptyResponse() {
        assertTrue(contextManagerResponse.getCurrentResponse().getResponse().getBody().asString().isEmpty(), localeManager.getMessage("responseIsNotEmptyAssertMessage"));
    }

    /**
     * Очистить полученные Cookie
     */
    @TestStep("очистить хранилище cookie")
    public void clearCookiesRepo() {
        cookie.clearCookies();
    }

}
