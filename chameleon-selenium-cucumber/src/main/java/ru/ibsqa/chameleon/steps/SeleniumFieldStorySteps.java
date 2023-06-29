package ru.ibsqa.chameleon.steps;

import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Тогда;
import org.springframework.beans.factory.annotation.Autowired;
import ru.ibsqa.chameleon.context.Context;
import ru.ibsqa.chameleon.context.ContextChange;
import ru.ibsqa.chameleon.element.ElementTypePage;
import ru.ibsqa.chameleon.converters.FieldTable;
import ru.ibsqa.chameleon.elements.selenium.IFacadeSelenium;
import ru.ibsqa.chameleon.selenium.enums.KeyEnum;
import ru.ibsqa.chameleon.steps.roles.*;

import java.util.List;

public class SeleniumFieldStorySteps extends AbstractSteps {

    @Autowired
    private SeleniumFieldSteps fieldSteps;

    @StepDescription(action = "UI->Элементы->Действия->Заполнить поле"
            , subAction = "Заполнить поле и нажать на ENTER"
            , parameters = {"fieldName - наименование поля", "value - значение, которым будет заполнено поле"})
    @Когда("^поле \"([^\"]*)\" заполняется значением \"([^\"]*)\" и нажимается ENTER$")
    public void stepFillFieldAndPressEnter(
            @Write @KeyPress String fieldName,
            @Value String value
    ) {
        flow(() -> {
            fieldSteps.fillField(fieldName, value);
            fieldSteps.pressKeyField(fieldName, KeyEnum.ENTER);
        });
    }

    @StepDescription(action = "UI->Элементы->Действия->Нажать клавишу в поле"
            , parameters = {"field - наименование поля", "key - наименование клавиши"})
    @Когда("^в поле \"([^\"]*)\" выполнено нажатие клавиши \"([^\"]*)\"$")
    public void pressKeyField(
            @KeyPress String field,
            @Value String key
    ) {
        flow(() -> {
            if (field.isEmpty()) {
                return;
            }
            fieldSteps.pressKeyField(field, KeyEnum.valueOf(key));
        });
    }

    @DebugPluginAction
    @StepDescription(action = "UI->Элементы->Действия->Нажать на элемент"
            , subAction = "Нажать на элемент"
            , parameters = {"fieldName - наименование поля"})
    @Когда("^выполнено нажатие на \"([^\"]*)\"$")
    public void stepClickField(
            @Mouse String fieldName
    ) {
        flow(() -> {
            if (fieldName.isEmpty()) {
                return;
            }
            fieldSteps.clickField(fieldName);
        });
    }

    @DebugPluginAction
    @StepDescription(action = "UI->Элементы->Действия->Нажать на элемент"
            , subAction = "Двойное нажатие на элемент"
            , parameters = {"fieldName - наименование поля"})
    @Когда("^выполнено двойное нажатие на \"([^\"]*)\"$")
    public void stepDoubleClickField(
            @Mouse String fieldName
    ) {
        flow(() -> {
            if (fieldName.isEmpty()) {
                return;
            }
            fieldSteps.doubleClickField(fieldName);
        });
    }

    @StepDescription(action = "UI->Элементы->Действия->Нажать на элемент"
            , subAction = "Нажать на элемент n раз"
            , parameters = {"fieldName - наименование поля", "amount - количество раз, которое требуется нажать на поле"}
            , expertView = true)
    @Когда("^выполнено нажатие на \"([^\"]*)\" \"([^\"]*)\" раза?$")
    public void stepClickFieldNTimes(
            @Mouse String fieldName, @Value String amount
    ) {
        flow(() -> {
            if (fieldName.isEmpty()) {
                return;
            }
            int count = Integer.parseInt(amount);
            if (count < 1) {
                return;
            }
            for (int i = 0; i < count; i++) {
                fieldSteps.clickField(fieldName);
            }
        });
    }

    @StepDescription(action = "UI->Элементы->Проверки->Проверить подсказку в поле"
            , parameters = {"fieldName - наименование поля", "operator - оператор сравнения", "expected - ожидаемая подсказка"}
            , expertView = true)
    @Тогда("^значение подсказки для поля \"([^\"]*)\" ([^\"]+) \"([^\"]*)\"$")
    public void stepCheckFieldPlaceholder(
            @Visible String fieldName,
            @Operator String operator,
            @Value String expected
    ) {
        flow(() ->
                fieldSteps.checkFieldPlaceholder(fieldName, operator, expected)
        );
    }

