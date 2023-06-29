package ru.ibsqa.chameleon.checker;

public interface IStartChecker {
    void check();
    default StartCheckerPriority getPriority() {
        return StartCheckerPriority.LOW;
    }
}
