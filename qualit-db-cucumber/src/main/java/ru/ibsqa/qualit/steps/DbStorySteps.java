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

    @Дано("^к БД \"([^\"]*)\" выполнено подключение \"([^\"]*)\"$")
    public void connect(String dbName, String connectionName) {
        flow(() ->
                dbSteps.connect(dbName, connectionName)
        );
    }

    @Дано("^к БД выполнено подключение \"([^\"]*)\"$")
    public void connect(String connectionName) {
        flow(() ->
                dbSteps.connect(connectionName)
        );
    }

    @Дано("^к БД \"([^\"]*)\" выполнено подключение \"([^\"]*)\" с параметрами:$")
    public void connect(String dbName, String connectionName, List<FieldValueTable> params) {
        flow(() ->
                dbSteps.connect(dbName, connectionName, parseConditions(params))
        );
    }

    @Дано("^к БД выполнено подключение \"([^\"]*)\" с параметрами:$")
    public void connect(String connectionName, List<FieldValueTable> params) {
        flow(() ->
                dbSteps.connect(connectionName, parseConditions(params))
        );
    }

    @Когда("^закрыто подключение к БД \"([^\"]*)\"$")
    public void close(String dbName) {
        flow(() ->
                dbSteps.close(dbName)
        );
    }

    @Когда("^закрыто подключение к БД$")
    public void close() {
        flow(() ->
                dbSteps.close()
        );
    }

    @Когда("^к БД \"([^\"]*)\" выполняется запрос \"([^\"]*)\"$")
    public void query(String dbName, String queryName) {
        flow(() ->
                dbSteps.query(dbName, queryName)
        );
    }

    @Когда("^к БД выполняется запрос \"([^\"]*)\"$")
    public void query(String queryName) {
        flow(() ->
                dbSteps.query(evalVariable(queryName))
        );
    }

    @Когда("^из БД \"([^\"]*)\" выбирается строка запросом \"([^\"]*)\"$")
    public void queryFirst(String dbName, String queryName) {
        flow(() -> {
            dbSteps.query(dbName, queryName);
            collectionSteps.stepSetCollectionByIndex(queryName, 0);
        });
    }

    @Когда("^из БД выбирается строка запросом \"([^\"]*)\"$")
    public void queryFirst(String queryName) {
        flow(() -> {
            dbSteps.query(queryName);
            collectionSteps.stepSetCollectionByIndex(queryName, 0);
        });
    }

    @Когда("^к БД \"([^\"]*)\" выполняется запрос \"([^\"]*)\" c параметрами:$")
    public void query(String dbName, String queryName, List<FieldValueTable> params) {
        flow(() ->
                dbSteps.query(dbName, queryName, parseConditions(params))
        );
    }

    @Когда("^к БД выполняется запрос \"([^\"]*)\" c параметрами:$")
    public void query(String queryName, List<FieldValueTable> params) {
        flow(() ->
                dbSteps.query(queryName, parseConditions(params))
        );
    }

    @Когда("^из БД \"([^\"]*)\" выбирается строка запросом \"([^\"]*)\" c параметрами:$")
    public void queryFirst(String dbName, String queryName, List<FieldValueTable> params) {
        flow(() -> {
            dbSteps.query(dbName, queryName, parseConditions(params));
            collectionSteps.stepSetCollectionByIndex(queryName, 0);
        });
    }

    @Когда("^из БД выбирается строка запросом \"([^\"]*)\" c параметрами:$")
    public void queryFirst(String queryName, List<FieldValueTable> params) {
        flow(() -> {
            dbSteps.query(queryName, parseConditions(params));
            collectionSteps.stepSetCollectionByIndex(queryName, 0);
        });
    }

    @Когда("^сохранить изменения в БД \"([^\"]*)\"$")
    public void commit(String dbName) {
        flow(() ->
                dbSteps.commit(dbName)
        );
    }

    @Когда("^сохранить изменения в БД$")
    public void commit() {
        flow(() ->
                dbSteps.commit()
        );
    }

    @Когда("^отменить изменения в БД \"([^\"]*)\"$")
    public void rollback(String dbName) {
        flow(() ->
                dbSteps.rollback(dbName)
        );
    }

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
