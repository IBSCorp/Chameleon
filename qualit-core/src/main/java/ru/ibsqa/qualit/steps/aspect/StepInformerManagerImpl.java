package ru.ibsqa.qualit.steps.aspect;

import lombok.val;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Component
public class StepInformerManagerImpl implements IStepInformerManager {

    private List<IStepInformer> informers;

    @Autowired
    private void collectResolvers(List<IStepInformer> informers) {
        this.informers = informers;
        this.informers.sort(Comparator.comparing(IStepInformer::getPriority));
    }

    @Override
    public String getDescription(JoinPoint joinPoint, StepType stepType) {
        for (val informer : informers) {
            val result = informer.getDescription(joinPoint, stepType);
            if (result.isPresent()) {
                return result.get();
            }
        }
        return joinPoint.getSignature().getName();
    }

    @Override
    public List<ParamExtraction> getParamsList(JoinPoint joinPoint, StepType stepType) {
        for (val informer : informers) {
            val result = informer.getParamsList(joinPoint, stepType);
            if (result.isPresent()) {
                return result.get();
            }
        }
        return Collections.emptyList();
    }
}
