package ru.ibsqa.qualit.evaluate;

import ru.ibsqa.qualit.storage.IVariableScope;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;

/**
 * Форматирует заданную дату
 * Пример использования:
 *      #date{05.11.17;dd.MM.yy;-1d;dd-MM-yyyy}
 *      - дата (обязательный параметр)
 *      - исходный формат этой даты (необязательный параметр, по умолчанию dd.MM.yyyy)
 *      - смещение (необязательный параметр, по умолчанию не делается смещение)
 *      - результирующий формат (необязательный параметр, по умолчанию дата возвращается в исходном формате)
 */
@Component
@Evaluator({
        "#date{дата}",
        "#date{дата;исходный_формат;смещение;результирующий_формат}",
        "#date{05.11.17;dd.MM.yy;-1d;dd-MM-yyyy}"
})
public class EvaluatorDateImpl extends AbstractEvaluator implements IDateEvaluator {

    @Override
    protected String getPlaceHolderName() {
        return "date";
    }

    @Override
    protected boolean isMultiArgs() {
        return true;
    }

    @Override
    protected String evalExpression(IVariableScope variableScope, String... args) {

        String value = extract("", args,0);
        String formatFrom = extract("dd.MM.yyyy", args, 1);
        String delta = extract("", args, 2);
        String formatTo = extract(formatFrom, args, 3);

        DateFormat formatterFrom = createFormatter(formatFrom);
        DateFormat formatterTo = createFormatter(formatTo);

        Calendar cl = Calendar.getInstance();

        try {

            cl.setTime(formatterFrom.parse(value));

            applyDelta(cl, delta);

            return formatterTo.format(cl.getTime());

        } catch (ParseException error) {
            throw new IllegalStateException(error);
        }
    }
    
}
