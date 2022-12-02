package ru.ibsqa.qualit.db.context;

import ru.ibsqa.qualit.context.IContextManager;

import java.util.Map;

public interface IContextManagerConnection extends IContextManager<IConnectionObject> {
    IConnectionObject connect(String dbName, String connectionName, Map<String,String> params);
    void close(String dbName);
    void closeAll();
    IConnectionObject getConnection(String dbName);
    void commit(String dbName);
    void rollback(String dbName);
}
