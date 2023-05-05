package ru.ibsqa.qualit.steps.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ibsqa.qualit.asserts.AssertLayer;
import ru.ibsqa.qualit.asserts.IAssertManager;
import ru.ibsqa.qualit.steps.visibility.IStepVisibilityManager;
import ru.ibsqa.qualit.steps.HiddenStep;
import ru.ibsqa.qualit.utils.aspect.AspectUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Slf4j
public class StepListenerManagerImpl implements IStepListenerManager {

    private List<IStepListener> beforeListeners;
    private List<IStepListener> afterListeners;
    private final ThreadLocal<Boolean> ignoredMode = new ThreadLocal<>();

    @Autowired
    private IAssertManager assertManager;

    @Autowired
    private IStepVisibilityManager stepVisibilityManager;

    @Autowired
    private void collectListeners(List<IStepListener> listeners) {
        this.beforeListeners = listeners.stream().sorted(Comparator.comparing(IStepListener::getPriority).reversed()).collect(Collectors.toList());
        this.afterListeners = listeners.stream().sorted(Comparator.comparing(IStepListener::getPriority)).collect(Collectors.toList());
    }

    @Override
    public void stepBefore(JoinPoint joinPoint, StepType stepType) {
        if (isIgnoredMode()) {
            return;
        }

        stepVisibilityManager.openLayer(AspectUtils.getAnnotation(joinPoint, HiddenStep.class).isPresent());

        if (stepType.isTestStep() || stepType.isBddStep()) {
            assertManager.openLayer();
        }

        if (!stepVisibilityManager.isHidden()) {
            beforeListeners.forEach(l -> l.stepBefore(joinPoint, stepType));
        }
    }

    @Override
    public void stepAfter(JoinPoint joinPoint, StepType stepType) {
        if (isIgnoredMode()) {
            return;
        }

        if (!stepVisibilityManager.isHidden()) {
            afterListeners.forEach(l -> l.stepAfter(joinPoint, stepType));

            if (stepType.isBddStep() && assertManager.isSoftAssertForNextStep()) {
                assertManager.setSoftAssertForNextStep(false);
                assertManager.softAssertOff();
            }
        }
    }

    @Override
    public void stepAfterReturning(JoinPoint joinPoint, Object data, StepType stepType) {
        if (isIgnoredMode()) {
            return;
        }

        AssertLayer assertLayer = null;
        if (stepType.isTestStep() || stepType.isBddStep()) {
            assertLayer = assertManager.closeLayer();
        }
        boolean sucess = Objects.isNull(assertLayer) || !assertLayer.hasErrors();

        if (!stepVisibilityManager.isHidden()) {
            if (sucess) {
                afterListeners.forEach(l -> l.stepAfterReturning(joinPoint, data, stepType));
            } else {
                Throwable throwable = assertLayer.getErrors().get(0);
                afterListeners.forEach(l -> l.stepAfterThrowing(joinPoint, throwable, stepType));
            }
        }

        if (sucess) {
            stepVisibilityManager.closeLayer();
        }
    }

    @Override
    public void stepAfterThrowing(JoinPoint joinPoint, Throwable throwable, StepType stepType) {
        if (isIgnoredMode()) {
            return;
        }

        if (!stepVisibilityManager.isHidden()) {
            afterListeners.forEach(l -> l.stepAfterThrowing(joinPoint, throwable, stepType));
        }

        stepVisibilityManager.closeLayer();
    }

    @Override
    public Object stepAround(ProceedingJoinPoint proceedingJoinPoint, StepType stepType) throws Throwable {
        Object[] args = proceedingJoinPoint.getArgs();
        if (assertManager.isSoftAssert() && !isIgnoredMode()) {
            try {
                return proceedingJoinPoint.proceed(args);
            } catch (AssertionError assertionError) {
                log.error(assertionError.getMessage(), assertionError);
                assertManager.addError(assertionError);
            }
            return null;
        } else {
            return proceedingJoinPoint.proceed(args);
        }
    }

    @Override
    public void setIgnoredMode(boolean ignoredMode) {
        this.ignoredMode.set(ignoredMode);
    }

    @Override
    public boolean isIgnoredMode() {
        Boolean ignoredMode = this.ignoredMode.get();
        return Objects.nonNull(ignoredMode) && ignoredMode;
    }
}
