package ru.ibsqa.qualit.steps;

import ru.ibsqa.qualit.evaluate.IEvaluateManager;
import ru.ibsqa.qualit.i18n.ILocaleManager;
import ru.ibsqa.qualit.storage.IVariableStorage;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Locale;

public abstract class AbstractSteps {

    @Getter(AccessLevel.PROTECTED)
    @Autowired
    private IEvaluateManager evaluateManager;

    @Getter(AccessLevel.PROTECTED)
    @Autowired
    private IVariableStorage variableStorage;

    @Getter(AccessLevel.PROTECTED)
    @Autowired
    private ILocaleManager localeManager;

    @Getter(AccessLevel.PROTECTED)
    @Autowired
    private IStepFlow stepFlow;

    protected void flow(Runnable stepAction) {
        stepFlow.checkStepFlow(stepAction);
    }

    protected String evalVariable(String param) {
        return evaluateManager.evalVariable(param);
    }

    protected void setVariable(String name, Object value) {
        variableStorage.setVariable(name, value);
    }

    protected String message(String messageName) {
        return localeManager.getMessage(messageName);
    }

    protected String message(String messageName, Object ... arguments) {
        return localeManager.getMessage(messageName, arguments);
    }

    protected String message(String messageName, Locale locale)  {
        return localeManager.getMessage(messageName, locale);
    }

    protected String message(String messageName, Locale locale, Object ... arguments)  {
        return localeManager.getMessage(messageName, locale, arguments);
    }

    public String prepareValue(String value) {
        if (null != value) {
            return value.replace(" ", "").replace(" ", "");
        }
        return null;
    }
}
