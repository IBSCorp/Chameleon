package ru.ibsqa.qualit.elements.uia;

import lombok.extern.slf4j.Slf4j;
import mmarquee.automation.AutomationException;
import mmarquee.automation.ControlType;
import mmarquee.automation.controls.Menu;
import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;
import ru.ibsqa.qualit.elements.MetaElement;
import ru.ibsqa.qualit.definitions.repository.uia.MetaDropDownCell;
import ru.ibsqa.qualit.uia.driver.UiaSupportedDriver;

import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
@MetaElement(value = MetaDropDownCell.class, supportedDriver = UiaSupportedDriver.class, priority = ConfigurationPriority.LOW)
public class DropDownCell extends AbstractCell {

    @Override
    public void type(String value) {
        Menu menu;
        //ui-automation не всегда видит выпадающее меню после клика, поэтому делаем 3 попытки
        int loopCount = 0;
        while (loopCount < 3) {
            try {
                loopCount++;
                super.click();
                getRobot().delay(100);
                menu = findElement(Menu.class, "DropDown", ControlType.Menu);
                if (menu != null) {
                    menu.getMenuItem(String.valueOf(value)).click();
                    break;
                }
            } catch (AutomationException e) {
                log.error(e.getMessage(), e);
                fail(String.format("Ошибка при выборе пункта [%s]", value));
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                fail(String.format("Ошибка при выборе пункта [%s], попытка [%d]", value, loopCount));
            }
        }
    }
}
