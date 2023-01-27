package ru.ibsqa.qualit.steps;

import io.cucumber.java.ru.Когда;
import org.springframework.beans.factory.annotation.Autowired;
import ru.ibsqa.qualit.asserts.IAssertManager;

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
