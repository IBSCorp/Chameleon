package ru.ibsqa.chameleon.steps.aspect;

import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractStepListener implements IStepListener {

    @Autowired
    protected IStepInformerManager stepInformerManager;

    @Override
    public void stepBefore(JoinPoint joinPoint, StepType stepType) {

    }

    @Override
    public void stepAfter(JoinPoint joinPoint, StepType stepType) {

    }

    @Override
    public void stepAfterReturning(JoinPoint joinPoint, Object data, StepType stepType) {

    }

    @Override
    public void stepAfterThrowing(JoinPoint joinPoint, Throwable throwable, StepType stepType) {

    }
}
