package ru.ibsqa.chameleon.steps.aspect;

import org.aspectj.lang.JoinPoint;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface IStepInformerManager {
    String getDescription(JoinPoint joinPoint, StepType stepType);
    List<ParamExtraction> getParamsList(JoinPoint joinPoint, StepType stepType);

    default Map<String, Object> getParamsMap(JoinPoint joinPoint, StepType stepType) {
        return getParamsList(joinPoint, stepType)
                .stream()
                .collect(Collectors.toMap(ParamExtraction::getKey, ParamExtraction::getValue));
    }
}
