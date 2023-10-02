package ru.ibsqa.chameleon.db.context;

import ru.ibsqa.chameleon.context.IContextManager;
import ru.ibsqa.chameleon.context.IContextObject;
import ru.ibsqa.chameleon.context.IContextRegistrator;
import ru.ibsqa.chameleon.context.PickElementResult;
import ru.ibsqa.chameleon.elements.IFacade;
import ru.ibsqa.chameleon.elements.IFacadeCollection;
import ru.ibsqa.chameleon.elements.db.IFacadeResultSet;
import ru.ibsqa.chameleon.i18n.ILocaleManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

@Component
public class ContextManagerQueryImpl implements IContextManagerQuery, IContextRegistrator {

    @Override
    public String getContextName() {
        return localeManager.getMessage("dbQueryContextName");
    }

    @Autowired
    private ILocaleManager localeManager;

    private ThreadLocal<IQueryObject> currentQuery = new InheritableThreadLocal<>();

    private ThreadLocal<IRowObject> currentRow = new InheritableThreadLocal<>();

    @Override
    public IQueryObject query(String dbName, String queryName, Map<String, String> params) {

        // Закрыть предыдущий запрос
        IQueryObject current = currentQuery.get();
        if (null != current) {
            current.closeIfOpened();
        }

        IQueryObject queryObject = new DefaultQueryObject();

        // выполнить подключение к БД
        queryObject.initialize(dbName, queryName, params);
        queryObject.query();

        currentQuery.set(queryObject);
        return queryObject;
    }

    @Override
    public void close() {
        getCurrentQuery().close();
        currentQuery.remove();
        currentRow.remove();
    }

    @Override
    public void closeIfOpened() {
        IQueryObject result = currentQuery.get();
        if (null != result) {
            result.closeIfOpened();
            currentQuery.remove();
            currentRow.remove();
        }
    }

    @Override
    public IQueryObject getCurrentQuery() {
        IQueryObject result = currentQuery.get();
        assertNotNull(result, localeManager.getMessage("noCurrentQueryErrorMessage"));
        return result;
    }

    @Override
    public void setContextCollectionItem(IContextObject contextObject) {
        if (contextObject instanceof IRowObject) {
            currentRow.set((IRowObject) contextObject);
        } else {
            fail(localeManager.getMessage("elements.incorrectRowContextErrorMessage"));
        }
    }

    /**
     * Данный метод ориентирован на получение коллекции строк результатов запроса. Имя коллекции совпадает с именем запроса
     * @param fullPathName
     * @param fieldType
     * @param <FACADE>
     * @return
     */
    @Override
    public <FACADE extends IFacade> PickElementResult pickElement(String fullPathName, Class<FACADE> fieldType) {

        IQueryObject queryObject = currentQuery.get();

        // Если запос не был выполнен, то не ищем в нем
        if (null == queryObject) {
            return null;
        }

        // Сформируем ответ
        PickElementResult<FACADE, IQueryObject> pickElementResult = PickElementResult
                .builder()
                .contextManager((IContextManager) this) // Ссылка на наш менеджер контекстов, который нашел
                .contextObject(queryObject) // Ссылка на запрос, где найден элемент
                .build();

        if (IFacadeCollection.class.isAssignableFrom(fieldType)) {
            // Ищем resultset
            IFacadeResultSet resultSet = null;
            if (fullPathName.equals(queryObject.getName())) {
                resultSet = queryObject.getResultSet();
            }
            pickElementResult.setElement((FACADE) resultSet); // Ссылка на найденный resultSet
        } else {
            // Ищем поле
            pickElementResult.setElement(queryObject.getField(fullPathName)); // Ссылка на найденное поле
        }

        if (null == pickElementResult.getElement()) {
            return null;
        }

        return pickElementResult;
    }

}
