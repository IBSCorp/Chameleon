package ru.ibsqa.qualit.converters;

import ru.ibsqa.qualit.evaluate.IEvaluateManager;
import ru.ibsqa.qualit.utils.spring.SpringUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

public class Variable {

    @NonNull
    @Getter @Setter(AccessLevel.PRIVATE)
    private String value;

    @NonNull
    @Getter @Setter(AccessLevel.PRIVATE)
    private String expression;

    @Deprecated
    public Variable(String expressionToEval) {
        this(expressionToEval, SpringUtils.getBean(IEvaluateManager.class));
    }

    public Variable(String expressionToEval, IEvaluateManager evaluateManager) {
        setExpression(expressionToEval);
        setValue(evaluateManager.evalVariable(expressionToEval));
    }

    @Override
    public String toString() {
        return getValue();
    }
}

