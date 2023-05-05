package ru.ibsqa.qualit.steps;

import org.springframework.lang.NonNull;
import ru.ibsqa.qualit.evaluate.IEvaluateManager;
import ru.ibsqa.qualit.i18n.ILocaleManager;
import ru.ibsqa.qualit.steps.aspect.IStepListenerManager;
import ru.ibsqa.qualit.storage.IVariableStorage;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import ru.ibsqa.qualit.utils.spring.SpringUtils;
import ru.ibsqa.qualit.utils.waiting.WaitingUtils;

import java.time.Duration;
import java.util.Locale;
import java.util.function.Supplier;

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
    private WaitingUtils waitingUtils;

    @Getter(AccessLevel.PROTECTED)
    @Autowired
    private IStepListenerManager stepListenerManager;

    protected boolean waiting(@NonNull Duration timeout, @NonNull Supplier<Boolean> supplier) {
        stepListenerManager.setIgnoredMode(true);
        try {
            return waitingUtils.waiting(timeout, supplier);
        } finally {
            stepListenerManager.setIgnoredMode(false);
        }
    }

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
