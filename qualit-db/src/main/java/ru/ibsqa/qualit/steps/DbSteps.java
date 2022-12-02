package ru.ibsqa.qualit.steps;

import ru.ibsqa.qualit.db.context.IContextManagerConnection;
import ru.ibsqa.qualit.db.context.IContextManagerQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DbSteps extends AbstractSteps {

    @Autowired
    private IContextManagerConnection contextManagerConnection;

    @Autowired
    private IContextManagerQuery contextManagerQuery;

    @TestStep("к БД \"${dbName}\" выполнено подключение \"${connectionName}\"")
    public void connect(String dbName, String connectionName) {
        contextManagerConnection.connect(dbName, connectionName, null);
    }

    @TestStep("к БД выполнено подключение \"${connectionName}\"")
    public void connect(String connectionName) {
        contextManagerConnection.connect(null, connectionName, null);
    }

    @TestStep("к БД \"${dbName}\" выполнено подключение \"${connectionName}\" с параметрами: \"${params}\"")
    public void connect(String dbName, String connectionName, Map<String,String> params) {
        contextManagerConnection.connect(dbName, connectionName, params);
    }

    @TestStep("к БД выполнено подключение \"${connectionName}\" с параметрами: \"${params}\"")
    public void connect(String connectionName, Map<String,String> params) {
        contextManagerConnection.connect(null, connectionName, params);
    }

    @TestStep("сохранить изменения в БД \"${dbName}\"")
    public void commit(String dbName) {
        contextManagerConnection.commit(dbName);
    }

    @TestStep("сохранить изменения в БД")
    public void commit() {
        contextManagerConnection.commit(null);
    }

    @TestStep("отменить изменения в БД \"${dbName}\"")
    public void rollback(String dbName) {
        contextManagerConnection.rollback(dbName);
    }

    @TestStep("отменить изменения в БД")
    public void rollback() {
        contextManagerConnection.rollback(null);
    }

    @TestStep("закрыто подключение к БД \"${dbName}\"")
    public void close(String dbName) {
        contextManagerConnection.close(dbName);
    }

    @TestStep("закрыто подключение к БД")
    public void close() {
        contextManagerConnection.close(null);
    }

    @TestStep("к БД \"${dbName}\" выполняется запрос \"${queryName}\"")
    public void query(String dbName, String queryName) {
        contextManagerQuery.query(dbName, queryName, null);
    }

    @TestStep("к БД выполняется запрос \"${queryName}\"")
    public void query(String queryName) {
        contextManagerQuery.query(null, queryName, null);
    }

    @TestStep("к БД \"${dbName}\" выполняется запрос \"${queryName}\" c параметрами: \"${params}\"")
    public void query(String dbName, String queryName, Map<String,String> params) {
        contextManagerQuery.query(dbName, queryName, params);
    }

    @TestStep("к БД выполняется запрос \"${queryName}\" c параметрами: \"${params}\"")
    public void query(String queryName, Map<String,String> params) {
        contextManagerQuery.query(null, queryName, params);
    }

}
