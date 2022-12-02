package ru.ibsqa.qualit.steps;

import io.cucumber.java.ru.Допустим;
import org.springframework.beans.factory.annotation.Autowired;
import ru.ibsqa.qualit.converters.FieldValueTable;
import ru.ibsqa.qualit.steps.roles.Read;
import ru.ibsqa.qualit.steps.roles.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeatureStorySteps extends AbstractSteps {
    @Autowired
    private CoreFeatureSteps featureSteps;

    @Допустим("^выполнен сценарий \"([^\"]*)\" из функционала \"([^\"]*)\"$")
    public void executeScenario(@Value String scenario, @Value String function) {
        flow(() ->
                featureSteps.executeScenario(scenario, function)
        );
    }

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