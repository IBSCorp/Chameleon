package ru.ibsqa.qualit.evaluate;

import bsh.EvalError;
import bsh.Interpreter;
import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;
import ru.ibsqa.qualit.storage.IVariableScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Возвращает результат математической операции
 * Пример использования:
 *      #math{5*2+7}
 */
@Component
@Evaluator(value = {
        "#math{арифметическое_выражение}",
        "#math{5*2+7}"
}, priority = ConfigurationPriority.LOW)
@Slf4j
public class EvaluatorMathImpl extends AbstractEvaluator {

    @Override
    protected String getPlaceHolderName() {
        return "math";
    }

    @Override
    protected String getExpressionPattern() {
        return "[\\dL\\.,\\(\\)\\s+\\-*\\/]+";
    }

    @Override
    protected String evalExpression(IVariableScope variableScope, String... args) {
        String mathExpression = args[0];
        String commaSeparator = "";
        if (mathExpression.contains(","))
            commaSeparator = ",";
        mathExpression = mathExpression.replace(" ", "").replace(",", ".");
        String value = "";
        Interpreter interpreter = new Interpreter();
        try {
            interpreter.eval("result = " + mathExpression);
            value = String.valueOf(interpreter.get("result"));
        } catch (EvalError e) {
            throw new AssertionError(localeManager.getMessage("evalErrorMessage", mathExpression), e);
        }
        if (commaSeparator.isEmpty())
            return value;
        return value.replace(".",",");
    }

    @Override
    protected boolean isMultiArgs() {
        return false;
    }

}
