package ru.ibsqa.chameleon.steps;

import org.springframework.lang.NonNull;
import ru.ibsqa.chameleon.compare.ICompareManager;
import ru.ibsqa.chameleon.elements.InvokeFieldException;
import ru.ibsqa.chameleon.elements.selenium.IFacadeSelenium;
import ru.ibsqa.chameleon.page_factory.pages.Page;
import ru.ibsqa.chameleon.selenium.enums.KeyEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ibsqa.chameleon.utils.waiting.ExploreResult;
import ru.ibsqa.chameleon.utils.waiting.Waiting;

import java.time.Duration;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Component
public class SeleniumFieldSteps extends CoreFieldSteps {

    @Autowired
    private PageSteps pageSteps;

    @Autowired
    private ICompareManager compareManager;

    @UIStep
    @TestStep("в поле \"${fieldName}\" выполнено нажатие клавиши \"${key}\"")
    public void pressKeyField(String fieldName, KeyEnum key) {
        getSeleniumField(fieldName).pressKey(key);
    }

    @UIStep
    @TestStep("выполнено нажатие на \"${fieldName}\"")
    public void clickField(String fieldName) {
        IFacadeSelenium field = getSeleniumField(fieldName);
        Waiting.on(field).apply(field::click);
    }

    @UIStep
    @TestStep("выполнено двойное нажатие на \"${fieldName}\"")
    public void doubleClickField(String fieldName) {
        IFacadeSelenium field = getSeleniumField(fieldName);
        Waiting.on(field).apply(field::doubleClick);
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
    public void checkFieldPlaceholder(String fieldName, String operator, String expected) {
        IFacadeSelenium field = getSeleniumField(fieldName);
        Waiting.on(field).explore(() -> {
            final String actual = field.getPlaceholder();
            return ExploreResult.create(
                    compareManager.checkValue(operator, actual, expected),
                    actual
            );
        }).ifNegative((actual) ->
                fail(compareManager.buildErrorMessage(operator, message("checkFieldPlaceholder"), actual, expected))
        );
    }

    @UIStep
    @TestStep("ожидается появление поля \"${fieldName}\" в течение \"${sec}\" секунд")
    public void waitFieldVisible(String fieldName, int sec) {
        IFacadeSelenium field = getSeleniumField(fieldName);
        Waiting.on(Duration.ofSeconds(sec))
                .ignoring(InvokeFieldException.class)
                .check(field::isDisplayed)
                .ifNegative(() -> fail(message("fieldIsNotDisplayed", fieldName)));
    }

    @UIStep
    @TestStep("ожидается исчезновение поля \"${fieldName}\" в течение \"${sec}\" секунд")
    public void waitFieldInVisible(String fieldName, int sec) {
        IFacadeSelenium field = getSeleniumField(fieldName);
        // При наличии поля ожидаем его невидимость
        Waiting.on(Duration.ofSeconds(sec)).ignoring(InvokeFieldException.class).check(() -> {
            if (field.isAbsent()) { // Здесь происходит быстрая проверка отсутствия поля на странице
                return true;
            }
            return !field.isDisplayed();
        }).ifNegative(() ->
                fail(message("fieldIsDisplayed", fieldName))
        );
    }

    @UIStep
    @TestStep("ожидается что поле \"${fieldName}\" станет активным в течение \"${sec}\" секунд")
    public void waitFieldIsEnabled(String fieldName, int sec) {
        IFacadeSelenium field = getSeleniumField(fieldName);
        Waiting.on(Duration.ofSeconds(sec))
                .ignoring(InvokeFieldException.class)
                .check(field::isEnabled)
                .ifNegative(() ->
                        fail(message("fieldIsNotActive", fieldName))
                );
    }

    @UIStep
    @TestStep("ожидается что поле \"${fieldName}\" станет неактивным в течение \"${sec}\" секунд")
    public void waitFieldIsDisabled(String fieldName, int sec) {
        IFacadeSelenium field = getSeleniumField(fieldName);
        Waiting.on(Duration.ofSeconds(sec))
                .ignoring(InvokeFieldException.class)
                .check(() -> !field.isEnabled())
                .ifNegative(() ->
                        fail(message("fieldIsActive", fieldName))
                );
    }

    @UIStep
    @TestStep("поле \"${fieldName}\" видимо")
    public void fieldIsDisplayed(String fieldName) {
        IFacadeSelenium field = getSeleniumField(fieldName);
        Waiting.on(field)
                .check(field::isDisplayed)
                .ifNegative(() -> fail(message("fieldIsNotDisplayed", fieldName)));
    }

    @UIStep
    @TestStep("поле \"${fieldName}\" невидимо")
    public void fieldIsNotDisplayed(String fieldName) {
        IFacadeSelenium field = getSeleniumField(fieldName);
        // При наличии поля ожидаем его невидимость
        Waiting.on(field).check(() -> {
            if (field.isAbsent()) { // Здесь происходит быстрая проверка отсутствия поля на странице
                return true;
            }
            return !field.isDisplayed();
        }).ifNegative(() ->
                fail(message("fieldIsDisplayed", fieldName))
        );
    }

    @UIStep
    @TestStep("поле \"${fieldName}\" активно")
    public void fieldIsEnabled(String fieldName) {
        IFacadeSelenium field = getSeleniumField(fieldName);
        Waiting.on(field)
                .check(field::isEnabled)
                .ifNegative(() ->
                        fail(message("fieldIsNotActive", fieldName))
                );
    }

    @UIStep
    @TestStep("поле \"${fieldName}\" неактивно")
    public void fieldIsDisabled(String fieldName) {
        IFacadeSelenium field = getSeleniumField(fieldName);
        Waiting.on(field)
                .check(() -> !field.isEnabled())
                .ifNegative(() ->
                        fail(message("fieldIsActive", fieldName))
                );
    }

    @UIStep
    @TestStep("поле \"${fieldName}\" редактируемо")
    public void fieldIsEditable(String fieldName) {
        IFacadeSelenium field = getSeleniumField(fieldName);
        Waiting.on(field)
                .check(field::isEditable)
                .ifNegative(() ->
                        fail(message("fieldIsNotEditable", fieldName))
                );
    }

    @UIStep
    @TestStep("поле \"${fieldName}\" нередактируемо")
    public void fieldIsNotEditable(String fieldName) {
        IFacadeSelenium field = getSeleniumField(fieldName);
        Waiting.on(field)
                .check(() -> !field.isEditable())
                .ifNegative(() ->
                        fail(message("fieldIsEditable", fieldName))
                );
    }

    @UIStep
    @TestStep("в поле \"${fieldName}\" значение ошибки ${operator} \"${expected}\"")
    public void checkFieldError(String fieldName, String operator, String expected) {
        IFacadeSelenium field = getSeleniumField(fieldName);
        Waiting.on(field).explore(() -> {
            final String actual = Optional.ofNullable(field.getErrorMsg()).orElse("");
            return ExploreResult.create(
                    compareManager.checkValue(operator, actual, expected),
                    actual
            );
        }).ifNegative((actual) ->
                fail(compareManager.buildErrorMessage(operator, message("checkFieldError"), actual, expected))
        );
    }

    @UIStep
    @TestStep("поле \"${fieldName}\" очищено")
    public void clearField(String fieldName) {
        IFacadeSelenium field = getSeleniumField(fieldName);
        Waiting.on(field).apply(field::clear);
    }

    @UIStep
    @TestStep("получено значение атрибута \"${attribute}\" поля \"${fieldName}\"")
    public String getFieldAttribute(String fieldName, String attribute) {
        IFacadeSelenium field = getSeleniumField(fieldName);
        return Waiting.on(field).get(() -> field.getAttribute(attribute));
    }

    @UIStep
    @TestStep("значение атрибута \"${attribute}\" поля \"${fieldName}\" ${operator} \"${expected}\"")
    public void checkFieldAttribute(String attribute, String fieldName, String operator, String expected) {
        IFacadeSelenium field = getSeleniumField(fieldName);
        Waiting.on(field).explore(() -> {
            final String actual = Optional.ofNullable(field.getAttribute(attribute)).orElse("");
            return ExploreResult.create(
                    compareManager.checkValue(operator, actual, expected),
                    actual
            );
        }).ifNegative((actual) ->
                fail(compareManager.buildErrorMessage(operator, message("checkFieldAttribute", attribute), actual, expected))
        );
    }

    @UIStep
    @TestStep("выполнено нажатие на \"${fieldName}\" правой кнопкой мыши")
    public void rightClickField(String fieldName) {
        IFacadeSelenium field = getSeleniumField(fieldName);
        Waiting.on(field).apply(field::rightClick);
    }

    @UIStep
    @TestStep("выполнено наведение мыши на \"${fieldName}\"")
    public void moveMouseToField(String fieldName) {
        IFacadeSelenium field = getSeleniumField(fieldName);
        Waiting.on(field).apply(field::moveMouseTo);
    }

    @UIStep
    @TestStep("выполнена прокрутка на \"${fieldName}\"")
    public void scrollIntoViewField(String fieldName) {
        IFacadeSelenium field = getSeleniumField(fieldName);
        Waiting.on(field).apply(field::scrollIntoView);
    }

    @SuppressWarnings("unchecked")
    public <T extends IFacadeSelenium> T getSeleniumField(@NonNull String fieldName) {
        return (T)super.getField(fieldName, IFacadeSelenium.class);
    }

}
