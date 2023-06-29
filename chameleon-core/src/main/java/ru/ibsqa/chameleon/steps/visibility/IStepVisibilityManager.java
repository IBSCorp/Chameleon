package ru.ibsqa.chameleon.steps.visibility;

public interface IStepVisibilityManager {
    void openLayer(boolean hidden);
    void closeLayer();
    boolean isHidden();
    default void openLayer() {
        openLayer(false);
    }
    StepVisibilityLayer getCurrentLayer();
}
