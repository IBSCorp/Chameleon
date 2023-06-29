package ru.ibsqa.chameleon.steps.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import ru.ibsqa.chameleon.utils.spring.SpringUtils;

@Aspect
@Slf4j
public class AspectSteps {

    private final IStepListenerManager stepListenerManager = SpringUtils.getBean(IStepListenerManager.class);

    // ---------------------------------------------Pointcuts-----------------------------------------------------------
    @Pointcut("execution(* *(..)) && !execution(* org.springframework..*(..))")
    public void anyMethod() {
        //pointcut body, should be empty
    }

    @Pointcut("@annotation(ru.ibsqa.chameleon.steps.UIStep)")
    public void uiStep() {
        //pointcut body, should be empty
    }

    @Pointcut("@annotation(ru.ibsqa.chameleon.steps.TestStep)")
    public void testStep() {
        //pointcut body, should be empty
    }

    // ------------------------------------------- UI && TestSteps (Nested)  -------------------------------------------
    @Around("anyMethod() && uiStep() && testStep()")
    public Object stepUINestedAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return stepListenerManager.stepAround(proceedingJoinPoint, StepType.builder().uiStep(true).build());
    }

    @Before("anyMethod() && uiStep() && testStep()")
    public void stepUINestedBefore(JoinPoint joinPoint) {
        stepListenerManager.stepBefore(joinPoint, StepType.builder().uiStep(true).build());
    }

    @After("anyMethod() && uiStep() && testStep()")
    public void stepUINestedAfter(JoinPoint joinPoint) {
        stepListenerManager.stepAfter(joinPoint, StepType.builder().uiStep(true).build());
    }

    @AfterReturning(
            pointcut = "anyMethod() && uiStep() && testStep()",
            returning = "data")
    public void stepUINestedAfterReturning(JoinPoint joinPoint, Object data) {
        stepListenerManager.stepAfterReturning(joinPoint, data, StepType.builder().uiStep(true).build());
    }

    @AfterThrowing(pointcut = "anyMethod() && uiStep() && testStep()", throwing = "throwable")
    public void stepUINestedAfterThrowing(JoinPoint joinPoint, Throwable throwable) {
        stepListenerManager.stepAfterThrowing(joinPoint, throwable, StepType.builder().uiStep(true).build());
    }

    // ------------------------------------------- TestSteps (Nested) --------------------------------------------------
    @Around("anyMethod() && !uiStep() && testStep()")
    public Object stepNonUINestedAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return stepListenerManager.stepAround(proceedingJoinPoint, StepType.builder().build());
    }

    @Before("anyMethod() && !uiStep() && testStep()")
    public void stepNonUINestedBefore(JoinPoint joinPoint) {
        stepListenerManager.stepBefore(joinPoint, StepType.builder().build());
    }

    @After("anyMethod() && !uiStep() && testStep()")
    public void stepNonUINestedAfter(JoinPoint joinPoint) {
        stepListenerManager.stepAfter(joinPoint, StepType.builder().build());
    }

    @AfterReturning(
            pointcut = "anyMethod() && !uiStep() && testStep()",
            returning = "data")
    public void stepNonUINestedAfterReturning(JoinPoint joinPoint, Object data) {
        stepListenerManager.stepAfterReturning(joinPoint, data, StepType.builder().build());
    }

    @AfterThrowing(pointcut = "anyMethod() && !uiStep() && testStep()", throwing = "throwable")
    public void stepNonUINestedAfterThrowing(JoinPoint joinPoint, Throwable throwable) {
        stepListenerManager.stepAfterThrowing(joinPoint, throwable, StepType.builder().build());
    }
}
