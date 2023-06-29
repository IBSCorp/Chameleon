package ru.ibsqa.chameleon.steps.aspect;

import org.aspectj.lang.JoinPoint;

import java.util.List;
import java.util.Optional;

public abstract class AbstractStepInformer implements IStepInformer {

    @Override
    public Optional<String> getDescription(JoinPoint joinPoint, StepType stepType) {
        return Optional.empty();
    }

    @Override
    public Optional<List<ParamExtraction>> getParamsList(JoinPoint joinPoint, StepType stepType) {
        return Optional.empty();
    }

}
