package ru.ibsqa.qualit.evaluate;

import bsh.EvalError;
import bsh.Interpreter;
import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;
import ru.ibsqa.qualit.storage.IVariableScope;
import ru.ibsqa.qualit.storage.IVariableStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Вычисляет код Java. Значение сохраняется в RESULT
 * Пример использования:
 *      #script{RESULT=System.currentTimeMillis()}
 */
@Component
@Evaluator(value = {
        "#script{RESULT=выражение_java}",
        "#script{RESULT=System.currentTimeMillis()}"
}, priority = ConfigurationPriority.LOW)
@Slf4j
public class EvaluatorScriptImpl extends AbstractEvaluator {

    @Autowired
    private IVariableStorage variableStorage;

    @Override
    protected String getPlaceHolderName() {
        return "script";
    }

    @Override
    protected String evalExpression(IVariableScope variableScope, String... args) {
        String script = args[0];
        Interpreter interpreter = new Interpreter();
        try {
            for (Map.Entry<String, Object> entry : variableStorage.getVariables().entrySet()) {
                if (entry.getValue() != null) {
                    try {
                        interpreter.set(entry.getKey(), entry.getValue());
                    } catch (EvalError error) {
                        continue;
                    }
                }
            }
            interpreter.eval(script);
            return String.valueOf(interpreter.get("RESULT"));
        } catch (EvalError e) {
            log.error(e.getMessage(), e);
            throw new AssertionError(String.format("Невозможно выполнить скрипт: [%s]", script), e);
        }
    }

    @Override
    protected boolean isMultiArgs() {
        return false;
    }

}
