package ru.ibsqa.qualit.steps;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import static org.junit.jupiter.api.Assertions.fail;

@Component
@Primary
@Slf4j
public class CoreVariableSteps extends AbstractSteps {

    @Autowired
    private CoreUtilSteps utilSteps;

    @TestStep("в переменной \"${variable}\" сохранено значение \"${value}\"")
    public void createVariable(String variable, String value) {
        setVariable(variable, evalVariable(value));
    }

    @TestStep("значение выражения \"${expression}\" ${operator} \"${expected}\"")
    public void checkExpressionValue(String expression, CompareOperatorEnum operator, String expected) {
        String actual = evalVariable(expression);
        if (!operator.checkValue(actual, expected)) {
            fail(operator.buildErrorMessage(message("checkExpression"), actual, expected));
        }
    }
}
