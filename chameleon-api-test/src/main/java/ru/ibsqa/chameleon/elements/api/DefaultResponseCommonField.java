package ru.ibsqa.chameleon.elements.api;

import ru.ibsqa.chameleon.definitions.repository.api.IRestFieldChapterResolver;
import ru.ibsqa.chameleon.definitions.repository.api.RestFieldChapter;
import ru.ibsqa.chameleon.definitions.repository.data.AbstractDataMetaField;
import ru.ibsqa.chameleon.elements.data.AbstractDataField;
import io.restassured.response.Response;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DefaultResponseCommonField extends AbstractDataField implements IFacadeResponseField, IRestFieldChapterResolver {

    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private Response response;

    protected String getValue() {

        if (getChapter(getMetaField()) == RestFieldChapter.HEADER) {

            String key = getMetaField().getLocator();

            return response.getHeader(key);

        }

        return null;

    }


    @Override
    public boolean isFieldExists() {

        if (getChapter(getMetaField()) == RestFieldChapter.HEADER) {

            String key = getMetaField().getLocator();

            return response.getHeaders().hasHeaderWithName(key);

        }

        return false;
    }

    public void initialize(AbstractDataMetaField metaField, Response response) {
        assertNotNull(metaField);
        setMetaField(metaField);

        assertNotNull(response);
        setResponse(response);

    }

    @Override
    public String getFieldValue() {
        return getValue();
    }
}
