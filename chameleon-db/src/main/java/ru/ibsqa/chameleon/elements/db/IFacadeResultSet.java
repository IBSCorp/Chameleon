package ru.ibsqa.chameleon.elements.db;

import ru.ibsqa.chameleon.db.context.IRowObject;
import ru.ibsqa.chameleon.db.context.sql.NamedParameterStatement;
import ru.ibsqa.chameleon.definitions.repository.db.MetaQuery;
import ru.ibsqa.chameleon.elements.IFacadeCollection;

public interface IFacadeResultSet extends IFacadeCollection<IRowObject> {
    void initialize(MetaQuery metaQuery, NamedParameterStatement statement);
    void close();
    String getFieldValue(String locator);
}
