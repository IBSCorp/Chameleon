package ru.ibsqa.qualit.elements.uia;

import ru.ibsqa.qualit.selenium.driver.WebDriverFacade;
import ru.ibsqa.qualit.selenium.enums.KeyEnum;
import ru.ibsqa.qualit.uia.driver.UiaDriver;
import ru.ibsqa.qualit.uia.driver.UiaSearchBy;
import ru.ibsqa.qualit.uia.search_context.UiaSearchContext;
import ru.ibsqa.qualit.uia.search_context.UiaTableRowSearchContext;
import ru.ibsqa.qualit.utils.spring.SpringUtils;
import com.sun.jna.platform.win32.WinDef;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import mmarquee.automation.AutomationException;
import mmarquee.automation.Element;
import mmarquee.automation.controls.AutomationBase;
import mmarquee.automation.controls.DataGridCell;
import mmarquee.automation.controls.ElementBuilder;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Coordinates;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
public class UiaElement extends AutomationBase implements IUiaElement {

    @Getter
    private final UiaElementType uiaElementType;
    private final WebDriverFacade driver;
    private final AutomationBase automationBase;
    private final Element element;
    private final UiaSearchBy searchBy = SpringUtils.getBean(UiaSearchBy.class);

    @Getter
    private DataGridCell cell = null;

    public UiaElement(UiaElementType uiaElementType, AutomationBase automationBase, Element element, WebDriverFacade driver) {
        super(new ElementBuilder(element));
        this.uiaElementType = Objects.isNull(uiaElementType) ? UiaElementType.GENERAL : uiaElementType;
        this.cell = getUiaElementType().equals(UiaElementType.TABLE_CELL) ? new DataGridCell(new ElementBuilder(element)) : null;
        this.automationBase = automationBase;
        this.element = element;
        this.driver = driver;
    }

    public AutomationBase getAutomationBase() {
        return automationBase;
    }

    public Element getElement() {
        return element;
    }

    public Robot getRobot() {
        return getUiaDriver().getRobot();
    }

    public UiaDriver getUiaDriver() {
        return ((UiaDriver) getDriver());
    }

    @Override
    public void moveMouseTo() {
        Rectangle rect = getRect();
        int x = (getRect().getWidth()) / 2 + getRect().getX();
        int y = (getRect().getHeight()) / 2 + getRect().getY();
        getRobot().mouseMove(x, y);
    }

    @Override
    public String getAriaRole() {
        try {
            return automationBase.getAriaRole();
        } catch (AutomationException ex) {
            return null;
        }
    }

    @Override
    public void click() {
        getElement().setFocus();
        moveMouseTo();
        getRobot().mousePress(InputEvent.BUTTON1_DOWN_MASK);
        getRobot().mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    @Override
    public void doubleClick() {
        getElement().setFocus();
        moveMouseTo();
        getRobot().mousePress(InputEvent.BUTTON1_DOWN_MASK);
        getRobot().mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        getRobot().delay(100);
        getRobot().mousePress(InputEvent.BUTTON1_DOWN_MASK);
        getRobot().mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    @Override
    public void rightClick() {
        moveMouseTo();
        getUiaDriver().getRobot().delay(100);
        getRobot().mousePress(InputEvent.BUTTON3_DOWN_MASK);
        getRobot().mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
    }

    @Override
    public void submit() {
        fail("Uia.Element.submit() не реализован");
    }

    @Override
    public void sendKeys(CharSequence... keysToSend) {
        fail("Uia.Element.sendKeys() не реализован");
    }

    @Override
    public void clear() {
        sendKeys("");
    }

    @Override
    public String getTagName() {
        fail("Uia.Element.getTagName() не реализован");
        return null;
    }

    @Override
    public String getAttribute(String name) {
        fail("Uia.Element.getAttribute() не реализован");
        return null;
    }

    @Override
    public boolean isSelected() {
        fail("Uia.Element.isSelected() не реализован");
        return false;
    }

    @Override
    public boolean isEnabled() {
        try {
            return getElement().isEnabled();
        } catch (AutomationException e) {
            log.error(e.getMessage(), e);
            fail(Objects.nonNull(e.getMessage()) ? e.getMessage() : e.toString());
            return false;
        }
    }

    @Override
    public String getText() {
        fail("Uia.Element.getText() не реализован");
        return null;
    }

    @Override
    public List<WebElement> findElements(By by) {
        fail("Uia.Element.findElements() не реализован");
        return null;
    }

    @Override
    public WebElement findElement(By by) {
        if (by.toString().equals("By.xpath: .")) {
            return this;
        }
        if (Objects.nonNull(getElement()) && getUiaElementType().equals(UiaElementType.TABLE_ROW)) {
            UiaSearchContext searchContext = new UiaTableRowSearchContext(getUiaDriver(), getElement());
            return searchBy.create(searchContext, by);
        }
        fail(String.format("Uia.Element.findElement() не реализован у компонента [%s]", automationBase.getClass()));
        return null;
    }

    @Override
    public boolean isDisplayed() {
        // Если компонент найден, считаем, что он виден
        try {
            return !getElement().offScreen();
        } catch (AutomationException e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public Point getLocation() {
        Rectangle rect = getRect();
        return new Point(rect.getX(), rect.getY());
    }

    @Override
    public Dimension getSize() {
        Rectangle rect = getRect();
        return new Dimension(rect.getWidth(), rect.getHeight());
    }

    @Override
    public Rectangle getRect() {
        WinDef.RECT rect = null;
        try {
            int tryCount = 3;
            while (tryCount > 0) {
                tryCount--;
                rect = this.getElement().getBoundingRectangle();
                if (rect.bottom > 0 ||
                        rect.left > 0 ||
                        rect.right > 0 ||
                        rect.top > 0) {
                    return new Rectangle(rect.left, rect.top, rect.bottom - rect.top, rect.right - rect.left);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignore) {
                }
            }
        } catch (AutomationException e) {
            log.error(e.getMessage(), e);
            fail(Objects.nonNull(e.getMessage()) ? e.getMessage() : e.toString());
        }
        fail(String.format("Не удалось определить координаты компонента [%s]", automationBase.getClass()));
        return null;
    }

    @Override
    public String getCssValue(String propertyName) {
        return null;
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
        return null;
    }

    @Override
    public String getPlaceholder() {
        return null;
    }

    @Override
    public void pressKey(KeyEnum key) {
        fail("Uia.Element.pressKey() не реализован");
    }

    @Override
    public void type(String value) {
        sendKeys(value);
    }

    @Override
    public boolean isEditable() {
        return false;
    }

    @Override
    public String getErrorMsg() {
        return null;
    }

    @Override
    public String getLabel() {
        return null;
    }

    @Override
    public WebDriverFacade getDriver() {
        return driver;
    }

    @Override
    public WebElement getWrappedElement() {
        return this;
    }

    @Override
    public Coordinates getCoordinates() {
        return null;
    }
}
