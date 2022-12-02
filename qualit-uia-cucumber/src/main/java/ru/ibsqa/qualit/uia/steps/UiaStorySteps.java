package ru.ibsqa.qualit.uia.steps;

import ru.ibsqa.qualit.selenium.enums.KeyEnum;
import ru.ibsqa.qualit.steps.AbstractSteps;
import ru.ibsqa.qualit.steps.SeleniumFieldSteps;
import ru.ibsqa.qualit.steps.StepDescription;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Тогда;
import org.springframework.beans.factory.annotation.Autowired;

public class UiaStorySteps extends AbstractSteps {

    @Autowired
    private SeleniumFieldSteps fieldSteps;

    @Autowired
    private UiaSteps uiaSteps;

    @StepDescription(
            action = "UI Automation->Общее->Переключение к окну",
            parameters = {"title - заголовок окна (возможно регулярное выражение, должно начинаться с символа ^)"}
    )
    @Когда("^выполнено переключение к окну \"(.*)\"$")
    public void stepSwitchToWindowWithTitle(String title) {
        flow(() ->
                uiaSteps.switchToWindow(title)
        );
    }

    @StepDescription(
            action = "UI Automation->Общее->Нажатие клавиши",
            parameters = {"key - код клавиши"}
    )
    @Когда("^выполнено нажатие клавиши \"(.*)\"$")
    public void stepSelectCategory(String key) {
        flow(() ->
                uiaSteps.pressKey(KeyEnum.valueOf(key))
        );
    }

    @StepDescription(
            action = "UI Automation->Элементы->Проверка цвета поля",
            parameters = {"fieldName - наименование поля", "colorName - наименование цвета"}
    )
    @Тогда("^ожидается что цвет поля \"(.*)\" виден как \"(\\S+)\"$")
    public void stepVerifyFieldColor(String fieldName, String colorName) {
        flow(() ->
                uiaSteps.checkFieldColor(fieldName, colorName)
        );
    }

    @StepDescription(
            action = "UI Automation->Элементы->Выбор вкладки на панели закладок",
            parameters = {"tabName - наименование вкладки", "ribbonName - наименование панели закладок"}
    )
    @Тогда("^выбрана вкладка \"([^\"]+)\" на панели закладок \"([^\"]+)\"$")
    public void stepCheckFieldValue(String tabName, String ribbonName) {
        this.flow(() -> {
            fieldSteps.fillField(ribbonName, tabName);
        });
    }

}
