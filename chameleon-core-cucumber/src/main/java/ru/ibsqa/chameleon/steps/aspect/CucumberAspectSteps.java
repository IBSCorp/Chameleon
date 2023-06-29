package ru.ibsqa.chameleon.steps.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import ru.ibsqa.chameleon.utils.spring.SpringUtils;

@Aspect
@Slf4j
public class CucumberAspectSteps {
    private IStepListenerManager listenerManager = SpringUtils.getBean(IStepListenerManager.class);

    // ---------------------------------------------Pointcuts-----------------------------------------------------------
    @Pointcut("execution(* *(..)) && !execution(* org.springframework..*(..))")
    public void anyMethod() {
        //pointcut body, should be empty
    }

    @Pointcut("@annotation(ru.ibsqa.chameleon.steps.UIStep)")
    public void uiStep() {
        //pointcut body, should be empty
    }

    @Pointcut("@annotation(io.cucumber.java.ru.Допустим)" + " || @annotation(io.cucumber.java.ru.Дано)" +
            " || @annotation(io.cucumber.java.ru.Тогда)" + " || @annotation(io.cucumber.java.ru.Когда)")
    public void bddStep() {
        //pointcut body, should be empty
    }

    @Pointcut("@annotation(io.cucumber.java.Before)")
    public void setupStep() {
        //pointcut body, should be empty
    }

    @Pointcut("@annotation(io.cucumber.java.After)")
    public void tearDownStep() {
        //pointcut body, should be empty
    }

    // ------------------------------------------- SetupSteps ----------------------------------------------------------
    @Before("anyMethod() && setupStep()")
    public void setupBefore(JoinPoint joinPoint) {
        listenerManager.stepBefore(joinPoint, StepType.builder().setupStep(true).build());
    }

    @After("anyMethod() && setupStep()")
    public void setupAfter(JoinPoint joinPoint) {
        listenerManager.stepAfter(joinPoint, StepType.builder().setupStep(true).build());
    }

    @AfterReturning(
            pointcut = "anyMethod() && setupStep()",
            returning = "data")
    public void setupAfterReturning(JoinPoint joinPoint, Object data) {
        listenerManager.stepAfterReturning(joinPoint, data, StepType.builder().setupStep(true).build());
    }

    @AfterThrowing(pointcut = "anyMethod() && setupStep()", throwing = "throwable")
    public void setupAfterThrowing(JoinPoint joinPoint, Throwable throwable) {
        listenerManager.stepAfterThrowing(joinPoint, throwable, StepType.builder().setupStep(true).build());
    }

    // ------------------------------------------- TearDownSteps -------------------------------------------------------
    @Before("anyMethod() && tearDownStep()")
    public void tearDownBefore(JoinPoint joinPoint) {
        listenerManager.stepBefore(joinPoint, StepType.builder().tearDownStep(true).build());
    }

    @After("anyMethod() && tearDownStep()")
    public void tearDownAfter(JoinPoint joinPoint) {
        listenerManager.stepAfter(joinPoint, StepType.builder().tearDownStep(true).build());
    }

    @AfterReturning(
            pointcut = "anyMethod() && tearDownStep()",
            returning = "data")
    public void tearDownAfterReturning(JoinPoint joinPoint, Object data) {
        listenerManager.stepAfterReturning(joinPoint, data, StepType.builder().tearDownStep(true).build());
    }

    @AfterThrowing(pointcut = "anyMethod() && tearDownStep()", throwing = "throwable")
    public void tearDownAfterThrowing(JoinPoint joinPoint, Throwable throwable) {
        listenerManager.stepAfterThrowing(joinPoint, throwable, StepType.builder().tearDownStep(true).build());
    }

    // ------------------------------------------- UI && BDD steps -----------------------------------------------------
    @Before("anyMethod() && uiStep() && bddStep()")
    public void stepUIBddBefore(JoinPoint joinPoint) {
        listenerManager.stepBefore(joinPoint, StepType.builder().uiStep(true).bddStep(true).build());
    }

    @After("anyMethod() && uiStep() && bddStep()")
    public void stepUIBddAfter(JoinPoint joinPoint) {
        listenerManager.stepAfter(joinPoint, StepType.builder().uiStep(true).bddStep(true).build());
    }

    @AfterReturning(
            pointcut = "anyMethod() && uiStep() && bddStep()",
            returning = "data")
    public void stepUIBddAfterReturning(JoinPoint joinPoint, Object data) {
        listenerManager.stepAfterReturning(joinPoint, data, StepType.builder().uiStep(true).bddStep(true).build());
    }

    @AfterThrowing(pointcut = "anyMethod() && uiStep() && bddStep()", throwing = "throwable")
    public void stepUIBddAfterThrowing(JoinPoint joinPoint, Throwable throwable) {
        listenerManager.stepAfterThrowing(joinPoint, throwable, StepType.builder().uiStep(true).bddStep(true).build());
    }

    // ------------------------------------------- BDD steps -----------------------------------------------------------
    @Before("anyMethod() && !uiStep() && bddStep()")
    public void stepNonUIBddBefore(JoinPoint joinPoint) {
        listenerManager.stepBefore(joinPoint, StepType.builder().bddStep(true).build());
    }

    @After("anyMethod() && !uiStep() && bddStep()")
    public void stepNonUIBddAfter(JoinPoint joinPoint) {
        listenerManager.stepAfter(joinPoint, StepType.builder().bddStep(true).build());
    }

    @AfterReturning(
            pointcut = "anyMethod() && !uiStep() && bddStep()",
            returning = "data")
    public void stepNonUIBddAfterReturning(JoinPoint joinPoint, Object data) {
        listenerManager.stepAfterReturning(joinPoint, data, StepType.builder().bddStep(true).build());
    }

    @AfterThrowing(pointcut = "anyMethod() && !uiStep() && bddStep()", throwing = "throwable")
    public void stepNonUIBddAfterThrowing(JoinPoint joinPoint, Throwable throwable) {
        listenerManager.stepAfterThrowing(joinPoint, throwable, StepType.builder().bddStep(true).build());
    }
}
