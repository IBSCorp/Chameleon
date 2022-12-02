package ru.ibsqa.qualit.evaluate;

import ru.ibsqa.qualit.steps.CoreFieldSteps;
import ru.ibsqa.qualit.storage.IVariableScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Возвращает значение поля
 * Пример использования:
 *      #field{название_поля}
 */
@Component
@Evaluator({
        "#field{название_поля}"
})
public class EvaluatorFieldImpl extends AbstractEvaluator {

    @Autowired
    private CoreFieldSteps fieldSteps;

    @Override
    protected String getPlaceHolderName() {
        return "field";
    }

    @Override
    protected String evalExpression(IVariableScope variableScope, String... args) {
        return fieldSteps.getFieldValue(args[0]);
    }

    @Override
    protected boolean isMultiArgs() {
        return false;
    }

}
