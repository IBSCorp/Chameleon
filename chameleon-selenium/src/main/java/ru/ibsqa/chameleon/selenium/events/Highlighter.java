package ru.ibsqa.chameleon.selenium.events;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Listener для подсветки элементов при нажатии, изменении значения и поиске.
 */
@Slf4j
public class Highlighter extends AbstractWebDriverEventListener {

    private final String ORIGINAL_STYLE_ATTR = "__seleniumHighlighting";
    private final String STYLE_ATTR = "style";
    private final String HIGHLIGHTING_VALUE = "done";

    @Override
    public void beforeClickOn(WebElement element, WebDriver driver) {
        highlight(driver, element, "orange");
    }

    @Override
    public void afterFindBy(By by, WebElement element, WebDriver driver) {
        highlight(driver, getElement(by, element), "red");
    }

    @Override
    public void beforeChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {
        highlight(driver, element, "orange");
    }

    /**
     * Данный метод решает частный случай с коллекциями. afterFindBy возвращает для коллекций в параметре
     * element - строку, а в параметре by - локатор ячейки. Пробуем найти ячейку. Для обычных полей
     * element.findElements(by) будет возвращать пустой список, что приведет к пробросу исходного значения element.
     *
     * @param by
     * @param element
     * @return
     */
    private WebElement getElement(By by, WebElement element) {
        if (Objects.nonNull(element) && Objects.nonNull(by)) {
            try {
                List<WebElement> elements = element.findElements(by);
                if (1 == elements.size()) {
                    return elements.get(0);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return element;
    }


    /**
     * Подсветка с анимацией
     *
     * @param element подсвечиваемый веб-элемент
     * @param color цвет подсветки
     * @return веб-элемент
     */
    private <T extends WebElement> T highlight(WebDriver driver, T element, final String color) {

        if (element != null) {
            String originalStyle = element.getAttribute(ORIGINAL_STYLE_ATTR);
            boolean storeOriginalStyle = false;
            if (Objects.isNull(originalStyle)) {
                originalStyle = element.getAttribute(STYLE_ATTR);
                storeOriginalStyle = true;
            }
            if (Objects.isNull(originalStyle)) {
                originalStyle = HIGHLIGHTING_VALUE;
            }

            highlightElement(driver, element, color, originalStyle, storeOriginalStyle);
        }
        return element;
    }

    /**
     * Вызывает JavaScript код, который меняет css стиль переданному элементу, рисуя вокруг него рамку заданного цвета
     * @param element веб-элемент
     * @param color цвет рамки
     * @param originalStyle исходный стиль
     * @param storeOriginalStyle признак сохранения исходного стиля
     */
    private void highlightElement(WebDriver driver, WebElement element, String color, String originalStyle, boolean storeOriginalStyle) {
        List<String> scripts = new ArrayList<>();

        if (storeOriginalStyle) {
            scripts.add(String.format("arguments[0].setAttribute('%s', '%s'); ", ORIGINAL_STYLE_ATTR, originalStyle));
        }
        scripts.add(String.format("arguments[0].setAttribute('%s', '%soutline: 3px dotted %s !important;')", STYLE_ATTR, (originalStyle.equals(HIGHLIGHTING_VALUE) ? "" : (originalStyle + " ")), color));

        if (scripts.size() > 0) {
            ((JavascriptExecutor) driver).executeScript(String.join(" ", scripts), element);
        }
    }
}