package ru.ibsqa.qualit.api.context;

import ru.ibsqa.qualit.api.authentication.IApiAuthentication;
import ru.ibsqa.qualit.api.cookie.IApiCookie;
import ru.ibsqa.qualit.api.reporter.IApiLogger;
import ru.ibsqa.qualit.api.reporter.ReportInformerApiTimingImpl;
import ru.ibsqa.qualit.api.reporter.RequestLogReceiver;
import ru.ibsqa.qualit.api.reporter.ResponseLogReceiver;
import ru.ibsqa.qualit.definitions.repository.IRepositoryElement;
import ru.ibsqa.qualit.definitions.repository.api.ContentTypeEnum;
import ru.ibsqa.qualit.definitions.repository.api.MetaEndpoint;
import ru.ibsqa.qualit.definitions.repository.api.MetaRequest;
import ru.ibsqa.qualit.definitions.repository.data.AbstractDataMetaField;
import ru.ibsqa.qualit.elements.IFacade;
import ru.ibsqa.qualit.elements.IFacadeWritable;
import ru.ibsqa.qualit.elements.api.DefaultRequestField;
import ru.ibsqa.qualit.elements.api.IFacadeRequestField;
import ru.ibsqa.qualit.elements.data.AbstractDataField;
import ru.ibsqa.qualit.i18n.ILocaleManager;
import ru.ibsqa.qualit.json.context.wrapper.DefaultJsonWrapper;
import ru.ibsqa.qualit.json.context.wrapper.IDataWrapper;
import ru.ibsqa.qualit.utils.spring.SpringUtils;
import ru.ibsqa.qualit.xml.context.wrapper.DefaultXMLWrapper;
import ru.ibsqa.qualit.reporter.TestAttachment;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.ConnectException;
import java.util.*;

import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

@Slf4j @Component
public class DefaultApiRequestObject implements IApiRequestObject {

    protected final boolean attachMeta = Boolean.parseBoolean(System.getProperty("api.attachMeta", "true"));
    protected final boolean attachContent = Boolean.parseBoolean(System.getProperty("api.attachContent", "true"));

    protected List<IFacadeRequestField> fields = new ArrayList<>();

    protected MetaRequest metaRequest = null;

    protected MetaEndpoint metaEndpoint = null;

    protected RequestSpecification requestSpecification = null;

    protected IDataWrapper dataWrapper = new DefaultJsonWrapper();

    protected Response response = null;

    protected IApiLogger restLogger = SpringUtils.getBean(IApiLogger.class);

    protected RequestLogReceiver requestLogReceiver = SpringUtils.getBean(RequestLogReceiver.class);

    protected ResponseLogReceiver responseLogReceiver = SpringUtils.getBean(ResponseLogReceiver.class);

    protected ReportInformerApiTimingImpl informer = SpringUtils.getBean(ReportInformerApiTimingImpl.class);

    protected IApiAuthentication authentication = SpringUtils.getBean(IApiAuthentication.class);

    protected IApiCookie cookie = SpringUtils.getBean(IApiCookie.class);

    protected Map<String, Object> headerValues = null;

    @Override
    public Response getRawResponse() {
        return response;
    }

    @Override
    public void initialize(IApiEndpointObject endpoint, String requestName, String template) {
        metaEndpoint = (MetaEndpoint) endpoint;

        // Найти метаданные запроса в репозитории
        if (null != requestName) {
            for (IRepositoryElement r : endpoint.getRequests()) {
                if (r instanceof MetaRequest && requestName.equals(((MetaRequest) r).getName())) {
                    metaRequest = (MetaRequest) r;
                }
            }
            assertNotNull(metaRequest, ILocaleManager.message("elementNotFoundInRepositoriesErrorMessage", requestName));
        } else {
            if (endpoint.getRequests().size() == 0) {
               assertNotNull(metaRequest, ILocaleManager.message("noRequestsErrorMessage"));
            } else if (endpoint.getRequests().size() > 1) {
               assertNotNull(metaRequest, ILocaleManager.message("multiRequestsErrorMessage"));
            } else {
                metaRequest = (MetaRequest) endpoint.getRequests().get(0);
            }
        }
        if (attachMeta) {
            attachRequestMeta(metaRequest);
        }

        // Создать контекст запроса
        requestSpecification = given()
                .config(
                        RestAssured
                                .config()
                                .logConfig(restLogger.getLogConfig())
                                .encoderConfig(encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false))
                                .jsonConfig(
                                        jsonConfig()
                                                .numberReturnType(BIG_DECIMAL)
                                )
                )
                .header("Content-Type", "application/json;charset=UTF-8")
                .log()
                .all();

