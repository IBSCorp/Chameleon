package ru.ibsqa.qualit.evaluate;

import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;
import ru.ibsqa.qualit.storage.IVariableScope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * Возвращает число в формате с запятой в качестве разделителя дробных знаков, пробелом в качестве разделителя групп и с двумя знаками в дробной части
 * Пример использования:
 *      #amount{1234.5} => 1 234,50
 *      #amount{1234.5;'#,##0.00';',';' '} => 1 234,50
 *      #amount{1234.5;;'.';','} => 1,234.50
 *      #amount{1234.5;'#,#0.000';'.';'_'} => 12_34.500
 */
@Component
@Evaluator(value = {
        "#amount{число}",
        "#amount{число;формат_числа;разделитель_дробной_части;разделитель_групп_разрядов}",
        "#amount{1234.5}",
        "#amount{1234.5;'#,##0.00';',';' '}",
        "#amount{1234.5;;'.';','}",
        "#amount{1234.5;'#,#0.000';'.';'_'}"
}, priority = ConfigurationPriority.LOW)
public class EvaluatorAmountImpl extends AbstractEvaluator {

    @Override
    protected String getPlaceHolderName() {
        return "amount";
    }

    @Override
    protected boolean isMultiArgs() {
        return true;
    }

    @Override
    protected String evalExpression(IVariableScope variableScope, String... args){

        String value = extract("", args, 0);
        String pattern = extract("#,##0.00", args, 1);
        String decimalSeparator = extract(",", args, 2);
        String groupingSeparator = extract(" ", args, 3);

        value = value.replace(" ", "");
        DecimalFormatSymbols customSymbols = new DecimalFormatSymbols();
        customSymbols.setDecimalSeparator(decimalSeparator.charAt(0));
        customSymbols.setGroupingSeparator(groupingSeparator.charAt(0));
        DecimalFormat df = new DecimalFormat(pattern, customSymbols);

        return df.format(new BigDecimal(value.replace(",",".")));
    }

}