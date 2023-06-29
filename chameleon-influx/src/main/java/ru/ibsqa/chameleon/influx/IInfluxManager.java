package ru.ibsqa.chameleon.influx;

public interface IInfluxManager {

    void startTest(String name);

    void endTest(String status);

    void startStep(String name);

    void endStep(String status);

    void endStep(String status, String failureStep, String failureMessage); // todo более ясное имя?

    void increaseErrorCounter();
}
