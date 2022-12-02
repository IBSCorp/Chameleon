package ru.ibsqa.qualit.evaluate.spel;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Objects;

public class AbstractSpelParser implements ISpelParser {
    private StandardEvaluationContext evaluationContext = null;

    @Override
    public EvaluationContext getEvaluationContext() {
        if (Objects.isNull(evaluationContext)) {
            evaluationContext = new StandardEvaluationContext();
            evaluationContext.setRootObject(this);
        }
        return evaluationContext;
    }
}
