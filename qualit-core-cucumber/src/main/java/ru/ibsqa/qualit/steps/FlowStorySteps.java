package ru.ibsqa.qualit.steps;

import ru.ibsqa.qualit.compare.ICompareManager;
import ru.ibsqa.qualit.steps.roles.Operator;
import ru.ibsqa.qualit.steps.roles.Read;
import ru.ibsqa.qualit.steps.roles.Value;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Тогда;
import org.springframework.beans.factory.annotation.Autowired;

public class FlowStorySteps extends AbstractSteps {

    @Autowired
    private CoreFieldSteps fieldSteps;

    @Autowired
    private ICompareManager compareManager;

    @StepDescription(action = "Условия->Если значение поля равно, то"
            , parameters = {"fieldName - наименование поля", "operator - оператор сравнения", "value - проверяемое значение"}
            , expertView = true)
    @Когда("^значение поля \"([^\"]*)\" ([^\"]+) \"([^\"]*)\", выполнять следующие шаги:$")
    public void ifFlowFieldValueEquals(
            @Read String fieldName,
            @Operator String operator,
            @Value String value
    ) {
        if (getStepFlow().prepareFlowStep()) {
            String actual = fieldSteps.getFieldValue(fieldName);
            getStepFlow().createBlock(compareManager.checkValue(operator, actual, value));
        }
    }

    @StepDescription(action = "Условия->Если значение выражения равно, то"
            , parameters = {"expression - вычисляемое выражение", "operator - оператор сравнения", "value - проверяемое значение"}
            , expertView = true)
    @Когда("^значение выражения \"([^\"]*)\" ([^\"]+) \"([^\"]*)\", выполнять следующие шаги:$")
    public void ifFlowVariableValueEquals(
            @Value String expression,
            @Operator String operator,
            @Value String value
    ) {
        if (getStepFlow().prepareFlowStep()) {
            String actual = evalVariable(expression);
            getStepFlow().createBlock(compareManager.checkValue(operator, actual, value));
        }
    }

    @StepDescription(action = "Условия->Иначе выполнить", expertView = true)
    @Тогда("^иначе, выполнять следующие шаги:$")
    public void elseFlow() {
        getStepFlow().inverseBlock();
    }

    @StepDescription(action = "Условия->Конец условия", expertView = true)
    @Тогда("^конец условия$")
    public void endIfFlow() {
        getStepFlow().completeBlock();
    }

}
