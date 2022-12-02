package ru.ibsqa.qualit.steps;

import ru.ibsqa.qualit.context.IContextExplorer;
import ru.ibsqa.qualit.context.PickElementResult;
import ru.ibsqa.qualit.elements.selenium.IFacadeSelenium;
import ru.ibsqa.qualit.page_factory.pages.Page;
import ru.ibsqa.qualit.selenium.enums.KeyEnum;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

// TODO локализовать
@Component
public class SeleniumFieldSteps extends CoreFieldSteps {

    @Autowired
    private PageSteps pageSteps;

    @Autowired
    private IContextExplorer contextExplorer;

    @UIStep
    @TestStep("в поле \"${fieldName}\" выполнено нажатие клавиши \"${key}\"")
    public void pressKeyField(String fieldName, KeyEnum key) {
        getSeleniumField(fieldName).pressKey(key);
    }

    @UIStep
    @TestStep("выполнено нажатие на \"${fieldName}\"")
    public void clickField(String fieldName) {
        getSeleniumField(fieldName).click();
    }

    @UIStep
    @TestStep("выполнено двойное нажатие на \"${fieldName}\"")
    public void doubleClickField(String fieldName) {
        getSeleniumField(fieldName).doubleClick();
    }

    public void clickFieldAndPageLoaded(String fieldName, String page) {
        clickField(fieldName);
        pageSteps.stepLoadedPage(page);
    }

    @UIStep
    @TestStep("значение подсказки для поля \"${fieldName}\" ${operator} \"${expected}\"")
    public void checkFieldPlaceholder(String fieldName, CompareOperatorEnum operator, String expected) {
        String actual = getSeleniumField(fieldName).getPlaceholder().trim().replaceAll("\u00A0", " ");
        if (!operator.checkValue(actual, expected)) {
            fail(operator.buildErrorMessage(message("checkFieldPlaceholder"), actual, expected));
        }
    }

    public void clickFieldAndPageLoaded(String fieldName, Page page) {
        clickField(fieldName);
        pageSteps.stepLoadedPage(page);
    }

    @UIStep
    @TestStep("ожидается появление поля \"${fieldName}\" в течение \"${sec}\" секунд")
    public void waitFieldVisible(String fieldName, int sec) {
        IFacadeSelenium field = getSeleniumField(fieldName);
        new WebDriverWait(field.getDriver().getWrappedDriver(), Duration.ofSeconds(sec))
                .ignoring(NoSuchFieldException.class)
                .until(ExpectedConditions.visibilityOf(field));
    }

    @UIStep
    @TestStep("ожидается исчезновение поля \"${fieldName}\" в течение \"${sec}\" секунд")
    public void waitFieldInVisible(String fieldName, int sec) {
        IFacadeSelenium field = getSeleniumField(fieldName);
        new WebDriverWait(field.getDriver().getWrappedDriver(), Duration.ofSeconds(sec))
                .until(ExpectedConditions.invisibilityOf(field));
    }

    @UIStep
    @TestStep("ожидается что поле \"${fieldName}\" станет активным в течение \"${sec}\" секунд")
    public void waitFieldIsEnabled(String fieldName, int sec) {
        IFacadeSelenium field = getSeleniumField(fieldName);
        new WebDriverWait(field.getDriver().getWrappedDriver(), Duration.ofSeconds(sec))
                .ignoring(NoSuchFieldException.class)
                .until(ExpectedConditions.elementToBeClickable(field));
    }

    @UIStep
    @TestStep("ожидается что поле \"${fieldName}\" станет неактивным в течение \"${sec}\" секунд")
    public void waitFieldIsDisabled(String fieldName, int sec) {
        IFacadeSelenium field = getSeleniumField(fieldName);
        new WebDriverWait(field.getDriver().getWrappedDriver(), Duration.ofSeconds(sec))
                .ignoring(NoSuchFieldException.class)
                .until(ExpectedConditions.not(ExpectedConditions.elementToBeClickable(field)));
    }

    @UIStep
    @TestStep("поле \"${fieldName}\" видимо")
    public void fieldIsDisplayed(String fieldName) {
        boolean isDisplayed = getSeleniumField(fieldName).isDisplayed();
        assertTrue(isDisplayed, "Поле " + fieldName + " не отображается на странице");
    }

