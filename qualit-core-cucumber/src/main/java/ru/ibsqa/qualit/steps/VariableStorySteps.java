package ru.ibsqa.qualit.steps;

import ru.ibsqa.qualit.context.Context;
import ru.ibsqa.qualit.converters.ExpressionOperatorValueTable;
import ru.ibsqa.qualit.converters.VariableValueTable;
import ru.ibsqa.qualit.steps.roles.Operator;
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
    @Тогда("^значение выражения \"([^\"]*)\" ([^\"]+) \"([^\"]*)\"$")
    public void stepCheckExpressionValue(
            @Value String expression,
            @Operator String operator,
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
            @Value({"expression", "value"})
            @Operator({"operator"})
                    List<ExpressionOperatorValueTable> expressions) {
        flow(()-> {
            for (ExpressionOperatorValueTable row : expressions) {
                variableSteps.checkExpressionValue(row.getExpression(), row.getOperator(), row.getValue());
            }
        });
    }
}