package ru.ibsqa.qualit.steps.visibility;

public interface IStepVisibilityManager {
    void openLayer(boolean hidden);
    void closeLayer();
    boolean isHidden();
    default void openLayer() {
        openLayer(false);
    }
    StepVisibilityLayer getCurrentLayer();
}
