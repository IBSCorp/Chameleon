package ru.ibsqa.qualit.evaluate;

import ru.ibsqa.qualit.page_factory.pages.IContextManagerPage;
import ru.ibsqa.qualit.storage.IVariableScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Возвращает json текущей страницы
 * Пример использования:
 *      #json{currentPage}
 */
@Component
@Evaluator({
        "#json{currentPage}"
})
public class EvaluatorPageJsonImpl implements IEvaluator {

    @Autowired
    private IContextManagerPage contextManager;

    private final static String PAGE_JSON_PATTERN = "\\#json\\{currentPage\\}";

    private final static Pattern PAGE_JSON_PATTERN_COMPILED = Pattern.compile(PAGE_JSON_PATTERN);

    @Override
    public boolean matches(String param) {
        if (null == param) {
            return false;
        }
        return param.trim().matches("(?s).*" + PAGE_JSON_PATTERN + ".*");
    }

    @Override
    public <T> T evalVariable(IVariableScope variableScope, String param) {
        Matcher varMatcher = PAGE_JSON_PATTERN_COMPILED.matcher(param);
        StringBuffer jsonSB = new StringBuffer();
        while(varMatcher.find()) {
            String value = contextManager.getCurrentPage().getFieldValue();
            varMatcher.appendReplacement(jsonSB, prepareValue(value));
        }
        varMatcher.appendTail(jsonSB);
        return (T)jsonSB.toString();
    }

}
