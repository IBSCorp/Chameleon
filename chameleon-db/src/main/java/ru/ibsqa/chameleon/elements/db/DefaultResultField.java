package ru.ibsqa.chameleon.elements.db;

import ru.ibsqa.chameleon.db.context.IQueryObject;
import ru.ibsqa.chameleon.definitions.repository.db.AbstractField;
import ru.ibsqa.chameleon.i18n.ILocaleManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DefaultResultField implements IFacadeResultField {

    @Setter(AccessLevel.PROTECTED)
    @Getter(AccessLevel.PROTECTED)
    private AbstractField metaField;

    @Setter(AccessLevel.PROTECTED)
    @Getter(AccessLevel.PROTECTED)
    private IQueryObject queryObject;

    @Override
    public String getName() {
        String name = "";

        if (null != metaField && null != metaField.getName()) {
            name = metaField.getName();
        }

        return name;
    }

    @Override
    public boolean isFieldExists() {
        return true;
    }

    @Override
    public String getFieldValue() {
        IFacadeResultSet resultSet = getQueryObject().getResultSet();
        assertNotNull(resultSet, ILocaleManager.message("noCurrentQueryErrorMessage"));
        return resultSet.getFieldValue(getMetaField().getLocator());
    }

    @Override
    public void initialization(IQueryObject queryObject, AbstractField metaField) {
        this.metaField = metaField;
        this.queryObject = queryObject;
    }
}
