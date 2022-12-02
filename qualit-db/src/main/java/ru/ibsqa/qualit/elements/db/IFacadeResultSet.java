package ru.ibsqa.qualit.elements.db;

import ru.ibsqa.qualit.db.context.IRowObject;
import ru.ibsqa.qualit.db.context.sql.NamedParameterStatement;
import ru.ibsqa.qualit.definitions.repository.db.MetaQuery;
import ru.ibsqa.qualit.elements.IFacadeCollection;

public interface IFacadeResultSet extends IFacadeCollection<IRowObject> {
    void initialize(MetaQuery metaQuery, NamedParameterStatement statement);
    void close();
    String getFieldValue(String locator);
}
