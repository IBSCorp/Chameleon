package ru.ibsqa.qualit.steps;

import io.cucumber.java.ru.Тогда;
import org.springframework.beans.factory.annotation.Autowired;
import ru.ibsqa.qualit.steps.roles.Value;

public class AlertStorySteps extends AbstractSteps {

    @Autowired
    private AlertSteps alertSteps;

    @StepDescription(action = "UI->Прочее->Проверить alert"
            , subAction = "Присутствует alert")
    @Тогда("^присутствует alert$")
    public void alertIsPresent() {
        flow(()->
                alertSteps.stepAlertIsPresent()
        );
    }

    @StepDescription(action = "UI->Прочее->Проверить alert"
            , subAction = "Присутствует alert с текстом"
            , parameters = {"text - ожидаемый текст alert-а"})
    @Тогда("^присутствует alert с текстом \"([^\"]*)\"$")
    public void alertIsPresentWithText(@Value String text) {
        flow(()->
                alertSteps.stepAlertIsPresentWithText(text)
        );
    }

    @StepDescription(action = "UI->Прочее->Проверить alert"
            , subAction = "Присутствует alert, содержащий текст"
            , parameters = {"text - фрагмент, который ожидаем встретить в тексте alert-а"})
    @Тогда("^присутствует alert, содержащий текст \"([^\"]*)\"$")
    public void stepAlertIsPresentWithSubstring(@Value String text) {
        flow(()->
                alertSteps.stepAlertIsPresentWithSubstring(text)
        );
    }

    @StepDescription(action = "UI->Прочее->Проверить alert"
            , subAction = "Отсутствует alert")
    @Тогда("^отсутствует alert$")
    public void alertIsNotPresent() {
        flow(()->
                alertSteps.stepAlertIsNotPresent()
        );
    }

    @StepDescription(action = "UI->Прочее->Закрыть alert"
            , subAction = "Отменить alert")
    @Тогда("^отменить alert$")
    public void alertDismiss() {
        flow(()->
                alertSteps.stepAlertDismiss()
        );
    }

    @StepDescription(action = "UI->Прочее->Закрыть alert"
            , subAction = "Подтвердить alert")
    @Тогда("^подтвердить alert$")
    public void alertAccept() {
        flow(()->
                alertSteps.stepAlertAccept()
        );
    }

    @StepDescription(action = "UI->Прочее->Ввести в alert"
            , parameters = {"text - текст для ввода в alert"})
    @Тогда("^ввести в alert текст \"([^\"]*)\"$")
    public void alertSendKeys(@Value String text) {
        flow(()->
                alertSteps.stepAlertSendKeys(text)
        );
    }

}
