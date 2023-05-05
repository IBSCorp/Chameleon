package ru.ibsqa.qualit.evaluate;

import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;
import ru.ibsqa.qualit.storage.IVariableScope;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * Возвращает случайное число
 * Пример использования:
 *      #random{100;200}
 */
@Component
@Evaluator(value = {
        "#random{начало_диапазона;конец_диапазона}",
        "#random{100;200}"
}, priority = ConfigurationPriority.LOW)
public class EvaluatorRandomNumberImpl extends AbstractEvaluator {

    @Override
    protected String getPlaceHolderName() {
        return "random";
    }

    @Override
    protected boolean isMultiArgs() {
        return true;
    }

    @Override
    protected String evalExpression(IVariableScope variableScope, String... args) {
        int min = Integer.parseInt(extract("0", args, 0));
        int max = Integer.parseInt(extract("1", args, 1));
        Random rn = new Random();
        int range = max - min + 1;
        int randomNum =  rn.nextInt(range) + min;
        return String.valueOf(randomNum);
    }

}
