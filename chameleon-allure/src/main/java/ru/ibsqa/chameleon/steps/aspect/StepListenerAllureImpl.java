package ru.ibsqa.chameleon.steps.aspect;

import io.qameta.allure.Allure;
import io.qameta.allure.model.Parameter;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;
import io.qameta.allure.util.ObjectUtils;
import io.qameta.allure.util.ResultsUtils;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ibsqa.chameleon.AllureConfiguration;
import ru.ibsqa.chameleon.utils.aspect.AspectUtils;

import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class StepListenerAllureImpl extends AbstractStepListener {

    @Autowired
    private AllureConfiguration allureConfiguration;

    @Override
    public void stepBefore(JoinPoint joinPoint, StepType stepType) {
        if (!allureConfiguration.isEnabled()) {
            return;
        }
        if (stepType.isTestStep()) {
            String uuid = UUID.randomUUID().toString();
            StepResult stepResult = new StepResult();
            stepResult.setName(stepInformerManager.getDescription(joinPoint, stepType));
            stepResult.setParameters(
                    AspectUtils.getParamsList(joinPoint).stream()
                            .map(p -> {
                                Parameter parameter = new Parameter();
                                parameter.setName(p.getKey());
                                parameter.setValue(ObjectUtils.toString(p.getValue()));
                                return parameter;
                            })
                            .collect(Collectors.toList())
            );
            Allure.getLifecycle().startStep(uuid, stepResult);
        }
    }

    @Override
    public void stepAfterReturning(JoinPoint joinPoint, Object data, StepType stepType) {
        if (!allureConfiguration.isEnabled()) {
            return;
        }
        if (stepType.isTestStep()) {
            Allure.getLifecycle().updateStep((u) ->
                    u.setStatus(Status.PASSED)
            );
            Allure.getLifecycle().stopStep();
        }
    }

    @Override
    public void stepAfterThrowing(JoinPoint joinPoint, Throwable throwable, StepType stepType) {
        if (!allureConfiguration.isEnabled()) {
            return;
        }
        if (stepType.isTestStep()) {
            Allure.getLifecycle().updateStep((u) ->
                    u.setStatus(ResultsUtils.getStatus(throwable).orElse(Status.BROKEN))
                            .setStatusDetails(ResultsUtils.getStatusDetails(throwable).orElse(null))
            );
            Allure.getLifecycle().stopStep();
        }
    }
}