    @StepDescription(action = "UI->Элементы->Проверки->Проверить ошибку в поле"
            , parameters = {"fieldName - наименование поля", "operator - оператор сравнения", "expected - ожидаемая ошибка"}
            , expertView = true)
    @Тогда("^значение ошибки в поле \"([^\"]*)\" ([^\"]+) \"([^\"]*)\"$")
    public void stepCheckFieldError(
            @Visible String fieldName,
            @Operator String operator,
            @Value String expected
    ) {
        flow(() ->
                fieldSteps.checkFieldError(fieldName, operator, expected)
        );
    }

    @StepDescription(action = "UI->Элементы->Действия->Нажать на элемент"
            , subAction = "Нажать на кнопку и ждать загрузку страницы"
            , parameters = {"fieldName - наименование поля", "page - страница, которая будет открыта после нажатия на кнопку"})
    @Когда("^нажатием на кнопку \"([^\"]*)\" загружена страница \"([^\"]*)\"$")
    @Context(type = ElementTypePage.class, change = ContextChange.AFTER, parameter = "page")
    public void stepClickFieldAndPageLoaded(
            @Mouse String fieldName,
            @Page String page
    ) {
        flow(() ->
                fieldSteps.clickFieldAndPageLoaded(fieldName, page)
        );
    }

    @DebugPluginAction
    @StepDescription(action = "UI->Элементы->Проверки->Проверить поле"
            , subAction = "Поле видимо"
            , parameters = {"fieldName - наименование поля, видимость которого будет проверяться"})
    @Тогда("^поле \"([^\"]*)\" видимо$")
    public void stepFieldIsDisplayed1(
            @Visible String fieldName
    ) {
        flow(() ->
                fieldSteps.fieldIsDisplayed(fieldName)
        );
    }

    @StepDescription(action = "UI->Элементы->Проверки->Проверить поле"
            , subAction = "Кнопка видима"
            , parameters = {"fieldName - наименование кнопки, видимость которой будет проверяться"})
    @Тогда("^кнопка \"([^\"]*)\" видима$")
    public void stepFieldIsDisplayed2(
            @Visible String fieldName
    ) {
        flow(() ->
                fieldSteps.fieldIsDisplayed(fieldName)
        );
    }

    @StepDescription(action = "UI->Элементы->Проверки->Проверить поле"
            , subAction = "Поля видимы"
            , parameters = {"fields - список полей, видимость которых будет проверяться"})
    @Тогда("^следующие поля видимы:$")
    public void stepFieldsIsDisplayed(
            @Visible List<FieldTable> fields
    ) {
        flow(() -> {
            for (FieldTable field : fields) {
                fieldSteps.fieldIsDisplayed(field.getField());
            }
        });
    }

    @StepDescription(action = "UI->Элементы->Проверки->Проверить поле"
            , subAction = "Кнопка отсутствует"
            , parameters = {"fieldName - наименование кнопки, отсутствие которой будет проверяться"})
    @Тогда("^кнопка \"([^\"]*)\" отсутствует$")
    public void stepFieldIsNotExist(
            @Exists String fieldName
    ) {
        flow(() ->
                fieldSteps.fieldIsNotDisplayed(fieldName)
        );
    }

    @StepDescription(action = "UI->Элементы->Проверки->Проверить поле"
            , subAction = "Поле активно"
            , parameters = {"fieldName - наименование поля, активность которого будет проверяться"})
    @Тогда("^поле \"([^\"]*)\" активно$")
    public void stepFieldIsEnabled1(
            @Enable String fieldName
    ) {
        flow(() ->
                fieldSteps.fieldIsEnabled(fieldName)
        );
    }

    @StepDescription(action = "UI->Элементы->Проверки->Проверить поле"
            , subAction = "Кнопка активна"
            , parameters = {"fieldName - наименование кнопки, активность которой будет проверяться"})
    @Тогда("^кнопка \"([^\"]*)\" активна$")
    public void stepFieldIsEnabled2(
            @Enable String fieldName
    ) {
        flow(() ->
                fieldSteps.fieldIsEnabled(fieldName)
        );
    }

    @StepDescription(action = "UI->Элементы->Проверки->Проверить поле"
            , subAction = "Поля активны"
            , parameters = {"fields - наименование полей, активность которых будет проверяться"})
    @Тогда("^следующие поля активны:$")
    public void stepFieldsIsEnabled(
            @Enable List<FieldTable> fields
    ) {
        flow(() -> {
            for (FieldTable field : fields) {
                fieldSteps.fieldIsEnabled(field.getField());
            }
        });
    }

