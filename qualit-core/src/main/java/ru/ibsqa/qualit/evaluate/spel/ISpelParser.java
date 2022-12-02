package ru.ibsqa.qualit.evaluate.spel;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

public interface ISpelParser {
    EvaluationContext getEvaluationContext();

    ExpressionParser expressionParser = new SpelExpressionParser();

    default Object getAttr(String attrName) {
        return expressionParser.parseExpression(attrName).getValue(getEvaluationContext());
    }
}
