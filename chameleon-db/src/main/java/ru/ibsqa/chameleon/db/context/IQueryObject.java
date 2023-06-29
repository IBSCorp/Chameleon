package ru.ibsqa.chameleon.db.context;

import ru.ibsqa.chameleon.context.IContextObject;
import ru.ibsqa.chameleon.elements.db.IFacadeResultSet;

import java.util.Map;

public interface IQueryObject extends IContextObject {
    void initialize(String dbName, String queryName, Map<String, String> params);
    void query();
    void close();
    void closeIfOpened();
    String getName();
    IFacadeResultSet getResultSet();
}