    @StepDescription(action = "UI->Элементы->Проверки->Проверить поле"
            , subAction = "Поле неактивно"
            , parameters = {"fieldName - наименование поля, неактивность которого будет проверяться"})
    @Тогда("^поле \"([^\"]*)\" неактивно$")
    public void stepFieldIsDisabled1(
            @Enable String fieldName
    ) {
        flow(() ->
                fieldSteps.fieldIsDisabled(fieldName)
        );
    }

    @StepDescription(action = "UI->Элементы->Проверки->Проверить поле"
            , subAction = "Кнопка неактивна"
            , parameters = {"fieldName - наименование кнопки, неактивность которой будет проверяться"})
    @Тогда("^кнопка \"([^\"]*)\" неактивна$")
    public void stepFieldIsDisabled2(
            @Enable String fieldName
    ) {
        flow(() ->
                fieldSteps.fieldIsDisabled(fieldName)
        );
    }

    @StepDescription(action = "UI->Элементы->Проверки->Проверить поле"
            , subAction = "Поля неактивны"
            , parameters = {"fields - наименование полей, неактивность которых будет проверяться"})
    @Тогда("^следующие поля неактивны:$")
    public void stepFieldsIsDisabled(
            @Enable List<FieldTable> fields
    ) {
        flow(() -> {
            for (FieldTable field : fields) {
                fieldSteps.fieldIsDisabled(field.getField());
            }
        });
    }

    @StepDescription(action = "UI->Элементы->Проверки->Проверить поле"
            , subAction = "Поле редактируемо"
            , parameters = {"fieldName - наименование поля, редактируемость которого будет проверяться"})
    @Тогда("^поле \"([^\"]*)\" редактируемо$")
    public void stepFieldIsEditable(
            @Write String fieldName
    ) {
        flow(() ->
                fieldSteps.fieldIsEditable(fieldName)
        );
    }

    @StepDescription(action = "UI->Элементы->Проверки->Проверить поле"
            , subAction = "Поля редактируемы"
            , parameters = {"fields - наименование полей, редактируемость которых будет проверяться"})
    @Тогда("^следующие поля редактируемы:$")
    public void stepFieldsIsEditable(
            @Write List<FieldTable> fields
    ) {
        flow(() -> {
            for (FieldTable field : fields) {
                fieldSteps.fieldIsEditable(field.getField());
            }
        });
    }

    @StepDescription(action = "UI->Элементы->Проверки->Проверить поле"
            , subAction = "Поле нередактируемо"
            , parameters = {"fieldName - наименование поля, нередактируемость которого будет проверяться"})
    @Тогда("^поле \"([^\"]*)\" нередактируемо$")
    public void stepFieldIsNotEnabled(
            @Write String fieldName
    ) {
        flow(() ->
                fieldSteps.fieldIsNotEditable(fieldName)
        );
    }

    @StepDescription(action = "UI->Элементы->Проверки->Проверить поле"
            , subAction = "Поля нередактируемы"
            , parameters = {"fields - наименование полей, нередактируемость которых будет проверяться"})
    @Тогда("^следующие поля нередактируемы:$")
    public void stepFieldsIsNotEditable(
            @Write List<FieldTable> fields
    ) {
        flow(() -> {
            for (FieldTable field : fields) {
                fieldSteps.fieldIsNotEditable(field.getField());
            }
        });
    }

    @StepDescription(action = "UI->Элементы->Действия->Дождаться состояния поля"
            , subAction = "Дождаться исчезновения поля"
            , parameters = {"fields - наименование поля", "seconds - время ожидания"})
    @Тогда("^ожидается исчезновение поля \"([^\"]*)\" в течение \"([^\"]*)\" секунд$")
    public void stepWaitFieldInVisible(
            @Exists String fieldName,
            @Value String seconds
    ) {
        flow(() ->
                fieldSteps.waitFieldInVisible(fieldName, Integer.parseInt(seconds))
        );
    }

    @StepDescription(action = "UI->Элементы->Действия->Дождаться состояния поля"
            , subAction = "Дождаться появление поля"
            , parameters = {"fields - наименование поля", "seconds - время ожидания"})
    @Тогда("^ожидается появление поля \"([^\"]*)\" в течение \"([^\"]*)\" секунд$")
    public void stepWaitFieldVisible(
            @Exists String fieldName,
            @Value String seconds
    ) {
        flow(() ->
                fieldSteps.waitFieldVisible(fieldName, Integer.parseInt(seconds))
        );
    }

