package ru.ibsqa.qualit.db.context;

import ru.ibsqa.qualit.i18n.ILocaleManager;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

@Component
@Slf4j
public class ContextManagerConnectionImpl implements IContextManagerConnection {

    @Autowired
    public IContextManagerQuery contextManagerQuery;

    @Override
    public String getContextName() {
        return localeManager.getMessage("dbConnectionContextName");
    }

    @Autowired
    private ILocaleManager localeManager;

    //private ThreadLocal<Map<String, IConnectionObject>> connections1;
    private Map<String, IConnectionObject> connections2;

    @PostConstruct
    private void init() {
        //connections1 = new ThreadLocal<>();
        //connections1.set(new HashMap<>());
        connections2 = new HashMap<>();
    }

    private Map<String, IConnectionObject> getConnections() {
        //return connections1.get();
        return connections2;
    }

    @Override
    public IConnectionObject connect(String dbName, String connectionName, Map<String,String> params) {

        // Закрыть подключение, если оно уже было с таким именем
        IConnectionObject connectionObject = getConnections().get(dbName);
        if (null != connectionObject) {
            close(dbName);
        }

        connectionObject = new DefaultConnectionObject();

        // выполнить подключение к БД
        connectionObject.initialize(connectionName, params);
        connectionObject.connect();

        getConnections().put(dbName, connectionObject);
        return connectionObject;
    }

    @Override
    public void close(String dbName) {
        contextManagerQuery.closeIfOpened();
        getConnection(dbName).close();
        getConnections().remove(dbName);
    }

    @Override
    @PreDestroy
    public void closeAll() {
        contextManagerQuery.closeIfOpened();
        if (null != getConnections()) {
            for (Map.Entry<String, IConnectionObject> entry : getConnections().entrySet()) {
                entry.getValue().close();
            }
            getConnections().clear();
        }
    }

    @Override
    public IConnectionObject getConnection(String dbName) {
        IConnectionObject result = getConnections().get(dbName);
        assertNotNull(result, localeManager.getMessage("noCurrentConnectionErrorMessage", dbName));
        return result;
    }

    @Override
    public void commit(String dbName) {
        try {
            getConnection(dbName).getConnection().commit();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @Override
    public void rollback(String dbName) {
        try {
            getConnection(dbName).getConnection().rollback();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

}
