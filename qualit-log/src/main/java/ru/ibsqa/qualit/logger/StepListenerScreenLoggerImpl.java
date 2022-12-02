package ru.ibsqa.qualit.logger;

import ru.ibsqa.qualit.steps.aspect.AbstractStepListener;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import ru.ibsqa.qualit.steps.aspect.StepType;

import java.util.Objects;

public class StepListenerScreenLoggerImpl extends AbstractStepListener {

    @Autowired
    private ILogRender logRender;

    @Override
    public void stepBefore(JoinPoint joinPoint, StepType stepType) {
        String description = stepInformerManager.getDescription(joinPoint, stepType);
        if (Objects.nonNull(description)) {
            logRender.println(false, description);
        }
    }

    @Override
    public void stepAfterThrowing(JoinPoint joinPoint, Throwable throwable, StepType stepType) {
        logRender.println(true, throwable.toString());
    }

}
