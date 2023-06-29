package ru.ibsqa.chameleon.steps.aspect;

import org.aspectj.lang.JoinPoint;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public interface IStepInformer {
    Optional<String> getDescription(JoinPoint joinPoint, StepType stepType);
    Optional<List<ParamExtraction>> getParamsList(JoinPoint joinPoint, StepType stepType);

    default Optional<Map<String, Object>> getParamsMap(JoinPoint joinPoint, StepType stepType) {
        return getParamsList(joinPoint, stepType)
                .map(list -> list
                        .stream()
                        .collect(Collectors.toMap(ParamExtraction::getKey, ParamExtraction::getValue)));
    }

    default ConfigurationPriority getPriority() {
        return ConfigurationPriority.HIGH;
    }
}
