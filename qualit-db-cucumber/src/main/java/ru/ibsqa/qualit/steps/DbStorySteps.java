package ru.ibsqa.qualit.steps;

import ru.ibsqa.qualit.converters.FieldValueTable;
import io.cucumber.java.ru.Дано;
import io.cucumber.java.ru.Когда;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbStorySteps extends AbstractSteps {

    @Autowired
    private DbSteps dbSteps;

    @Autowired
    private CollectionSteps collectionSteps;

    @StepDescription(action = "Базы данных->Открыть подключение к БД"
            , subAction = "Именованное подключение"
            , parameters = {"dbName - наименование подключения", "connectionName - наименование соединения"})
    @Дано("^к БД \"([^\"]*)\" выполнено подключение \"([^\"]*)\"$")
    public void connect(String dbName, String connectionName) {
        flow(() ->
                dbSteps.connect(dbName, connectionName)
        );
    }

    @StepDescription(action = "Базы данных->Открыть подключение к БД"
            , subAction = "Безымянное подключение"
            , parameters = {"connectionName - наименование соединения"})
    @Дано("^к БД выполнено подключение \"([^\"]*)\"$")
    public void connect(String connectionName) {
        flow(() ->
                dbSteps.connect(connectionName)
        );
    }

    @StepDescription(action = "Базы данных->Открыть подключение к БД с параметрами"
            , subAction = "Именованное подключение"
            , parameters = {"dbName - наименование подключения", "connectionName - наименование соединения", "params - список параметров"})
    @Дано("^к БД \"([^\"]*)\" выполнено подключение \"([^\"]*)\" с параметрами:$")
    public void connect(String dbName, String connectionName, List<FieldValueTable> params) {
        flow(() ->
                dbSteps.connect(dbName, connectionName, parseConditions(params))
        );
    }

    @StepDescription(action = "Базы данных->Открыть подключение к БД с параметрами"
            , subAction = "Безымянное подключение"
            , parameters = {"connectionName - наименование соединения", "params - список параметров"})
    @Дано("^к БД выполнено подключение \"([^\"]*)\" с параметрами:$")
    public void connect(String connectionName, List<FieldValueTable> params) {
        flow(() ->
                dbSteps.connect(connectionName, parseConditions(params))
        );
    }

    @StepDescription(action = "Базы данных->Закрыть подключение к БД"
            , subAction = "Именованное подключение"
            , parameters = {"dbName - наименование подключения"})
    @Когда("^закрыто подключение к БД \"([^\"]*)\"$")
    public void close(String dbName) {
        flow(() ->
                dbSteps.close(dbName)
        );
    }

    @StepDescription(action = "Базы данных->Закрыть подключение к БД"
            , subAction = "Безымянное подключение")
    @Когда("^закрыто подключение к БД$")
    public void close() {
        flow(() ->
                dbSteps.close()
        );
    }

    @StepDescription(action = "Базы данных->Выполнить запрос"
            , subAction = "Именованное подключение"
            , parameters = {"dbName - наименование подключения", "queryName - наименование запроса"})
    @Когда("^к БД \"([^\"]*)\" выполняется запрос \"([^\"]*)\"$")
    public void query(String dbName, String queryName) {
        flow(() ->
                dbSteps.query(dbName, queryName)
        );
    }

    @StepDescription(action = "Базы данных->Выполнить запрос"
            , subAction = "Безымянное подключение"
            , parameters = {"queryName - наименование запроса"})
    @Когда("^к БД выполняется запрос \"([^\"]*)\"$")
    public void query(String queryName) {
        flow(() ->
                dbSteps.query(evalVariable(queryName))
        );
    }

    @StepDescription(action = "Базы данных->Выполнить запрос и получить первую строку"
            , subAction = "Именованное подключение"
            , parameters = {"dbName - наименование подключения", "queryName - наименование запроса"})
    @Когда("^из БД \"([^\"]*)\" выбирается строка запросом \"([^\"]*)\"$")
    public void queryFirst(String dbName, String queryName) {
        flow(() -> {
            dbSteps.query(dbName, queryName);
            collectionSteps.stepSetCollectionByIndex(queryName, 0);
        });
    }

    @StepDescription(action = "Базы данных->Выполнить запрос и получить первую строку"
            , subAction = "Безымянное подключение"
            , parameters = {"queryName - наименование запроса"})
    @Когда("^из БД выбирается строка запросом \"([^\"]*)\"$")
    public void queryFirst(String queryName) {
        flow(() -> {
            dbSteps.query(queryName);
            collectionSteps.stepSetCollectionByIndex(queryName, 0);
        });
    }

    @StepDescription(action = "Базы данных->Выполнить запрос с параметрами"
            , subAction = "Именованное подключение"
            , parameters = {"dbName - наименование подключения", "queryName - наименование запроса", "params - список параметров"})
    @Когда("^к БД \"([^\"]*)\" выполняется запрос \"([^\"]*)\" c параметрами:$")
    public void query(String dbName, String queryName, List<FieldValueTable> params) {
        flow(() ->
                dbSteps.query(dbName, queryName, parseConditions(params))
        );
    }

    @StepDescription(action = "Базы данных->Выполнить запрос с параметрами"
            , subAction = "Безымянное подключение"
            , parameters = {"queryName - наименование запроса", "params - список параметров"})
    @Когда("^к БД выполняется запрос \"([^\"]*)\" c параметрами:$")
    public void query(String queryName, List<FieldValueTable> params) {
        flow(() ->
                dbSteps.query(queryName, parseConditions(params))
        );
    }

    @StepDescription(action = "Базы данных->Выполнить запрос с параметрами и получить первую строку"
            , subAction = "Именованное подключение"
            , parameters = {"dbName - наименование подключения", "queryName - наименование запроса", "params - список параметров"})
    @Когда("^из БД \"([^\"]*)\" выбирается строка запросом \"([^\"]*)\" c параметрами:$")
    public void queryFirst(String dbName, String queryName, List<FieldValueTable> params) {
        flow(() -> {
            dbSteps.query(dbName, queryName, parseConditions(params));
            collectionSteps.stepSetCollectionByIndex(queryName, 0);
        });
    }

    @StepDescription(action = "Базы данных->Выполнить запрос с параметрами и получить первую строку"
            , subAction = "Безымянное подключение"
            , parameters = {"queryName - наименование запроса", "params - список параметров"})
    @Когда("^из БД выбирается строка запросом \"([^\"]*)\" c параметрами:$")
    public void queryFirst(String queryName, List<FieldValueTable> params) {
        flow(() -> {
            dbSteps.query(queryName, parseConditions(params));
            collectionSteps.stepSetCollectionByIndex(queryName, 0);
        });
    }

    @StepDescription(action = "Базы данных->Сохранить изменения"
            , subAction = "Именованное подключение"
            , parameters = {"dbName - наименование подключения"})
    @Когда("^сохранить изменения в БД \"([^\"]*)\"$")
    public void commit(String dbName) {
        flow(() ->
                dbSteps.commit(dbName)
        );
    }

    @StepDescription(action = "Базы данных->Сохранить изменения"
            , subAction = "Безымянное подключение")
    @Когда("^сохранить изменения в БД$")
    public void commit() {
        flow(() ->
                dbSteps.commit()
        );
    }

    @StepDescription(action = "Базы данных->Отменить изменения"
            , subAction = "Именованное подключение"
            , parameters = {"dbName - наименование подключения"})
    @Когда("^отменить изменения в БД \"([^\"]*)\"$")
    public void rollback(String dbName) {
        flow(() ->
                dbSteps.rollback(dbName)
        );
    }

    @StepDescription(action = "Базы данных->Отменить изменения"
            , subAction = "Безымянное подключение")
    @Когда("^отменить изменения в БД$")
    public void rollback() {
        flow(() ->
                dbSteps.rollback()
        );
    }

    /**
     * Преобразовать List<FieldValueTable> со столбцами field и value в Map
     *
     * @param conditions
     * @return
     */
    private Map<String, String> parseConditions(List<FieldValueTable> conditions) {
        Map<String, String> map = new HashMap<>();
        for (FieldValueTable fieldValue : conditions) {
            map.put(fieldValue.getField(), evalVariable(fieldValue.getValue()));
        }
        return map;
    }

}
