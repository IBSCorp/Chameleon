package ru.ibsqa.qualit.evaluate;

import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;
import ru.ibsqa.qualit.storage.IVariableScope;
import ru.ibsqa.qualit.storage.IVariableStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Возвращает значение переменной
 * Пример использования:
 *      #{имя_переменной}
 */
@Component
@Evaluator(value = {}, priority = ConfigurationPriority.LOW)
public class EvaluatorVariableImpl implements IEvaluator {

    @Autowired
    private IVariableStorage variableStorage;

    private final static String VAR_PATTERN = "#\\{(?<var>[^{^(})]*)}";

    private final static Pattern VAR_PATTERN_COMPILED = Pattern.compile(VAR_PATTERN);

    @Override
    public boolean matches(String param) {
        if (null == param) {
            return false;
        }
        return param.trim().matches("(?s).*" + VAR_PATTERN + ".*");
    }

    @Override
    public <T> T evalVariable(IVariableScope variableScope, String param) {
        Matcher varMatcher = VAR_PATTERN_COMPILED.matcher(param);
        StringBuffer varSB = new StringBuffer();
        while(varMatcher.find()) {
            String var = varMatcher.group("var");
            String value = null;
            if (variableStorage.hasVariable(variableScope, var)) {
                value = String.valueOf(variableStorage.getVariable(variableScope, var));
            } else {
                value = System.getProperty(var);
            }
            varMatcher.appendReplacement(varSB, prepareValue(value));
        }
        varMatcher.appendTail(varSB);
        return (T)varSB.toString();
    }

}
