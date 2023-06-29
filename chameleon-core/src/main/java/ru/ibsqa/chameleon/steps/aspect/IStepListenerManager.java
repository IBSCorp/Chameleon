package ru.ibsqa.chameleon.steps.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

public interface IStepListenerManager {
    void stepBefore(JoinPoint joinPoint, StepType stepType);

    void stepAfter(JoinPoint joinPoint, StepType stepType);

    void stepAfterReturning(JoinPoint joinPoint, Object data, StepType stepType);

    void stepAfterThrowing(JoinPoint joinPoint, Throwable throwable, StepType stepType);

    Object stepAround(ProceedingJoinPoint proceedingJoinPoint, StepType stepType) throws Throwable;

    boolean isIgnoredMode();

    void setIgnoredMode(boolean hiddenMode);
}
