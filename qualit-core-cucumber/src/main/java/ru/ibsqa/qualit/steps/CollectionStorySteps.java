package ru.ibsqa.qualit.steps;

import org.apache.commons.lang3.StringUtils;
import ru.ibsqa.qualit.context.Context;
import ru.ibsqa.qualit.context.ContextChange;
import ru.ibsqa.qualit.context.ContextType;
import ru.ibsqa.qualit.converters.FieldOperatorValueTable;
import ru.ibsqa.qualit.converters.FieldValueTable;
import ru.ibsqa.qualit.steps.roles.Collection;
import ru.ibsqa.qualit.steps.roles.Read;
import ru.ibsqa.qualit.steps.roles.Value;
import ru.ibsqa.qualit.steps.roles.Variable;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Тогда;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.fail;

public class CollectionStorySteps extends AbstractSteps implements ICollectionUtils {

    @Autowired
    private CollectionSteps collectionSteps;

    @StepDescription(action = "UI->Коллекции->Выбрать элемент коллекции"
            , parameters = {"collectionName - наименование коллекции", "conditions - условия"})
    @Когда("^выбран элемент коллекции \"([^\"]*)\" с параметрами:$")
    @Context(type = ContextType.COLLECTION, change = ContextChange.BEFORE, parameter = "collectionName", onlyStepContext = true)
    public void stepSetCollectionByConditions(
            @Collection String collectionName,
            @Read("field") @Value({"operator", "value"}) List<FieldOperatorValueTable> conditions
    ) {
        flow(() ->
                collectionSteps.stepSetCollectionByConditions(collectionName, parseConditions(conditions))
        );
    }

    @StepDescription(action = "UI->Коллекции->Выбрать элемент коллекции по индексу"
            , parameters = {"collectionName - наименование коллекции", "index - порядковый номер, начинается с 0"})
    @Когда("^выбран элемент коллекции \"([^\"]*)\" с индексом \"([^\"]*)\"$")
    @Context(type = ContextType.COLLECTION, change = ContextChange.BEFORE, parameter = "collectionName", onlyStepContext = true)
    public void stepSetCollectionByIndex(
            @Collection String collectionName, @Value String index
    ) {
        flow(()-> {
            int indexInt = Integer.parseInt(evalVariable(index));
            collectionSteps.stepSetCollectionByIndex(collectionName, indexInt);
        });
    }

    @StepDescription(action = "UI->Коллекции->Присутствует элемент коллекции"
            , parameters = {"collectionName - наименование коллекции", "conditions - параметры элемента"})
    @Тогда("^присутствует элемент коллекции \"([^\"]*)\" с параметрами:$")
    @Context(type = ContextType.COLLECTION, change = ContextChange.USE, parameter = "collectionName", onlyStepContext = true)
    public void stepCheckItemExisting(
            @Collection String collectionName,
            @Read("field") @Value({"operator", "value"}) List<FieldOperatorValueTable> conditions
    ) {
        flow(()->
                collectionSteps.searchItem(collectionName, parseConditions(conditions))
        );
    }


    @StepDescription(action = "UI->Коллекции->Отсутствует элемент коллекции"
            , parameters = {"collectionName - наименование коллекции", "conditions - параметры элемента"})
    @Тогда("^отсутствует элемент коллекции \"([^\"]*)\" с параметрами:$")
    @Context(type = ContextType.COLLECTION, change = ContextChange.USE, parameter = "collectionName", onlyStepContext = true)
    public void stepCheckItemNotExisting(
            @Collection String collectionName,
            @Read("field") @Value({"operator", "value"}) List<FieldOperatorValueTable> conditions
    ) {
        flow(()-> {
            try {
                collectionSteps.searchItem(collectionName, parseConditions(conditions));
            } catch (AssertionError e) {
                return;
            }
            fail(message("elementExistsAssertMessage"));
        });
    }

    @StepDescription(action = "UI->Коллекции->Количество элементов коллекции равно"
            , parameters = {"collectionName - наименование коллекции", "count - ожидаемое количество элементов"})
    @Тогда("^количество элементов коллекции \"([^\"]*)\" равно \"([^\"]*)\"$")
    @Context(type = ContextType.COLLECTION, change = ContextChange.USE, parameter = "collectionName", onlyStepContext = true)
    public void stepCheckItemCount(
            @Collection String collectionName,
            @Value String count
    ) {
        flow(()-> {
            int countInt = Integer.parseInt(evalVariable(count));
            collectionSteps.stepCheckItemCount(collectionName, countInt);
        });
    }

    @StepDescription(action = "UI->Коллекции->Количество элементов коллекции не равно нулю"
            , parameters = {"collectionName - наименование коллекции"})
    @Тогда("^количество элементов коллекции \"([^\"]*)\" не равно нулю$")
    @Context(type = ContextType.COLLECTION, change = ContextChange.USE, parameter = "collectionName", onlyStepContext = true)
    public void stepCheckNotEmpty(
            @Collection String collectionName
    ) {
        flow(()->
                collectionSteps.stepCheckNotEmpty(collectionName)
        );
    }

