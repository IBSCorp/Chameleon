package ru.ibsqa.qualit.elements.selenium;

import ru.ibsqa.qualit.elements.IFacadeClearable;
import ru.ibsqa.qualit.elements.IFacadeMappedByMeta;
import ru.ibsqa.qualit.elements.IFacadeReadable;
import ru.ibsqa.qualit.elements.IFacadeWritable;
import ru.ibsqa.qualit.selenium.driver.WebDriverFacade;
import ru.ibsqa.qualit.selenium.enums.KeyEnum;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.Locatable;

public interface IFacadeSelenium extends IFacadeReadable, IFacadeWritable, IFacadeClearable, WebElement, WrapsElement, Locatable, IFacadeMappedByMeta {

    @Override
    default void setFieldValue(String value) {
        type(value);
    }

    @Override
    default String getFieldValue() {
        String value = getText().replaceAll("\\u00A0", " ");
        if (value.isEmpty()) {
            value = getAttribute("value");
            if (value == null) {
                return "";
            }
            value = value.replaceAll("\\u00A0", " ");
        }
        return value;
    }

    @Override
    default void clearFieldValue() {
        clear();
    }

    @Override
    default boolean isFieldExists() {
        try {
            getWrappedElement().isDisplayed();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    String getPlaceholder();

    void pressKey(KeyEnum key);

    void doubleClick();

    void type(String value);

    boolean isEditable();

    String getErrorMsg();

    String getLabel();

    WebDriverFacade getDriver();

    /**
     * Передвинуть указатель мыши к элементу
     *
     * @param webDriver драйвер
     * @param element   элемент
     */
    static void moveMouseTo(WebDriverFacade webDriver, WebElement element) {
        Actions builder = new Actions(webDriver);
        builder.moveToElement(element).pause(200).build().perform();
    }

    /**
     * Передвинуть указатель мыши к элементу
     *
     * @param element элемент
     */
    default void moveMouseTo(WebElement element) {
        moveMouseTo(getDriver(), element);
    }

    /**
     * Передвинуть указатель мыши к текущему элементу
     */
    default void moveMouseTo() {
        moveMouseTo(getDriver(), getWrappedElement());
    }

    /**
     * Прокрутить страницу/список к элементу
     *
     * @param webDriver драйвер
     * @param element   элемент
     */
    static void scrollIntoView(WebDriverFacade webDriver, WebElement element) {
        webDriver.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    /**
     * Прокрутить страницу/список к элементу
     *
     * @param element элемент
     */
    default void scrollIntoView(WebElement element) {
        scrollIntoView(getDriver(), element);
    }

    /**
     * Прокрутить страницу/список к текущему элементу
     */
    default void scrollIntoView() {
        scrollIntoView(getDriver(), getWrappedElement());
    }

    default void scrollToElement() {
        scrollIntoView();
    }

    /**
     * Нажать правую кнопку мыши на элементе
     *
     * @param webDriver драйвер
     * @param element   элемент
     */
    static void rightClick(WebDriverFacade webDriver, WebElement element) {
        webDriver.executeScript(
                "var evt = document.createEvent('MouseEvents');"
                        + "var RIGHT_CLICK_BUTTON_CODE = 2;"
                        + "evt.initMouseEvent('contextmenu', true, true, window, 1, 0, 0, 0, 0, false, false, false, false, RIGHT_CLICK_BUTTON_CODE, null);"
                        + "arguments[0].dispatchEvent(evt);", element);
    }

    /**
     * Нажать правую кнопку мыши на элементе
     *
     * @param element элемент
     */
    default void rightClick(WebElement element) {
        rightClick(getDriver(), element);
    }

    /**
     * Нажать правую кнопку мыши на текущем элементе
     */
    default void rightClick() {
        rightClick(getDriver(), getWrappedElement());
    }

}
