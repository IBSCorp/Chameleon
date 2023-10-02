package ru.ibsqa.chameleon.steps;

import ru.ibsqa.chameleon.evaluate.IEvaluateManager;
import ru.ibsqa.chameleon.i18n.ILocaleManager;
import ru.ibsqa.chameleon.steps.aspect.IStepListenerManager;
import ru.ibsqa.chameleon.storage.IVariableStorage;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import ru.ibsqa.chameleon.utils.spring.SpringUtils;

import java.util.Locale;

public abstract class AbstractSteps {

    @Getter(AccessLevel.PROTECTED)
    @Autowired
    private IEvaluateManager evaluateManager;

    @Getter(AccessLevel.PROTECTED)
    @Autowired
    private IVariableStorage variableStorage;

    @Getter(AccessLevel.PROTECTED)
    private final static ILocaleManager localeManager = SpringUtils.getBean(ILocaleManager.class);

    @Getter(AccessLevel.PROTECTED)
    @Autowired
    private IStepFlow stepFlow;

    @Getter(AccessLevel.PROTECTED)
    @Autowired
    private IStepListenerManager stepListenerManager;

    protected void flow(Runnable stepAction) {
        stepFlow.checkStepFlow(stepAction);
    }

    protected String evalVariable(String param) {
        return evaluateManager.evalVariable(param);
    }

    @TestStep("в переменной \"${name}\" установлено значение \"${value}\"")
    protected void setVariable(String name, Object value) {
        variableStorage.setVariable(name, value);
    }

    protected static String message(String messageName) {
        return localeManager.getMessage(messageName);
    }

    protected static String message(String messageName, Object ... arguments) {
        return localeManager.getMessage(messageName, arguments);
    }

    protected static String message(String messageName, Locale locale)  {
        return localeManager.getMessage(messageName, locale);
    }

    protected static String message(String messageName, Locale locale, Object ... arguments)  {
        return localeManager.getMessage(messageName, locale, arguments);
    }

    public String prepareValue(String value) {
        if (null != value) {
            return value.replace(" ", "");
        }
        return null;
    }
}
