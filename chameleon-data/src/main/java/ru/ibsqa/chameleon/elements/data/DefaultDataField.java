package ru.ibsqa.chameleon.elements.data;

import ru.ibsqa.chameleon.definitions.repository.data.AbstractDataMetaField;
import ru.ibsqa.chameleon.i18n.ILocaleManager;
import ru.ibsqa.chameleon.json.context.wrapper.IDataWrapper;
import ru.ibsqa.chameleon.json.context.wrapper.PathException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

public class DefaultDataField extends AbstractDataField implements IFacadeDataExtractableField, IFacadeDataMutableField {

    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private IDataWrapper dataWrapper;

    protected void setValue(String value) {

        Object castValue = getMetaField().parseValue(value);

        String path = getMetaField().getLocator();

        try {
            dataWrapper.set(path, castValue);
        } catch (PathException e) {
            fail(ILocaleManager.message("writeToFieldErrorMessage", getName()) + ". " + e.getMessage());
        }

    }

    protected String getValue() {

        String path = getMetaField().getLocator();

        try {
            return dataWrapper.read(path, String.class);
        } catch (PathException e) {
            fail(ILocaleManager.message("readFromFieldErrorMessage", getName()) + ". " + e.getMessage());
        }

        return null;

    }

    public void initialize(AbstractDataMetaField metaField, IDataWrapper dataWrapper) {
        assertNotNull(metaField);
        setMetaField(metaField);

        assertNotNull(dataWrapper);
        setDataWrapper(dataWrapper);
    }

    @Override
    public boolean isFieldExists() {
        return dataWrapper.isExists(getMetaField().getLocator());
    }

    @Override
    public void createField(String value) {
        Object castValue = getMetaField().parseValue(value);

        String locator = getMetaField().getLocator();

        dataWrapper.create(locator, castValue);
    }

    @Override
    public void createObject() {
        String locator = getMetaField().getLocator();

        dataWrapper.create(locator, new HashMap<>());
    }

    @Override
    public void createArray() {
        String locator = getMetaField().getLocator();

        dataWrapper.create(locator, new ArrayList<>());
    }

    @Override
    public void deleteField() {
        String locator = getMetaField().getLocator();

        try {
            dataWrapper.delete(locator);
        } catch (PathException e) {
            fail(ILocaleManager.message("deleteFieldErrorMessage", getName()) + ". " + e.getMessage());
        }
    }

    @Override
    public void addField(String value) {
        Object castValue = getMetaField().parseValue(value);

        String locator = getMetaField().getLocator();

        dataWrapper.add(locator, castValue);

    }

    @Override
    public void addObject() {

        String locator = getMetaField().getLocator();

        dataWrapper.add(locator, new HashMap<>());
    }

    @Override
    public void addArray() {
        String locator = getMetaField().getLocator();

        dataWrapper.add(locator, new ArrayList<>());
    }

    @Override
    public void setObject() {

        String locator = getMetaField().getLocator();

        dataWrapper.set(locator, new HashMap<>());
    }

    @Override
    public void setArray() {
        String locator = getMetaField().getLocator();

        dataWrapper.set(locator, new ArrayList<>());
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

    @Override
    public void clearFieldValue() {
        setValue("");
    }

    @Override
    public void setFieldValue(String value) {
        setValue(value);
    }

    @Override
    public String getFieldValue() {
        return getValue();
    }
}
