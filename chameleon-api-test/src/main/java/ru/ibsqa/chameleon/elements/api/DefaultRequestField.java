package ru.ibsqa.chameleon.elements.api;

import ru.ibsqa.chameleon.definitions.repository.api.IRestFieldChapterResolver;
import ru.ibsqa.chameleon.definitions.repository.api.RestFieldChapter;
import ru.ibsqa.chameleon.definitions.repository.data.AbstractDataMetaField;
import ru.ibsqa.chameleon.elements.data.AbstractDataField;
import ru.ibsqa.chameleon.i18n.ILocaleManager;
import ru.ibsqa.chameleon.json.context.wrapper.IDataWrapper;
import ru.ibsqa.chameleon.json.context.wrapper.PathException;
import io.restassured.specification.RequestSpecification;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

public class DefaultRequestField extends AbstractDataField implements IFacadeRequestField, IRestFieldChapterResolver {

    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private RequestSpecification requestSpecification;

    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private IDataWrapper dataWrapper; //request body

    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private Map<String, Object> headerValues; //header values

    protected void setValue(String value) {

        Object castValue = getMetaField().parseValue(value);

        if (getChapter(getMetaField()) == RestFieldChapter.PARAMS) {

            String key = getMetaField().getLocator();

            if (getMetaField().isInPath()) {
                requestSpecification.pathParam(key, castValue);
            } else {
                if (null == castValue || castValue.toString().isEmpty()) {
                    requestSpecification.queryParam(key, new Object[0]);
                } else {
                    requestSpecification.queryParam(key, castValue);
                }
            }

        } else if (getChapter(getMetaField()) == RestFieldChapter.HEADER) {

            String key = getMetaField().getLocator();

            requestSpecification.header(key, castValue);
            headerValues.put(key, castValue);

        } else if (getChapter(getMetaField()) == RestFieldChapter.COOKIE) {

            String key = getMetaField().getLocator();

            requestSpecification.cookie(key, castValue);

        } else if (getChapter(getMetaField()) == RestFieldChapter.BODY) {

            String locator = getMetaField().getLocator();

            try {
                dataWrapper.set(locator, castValue);
            } catch (PathException e) {
                fail(ILocaleManager.message("writeToFieldErrorMessage", getName()) + ". " + e.getMessage());
            }

        }
    }

    public void initialize(AbstractDataMetaField metaField, RequestSpecification requestSpecification, IDataWrapper bodyJson, Map<String, Object> headerValues) {
        assertNotNull(metaField);
        setMetaField(metaField);

        assertNotNull(requestSpecification);
        setRequestSpecification(requestSpecification);

        assertNotNull(bodyJson);
        setDataWrapper(bodyJson);

        this.headerValues = headerValues;
    }

    @Override
    public void clearFieldValue() {
        setValue("");
    }

    @Override
    public void setFieldValue(String value) {
        setValue(value);
    }
}
