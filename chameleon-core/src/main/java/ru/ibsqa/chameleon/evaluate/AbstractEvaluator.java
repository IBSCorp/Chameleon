package ru.ibsqa.chameleon.evaluate;

import ru.ibsqa.chameleon.i18n.ILocaleManager;
import ru.ibsqa.chameleon.storage.IVariableScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractEvaluator implements IEvaluator {

    @Autowired
    protected ILocaleManager localeManager;

    /**
     * Здесь обязательно надо задать имя вычислителя
     * @return
     */
    protected abstract String getPlaceHolderName();

    /**
     * Здесь надо указать ожидается один или несколько параметров
     * @return
     */
    protected abstract boolean isMultiArgs();

    /**
     * Здесь обязательно надо вычислить выражение
     * @return
     */
    protected abstract String evalExpression(IVariableScope variableScope, String... args);

    /**
     * Здесь можно переопределить имя группы
     * @return
     */
    protected String getGroupName() {
        return "value";
    }

    /**
     * Здесь можем переопределить паттерн выражения
     * @return
     */
    protected String getExpressionPattern() {
        return "[^\\{]*";
    }

    protected String getPattern() {
        return String.format("\\#\\s*%1$s\\s*\\{(?<%2$s>%3$s?)\\}", getPlaceHolderName(), getGroupName(), getExpressionPattern());
    }

    private Pattern patternCompiled = null;

    protected Pattern getPatternCompiled() {
        if (null == patternCompiled) {
            patternCompiled = Pattern.compile(getPattern());
        }
        return patternCompiled;
    }

    @Override
    public boolean matches(String param) {
        if (null == param) {
            return false;
        }
        return param.trim().matches("(?s).*" + getPattern() + ".*");
    }

    @Override
    public <T> T evalVariable(IVariableScope variableScope, String param) {
        Matcher matcher = getPatternCompiled().matcher(param);
        StringBuffer stringBuffer = new StringBuffer();
        String value = "";
        while(matcher.find()) {
            String expression = matcher.group(0);
            if (null != getGroupName()) {
                expression = matcher.group(getGroupName());
            }
            String[] args;
            if (isMultiArgs()) {
                args = expression.split("\\s*;s*");
            } else {
                args = new String[1];
                args[0] = expression;
            }
            value = evalExpression(variableScope, args);
            matcher.appendReplacement(stringBuffer, prepareValue(value));
        }
        matcher.appendTail(stringBuffer);
        return (T)stringBuffer.toString();
    }

    protected String extract(String defaultValue, String[] args, int position) {
        String result = (args.length>position && StringUtils.hasText(args[position])) ? args[position].trim() : defaultValue;
        if (result.startsWith("'")) {
            result = result.substring(1,result.length()-1);
        }
        return result;
    }
}