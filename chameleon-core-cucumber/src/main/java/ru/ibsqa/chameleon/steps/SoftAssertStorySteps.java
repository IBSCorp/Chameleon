package ru.ibsqa.chameleon.steps;

import io.cucumber.java.ru.Когда;
import org.springframework.beans.factory.annotation.Autowired;
import ru.ibsqa.chameleon.asserts.IAssertManager;

public class SoftAssertStorySteps extends AbstractSteps {

    @Autowired
    private SoftAssertSteps softAssertSteps;

    @Autowired
    private IAssertManager assertManager;

    @StepDescription(action = "Условия->SoftAssert", subAction = "Включить SoftAssert", expertView = true)
    @Когда("^~SoftAssert \"Включить\"$")
    @HiddenStep
    public void softAssertOn() {
        flow(() ->
                softAssertSteps.softAssertOn()
        );
    }

    @StepDescription(action = "Условия->SoftAssert", subAction = "Выключить SoftAssert", expertView = true)
    @Когда("^~SoftAssert \"Выключить\"$")
    @HiddenStep
    public void softAssertOff() {
        flow(() ->
                softAssertSteps.softAssertOff()
        );
    }

    @StepDescription(action = "Условия->SoftAssert", subAction = "Прервать выполнение теста, если были ошибки", expertView = true)
    @Когда("^~SoftAssert \"Прервать, если были ошибки\"$")
    @HiddenStep
    public void softAssertCheck() {
        flow(() -> {
            softAssertSteps.softAssertCheck();
        });
    }

    @StepDescription(action = "Условия->SoftAssert", subAction = "Выключить SoftAssert и прервать выполнение теста, если были ошибки", expertView = true)
    @Когда("^~SoftAssert \"Выключить и прервать, если были ошибки\"$")
    @HiddenStep
    public void softAssertOffAndCheck() {
        flow(() -> {
            softAssertSteps.softAssertCheck();
            softAssertSteps.softAssertOff();
        });
    }

    @StepDescription(action = "Условия->SoftAssert", subAction = "SoftAssert для следующего шага", expertView = true)
    @Когда("^~SoftAssert \"Для следующего шага\"$")
    @HiddenStep
    public void softAssertForNextStep() {
        flow(() -> {
            softAssertSteps.softAssertOn();
            assertManager.setSoftAssertForNextStep(true);
        });
    }

}
