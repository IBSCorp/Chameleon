package ru.ibsqa.chameleon.uia.steps;

import ru.ibsqa.chameleon.selenium.driver.IDriverManager;
import ru.ibsqa.chameleon.selenium.enums.KeyEnum;
import ru.ibsqa.chameleon.steps.CoreUtilSteps;
import ru.ibsqa.chameleon.steps.SeleniumFieldSteps;
import ru.ibsqa.chameleon.steps.UIStep;
import ru.ibsqa.chameleon.uia.driver.UiaDriver;
import ru.ibsqa.chameleon.elements.uia.UiaElementFacade;
import ru.ibsqa.chameleon.uia.palette.IPalette;
import ru.ibsqa.chameleon.steps.TestStep;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
@Component
public class UiaSteps extends CoreUtilSteps {

    @Autowired
    private IDriverManager driverManager;

    @Autowired
    private SeleniumFieldSteps seleniumFieldSteps;

    @Autowired
    private IPalette palette;

    @UIStep
    @TestStep("выполнено нажатие клавиши \"${key}\"")
    public void pressKey(KeyEnum key) {
        try {
            Robot robot = new Robot();
            robot.setAutoDelay(250);
            robot.keyPress(key.getValue());
            robot.keyRelease(key.getValue());
        } catch (Exception ignore) {
        }
    }

    @UIStep
    @TestStep("выполнено переключение к окну \"${title}\"")
    public void switchToWindow(String title) {
        UiaDriver driver = (UiaDriver) driverManager.getLastDriver();
        driver.resetAutomationWindow();
        driver.setMainWindowTitle(title);
        driver.getAutomationWindow();
    }

    @UIStep
    @TestStep("цвет поля \"${fieldName}\" виден как \"${colorName}\"")
    public void checkFieldColor(String fieldName, String colorName) {

        if (palette.getColor(colorName).size() < 1) {
            fail("Неизвестное название цвета: " + colorName);
        }

        UiaElementFacade element = seleniumFieldSteps.getSeleniumField(fieldName);
        element.click();
        Point location = MouseInfo.getPointerInfo().getLocation();

        boolean res = colorName.equals(palette.getColor(location).getName());

        assertTrue(res, String.format("Фактический цвет {%s} в точке {%s} не соответствует ожидаемому {%s}",
                palette.getColor(location).getName(),
                location.toString().replace("java.awt.", ""),
                colorName));
    }
}
