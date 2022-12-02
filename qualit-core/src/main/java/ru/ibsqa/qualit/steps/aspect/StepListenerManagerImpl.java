package ru.ibsqa.qualit.steps.aspect;

import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class StepListenerManagerImpl implements IStepListenerManager {

    private List<IStepListener> beforeListeners;
    private List<IStepListener> afterListeners;

    @Autowired
    private void collectListeners(List<IStepListener> listeners) {
        this.beforeListeners = listeners.stream().sorted(Comparator.comparing(IStepListener::getPriority).reversed()).collect(Collectors.toList());
        this.afterListeners = listeners.stream().sorted(Comparator.comparing(IStepListener::getPriority)).collect(Collectors.toList());
    }

    @Override
    public void stepBefore(JoinPoint joinPoint, StepType stepType) {
        beforeListeners.forEach(l -> l.stepBefore(joinPoint, stepType));
    }

    @Override
    public void stepAfter(JoinPoint joinPoint, StepType stepType) {
        afterListeners.forEach(l -> l.stepAfter(joinPoint, stepType));
    }

    @Override
    public void stepAfterReturning(JoinPoint joinPoint, Object data, StepType stepType) {
        afterListeners.forEach(l -> l.stepAfterReturning(joinPoint, data, stepType));
    }

    @Override
    public void stepAfterThrowing(JoinPoint joinPoint, Throwable throwable, StepType stepType) {
        afterListeners.forEach(l -> l.stepAfterThrowing(joinPoint, throwable, stepType));
    }
}
