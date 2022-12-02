package ru.ibsqa.qualit.steps;

import ru.ibsqa.qualit.context.Context;
import ru.ibsqa.qualit.converters.ExpressionOperatorValueTable;
import ru.ibsqa.qualit.converters.VariableValueTable;
import ru.ibsqa.qualit.steps.roles.Value;
import ru.ibsqa.qualit.steps.roles.Variable;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Тогда;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
public class VariableStorySteps extends AbstractSteps {

    @Autowired
    private CoreVariableSteps variableSteps;

    @StepDescription(action = "Переменные->Создать переменную",
            subAction = "Создать переменную"
            , parameters = {"variable - наименование переменной", "value - значение переменной"})
    @Когда("^в переменной \"([^\"]*)\" сохранено значение \"(.*)\"$")
    @Context(variables = {"variable"})
    public void createVariable(
            @Variable String variable,
            @Value String value) {
        flow(()-> variableSteps.createVariable(variable, evalVariable(value)));
    }

    @StepDescription(action = "Переменные->Создать переменную",
            subAction = "Создать переменную"
            , multiple = true
            , parameters = {"variables - переменные и их значения"})
    @Когда("^заполняются переменные:$")
//TODO    @Context(variables = {"variables.variable"})
    public void stepFillVariable(
            @Variable("variable") @Value("value") List<VariableValueTable> variables
    ) {
        flow(()-> {
            for (VariableValueTable variableValue : variables) {
                setVariable(variableValue.getVariable(), evalVariable(variableValue.getValue()));
            }
        });
    }

    @StepDescription(action = "Переменные->Сравнить значение выражения"
            , subAction = "Сравнить значение выражения"
            , parameters = {"expression - вычисляемое выражение", "operator - оператор сравнения", "value - значение"})
    @Тогда("^значение выражения \"([^\"]*)\" (равно|не равно|содержит значение|не содержит значение|начинается с|не начинается с|оканчивается на|не оканчивается на|соответствует|не соответствует|равно игнорируя регистр|не равно игнорируя регистр|равно игнорируя пробелы|не равно игнорируя пробелы|по длине равно|по длине не равно|по длине больше|по длине не меньше|по длине меньше|по длине не больше|больше|больше или равно|меньше|меньше или равно|раньше или равно|позже или равно|позже|раньше) \"([^\"]*)\"$")
    public void stepCheckExpressionValue(
            @Value String expression,
            @Value CompareOperatorEnum operator,
            @Value String value
    ) {
        flow(()->
                variableSteps.checkExpressionValue(expression, operator, value)
        );
    }

    @StepDescription(action = "Переменные->Сравнить значение выражения"
            , subAction = "Сравнить значение выражения"
            , multiple = true
            , parameters = {"expressions - вычисляемые выражения, операторы, значения"})
    @Тогда("^значения выражений:$")
    public void stepCheckExpressionValue(
            @Value({"expression", "operator", "value"}) List<ExpressionOperatorValueTable> expressions) {
        flow(()-> {
            for (ExpressionOperatorValueTable row : expressions) {
                variableSteps.checkExpressionValue(row.getExpression(), row.getOperator(), row.getValue());
            }
        });
    }
}