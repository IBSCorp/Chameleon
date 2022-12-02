package ru.ibsqa.qualit.db.context;

import ru.ibsqa.qualit.context.IContextManager;

import java.util.Map;

public interface IContextManagerQuery extends IContextManager<IQueryObject> {
    IQueryObject query(String dbName, String queryName, Map<String, String> params);
    void close();
    void closeIfOpened();
    IQueryObject getCurrentQuery();
}
