package ru.ibsqa.qualit.influx;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ibsqa.qualit.influx.config.InfluxConfig;
import ru.ibsqa.qualit.steps.aspect.AbstractStepListener;
import ru.ibsqa.qualit.steps.aspect.StepType;

@Slf4j
@Component
public class StepListenerInfluxImpl extends AbstractStepListener {

    @Autowired
    private IInfluxManager influxManager;

    @Autowired
    private InfluxConfig influxConfig;

    @Override
    public void stepBefore(JoinPoint joinPoint, StepType stepType) {
        if (influxConfig.isEnabled()) {
            String description = stepInformerManager.getDescription(joinPoint, stepType);
            if (description != null) {
                influxManager.startStep(description);
            }
        }
    }

    @Override
    public void stepAfterReturning(JoinPoint joinPoint, Object data, StepType stepType) {
        if (influxConfig.isEnabled()) {
            String description = stepInformerManager.getDescription(joinPoint, stepType);
            if (description != null) {
                influxManager.endStep("passed");
            }
        }

    }

    @Override
    public void stepAfterThrowing(JoinPoint joinPoint, Throwable throwable, StepType stepType) {
        if (influxConfig.isEnabled()) {
            String failureStep = stepInformerManager.getDescription(joinPoint, stepType);
            if (failureStep != null) {
                influxManager.increaseErrorCounter();
                influxManager.endStep("failed", failureStep, throwable.getMessage());
            }
        }
    }
}