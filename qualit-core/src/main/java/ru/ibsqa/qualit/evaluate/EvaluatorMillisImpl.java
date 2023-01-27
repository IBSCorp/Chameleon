package ru.ibsqa.qualit.evaluate;

import org.springframework.stereotype.Component;
import ru.ibsqa.qualit.storage.IVariableScope;

/**
 * Возвращает текущее время в миллисекундах
 * Пример использования:
 *      #millis{}
 */
@Component
@Evaluator({
        "#millis{}"
})
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
