package ru.ibsqa.qualit.db.context;

import ru.ibsqa.qualit.context.IContextObject;
import ru.ibsqa.qualit.elements.db.IFacadeResultSet;

import java.util.Map;

public interface IQueryObject extends IContextObject {
    void initialize(String dbName, String queryName, Map<String, String> params);
    void query();
    void close();
    void closeIfOpened();
    String getName();
    IFacadeResultSet getResultSet();
}
