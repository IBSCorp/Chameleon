package ru.ibsqa.chameleon.steps;

import io.cucumber.java.ru.Допустим;
import org.springframework.beans.factory.annotation.Autowired;
import ru.ibsqa.chameleon.converters.FieldValueTable;
import ru.ibsqa.chameleon.steps.roles.Read;
import ru.ibsqa.chameleon.steps.roles.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeatureStorySteps extends AbstractSteps {
    @Autowired
    private CoreFeatureSteps featureSteps;

    @StepDescription(action = "Сценарии->Выполнить сценарий"
            , subAction = "Выполнить сценарий"
            , parameters = {"scenario - наименование вызываемого сценария",
                            "function - наименование функции, в которой находится вызываемый сценарий"})
    @Допустим("^выполнен сценарий \"([^\"]*)\" из функционала \"([^\"]*)\"$")
    public void executeScenario(@Value String scenario, @Value String function) {
        flow(() ->
                featureSteps.executeScenario(scenario, function)
        );
    }

    @StepDescription(action = "Сценарии->Выполнить сценарий"
            , subAction = "Выполнить сценарий с параметрами"
            , parameters = {"scenario - наименование вызываемого сценария",
                            "function - наименование функции, в которой находится вызываемый сценарий",
                            "params - параметры вызываемого сценария"})
    @Допустим("^выполнен сценарий \"([^\"]*)\" из функционала \"([^\"]*)\" с параметрами:$")
    public void executeScenario(@Value String scenario, @Value String function, @Read("field") @Value("value") List<FieldValueTable> params) {
        flow(() ->
                featureSteps.executeScenario(scenario, function, parseConditions(params))
        );
    }

    /**
     * Преобразовать List<FieldValueTable> со столбцами field и value в Map
     *
     * @param conditions - параметры в виде FieldValueTable со столбцами field и value
     * @return - параметры в виде Map
     */
    private Map<String, String> parseConditions(List<FieldValueTable> conditions) {
        Map<String, String> map = new HashMap<>();
        for (FieldValueTable fieldValue : conditions) {
            map.put(fieldValue.getField(), evalVariable(fieldValue.getValue()));
        }
        return map;
    }
}