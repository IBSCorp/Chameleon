package ru.ibsqa.chameleon.db.context;

import ru.ibsqa.chameleon.context.IContextManager;

import java.util.Map;

public interface IContextManagerQuery extends IContextManager<IQueryObject> {
    IQueryObject query(String dbName, String queryName, Map<String, String> params);
    void close();
    void closeIfOpened();
    IQueryObject getCurrentQuery();
}
