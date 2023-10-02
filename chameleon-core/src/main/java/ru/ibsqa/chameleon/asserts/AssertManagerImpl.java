package ru.ibsqa.chameleon.asserts;

import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class AssertManagerImpl implements IAssertManager {

    @Autowired
    private IThrowableMixer throwableMixer;

    private final ThreadLocal<Boolean> softAssert = new InheritableThreadLocal<>();
    private final ThreadLocal<Boolean> softAssertForNextStep = new InheritableThreadLocal<>();
    private final ThreadLocal<AssertLayer> currentLayer = new InheritableThreadLocal<>();
    private final ThreadLocal<AssertLayer> lastLayer = new InheritableThreadLocal<>();

    @Override
    public void softAssertOn() {
        softAssert.set(true);
    }

    @Override
    public void softAssertOff() {
        softAssert.set(false);
    }

    @Override
    public void softAssertCheck() {
        if (Objects.nonNull(getCurrentLayer().getParent())) {
            throwableMixer.mixThrowableFromList(getCurrentLayer().getParent().getErrors())
                    .ifPresent((throwable) -> Assertions.fail(throwable.getMessage(), throwable));
        }
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
        return throwableMixer.mixThrowableFromList(getErrors()).orElse(null);
    }

}
