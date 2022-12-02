package ru.ibsqa.qualit.evaluate;

import ru.ibsqa.qualit.storage.IVariableScope;
import org.springframework.stereotype.Component;

import java.util.Calendar;

/**
 * Возвращает текущую дату в заданном формате
 * Примеры использования:
 *      #now{dd.MM.yyyy;+1M}
 *      #now{dd.MM.yyyy HH,mm}
 *      #now{d llll yyyyг.;-45d}
 */
@Component
@Evaluator({
        "#now{дата;исходный_формат;смещение}",
        "#now{dd.MM.yyyy;+1M}}",
        "#now{dd.MM.yyyy HH,mm}",
        "#now{;+5y}",
        "#now{'d llll yyyyг.';-45d}"
})
public class EvaluatorNowImpl extends AbstractEvaluator implements IDateEvaluator {

    @Override
    protected String getPlaceHolderName() {
        return "now";
    }

    @Override
    protected boolean isMultiArgs() {
        return true;
    }

    @Override
    protected String evalExpression(IVariableScope variableScope, String... args) {

        String format = extract("dd.MM.yyyy", args, 0);
        String delta = extract("", args, 1);

        Calendar cl = Calendar.getInstance();

        applyDelta(cl, delta);

        return createFormatter(format).format(cl.getTime());
    }

}
