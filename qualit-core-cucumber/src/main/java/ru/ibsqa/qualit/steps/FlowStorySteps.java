package ru.ibsqa.qualit.steps;

import ru.ibsqa.qualit.steps.roles.Read;
import ru.ibsqa.qualit.steps.roles.Value;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Тогда;
import org.springframework.beans.factory.annotation.Autowired;

public class FlowStorySteps extends AbstractSteps {

    @Autowired
    private CoreFieldSteps fieldSteps;

    @StepDescription(action = "Условия->Если значение поля равно, то"
            , parameters = {"fieldName - наименование поля", "operator - оператор сравнения", "value - проверяемое значение"}
            , expertView = true)
    @Когда("^значение поля \"([^\"]*)\" (равно|не равно|содержит значение|не содержит значение|начинается с|не начинается с|оканчивается на|не оканчивается на|соответствует|не соответствует|равно игнорируя регистр|не равно игнорируя регистр|равно игнорируя пробелы|не равно игнорируя пробелы|по длине равно|по длине не равно|по длине больше|по длине не меньше|по длине меньше|по длине не больше|больше|больше или равно|меньше|меньше или равно) \"([^\"]*)\", выполнять следующие шаги:$")
    public void ifFlowFieldValueEquals(
            @Read String fieldName,
            @Value CompareOperatorEnum operator,
            @Value String value
    ) {
        if (getStepFlow().prepareFlowStep()) {
            String actual = fieldSteps.getFieldValue(fieldName);
            getStepFlow().createBlock(operator.checkValue(actual, value));
        }
    }

    @StepDescription(action = "Условия->Если значение выражения равно, то"
            , parameters = {"expression - вычисляемое выражение", "operator - оператор сравнения", "value - проверяемое значение"}
            , expertView = true)
    @Когда("^значение выражения \"([^\"]*)\" (равно|не равно|содержит значение|не содержит значение|начинается с|не начинается с|оканчивается на|не оканчивается на|соответствует|не соответствует|равно игнорируя регистр|не равно игнорируя регистр|равно игнорируя пробелы|не равно игнорируя пробелы|по длине равно|по длине не равно|по длине больше|по длине не меньше|по длине меньше|по длине не больше|больше|больше или равно|меньше|меньше или равно) \"([^\"]*)\", выполнять следующие шаги:$")
    public void ifFlowVariableValueEquals(
            @Value String expression,
            @Value CompareOperatorEnum operator,
            @Value String value
    ) {
        if (getStepFlow().prepareFlowStep()) {
            String actual = evalVariable(expression);
            getStepFlow().createBlock(operator.checkValue(actual, value));
        }
    }

    /*
    @StepDescription(action = "Условия->Если значение поля равно, то"
            , parameters = {"fieldName - наименование поля", "value - проверяемое значение"}
            , expertView = true)
    @Когда("^значение поля \"([^\"]*)\" равно \"([^\"]*)\", выполнять следующие шаги:$")
    public void ifFlowFieldValueEquals(
            @Read String fieldName,
            @Value String value
    ) {
        String actual = fieldSteps.prepareValue(fieldSteps.getFieldValue(fieldName));
        String expected = fieldSteps.prepareValue(value);
        getStepFlow().createBlock(actual.equals(expected));
    }

    @StepDescription(action = "Условия->Если значение поля не равно, то"
            , parameters = {"fieldName - наименование поля", "value - проверяемое значение"}
            , expertView = true)
    @Когда("^значение поля \"([^\"]*)\" не равно \"([^\"]*)\", выполнять следующие шаги:$")
    public void ifFlowFieldValueNotEquals(
            @Read String fieldName,
            @Value String value
    ) {
        String actual = fieldSteps.prepareValue(fieldSteps.getFieldValue(fieldName));
        String expected = fieldSteps.prepareValue(value);
        getStepFlow().createBlock(!actual.equals(expected));
    }

    @StepDescription(action = "Условия->Если значение выражения равно, то"
            , parameters = {"expression - вычисляемое выражение", "value - проверяемое значение"}
            , expertView = true)
    @Когда("^значение выражения \"([^\"]*)\" равно \"([^\"]*)\", выполнять следующие шаги:$")
    public void ifFlowVariableValueEquals(
            @Value String expression,
            @Value String value
    ) {
        String actual = fieldSteps.prepareValue(variable);
        String expected = fieldSteps.prepareValue(value);
        getStepFlow().createBlock(actual.equals(expected));
    }

    @StepDescription(action = "Условия->Если значение выражения не равно, то"
            , parameters = {"expression - вычисляемое выражение", "value - проверяемое значение"}
            , expertView = true)
    @Когда("^значение выражения \"([^\"]*)\" не равно \"([^\"]*)\", выполнять следующие шаги:$")
    public void ifFlowVariableValueNotEquals(
            @Value String variable,
            @Value String value) {
        String actual = fieldSteps.prepareValue(variable);
        String expected = fieldSteps.prepareValue(value);
        getStepFlow().createBlock(!actual.equals(expected));
    }
    */

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
