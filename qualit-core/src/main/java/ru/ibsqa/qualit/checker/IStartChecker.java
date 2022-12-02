package ru.ibsqa.qualit.checker;

public interface IStartChecker {
    void check();
    default StartCheckerPriority getPriority() {
        return StartCheckerPriority.LOW;
    }
}