    @StepDescription(action = "UI->Коллекции->Количество определенных элементов коллекции равно"
            , parameters = {"collectionName - наименование коллекции", "count - проверяемое количество элементов", "conditions - параметры элементов"})
    @Тогда("^количество элементов коллекции \"([^\"]*)\" равно \"([^\"]*)\" с параметрами:$")
    @Context(type = ContextType.COLLECTION, change = ContextChange.USE, parameter = "collectionName", onlyStepContext = true)
    public void stepCheckItemCount(
            @Collection String collectionName,
            @Value String count,
            @Read("field") @Value({"operator", "value"}) List<FieldOperatorValueTable> conditions
    ) {
        flow(()-> {
            int countInt = Integer.parseInt(evalVariable(count));
            collectionSteps.stepCheckItemCount(collectionName, countInt, parseConditions(conditions));
        });
    }

    @StepDescription(action = "UI->Коллекции->Сохранить количество элементов коллекции"
            , parameters = {"variable - переменная, в которую будет сохранено количество",
                            "collectionName - наименование коллекции",
                            "conditions - параметры элементов"
                            }, expertView = true)
    @Когда("^в переменной \"([^\"]*)\" сохранено количество элементов коллекции \"([^\"]*)\" с параметрами:$")
    @Context(type = ContextType.COLLECTION, change = ContextChange.USE, parameter = "collectionName", onlyStepContext = true)
    public void stepCountCollectionItemsByConditions(
            @Variable String variable,
            @Collection String collectionName,
            @Read("field") @Value({"operator", "value"}) List<FieldOperatorValueTable> conditions
    ) {
        flow(()-> {
            int count = 0;
            try {
                count = collectionSteps.getItems(collectionName, parseConditions(conditions), false).size();
            } catch (AssertionError e) {

            }
            setVariable(variable, String.valueOf(count));
        });
    }

    @StepDescription(action = "UI->Коллекции->Проверить сортировку элементов коллекции"
            , parameters = {"collectionName - наименование коллекции",
            "fieldName - наименование поля, сортировка которого будет проверяться",
            "conditions - параметры элементов"
    }, expertView = true)
    @Тогда("^коллекция \"([^\"]*)\" отсортирована по полю \"([^\"]*)\" с параметрами:$")
    @Context(type = ContextType.COLLECTION, change = ContextChange.USE, parameter = "collectionName", onlyStepContext = true)
    public void checkSorted(
            @Collection String collectionName,
            @Read String fieldName,
            @Value({"field", "value"}) List<FieldValueTable> conditions) {
        flow(()->
                collectionSteps.checkSorted(collectionName, fieldName, parsePairs(conditions, getEvaluateManager()))
        );
    }

    @Тогда("^коллекция \"([^\"]*)\" отсортирована с параметрами:$")
    @Context(type = ContextType.COLLECTION, change = ContextChange.USE, parameter = "collectionName", onlyStepContext = true)
    public void checkSorted(
            @Collection String collectionName,
            @Value({"field", "value"}) List<FieldValueTable> conditions
    ) {
        flow(()->
                collectionSteps.checkSorted(collectionName, parsePairs(conditions, getEvaluateManager()))
        );
    }

    @StepDescription(
            action = "UI->Коллекции->Ожидать появления и выбрать элемент коллекции",
            parameters = {"collectionName - наименование коллекции", "seconds - таймаут в секундах", "conditions - условия"}
            , expertView = true
    )
    @Когда("^ожидается элемент коллекции \"([^\"]*)\" в течение \"([^\"]*)\" секунд с параметрами:$")
    @Context(
            type = ContextType.COLLECTION,
            change = ContextChange.BEFORE,
            parameter = "collectionName",
            onlyStepContext = true
    )
    public void stepWaitCollectionByConditions(
            @Collection String collectionName,
            @Value String seconds,
            @Read("field") @Value({"operator", "value"}) List<FieldOperatorValueTable> conditions
    ) {
        this.flow(() -> {
            collectionSteps.waitCollectionByConditions(collectionName, Integer.parseInt(seconds), this.parseConditions(conditions));
        });
    }

    @StepDescription(
            action = "UI->Коллекции->Ожидать появление элементов коллекции в течение заданного времени",
            parameters = {"collectionName - наименование коллекции", "seconds - таймаут в секундах", "conditions - условия"}
            , expertView = true
    )
    @Когда("^ожидается появление элементов коллекции \"([^\"]*)\" в течение \"([^\"]*)\" секунд$")
    @Context(
            type = ContextType.COLLECTION,
            change = ContextChange.BEFORE,
            parameter = "collectionName",
            onlyStepContext = true
    )
    public void stepWaitCollectionElements(
            @Collection String collectionName,
            @Value String seconds) {
        this.flow(() -> {
            collectionSteps.waitCollectionElements(collectionName, Integer.parseInt(seconds));
        });
    }

    /**
     * Преобразовать List<FieldValueTable> со столбцами field и value в Map
     * @param conditions
     * @return
     */
    private List<CollectionSteps.FindCondition> parseConditions(List<FieldOperatorValueTable> conditions) {
        List<CollectionSteps.FindCondition> list = new ArrayList<>();
        for (FieldOperatorValueTable c : conditions) {
            list.add(CollectionSteps.FindCondition.builder()
                    .fieldName(c.getField())
                    .operator(c.getOperator())
                    .value(Optional.ofNullable(c.getValue()).orElse(StringUtils.EMPTY))
                    .build());
        }
        return list;
    }

}
