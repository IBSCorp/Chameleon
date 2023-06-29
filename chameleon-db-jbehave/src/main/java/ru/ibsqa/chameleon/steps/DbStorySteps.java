package ru.ibsqa.chameleon.steps;

import ru.ibsqa.chameleon.converters.Variable;
import ru.ibsqa.chameleon.utils.IExamplesTableParser;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DbStorySteps extends AbstractSteps {

    @Autowired
    private DbSteps dbSteps;

    @Autowired
    private CollectionSteps collectionSteps;

    @Autowired
    private IExamplesTableParser examplesTableParser;

    @Given("к БД \"$dbName\" выполнено подключение \"$connectionName\"")
    public void connect(Variable dbName, Variable connectionName) {
        flow(()->
                dbSteps.connect(dbName.getValue(), connectionName.getValue())
        );
    }

    @Given("к БД выполнено подключение \"$connectionName\"")
    public void connect(Variable connectionName) {
        flow(()->
                dbSteps.connect(connectionName.getValue())
        );
    }

    @Given("к БД \"$dbName\" выполнено подключение \"$connectionName\" с параметрами: $params")
    public void connect(Variable dbName, Variable connectionName, ExamplesTable params) {
        flow(()->
                dbSteps.connect(dbName.getValue(), connectionName.getValue(),examplesTableParser.parseToMap(params,"field","value"))
        );
    }

    @Given("к БД выполнено подключение \"$connectionName\" с параметрами: $params")
    public void connect(Variable connectionName, ExamplesTable params) {
        flow(()->
                dbSteps.connect(connectionName.getValue(),examplesTableParser.parseToMap(params,"field","value"))
        );
    }

    @When("закрыто подключение к БД \"$dbName\"")
    public void close(Variable dbName) {
        flow(()->
                dbSteps.close(dbName.getValue())
        );
    }

    @When("закрыто подключение к БД")
    public void close() {
        flow(()->
                dbSteps.close()
        );
    }

    @When("к БД \"$dbName\" выполняется запрос \"$queryName\"")
    public void query(Variable dbName, Variable queryName) {
        flow(()->
                dbSteps.query(dbName.getValue(), queryName.getValue())
        );
    }

    @When("к БД выполняется запрос \"$queryName\"")
    public void query(Variable queryName) {
        flow(()->
                dbSteps.query(evalVariable(queryName.getValue()))
        );
    }

    @When("из БД \"$dbName\" выбирается строка запросом \"$queryName\"")
    public void queryFirst(Variable dbName, Variable queryName) {
        flow(()-> {
            dbSteps.query(dbName.getValue(), queryName.getValue());
            collectionSteps.stepSetCollectionByIndex(queryName.getValue(), 0);
        });
    }

    @When("из БД выбирается строка запросом \"$queryName\"")
    public void queryFirst(Variable queryName) {
        flow(()-> {
            dbSteps.query(queryName.getValue());
            collectionSteps.stepSetCollectionByIndex(queryName.getValue(), 0);
        });
    }

    @When("к БД \"$dbName\" выполняется запрос \"$queryName\" c параметрами: $params")
    public void query(Variable dbName, Variable queryName, ExamplesTable params) {
        flow(()->
                dbSteps.query(dbName.getValue(), queryName.getValue(), examplesTableParser.parseToMap(params,"field","value"))
        );
    }

    @When("к БД выполняется запрос \"$queryName\" c параметрами: $params")
    public void query(Variable queryName, ExamplesTable params) {
        flow(()->
                dbSteps.query(queryName.getValue(),examplesTableParser.parseToMap(params,"field","value"))
        );
    }

    @When("из БД \"$dbName\" выбирается строка запросом \"$queryName\" c параметрами: $params\"")
    public void queryFirst(Variable dbName, Variable queryName, ExamplesTable params) {
        flow(()-> {
            dbSteps.query(dbName.getValue(), queryName.getValue(), examplesTableParser.parseToMap(params, "field", "value"));
            collectionSteps.stepSetCollectionByIndex(queryName.getValue(), 0);
        });
    }

    @When("из БД выбирается строка запросом \"$queryName\" c параметрами: $params\"")
    public void queryFirst(Variable queryName, ExamplesTable params) {
        flow(()-> {
            dbSteps.query(queryName.getValue(), examplesTableParser.parseToMap(params, "field", "value"));
            collectionSteps.stepSetCollectionByIndex(queryName.getValue(), 0);
        });
    }

    @When("сохранить изменения в БД \"$dbName\"")
    public void commit(Variable dbName) {
        flow(()->
                dbSteps.commit(dbName.getValue())
        );
    }

    @When("сохранить изменения в БД")
    public void commit() {
        flow(()->
                dbSteps.commit()
        );
    }

    @When("отменить изменения в БД \"$dbName\"")
    public void rollback(Variable dbName) {
        flow(()->
                dbSteps.rollback(dbName.getValue())
        );
    }

    @When("отменить изменения в БД")
    public void rollback() {
        flow(()->
                dbSteps.rollback()
        );
    }

}
