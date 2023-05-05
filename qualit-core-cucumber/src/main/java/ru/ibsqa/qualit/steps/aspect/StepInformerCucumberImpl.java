package ru.ibsqa.qualit.steps.aspect;

import lombok.val;
import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Component;
import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;
import ru.ibsqa.qualit.utils.aspect.AspectUtils;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class StepInformerCucumberImpl extends AbstractStepInformer {

    private final Pattern pattern = Pattern.compile("^@io\\.cucumber\\.java\\.[^.]+\\.[^.]+\\((value=)?\"(.*)\"\\)$");

    @Override
    public Optional<String> getDescription(JoinPoint joinPoint, StepType stepType) {
        if (stepType.isBddStep()) {
            return AspectUtils.extractAnnotationValue(joinPoint, pattern).map(a -> {
                if (a.startsWith("^")) {
                    a = a.substring(1);
                }
                if (a.endsWith("$")) {
                    a = a.substring(0, a.length() - 1);
                }
                for (val param : AspectUtils.getParamsList(joinPoint)) {
                    a = a.replaceFirst("\\([^)]+\\)", Matcher.quoteReplacement(Objects.toString(param.getValue())));
                }
                return a;
            });
        }
        return Optional.empty();
    }

    @Override
    public ConfigurationPriority getPriority() {
        return ConfigurationPriority.NORMAL;
    }

}
