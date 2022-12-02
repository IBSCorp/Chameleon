package ru.ibsqa.qualit.db.context;

import ru.ibsqa.qualit.context.IContextObject;
import ru.ibsqa.qualit.definitions.repository.db.MetaQuery;

import java.sql.ResultSet;

public interface IRowObject extends IContextObject {
    void initialize(MetaQuery metaQuery, ResultSet resultSet);
}
