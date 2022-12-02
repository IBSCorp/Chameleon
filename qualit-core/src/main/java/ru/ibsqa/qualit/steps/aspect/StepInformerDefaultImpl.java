package ru.ibsqa.qualit.steps.aspect;

import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Component;
import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;
import ru.ibsqa.qualit.utils.aspect.AspectUtils;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Component
public class StepInformerDefaultImpl extends AbstractStepInformer {

    private final Pattern pattern = Pattern.compile("^@ru\\.ibsqa\\.qualit\\.steps\\.TestStep\\(value=\"(.*)\"\\)$");

    @Override
    public Optional<String> getDescription(JoinPoint joinPoint, StepType stepType) {
        if (stepType.isTestStep()) {
            return AspectUtils.extractAnnotationValue(joinPoint, pattern)
                    .map(v -> AspectUtils.getSubstitutedString(v, joinPoint));
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<ParamExtraction>> getParamsList(JoinPoint joinPoint, StepType stepType) {
        if (stepType.isTestStep()) {
            return Optional.of(AspectUtils.getParamsList(joinPoint));
        }
        return Optional.empty();
    }

    @Override
    public ConfigurationPriority getPriority() {
        return ConfigurationPriority.LOW;
    }

}
