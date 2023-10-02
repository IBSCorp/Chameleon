package ru.ibsqa.chameleon.steps.visibility;

import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class StepVisibilityManagerImpl implements IStepVisibilityManager {

    private final ThreadLocal<StepVisibilityLayer> currentLayer = new InheritableThreadLocal<>();

    @Override
    public void openLayer(boolean hidden) {
        currentLayer.set(new StepVisibilityLayer(currentLayer.get(), hidden));
    }

    @Override
    public void closeLayer() {
        StepVisibilityLayer current = currentLayer.get();
        if (Objects.nonNull(current)) {
            StepVisibilityLayer parent = current.getParent();
            currentLayer.set(parent);
        }
    }

    @Override
    public boolean isHidden() {
        StepVisibilityLayer layer = getCurrentLayer();
        while (Objects.nonNull(layer)) {
            if (layer.isHidden()) {
                return true;
            }
            layer = layer.getParent();
        }
        return false;
    }

    @Override
    public StepVisibilityLayer getCurrentLayer() {
        return currentLayer.get();
    }

}
