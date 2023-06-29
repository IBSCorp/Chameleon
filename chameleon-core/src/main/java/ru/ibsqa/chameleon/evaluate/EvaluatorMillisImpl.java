package ru.ibsqa.chameleon.evaluate;

import org.springframework.stereotype.Component;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;
import ru.ibsqa.chameleon.storage.IVariableScope;

/**
 * Возвращает текущее время в миллисекундах
 * Пример использования:
 *      #millis{}
 */
@Component
@Evaluator(value = {
        "#millis{}"
}, priority = ConfigurationPriority.LOW)
public class EvaluatorMillisImpl extends AbstractEvaluator {

    @Override
    protected String getPlaceHolderName() {
        return "millis";
    }

    @Override
    protected boolean isMultiArgs() {
        return true;
    }

    @Override
    protected String evalExpression(IVariableScope variableScope, String... args) {
        return String.valueOf(System.currentTimeMillis())+"L";
    }

}
