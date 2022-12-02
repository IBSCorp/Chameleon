package ru.ibsqa.qualit.evaluate;

import ru.ibsqa.qualit.i18n.ILocaleManager;
import ru.ibsqa.qualit.steps.IStepFlow;
import ru.ibsqa.qualit.storage.IVariableScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EvaluateManagerImpl implements IEvaluateManager {

    @Autowired
    private ILocaleManager localeManager;

    @Autowired
    private IStepFlow stepFlow;

    private List<IEvaluator> evaluators;

    @Autowired
    private void collectEvaluators(List<IEvaluator> evaluators) {
        this.evaluators = evaluators;
    }

    @Override
    public <T> T evalVariable(IVariableScope variableScope, String param) {
        if (null == param) {
            return null;
        }
        if (stepFlow.isIgnore()) {
            return (T) param;
        }
        try {
            for (IEvaluator evaluator : evaluators) {
                if (evaluator.matches(param)) {
                    return evalVariable(variableScope, evaluator.evalVariable(variableScope, param));
                }
            }
            return (T) param;
        } catch (Exception e) {
            throw new AssertionError(localeManager.getMessage("evalErrorMessage", param), e);
        }
    }

}
