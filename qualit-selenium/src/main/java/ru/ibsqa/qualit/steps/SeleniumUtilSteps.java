package ru.ibsqa.qualit.steps;

import ru.ibsqa.qualit.selenium.enums.KeyEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;

@Component
public class SeleniumUtilSteps extends CoreUtilSteps {

    @Autowired
    PageSteps pageSteps;

    @UIStep
    @TestStep("выполнено нажатие клавиши \"${key}\"")
    public void pressKey(KeyEnum key) {
        try {
            Robot robot = new Robot();
            robot.setAutoDelay(250);
            robot.keyPress(key.getValue());
            robot.keyRelease(key.getValue());
        } catch (Exception e) {
        }
    }

    @UIStep
    @TestStep("выполнено нажатие сочетания клавиш \"${key1}\", \"${key2}\"")
    public void pressKey(KeyEnum key1, KeyEnum key2) {
        try {
            Robot robot = new Robot();
            robot.setAutoDelay(250);
            robot.keyPress(key1.getValue());
            robot.keyPress(key2.getValue());
            robot.keyRelease(key1.getValue());
            robot.keyRelease(key2.getValue());
        } catch (Exception e) {
        }
    }
}