package ru.ibsqa.chameleon.soap;

import ru.ibsqa.chameleon.api.context.DefaultApiRequestObject;
import ru.ibsqa.chameleon.api.context.IApiEndpointObject;
import ru.ibsqa.chameleon.definitions.repository.IRepositoryElement;
import ru.ibsqa.chameleon.definitions.repository.api.MetaEndpoint;
import ru.ibsqa.chameleon.definitions.repository.api.MetaRequest;
import ru.ibsqa.chameleon.definitions.repository.data.AbstractDataMetaField;
import ru.ibsqa.chameleon.elements.api.DefaultRequestField;
import ru.ibsqa.chameleon.i18n.ILocaleManager;
import ru.ibsqa.chameleon.utils.spring.SpringUtils;
import ru.ibsqa.chameleon.xml.context.wrapper.DefaultXMLWrapper;
import io.restassured.RestAssured;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@Slf4j
public class DefaultSoapRequestObject extends DefaultApiRequestObject {

    private ISOAPMessageCreator isoapMessageCreator = SpringUtils.getBean(ISOAPMessageCreator.class);

    @Override
    public void initialize(IApiEndpointObject endpoint, String requestName, String wsdl) {
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
                .header("Content-Type", "text/xml;charset=UTF-8")
                .log()
                .all();

        // Установим схему авторизации
        if (null != metaRequest.getCredential() && !metaRequest.getCredential().isEmpty()) {
            requestSpecification = authentication.getSpecificationByCredentialName(requestSpecification.auth(), metaRequest.getCredential());
        }


        // Создать запрос из WSDL - схемы
        dataWrapper = new DefaultXMLWrapper();
        String soapMessage = createSoapMessageFromWSDL(wsdl);
        dataWrapper.setDataValue(soapMessage);
        if (attachMeta) {
            attachTemplate(soapMessage);
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

    private String createSoapMessageFromWSDL(String wsdl) {
        isoapMessageCreator.parseWsdl(wsdl);
        isoapMessageCreator.createMessage(metaRequest.getName());
        return isoapMessageCreator.modifyMessage();
    }

    @Override
    public void send(){
        Document document = ISOAPMessageCreator.stringToDocument(dataWrapper.getDataPretty());
        isoapMessageCreator.removeEmptyNodes(document);
        dataWrapper.setDataValue(ISOAPMessageCreator.documentToString(document));
        super.send();
    }








}
