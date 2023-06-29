package ru.ibsqa.chameleon.evaluate;

import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;
import ru.ibsqa.chameleon.steps.CoreFieldSteps;
import ru.ibsqa.chameleon.storage.IVariableScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Возвращает значение поля
 * Пример использования:
 *      #field{название_поля}
 */
@Component
@Evaluator(value = {
        "#field{название_поля}"
}, priority = ConfigurationPriority.LOW)
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
