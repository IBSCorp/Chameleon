package ru.ibsqa.chameleon.db.context;

import ru.ibsqa.chameleon.context.IContextObject;

import java.sql.Connection;
import java.util.Map;

public interface IConnectionObject extends IContextObject {
    void initialize(String connectionName, Map<String,String> params);
    void connect();
    void close();
    Connection getConnection();
}
