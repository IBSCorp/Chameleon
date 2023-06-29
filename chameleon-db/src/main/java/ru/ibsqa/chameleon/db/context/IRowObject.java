package ru.ibsqa.chameleon.db.context;

import ru.ibsqa.chameleon.context.IContextObject;
import ru.ibsqa.chameleon.definitions.repository.db.MetaQuery;

import java.sql.ResultSet;

public interface IRowObject extends IContextObject {
    void initialize(MetaQuery metaQuery, ResultSet resultSet);
}