    @UIStep
    @TestStep("поле \"${fieldName}\" активно")
    public void fieldIsEnabled(String fieldName) {
        boolean isEnabled = getSeleniumField(fieldName).isEnabled();
        assertTrue(isEnabled, "Поле " + fieldName + " неактивно на странице");
    }

    @UIStep
    @TestStep("поле \"${fieldName}\" неактивно")
    public void fieldIsDisabled(String fieldName) {
        boolean isEnabled = getSeleniumField(fieldName).isEnabled();
        assertTrue(!isEnabled, "Поле " + fieldName + " активно на странице");
    }

    @UIStep
    @TestStep("поле \"${fieldName}\" редактируемо")
    public void fieldIsEditable(String fieldName) {
        boolean isEditable = getSeleniumField(fieldName).isEditable();
        assertTrue(isEditable, "Field " + fieldName + " not editable");
    }

    @UIStep
    @TestStep("поле \"${fieldName}\" нередактируемо")
    public void fieldIsNotEditable(String fieldName) {
        boolean isEditable = getSeleniumField(fieldName).isEditable();
        assertFalse(isEditable, "Field " + fieldName + " editable");
    }

    @UIStep
    @TestStep("поле \"${fieldName}\" отсутствует")
    public void fieldIsNotExist(String fieldName) {
        boolean isDisplayed = getSeleniumField(fieldName).isDisplayed();
        assertFalse(isDisplayed, "Поле " + fieldName + " отображается на странице");
    }

    @UIStep
    @TestStep("в поле \"${fieldName}\" значение ошибки ${operator} \"${errorMsg}\"")
    public void checkFieldError(String fieldName, CompareOperatorEnum operator, String errorMsg) {
        String actualError = getSeleniumField(fieldName).getErrorMsg();
        if (!operator.checkValue(actualError, errorMsg)) {
            fail(operator.buildErrorMessage(message("checkFieldError"), actualError, errorMsg));
        }
        //assertEquals(String.format("Сообщение об ошибке в поле [%s] не соответствует ожиданиям", fieldName), errorMsg, actualError);
    }

    @UIStep
    @TestStep("поле \"${fieldName}\" очищено")
    public void clearField(String fieldName) {
        getSeleniumField(fieldName).clear();
    }

    @UIStep
    @TestStep("получено значение атрибута \"${attribute}\" поля \"${fieldName}\"")
    public String getFieldAttribute(String fieldName, String attribute) {
        return getSeleniumField(fieldName).getAttribute(attribute);
    }

    @UIStep
    @TestStep("значение атрибута \"${attribute}\" поля \"${fieldName}\" ${operator} \"${expected}\"")
    public void checkFieldAttribute(String attribute, String fieldName, CompareOperatorEnum operator, String expected) {
        String actual = getFieldAttribute(fieldName, attribute);
        if (actual == null) {
            actual = "";
        }
        if (!operator.checkValue(actual, expected)) {
            fail(operator.buildErrorMessage(message("checkFieldAttribute", attribute), actual, expected));
        }
    }

    @UIStep
    @TestStep("выполнено нажатие на \"${fieldName}\" правой кнопкой мыши")
    public void rightClickField(String fieldName) {
        getSeleniumField(fieldName).rightClick();
    }

    @UIStep
    @TestStep("выполнено наведение мыши на \"${fieldName}\"")
    public void moveMouseToField(String fieldName) {
        getSeleniumField(fieldName).moveMouseTo();
    }

    @UIStep
    @TestStep("выполнена прокрутка на \"${fieldName}\"")
    public void scrollIntoViewField(String fieldName) {
        getSeleniumField(fieldName).scrollIntoView();
    }

    public <T extends IFacadeSelenium> T getSeleniumField(String fieldName) {
        PickElementResult<IFacadeSelenium, ?> pickElementResult = contextExplorer.pickElement(fieldName, IFacadeSelenium.class);
        IFacadeSelenium field = pickElementResult.getElement();

        return (T) field;
    }

}
