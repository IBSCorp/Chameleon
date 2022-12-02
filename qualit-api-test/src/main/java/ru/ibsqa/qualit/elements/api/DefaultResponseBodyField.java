package ru.ibsqa.qualit.elements.api;

import ru.ibsqa.qualit.definitions.repository.data.AbstractDataMetaField;
import ru.ibsqa.qualit.elements.data.AbstractDataField;
import ru.ibsqa.qualit.elements.data.IFacadeDataExtractableField;
import ru.ibsqa.qualit.i18n.ILocaleManager;
import ru.ibsqa.qualit.json.context.wrapper.IDataWrapper;
import ru.ibsqa.qualit.json.context.wrapper.PathException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

public class DefaultResponseBodyField extends AbstractDataField implements IFacadeResponseField, IFacadeDataExtractableField {

    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private IDataWrapper dataWrapper; //response body

    protected String getValue() {

        String locator = getMetaField().getLocator();

        try {
            return dataWrapper.read(locator, String.class);
        } catch (PathException e) {
            fail(ILocaleManager.message("readFromFieldErrorMessage", getName()) + ". " + e.getMessage());
        }

        return null;

    }


    @Override
    public boolean isFieldExists() {

        String locator = getMetaField().getLocator();

        return dataWrapper.isExists(locator);
    }

    public void initialize(AbstractDataMetaField metaField, IDataWrapper bodyJson) {
        assertNotNull(metaField);
        setMetaField(metaField);

        assertNotNull(bodyJson);
        setDataWrapper(bodyJson);
    }

    @Override
    public String getFieldValue() {
        return getValue();
    }

    @Override
    public String getDataValue() {
        String locator = getMetaField().getLocator();

        try {
            return dataWrapper.getDataValue(locator);
        } catch (PathException e) {
            fail(ILocaleManager.message("readFromFieldErrorMessage", getName()) + ". " + e.getMessage());
        }

        return null;
    }

    @Override
    public String getDataPretty() {
        String locator = getMetaField().getLocator();

        try {
            return dataWrapper.getDataPretty(locator);
        } catch (PathException e) {
            fail(ILocaleManager.message("readFromFieldErrorMessage", getName()) + ". " + e.getMessage());
        }

        return null;
    }

    @Override
    public long getDataLength() {
        String locator = getMetaField().getLocator();

        try {
            return dataWrapper.getDataLength(locator);
        } catch (PathException e) {
            fail(ILocaleManager.message("readFromFieldErrorMessage", getName()) + ". " + e.getMessage());
        }

        return 0;
    }
}
