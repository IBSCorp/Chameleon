package ru.ibsqa.chameleon.asserts;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class AssertManagerImpl implements IAssertManager {

    private final ThreadLocal<Boolean> softAssert = new ThreadLocal<>();
    private final ThreadLocal<Boolean> softAssertForNextStep = new ThreadLocal<>();
    private final ThreadLocal<AssertLayer> currentLayer = new ThreadLocal<>();
    private final ThreadLocal<AssertLayer> lastLayer = new ThreadLocal<>();

    @Override
    public void softAssertOn() {
        softAssert.set(true);
    }

    @Override
    public void softAssertOff() {
        softAssert.set(false);
    }

    @Override
    public boolean isSoftAssert() {
        return Optional.ofNullable(softAssert.get()).orElse(false);
    }


    @Override
    public void setSoftAssertForNextStep(boolean softAssert) {
        softAssertForNextStep.set(softAssert);
    }

    @Override
    public boolean isSoftAssertForNextStep() {
        return Optional.ofNullable(softAssertForNextStep.get()).orElse(false);
    }

    @Override
    public void openLayer() {
        if (Objects.isNull(currentLayer.get())) {
            softAssert.set(false);
        }
        currentLayer.set(new AssertLayer(currentLayer.get()));
    }

    @Override
    public AssertLayer closeLayer() {
        AssertLayer current = currentLayer.get();
        AssertLayer parent = current.getParent();
        if (Objects.nonNull(parent)) {
            parent.getErrors().addAll(current.getErrors());
        }
        lastLayer.set(currentLayer.get());
        currentLayer.set(parent);
        return getLastLayer();
    }

    @Override
    public void addError(Throwable throwable) {
        getErrors().add(throwable);
    }

    @Override
    public AssertLayer getLastLayer() {
        return lastLayer.get();
    }

    @Override
    public AssertLayer getCurrentLayer() {
        return currentLayer.get();
    }

    private List<Throwable> getErrors() {
        return currentLayer.get().getErrors();
    }

    private boolean hasErrors() {
        return getErrors().size()>0;
    }

    private String getMessage() {
        return Optional.ofNullable(getThrowable()).map(Throwable::getMessage).orElse("");
    }
    private Throwable getThrowable() {
        return hasErrors() ? getErrors().get(0) : null;
    }

}
