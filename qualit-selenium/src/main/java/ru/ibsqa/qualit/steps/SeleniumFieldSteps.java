package ru.ibsqa.qualit.steps;

import ru.ibsqa.qualit.context.IContextExplorer;
import ru.ibsqa.qualit.elements.selenium.IFacadeSelenium;
import ru.ibsqa.qualit.page_factory.pages.Page;
import ru.ibsqa.qualit.selenium.enums.KeyEnum;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

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

    public void clickFieldAndPageLoaded(String fieldName, Page page) {
        clickField(fieldName);
        pageSteps.stepLoadedPage(page);
    }

    @UIStep
    @TestStep("значение подсказки для поля \"${fieldName}\" ${operator} \"${expected}\"")
    public void checkFieldPlaceholder(String fieldName, CompareOperatorEnum operator, String expected) {
        IFacadeSelenium field = getSeleniumField(fieldName);
        AtomicReference<String> actual = new AtomicReference<>();
        boolean isChecked = waiting(
                Duration.ofSeconds(field.getWaitTimeOut()),
                () -> {
                    actual.set(getSeleniumField(fieldName).getPlaceholder());
                    return operator.checkValue(actual.get(), expected);
                }
        );
        if (!isChecked) {
            fail(operator.buildErrorMessage(message("checkFieldPlaceholder"), actual.get(), expected));
        }
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
        IFacadeSelenium field = getSeleniumField(fieldName);
        boolean isDisplayed = waiting(
                Duration.ofSeconds(field.getWaitTimeOut()),
                field::isDisplayed
        );
        if (!isDisplayed) {
            fail(message("fieldIsNotDisplayed", fieldName));
        }
    }

    @UIStep
    @TestStep("поле \"${fieldName}\" невидимо")
    public void fieldIsNotDisplayed(String fieldName) {
        IFacadeSelenium field = getSeleniumField(fieldName);
        // При наличии поля ожидаем его невидимость
        boolean isNotDisplayed = waiting(
                Duration.ofSeconds(field.getWaitTimeOut()),
                () -> {
                    if (field.isAbsent()) { // Здесь происходит быстрая проверка отсутствия поля на странице
                        return true;
                    }
                    return !field.isDisplayed();
                }
        );
        if (!isNotDisplayed) {
            fail(message("fieldIsDisplayed", fieldName));
        }
    }

    @UIStep
    @TestStep("поле \"${fieldName}\" активно")
    public void fieldIsEnabled(String fieldName) {
        IFacadeSelenium field = getSeleniumField(fieldName);
        boolean isEnabled = waiting(
                Duration.ofSeconds(field.getWaitTimeOut()),
                field::isEnabled
        );
        if (!isEnabled) {
            fail(message("fieldIsNotActive", fieldName));
        }
    }

    @UIStep
    @TestStep("поле \"${fieldName}\" неактивно")
    public void fieldIsDisabled(String fieldName) {
        IFacadeSelenium field = getSeleniumField(fieldName);
        boolean isNotEnabled = waiting(
                Duration.ofSeconds(field.getWaitTimeOut()),
                () -> !field.isEnabled()
        );
        if (!isNotEnabled) {
            fail(message("fieldIsActive", fieldName));
        }
    }

    @UIStep
    @TestStep("поле \"${fieldName}\" редактируемо")
    public void fieldIsEditable(String fieldName) {
        IFacadeSelenium field = getSeleniumField(fieldName);
        boolean isEditable = waiting(
                Duration.ofSeconds(field.getWaitTimeOut()),
                field::isEditable
        );
        if (!isEditable) {
            fail(message("fieldIsNotEditable", fieldName));
        }
    }

    @UIStep
    @TestStep("поле \"${fieldName}\" нередактируемо")
    public void fieldIsNotEditable(String fieldName) {
        IFacadeSelenium field = getSeleniumField(fieldName);
        boolean isNotEditable = waiting(
                Duration.ofSeconds(field.getWaitTimeOut()),
                () -> !field.isEditable()
        );
        if (!isNotEditable) {
            fail(message("fieldIsEditable", fieldName));
        }
    }

    @UIStep
    @TestStep("в поле \"${fieldName}\" значение ошибки ${operator} \"${expected}\"")
    public void checkFieldError(String fieldName, CompareOperatorEnum operator, String expected) {
        IFacadeSelenium field = getSeleniumField(fieldName);
        AtomicReference<String> actual = new AtomicReference<>();
        boolean isChecked = waiting(
                Duration.ofSeconds(field.getWaitTimeOut()),
                () -> {
                    actual.set(Optional.ofNullable(getSeleniumField(fieldName).getErrorMsg()).orElse(""));
                    return operator.checkValue(actual.get(), expected);
                }
        );
        if (!isChecked) {
            fail(operator.buildErrorMessage(message("checkFieldError"), actual.get(), expected));
        }
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
        IFacadeSelenium field = getSeleniumField(fieldName);
        AtomicReference<String> actual = new AtomicReference<>();
        boolean isChecked = waiting(
                Duration.ofSeconds(field.getWaitTimeOut()),
                () -> {
                    actual.set(Optional.ofNullable(getSeleniumField(fieldName).getAttribute(attribute)).orElse(""));
                    return operator.checkValue(actual.get(), expected);
                }
        );
        if (!isChecked) {
            fail(operator.buildErrorMessage(message("checkFieldAttribute", attribute), actual.get(), expected));
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

    @SuppressWarnings("unchecked")
    public <T extends IFacadeSelenium> T getSeleniumField(String fieldName) {
        return (T)super.getField(fieldName, IFacadeSelenium.class);
    }

}
