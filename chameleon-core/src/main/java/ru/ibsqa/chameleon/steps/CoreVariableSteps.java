package ru.ibsqa.chameleon.steps;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.ibsqa.chameleon.compare.ICompareManager;

import static org.junit.jupiter.api.Assertions.fail;

@Component
@Primary
@Slf4j
public class CoreVariableSteps extends AbstractSteps {

    @Autowired
    private CoreUtilSteps utilSteps;

    @Autowired
    private ICompareManager compareManager;

    @TestStep("в переменной \"${variable}\" сохранено значение \"${value}\"")
    public void createVariable(String variable, String value) {
        setVariable(variable, evalVariable(value));
    }

    @TestStep("значение выражения \"${expression}\" ${operator} \"${expected}\"")
    public void checkExpressionValue(String expression, String operator, String expected) {
        String actual = evalVariable(expression);
        if (!compareManager.checkValue(operator, actual, expected)) {
            fail(compareManager.buildErrorMessage(operator, message("checkExpression"), actual, expected));
        }
    }
}
