package ru.ibsqa.chameleon.db.context;

import ru.ibsqa.chameleon.definitions.repository.IRepositoryManager;
import ru.ibsqa.chameleon.definitions.repository.db.AbstractField;
import ru.ibsqa.chameleon.definitions.repository.db.MetaConnection;
import ru.ibsqa.chameleon.elements.IFacade;
import ru.ibsqa.chameleon.evaluate.IEvaluateManager;
import ru.ibsqa.chameleon.i18n.ILocaleManager;
import ru.ibsqa.chameleon.storage.IVariableScope;
import ru.ibsqa.chameleon.storage.IVariableStorage;
import ru.ibsqa.chameleon.utils.spring.SpringUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class DefaultConnectionObject implements IConnectionObject {

    private MetaConnection metaConnection;

    @Getter
    private Connection connection;

    private String jdbcUrl;

    private IRepositoryManager repositoryManager = SpringUtils.getBean(IRepositoryManager.class);

    private IEvaluateManager evaluateManager = SpringUtils.getBean(IEvaluateManager.class);

    private IVariableStorage variableStorage = SpringUtils.getBean(IVariableStorage.class);

    @Override
    public void initialize(String connectionName, Map<String, String> params) {
        metaConnection = repositoryManager.pickElement(connectionName,MetaConnection.class);
        assertNotNull(metaConnection, ILocaleManager.message("elementNotFoundInRepositoriesErrorMessage", connectionName));

        // Проверить наличие драйвера
        if (null != metaConnection.getDriver()) {
            try {
                Class.forName(metaConnection.getDriver());
            } catch (ClassNotFoundException e) {
                fail(ILocaleManager.message("dbDriverNotFoundErrorMessage", metaConnection.getDriver()));
            }
        }

        // Вычислить jdbc url используя параметры
        IVariableScope connectionParamsScope = variableStorage.getDefaultScope().createChild();
        if (null != params) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (null != entry.getKey()) {
                    AbstractField metaField = null;
                    if (null != metaConnection.getParams()) {
                        metaField = metaConnection.getParams().getFields().stream().filter(field -> entry.getKey().equals(field.getName())).findFirst().orElse(null);
                    }
                    assertNotNull(metaField, ILocaleManager.message("elementNotFoundInRepositoriesErrorMessage", entry.getKey()));
                    connectionParamsScope.setVariable(metaField.getLocator(), entry.getValue());
                }
            }
        }
        jdbcUrl = evaluateManager.evalVariable(connectionParamsScope, metaConnection.getUrl());
    }

    @Override
    public void connect() {
        assertNotNull(metaConnection, ILocaleManager.message("dbConnectionInitAssertMessage"));
        assertNull(connection, ILocaleManager.message("dbConnectionEmptyAssertMessage"));
        try {
            log.debug(String.format("SQL get connection %s", jdbcUrl));
            connection = DriverManager.getConnection(jdbcUrl);
            connection.setAutoCommit(metaConnection.isAutoCommit());
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            fail(ILocaleManager.message("dbConnectionOpenErrorMessage", metaConnection.getName()));
        }
    }

    @Override
    public void close() {
        assertNotNull(connection, ILocaleManager.message("dbConnectionOpenAssertMessage"));
        try {
            log.debug(String.format("SQL close connection %s", jdbcUrl));
            connection.close();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            fail(ILocaleManager.message("dbConnectionCloseErrorMessage", metaConnection.getName()));
        }
        connection = null;
    }

    @Override
    public <T extends IFacade> T getField(String fieldName) {
        return null;
    }
}
