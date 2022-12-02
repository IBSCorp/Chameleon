package ru.ibsqa.qualit.steps.aspect;

import org.aspectj.lang.JoinPoint;

public interface IStepListenerManager {
    void stepBefore(JoinPoint joinPoint, StepType stepType);

    void stepAfter(JoinPoint joinPoint, StepType stepType);

    void stepAfterReturning(JoinPoint joinPoint, Object data, StepType stepType);

    void stepAfterThrowing(JoinPoint joinPoint, Throwable throwable, StepType stepType);
}
