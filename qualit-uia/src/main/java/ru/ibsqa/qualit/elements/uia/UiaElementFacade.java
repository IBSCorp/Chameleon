package ru.ibsqa.qualit.elements.uia;

import ru.ibsqa.qualit.elements.selenium.WebElementFacade;
import ru.ibsqa.qualit.uia.driver.UiaDriver;
import ru.ibsqa.qualit.utils.spring.SpringUtils;
import ru.ibsqa.qualit.utils.waiting.WaitingUtils;
import com.sun.jna.platform.win32.WinDef;
import lombok.extern.slf4j.Slf4j;
import mmarquee.automation.AutomationException;
import mmarquee.automation.ControlType;
import mmarquee.automation.Element;
import mmarquee.automation.controls.AutomationBase;
import mmarquee.automation.controls.ElementBuilder;
import org.openqa.selenium.By;

import java.awt.*;
import java.awt.event.InputEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
public abstract class UiaElementFacade extends WebElementFacade implements IUiaElement {

    private final WaitingUtils waitingUtils = SpringUtils.getBean(WaitingUtils.class);

    public UiaDriver getUiaDriver() {
        return (UiaDriver) getDriver();
    }

    public WaitingUtils getWaitingUtils() {
        return waitingUtils;
    }

    public UiaElement getUiaElement() {
        return (UiaElement) findElement(By.xpath("."));
    }

    public Robot getRobot() {
        return getUiaDriver().getRobot();
    }

    public <T extends AutomationBase> T findElement(Class<T> clazz, String name, ControlType controlType) {
        // Дождемся появления элемента
        AtomicReference<T> el = new AtomicReference<>();

        getWaitingUtils().waiting(getUiaDriver().getImplicitlywait() * 1000, () -> {
            try {
                Element ae = getUiaDriver().getAutomationWindow().getControlByControlType(name, controlType).getElement();
                try {
                    el.set(clazz.getConstructor(ElementBuilder.class).newInstance(new ElementBuilder(ae)));
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    log.error(e.getMessage(), e);
                    fail("Не удалось создать элемент");
                }
                return true;
            } catch (AutomationException e) {
                return false;
            }
        });
        return el.get();
    }

    @Override
    public void doubleClick() {
        getUiaElement().doubleClick();
    }

    @Override
    public void moveMouseTo() {
        getUiaElement().moveMouseTo();
    }

    @Override
    public void type(String value) {
        //this.clear(); // для AutomationUI не надо ничего чистить перед вводом
        this.sendKeys(value);
    }

    public WinDef.RECT getRectOfAutomationBase(AutomationBase automationBase) {
        WinDef.RECT rect = null;
        try {
            int tryCount = 3;
            while (tryCount > 0) {
                tryCount--;
                rect = automationBase.getBoundingRectangle();
                if (rect.bottom > 0 ||
                        rect.left > 0 ||
                        rect.right > 0 ||
                        rect.top > 0) {
                    return rect;
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

    public void mouseClickOnAutomationBase(AutomationBase automationBase) {
        WinDef.RECT rect = getRectOfAutomationBase(automationBase);
        int x = (rect.right - rect.left) / 2 + rect.left;
        int y = (rect.bottom - rect.top) / 2 + rect.top;
        getUiaDriver().getRobot().mouseMove(x, y);
        getUiaDriver().getRobot().mousePress(InputEvent.BUTTON1_DOWN_MASK);
        getUiaDriver().getRobot().mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    public void mouseDblClickOnAutomationBase(AutomationBase automationBase) {
        WinDef.RECT rect = getRectOfAutomationBase(automationBase);
        int x = (rect.right - rect.left) / 2;
        int y = (rect.bottom - rect.top) / 2;
        mouseDblClickOnAutomationBase(automationBase, x, y);
    }

    public void mouseDblClickOnAutomationBase(AutomationBase automationBase, int offsetX, int offsetY) {
        WinDef.RECT rect = getRectOfAutomationBase(automationBase);
        int x = rect.left + offsetX;
        int y = rect.top + offsetY;
        getUiaDriver().getRobot().mouseMove(x, y);
        getUiaDriver().getRobot().mousePress(InputEvent.BUTTON1_DOWN_MASK);
        getUiaDriver().getRobot().mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        getUiaDriver().getRobot().delay(100);
        getUiaDriver().getRobot().mousePress(InputEvent.BUTTON1_DOWN_MASK);
        getUiaDriver().getRobot().mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    @Override
    public String getFieldValue() {
        return this.getText();
    }

    @Override
    public String getText() {
        return this.getWrappedElement().getText();
    }

    @Override
    public void rightClick() {
        moveMouseTo();
        getUiaDriver().getRobot().delay(100);
        getRobot().mousePress(InputEvent.BUTTON3_DOWN_MASK);
        getRobot().mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
    }

}
