package ru.ibsqa.qualit.steps;

import ru.ibsqa.qualit.utils.IExamplesTableParser;
import org.apache.commons.lang3.tuple.Pair;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.fail;

@Component
public class CollectionStorySteps extends AbstractSteps {

    @Autowired
    private CollectionSteps collectionSteps;

    @Autowired
    private IExamplesTableParser examplesTableParser;

    @When("выбран элемент коллекции \"$collectionName\" с параметрами: $conditions")
    public void stepSetCollectionByConditions(String collectionName, ExamplesTable conditions){
        flow(()->
                collectionSteps.stepSetCollectionByConditions(collectionName, parseConditions(conditions, false))
        );
    }

    @When("выбран элемент коллекции \"$collectionName\" содержащий параметры: $conditions")
    public void stepSetCollectionByContainsConditions(String collectionName, ExamplesTable conditions){
        flow(()->
                collectionSteps.stepSetCollectionByConditions(collectionName, parseConditions(conditions, true))
        );
    }

    @When("выбран элемент коллекции \"$collectionName\" с индексом \"$index\"")
    public void stepSetCollectionByIndex(String collectionName, String index){
        flow(()-> {
            int indexInt = Integer.parseInt(evalVariable(index));
            collectionSteps.stepSetCollectionByIndex(collectionName, indexInt);
        });
    }

    @Then("присутствует элемент коллекции \"$collectionName\" с параметрами: $conditions")
    public void stepCheckItemExisting(String collectionName, ExamplesTable conditions) {
        flow(()->
                collectionSteps.searchItem(collectionName, parseConditions(conditions, false))
        );
    }

    @Then("присутствует элемент коллекции \"$collectionName\" с параметрами (по частичному вхождению): $conditions")
    public void stepCheckItemExistingByContainConditions(String collectionName, ExamplesTable conditions) {
        flow(()->
                collectionSteps.searchItem(collectionName, parseConditions(conditions, true))
        );
    }

    @Then("отсутствует элемент коллекции \"$collectionName\" с параметрами: $conditions")
    public void stepCheckItemNotExisting(String collectionName, ExamplesTable conditions) {
        flow(()-> {
            try {
                collectionSteps.searchItem(collectionName, parseConditions(conditions, false));
            } catch (AssertionError e) {
                return;
            }
            fail(message("elementExistsAssertMessage"));
        });
    }


    @Then("отсутствует элемент коллекции \"$collectionName\" с параметрами (по частичному вхождению): $conditions")
    public void stepCheckItemNotExistingByContainConditions(String collectionName, ExamplesTable conditions) {
        flow(()-> {
            try {
                collectionSteps.searchItem(collectionName, parseConditions(conditions, true));
            } catch (AssertionError e) {
                return;
            }
            fail(message("elementExistsAssertMessage"));
        });
    }

    @Then("количество элементов коллекции \"$collectionName\" равно \"$count\"")
    public void stepCheckItemCount(String collectionName, String count) {
        flow(()-> {
            int countInt = Integer.parseInt(evalVariable(count));
            collectionSteps.stepCheckItemCount(collectionName, countInt);
        });
    }

    @Then("количество элементов коллекции \"$collectionName\" не равно нулю")
    public void stepCheckNotEmpty(String collectionName) {
        flow(()->
            collectionSteps.stepCheckNotEmpty(collectionName)
        );
    }

    @Then("количество элементов коллекции \"$collectionName\" равно \"$count\" с параметрами: $conditions")
    public void stepCheckItemCount(String collectionName, String count, ExamplesTable conditions) {
        flow(()-> {
            int countInt = Integer.parseInt(evalVariable(count));
            collectionSteps.stepCheckItemCount(collectionName, countInt, parseConditions(conditions, false));
        });
    }

    @Then("количество элементов коллекции \"$collectionName\" равно \"$count\" с параметрами (по частичному вхождению): $conditions")
    public void stepCheckItemCountByContainConditions(String collectionName, String count, ExamplesTable conditions) {
        flow(()-> {
            int countInt = Integer.parseInt(evalVariable(count));
            collectionSteps.stepCheckItemCount(collectionName, countInt, parseConditions(conditions, true));
        });
    }

    @When ("в переменной \"$variable\" сохранено количество элементов коллекции \"$collectionName\" с параметрами: $conditions")
    public void stepCountCollectionItemsByConditions(String variable, String collectionName, ExamplesTable conditions) {
        flow(()-> {
            int count = 0;
            try {
                count = collectionSteps.getItems(collectionName, parseConditions(conditions, false), false).size();
            } catch (AssertionError e) {

            }
            setVariable(variable, String.valueOf(count));
        });
    }

    @When("в переменной \"$variable\" сохранено количество элементов коллекции \"$collectionName\" с параметрами \\(по частичному вхождению\\):")
    public void stepCountCollectionItemsByContainConditions(String variable, String collectionName, ExamplesTable conditions) {
        flow(()-> {
            int count = 0;
            try {
                count = collectionSteps.getItems(collectionName, parseConditions(conditions, true), true).size();
            } catch (AssertionError e) {

            }
            setVariable(variable, String.valueOf(count));
        });
    }

    /*
    @Then("коллекция \"$collectionName\" отсортирована по полю \"$field\" в порядке возрастания")
    public void stepCheckSortedAsc(String collectionName, String field) throws ParseException {
        collectionSteps.checkSorted(collectionName, field, true);
    }

    @Then("коллекция \"$collectionName\" отсортирована по полю \"$field\" в порядке убывания")
    public void stepCheckSortedDesc(String collectionName, String field) throws ParseException {
        collectionSteps.checkSorted(collectionName, field, false);
    }*/

    @Then("коллекция \"$collectionName\" отсортирована по полю \"$field\" с параметрами: $conditions")
    public void checkSorted(String collectionName, String field, ExamplesTable conditions) {
        flow(()->
            collectionSteps.checkSorted(collectionName, field, parseConditionsToList(conditions))
        );
    }

    @Then("коллекция \"$collectionName\" отсортирована  с параметрами: $conditions")
    public void checkSorted(String collectionName, ExamplesTable conditions) {
        flow(()->
            collectionSteps.checkSorted(collectionName, parseConditionsToList(conditions))
        );
    }

    /**
     * Преобразовать ExamplesTable со столбцами field и value в Map
     * @param conditions
     * @return
     */
    private List<CollectionSteps.FindCondition> parseConditions(ExamplesTable conditions, boolean contains) {
        List<CollectionSteps.FindCondition> result = new ArrayList<>();
        for (Map<String, String> row : conditions.getRows()) {
            result.add(CollectionSteps.FindCondition.builder()
                    .fieldName(row.get("field"))
                    .operator(contains ? CompareOperatorEnum.CONTAINS : CompareOperatorEnum.EQUALS)
                    .value(evalVariable(row.get("value")))
                    .build());
        }
        return result;
    }

    /**
     * Преобразовать ExamplesTable со столбцами field и value в List
     * @param conditions
     * @return
     */
    private List<Pair<String,String>> parseConditionsToList(ExamplesTable conditions) {
        return examplesTableParser.parseToList(conditions, "field", "value");
    }

}