        // Установим схему авторизации
        if (null != metaRequest.getCredential() && !metaRequest.getCredential().isEmpty()) {
            requestSpecification = authentication.getSpecificationByCredentialName(requestSpecification.auth(), metaRequest.getCredential());
        }

        // Заполнить тело запроса из шаблона - заменить на enum
        if (metaRequest.getContentType()!=null && metaRequest.getContentType().equals(ContentTypeEnum.XML)){
            dataWrapper = new DefaultXMLWrapper();
            requestSpecification.header("Content-Type", "text/xml;charset=UTF-8");
        }
        template = (null == template) ? metaRequest.getTemplate() : template;
        if (null != template && !template.isEmpty()) {
            dataWrapper.setDataValue(template);
            if (attachMeta) {
                attachTemplate(template);
            }
        }

        // Создадим хранилище для установленных значений заголовка
        headerValues = new HashMap<>();

        // Заполнить список полей
        List<AbstractDataMetaField> metaFields = new ArrayList<>();
        if (null != metaRequest.getParams()) metaFields.addAll(metaRequest.getParams().getFields());
        if (null != metaRequest.getHeader()) metaFields.addAll(metaRequest.getHeader().getFields());
        if (null != metaRequest.getCookie()) metaFields.addAll(metaRequest.getCookie().getFields());
        if (null != metaRequest.getBody()) metaFields.addAll(metaRequest.getBody().getFields());
        for (AbstractDataMetaField metaField : metaFields) {
            DefaultRequestField fieldFacade = new DefaultRequestField();
            fieldFacade.initialize(metaField, requestSpecification, dataWrapper, headerValues);
            fields.add(fieldFacade);
        }

    }

    @TestAttachment(value = "Метаданные запроса", mimeType = "application/xml")
    protected String attachRequestMeta(MetaRequest request) {
        return request.marshallXML();
    }

    @TestAttachment(value = "Шаблон запроса", mimeType = "text/plain")
    protected String attachTemplate(String attachTemplate) {
        return attachTemplate;
    }

    @Override
    public void send() {
        assertNotNull(metaEndpoint);
        assertNotNull(metaRequest);
        assertNotNull(requestSpecification);

        try {
            response = sendRequest();
        } catch (ConnectException e) {
            log.error(e.getMessage(), e);
            fail(ILocaleManager.message("serviceUnavailableErrorMessage", RestAssured.baseURI));
        }

    }

    private Response sendRequest() throws java.net.ConnectException {
        String path = metaEndpoint.getPath();
        if (path.startsWith("/")) {
            path = path.replaceFirst("/", "");
        }
        String url = RestAssured.baseURI
                + (RestAssured.baseURI.endsWith("/") ? "" : "/")
                + path;
        try {
            // Установить тело запроса
            if (null != dataWrapper.getDataValue() && !dataWrapper.getDataValue().isEmpty()) {
                requestSpecification.body(dataWrapper.getDataValue());
            }
            // Установить заголовки запроса по умолчанию
            if (null != metaRequest.getHeader()) {
                metaRequest.getHeader().getFields()
                        .stream()
                        .filter(header -> Objects.nonNull(header.getDefaultValue())) // Берем только те, у которых есть значение по умолчанию
                        .filter(header -> !headerValues.containsKey(header.getLocator())) // Отбросим те, которые были установлены при формировании запроса
                        .forEach(
                                header -> {
                                    IFacadeWritable field = getField(header.getName());
                                    field.setFieldValue(header.getDefaultValue());
                                }
                        );
            }
            //установить куки запроса
            if (null != metaRequest.getCookie()) {
                metaRequest.getCookie().getFields().forEach(
                        field -> {
                            if (null != (cookie.getCookie(field.getLocator(), url)))
                                requestSpecification.cookie(cookie.getCookie(field.getLocator(), url));
                        }
                );
            }
            // Выполнить запрос
            Response response = ((null == path || path.isEmpty())
                    ? requestSpecification.request(metaRequest.getMethod())
                    : requestSpecification.request(metaRequest.getMethod(), path));
            restLogger.flush(requestLogReceiver);
            response.then().log().all();
            analiseResponse(response);
            restLogger.flush(responseLogReceiver);
            informer.fixInfo(metaEndpoint, metaRequest, response.time());

            return response;
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage(), e);
            fail(e.getLocalizedMessage());
        }
        return null;
    }

    @Override
    public List<IFacadeRequestField> getFields() {
        return fields;
    }

    @Override
    public <T extends IFacade> T getField(String fieldName) {
        return (T) getFields().stream().filter(item -> ((AbstractDataField) item).getName().equals(fieldName)).findFirst().get();
    }

    private void analiseResponse(Response response) {
        responseLogReceiver.setSkipAttachBody(false);

        if (null != response) {
            String contentType = getHeader(response, "Content-Type");
            String contentDisposition = getHeader(response, "Content-Disposition");

            String type = "";
            String charset = "";
            String filename = "";
            boolean isAttachment = false;
            if (!contentType.isEmpty() && contentType.contains(";")) {
                String[] contentTypeParts = contentType.split(";");
                type = contentTypeParts[0];
                charset = contentTypeParts[1];
            }
            if (!contentDisposition.isEmpty() && contentDisposition.contains(";")) {
                String[] contentDispositionParts = contentDisposition.split(";");
                isAttachment = contentDispositionParts[0].toLowerCase().contains("attachment");
                if (contentDispositionParts[1].contains("=")) {
                    String[] filenameParts = contentDispositionParts[1].split("=");
                    if (filenameParts[0].toLowerCase().contains("filename") && !filenameParts[1].contains("=")) {
                        filename = filenameParts[1].replace("\"", "");
                        if (type.isEmpty()) {
                            if (filename.toLowerCase().endsWith(".xlsx")) {
                                type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                            } else if (filename.toLowerCase().endsWith(".xls")) {
                                type = "application/vnd.ms-excel";
                            }
                        }
                    }
                }
            }

            if (!type.isEmpty() && !type.toLowerCase().contains("application/json")) {
                responseLogReceiver.setSkipAttachBody(true);
                if (attachContent) {
                    if (type.toLowerCase().contains("xml")) {
                        attachBodyAsString(response.getBody().prettyPrint());
                    } else if (!isAttachment || filename.isEmpty()) {
                        attachBody(response.asByteArray(), type);
                    } else {
                        attachFile(response.asByteArray(), "text/plain", filename);
                    }
                }
            }
        }
    }

    private String getHeader(Response response, String key) {
        String value = null;
        try {
            value = response.getHeader(key);
        } catch (Exception e) {}

        if (null == value) {
            value = "";
        }
        return value;
    }

    @TestAttachment(value = "Тело ответа", mimeType = "${mimeType}")
    private byte[] attachBody(byte[] bytes, String mimeType) {
        return bytes;
    }

    @TestAttachment(value = "Тело ответа", mimeType = "text/plain")
    private String attachBodyAsString(String body) {
        return body;
    }

    @TestAttachment(value = "{filename}", mimeType = "${mimeType}")
    private byte[] attachFile(byte[] bytes, String mimeType, String filename) {
        return bytes;
    }

}
