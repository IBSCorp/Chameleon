package ru.ibsqa.chameleon.db.context;

import ru.ibsqa.chameleon.db.context.sql.NamedParameterStatement;
import ru.ibsqa.chameleon.definitions.repository.IRepositoryManager;
import ru.ibsqa.chameleon.definitions.repository.db.AbstractField;
import ru.ibsqa.chameleon.definitions.repository.db.MetaQuery;
import ru.ibsqa.chameleon.elements.IFacade;
import ru.ibsqa.chameleon.elements.db.DefaultResultField;
import ru.ibsqa.chameleon.elements.db.DefaultResultSet;
import ru.ibsqa.chameleon.elements.db.IFacadeResultField;
import ru.ibsqa.chameleon.elements.db.IFacadeResultSet;
import ru.ibsqa.chameleon.evaluate.IEvaluateManager;
import ru.ibsqa.chameleon.i18n.ILocaleManager;
import ru.ibsqa.chameleon.utils.spring.SpringUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
public class DefaultQueryObject implements IQueryObject {

    private MetaQuery metaQuery = null;

    private IConnectionObject connectionObject = null;

    private Map<String,String> params = null;

    private NamedParameterStatement statement = null;

    private IFacadeResultSet resultSetObject = null;

    private IContextManagerConnection contextManagerConnection = SpringUtils.getBean(IContextManagerConnection.class);

    private IRepositoryManager repositoryManager = SpringUtils.getBean(IRepositoryManager.class);

    private IEvaluateManager evaluateManager = SpringUtils.getBean(IEvaluateManager.class);

    private List<IFacadeResultField> fields = new ArrayList<>();

    @Override
    public void initialize(String dbName, String queryName, Map<String, String> params) {
        // Получим подключение по имени
        connectionObject = contextManagerConnection.getConnection(dbName);

        // Найдем метаданные запроса в репозитории
        metaQuery = repositoryManager.pickElement(queryName,MetaQuery.class);
        assertNotNull(metaQuery, ILocaleManager.message("elementNotFoundInRepositoriesErrorMessage", queryName));

        // Заполним список полей
        if (null != metaQuery.getResult()) {
            for (AbstractField metaField : metaQuery.getResult().getFields()) {
                IFacadeResultField resultField = new DefaultResultField();
                resultField.initialization(this, metaField);
                fields.add(resultField);
            }
        }

        this.params = params;
    }

    @Override
    public void query() {
        assertNotNull(metaQuery, ILocaleManager.message("dbQueryInitAssertMessage", metaQuery));

        Connection connection = connectionObject.getConnection();
        try {
            String text = evaluateManager.evalVariable(metaQuery.getStatement());
            if (SpringUtils.isClassPathResource(text)) {
                InputStream inputStream = null;
                try {
                    inputStream = SpringUtils.getResource(text).getInputStream();
                    Scanner s = new Scanner(inputStream, "UTF-8").useDelimiter("\\A");
                    text = s.hasNext() ? s.next() : "";
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                    fail();
                } finally {
                    if (null != inputStream) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            log.error(e.getMessage(), e);
                        }
                    }
                }
                text = evaluateManager.evalVariable(text);
            }

            log.debug(String.format("SQL prepare statement\r\n%s", text));

            statement = new NamedParameterStatement(connection, text);

            // установить параметры
            if (null != params && null != metaQuery.getParams()) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    if (null != entry.getKey()) {
                        AbstractField metaField = metaQuery.getParams().getFields().stream().filter(field -> entry.getKey().equals(field.getName())).findFirst().orElse(null);
                        assertNotNull(metaField, ILocaleManager.message("elementNotFoundInRepositoriesErrorMessage", entry.getKey()));
                        log.debug(String.format("SQL set param %s(%s)=%s", entry.getKey(), metaField.getLocator(), entry.getValue()));
                        if (metaField.getPrimitiveType() == Long.class) {
                            statement.setLong(metaField.getLocator(), Long.parseLong(entry.getValue()));
                        } else if (metaField.getPrimitiveType() == BigDecimal.class) {
                            statement.setBigDecimal(metaField.getLocator(), new BigDecimal(entry.getValue()));
                        } else if (metaField.getPrimitiveType() == Long.class) {
                            statement.setBoolean(metaField.getLocator(), Boolean.parseBoolean(entry.getValue()));
                        } else {
                            statement.setString(metaField.getLocator(), entry.getValue());
                        }
                    }
                }
            }

            if (null == metaQuery.getResult()) {
                log.debug(String.format("SQL execute update"));
                statement.executeUpdate();
            } else {
                resultSetObject = new DefaultResultSet();
                resultSetObject.initialize(metaQuery, statement);
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            fail(ILocaleManager.message("dbQueryExecuteErrorMessage", metaQuery.getName()));
        }
    }

    @Override
    public void close() {
        assertNotNull(metaQuery, ILocaleManager.message("dbQueryInitAssertMessage", metaQuery));
        assertNotNull(statement, ILocaleManager.message("noCurrentQueryErrorMessage"));
        closeInternal();
    }

    @Override
    public void closeIfOpened() {
        if (null != metaQuery && null != statement) {
            closeInternal();
        }
    }

    @Override
    public String getName() {
        if (null != metaQuery) {
            return metaQuery.getName();
        }
        return "";
    }

    private void closeInternal() {
        try {
            if (null != resultSetObject) {
                resultSetObject.close();
            }
            log.debug(String.format("SQL close statement"));
            statement.close();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            fail(ILocaleManager.message("dbQueryCloseErrorMessage", metaQuery.getName()));
        }
        statement = null;
    }

    @Override
    public IFacadeResultSet getResultSet() {
        return resultSetObject;
    }

    @Override
    public <T extends IFacade> T getField(String fieldName) {
        // возвращать поле текущей строки
        return (T) fields.stream().filter(field -> null != field.getName() && field.getName().equals(fieldName)).findFirst().orElse(null);
    }
}
