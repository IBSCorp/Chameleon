package ru.ibsqa.chameleon.elements.uia;

import com.sun.jna.platform.win32.WinDef;
import lombok.extern.slf4j.Slf4j;
import mmarquee.automation.AutomationException;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;
import ru.ibsqa.chameleon.elements.MetaElement;
import ru.ibsqa.chameleon.definitions.repository.uia.MetaRowSelectorCell;
import ru.ibsqa.chameleon.uia.driver.UiaSupportedDriver;

import java.awt.*;
import java.awt.event.InputEvent;

@Slf4j
@MetaElement(value = MetaRowSelectorCell.class, supportedDriver = UiaSupportedDriver.class, priority = ConfigurationPriority.LOW)
public class RowSelectorCell extends AbstractCell {

    @Override
    public void click() {
        try {
            WinDef.RECT rect = getUiaElement().getCell().getBoundingRectangle();
            Robot robot = getRobot();
            robot.mouseMove(rect.left - (rect.bottom - rect.top) / 2, rect.top + (rect.bottom - rect.top) / 2);
            getRobot().mousePress(InputEvent.BUTTON1_DOWN_MASK);
            getRobot().mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        } catch (AutomationException e) {
            log.warn(e.getMessage());
        }
    }

}
