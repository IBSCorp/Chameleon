package ru.ibsqa.qualit.asserts;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public interface IAssertManager {
    void softAssertOn();
    void softAssertOff();
    boolean isSoftAssert();
    AssertLayer getLastLayer();
    AssertLayer getCurrentLayer();
    void setSoftAssertForNextStep(boolean softAssert);
    boolean isSoftAssertForNextStep();

    void openLayer();
    AssertLayer closeLayer();
    void addError(Throwable e);
    default List<Throwable> getLastErrors() {
        return Optional.ofNullable(getLastLayer())
                .map(AssertLayer::getErrors)
                .orElseGet(Collections::emptyList);
    }
    default boolean hasLastErrors() {
        return Optional.ofNullable(getLastLayer())
                .map(AssertLayer::hasErrors)
                .orElse(false);
    }
    default List<Throwable> getCurrentErrors() {
        return Optional.ofNullable(getCurrentLayer())
                .map(AssertLayer::getErrors)
                .orElseGet(Collections::emptyList);
    }
    default boolean hasCurrentErrors() {
        return Optional.ofNullable(getCurrentLayer())
                .map(AssertLayer::hasErrors)
                .orElse(false);
    }
}
