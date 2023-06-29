package ru.ibsqa.chameleon.api.context;

import ru.ibsqa.chameleon.api.cookie.IApiCookie;
import ru.ibsqa.chameleon.definitions.repository.IRepositoryElement;
import ru.ibsqa.chameleon.definitions.repository.api.*;
import ru.ibsqa.chameleon.definitions.repository.data.AbstractDataMetaField;
import ru.ibsqa.chameleon.elements.IFacade;
import ru.ibsqa.chameleon.elements.api.DefaultResponseBodyField;
import ru.ibsqa.chameleon.elements.api.DefaultResponseCommonField;
import ru.ibsqa.chameleon.elements.api.IFacadeResponseField;
import ru.ibsqa.chameleon.elements.data.AbstractDataField;
import ru.ibsqa.chameleon.i18n.ILocaleManager;
import ru.ibsqa.chameleon.json.context.wrapper.DefaultJsonWrapper;
import ru.ibsqa.chameleon.json.context.wrapper.IDataWrapper;
import ru.ibsqa.chameleon.utils.spring.SpringUtils;
import ru.ibsqa.chameleon.xml.context.wrapper.DefaultXMLWrapper;
import ru.ibsqa.chameleon.reporter.TestAttachment;
import ru.ibsqa.chameleon.steps.TestStep;
import io.restassured.http.Cookie;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
public class DefaultApiResponseObject implements IApiResponseObject, IRestFieldChapterResolver {

    protected final boolean attachMeta = Boolean.parseBoolean(System.getProperty("api.attachMeta", "true"));

    private List<IFacadeResponseField> fields = new ArrayList<>();

    private MetaResponse metaResponse = null;

    private MetaEndpoint metaEndpoint = null;

    private IApiCookie cookie = SpringUtils.getBean(IApiCookie.class);

    @Getter
    private io.restassured.response.Response response = null;

    private IDataWrapper dataWrapper = new DefaultJsonWrapper();

    @Override
    public void initialize(IApiEndpointObject endpoint, String responseName) {

        // Определить последний запрос
        IContextManagerApiRequest requestManager = SpringUtils.getBean(IContextManagerApiRequest.class);
        IApiRequestObject requestObject = requestManager.getCurrentRequest();
        response = (io.restassured.response.Response)requestObject.getRawResponse();

        // Endpoint
        metaEndpoint = (MetaEndpoint) endpoint;

        // Найти метаданные ответа в репозитории
        if (null != responseName) {
            for (IRepositoryElement r : endpoint.getResponses()) {
                if (r instanceof MetaResponse && responseName.equals(((MetaResponse) r).getName())) {
                    metaResponse = (MetaResponse) r;
                }
            }
            assertNotNull(metaResponse, ILocaleManager.message("elementNotFoundInRepositoriesErrorMessage", responseName));
        } else {
            if (endpoint.getResponses().size()==0) {
                assertNotNull(metaResponse, ILocaleManager.message("noResponsesErrorMessage"));
            } else if (endpoint.getResponses().size()>1) {
                assertNotNull(metaResponse, ILocaleManager.message("multiResponsesErrorMessage"));
            } else {
                metaResponse = (MetaResponse) endpoint.getResponses().get(0);
            }
        }
        if (attachMeta) {
            attachResponseMeta((MetaResponse) metaResponse);
        }

        // Заполнить тело запроса из шаблона
        if (metaResponse.getContentType()!=null && metaResponse.getContentType().equals(ContentTypeEnum.XML)){
            dataWrapper = new DefaultXMLWrapper();
        }

        // Получить тело ответа
        if (null != response) {
            String responseText = response.getBody().asString();
            log.info(responseText);
            dataWrapper.setDataValue(responseText);
        }

        // Заполнить список полей
        List<AbstractDataMetaField> metaFields = new ArrayList<>();
        if (null != metaResponse.getHeader()) metaFields.addAll(metaResponse.getHeader().getFields());
        if (null != metaResponse.getCookie()) metaFields.addAll(metaResponse.getCookie().getFields());
        if (null != metaResponse.getBody()) metaFields.addAll(metaResponse.getBody().getFields());
        for (AbstractDataMetaField metaField : metaFields) {
            IFacadeResponseField fieldFacade = null;
            if (getChapter(metaField) == RestFieldChapter.BODY) {
                fieldFacade = new DefaultResponseBodyField();
                ((DefaultResponseBodyField)fieldFacade).initialize(metaField, dataWrapper);
            } else {
                fieldFacade = new DefaultResponseCommonField();
                ((DefaultResponseCommonField)fieldFacade).initialize(metaField, response);
            }
            fields.add(fieldFacade);
        }
        //записать Cookie, которые перечислены в метаданных ответа, из ответа в хранилище
        if(null!=metaResponse.getCookie()) {
            List<Cookie> list = response.detailedCookies().asList();
            for (AbstractDataMetaField field : metaResponse.getCookie().getFields()
            ) {
                for (Cookie item : list) {
                    if (field.getLocator().equals(item.getName())) {
                        cookie.setCookie(item);
                        break;
                    }
                }
            }
        }

    }

    @TestAttachment(value = "Метаданные ответа", mimeType = "application/xml")
    private String attachResponseMeta(MetaResponse response) {
        return response.marshallXML();
    }

    private void checkResponse() {
        assertNotNull(response, ILocaleManager.message("noResponseAssertMessage"));
    }

    @Override
    public void validateByMeta() {
        checkResponse();

        Integer expectedStatusCode = metaResponse.getStatusCode();
        if (null != expectedStatusCode) {
            checkStatusCode(response.statusCode(), expectedStatusCode);
        }

        String schema = metaResponse.getSchema();
        if (null != schema && !schema.isEmpty()) {
            dataWrapper.validateSchema(schema);
        }
    }

    @Override
    public void validateStatusCodes(int... expectedStatusCodes) {
        checkResponse();

        if (null != expectedStatusCodes && expectedStatusCodes.length > 0) {
            checkStatusCode(response.statusCode(), expectedStatusCodes);
        }

    }

    @Override
    public void validateSchema(String schema) {
        checkResponse();

        if (null != schema && !schema.isEmpty()) {
            dataWrapper.validateSchema(schema);
        }
    }

    @TestStep("проверка статуса ответа")
    private void checkStatusCode(int actual, int... expectedList) {
        boolean match = false;
        for (int i=0; i<expectedList.length; i++ ) {
            int expectedOne = expectedList[i];
            if (expectedOne==actual) {
                match = true;
                break;
            }
        }
        if (!match) {
            fail(ILocaleManager.message("invalidStatusCodeErrorMessage", Arrays.toString(expectedList), actual));
        }

    }

    @Override
    public List<IFacadeResponseField> getFields() {
        return fields;
    }

    @Override
    public IDataWrapper getDataWrapper() {
        return dataWrapper;
    }

    @Override
    public <T extends IFacade> T getField(String fieldName) {
        return (T) getFields().stream().filter(item -> ((AbstractDataField) item).getName().equals(fieldName)).findFirst().get();
    }

}