    @StepDescription(action = "UI->Элементы->Действия->Дождаться состояния поля"
            , subAction = "Дождаться активности поля"
            , parameters = {"fields - наименование поля", "seconds - время ожидания"})
    @Тогда("^ожидается что поле \"([^\"]*)\" станет активным в течение \"([^\"]*)\" секунд$")
    public void stepWaitFieldIsEnabled(
            @Enable String fieldName,
            @Value String seconds
    ) {
        flow(() ->
                fieldSteps.waitFieldIsEnabled(fieldName, Integer.parseInt(seconds))
        );
    }

    @StepDescription(action = "UI->Элементы->Действия->Дождаться состояния поля"
            , subAction = "Дождаться неактивности поля"
            , parameters = {"fields - наименование поля", "seconds - время ожидания"})
    @Тогда("^ожидается что поле \"([^\"]*)\" станет неактивным в течение \"([^\"]*)\" секунд$")
    public void stepWaitFieldIsDisabled(
            @Enable String fieldName,
            @Value String seconds
    ) {
        flow(() ->
                fieldSteps.waitFieldIsDisabled(fieldName, Integer.parseInt(seconds))
        );
    }

    @DebugPluginAction
    @StepDescription(action = "UI->Элементы->Проверки->Проверить атрибут поля"
            , parameters = {"attribute - наименоване атрибута", "fields - наименование поля", "operator - оператор сравнения", "value - значение атрибута"})
    @Тогда("^значение атрибута \"([^\"]*)\" поля \"([^\"]*)\" ([^\"]+) \"([^\"]*)\"$")
    public void stepCheckFieldAttribute(
            @Value String attribute,
            @UIAttr String fieldName,
            @Operator String operator,
            @Value String value
    ) {
        flow(() -> {
            fieldSteps.checkFieldAttribute(attribute, fieldName, operator, value);
        });
    }

    @StepDescription(action = "UI->Элементы->Действия->Нажать на элемент"
            , subAction = "Нажать на элемент правой кнопкой мыши"
            , parameters = {"fieldName - наименование поля"})
    @Когда("^выполнено нажатие на \"([^\"]*)\" правой кнопкой мыши$")
    public void stepClickRightField(
            @Mouse String fieldName
    ) {
        flow(() -> {
            if (fieldName.isEmpty()) {
                return;
            }
            fieldSteps.rightClickField(fieldName);
        });
    }

    @StepDescription(action = "UI->Элементы->Действия->Навести курсор мыши на элемент"
            , parameters = {"fieldName - наименование поля"})
    @Когда("^выполнено наведение мыши на \"([^\"]*)\"$")
    public void stepMoveMouseToField(
            @Mouse String fieldName
    ) {
        flow(() -> {
            if (fieldName.isEmpty()) {
                return;
            }
            fieldSteps.moveMouseToField(fieldName);
        });
    }

    @StepDescription(action = "UI->Элементы->Действия->Прокрутить страницу или список, чтобы элемент был видим"
            , parameters = {"fieldName - наименование поля"})
    @Когда("^выполнена прокрутка на \"([^\"]*)\"$")
    public void stepScrollIntoViewField(
            @Mouse String fieldName
    ) {
        flow(() -> {
            if (fieldName.isEmpty()) {
                return;
            }
            fieldSteps.scrollIntoViewField(fieldName);
        });
    }

    @StepDescription(action = "Условия->Если поле присутствует, то"
            , parameters = {"fieldName - наименование поля"}
            , expertView = true)
    @Когда("^присутствует поле \"([^\"]*)\", выполнять следующие шаги:$")
    public void ifFlowFieldIsDisplayed(
            @Exists String fieldName
    ) {
        if (getStepFlow().prepareFlowStep()) {
            IFacadeSelenium field = fieldSteps.getSeleniumField(fieldName);
            getStepFlow().createBlock(field.isDisplayed());
        }
    }

    @StepDescription(action = "Условия->Если поле отсутствует, то"
            , parameters = {"fieldName - наименование поля"}
            , expertView = true)
    @Когда("^отсутствует поле \"([^\"]*)\", выполнять следующие шаги:$")
    public void ifFlowFieldNotDisplayed(
            @Exists String fieldName
    ) {
        if (getStepFlow().prepareFlowStep()) {
            IFacadeSelenium field = fieldSteps.getSeleniumField(fieldName);
            getStepFlow().createBlock(!field.isDisplayed());
        }
    }
}