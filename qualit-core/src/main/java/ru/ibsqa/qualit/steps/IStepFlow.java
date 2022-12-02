package ru.ibsqa.qualit.steps;

/**
 * Управление условием выполнения шагов
 */
public interface IStepFlow {

    boolean isIgnore();
    void createBlock(boolean active);
    void inverseBlock();
    void completeBlock();
    void checkStepFlow(Runnable stepAction);

    boolean prepareFlowStep();
}
