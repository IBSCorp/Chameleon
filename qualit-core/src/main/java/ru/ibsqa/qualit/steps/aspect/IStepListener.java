package ru.ibsqa.qualit.steps.aspect;

import org.aspectj.lang.JoinPoint;

/**
 * Реализуя этот интерфейс можно вешать обработчики до и после выполнения шагов
 */
public interface IStepListener {

    /**
     * Происходит перевыполнением шага
     * @param joinPoint точка подключения аспекта
     * @param stepType информация о разновидности шага
     */
    void stepBefore(JoinPoint joinPoint, StepType stepType);

    /**
     * Происходит после выполнения шага, независимо от результата выполнения
     * @param joinPoint точка подключения аспекта
     * @param stepType информация о разновидности шага
     */
    void stepAfter(JoinPoint joinPoint, StepType stepType);

    /**
     * Происходит после успешного выполнения шага
     * @param joinPoint точка подключения аспекта
     * @param data данные, возвращаемые методом
     * @param stepType информация о разновидности шага
     */
    void stepAfterReturning(JoinPoint joinPoint, Object data, StepType stepType);

    /**
     * Происходит при возникновении ошибки на шаге
     * @param joinPoint точка подключения аспекта
     * @param throwable информация об исключении
     * @param stepType информация о разновидности шага
     */
    void stepAfterThrowing(JoinPoint joinPoint, Throwable throwable, StepType stepType);

    /**
     * Порядок выполнения относительно других обработчиков
     * @return
     */
    default Integer getPriority() {
        return 0;
    }
}