package ru.ibsqa.chameleon.configuration.steps;

/**
 * Этот интерфейс должны имплементировать классы, содержащие шаги jBehave
 */
@Deprecated
public interface IJBehaveStepsRegistrator {
    default String getStepsPackage() {
        return this.getClass().getPackage().toString().replaceFirst("^package\\s", "");
    }
}
