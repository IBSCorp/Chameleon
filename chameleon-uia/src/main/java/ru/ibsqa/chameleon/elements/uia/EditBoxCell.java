package ru.ibsqa.chameleon.elements.uia;

import lombok.extern.slf4j.Slf4j;
import mmarquee.automation.AutomationException;
import mmarquee.automation.ControlType;
import mmarquee.automation.controls.EditBox;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;
import ru.ibsqa.chameleon.elements.MetaElement;
import ru.ibsqa.chameleon.definitions.repository.uia.MetaEditBoxCell;
import ru.ibsqa.chameleon.uia.driver.UiaSupportedDriver;

import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
@MetaElement(value = MetaEditBoxCell.class, supportedDriver = UiaSupportedDriver.class, priority = ConfigurationPriority.LOW)
public class EditBoxCell extends AbstractCell {

    @Override
    public void type(String value) {
        super.click();
        // найдем edit
        EditBox editBox = findElement(EditBox.class, "", ControlType.Edit);
        // установим значение
        try {
            editBox.setValue(value);
        } catch (AutomationException e) {
            log.error(e.getMessage(), e);
            fail(String.format("Ошибка при установке значения [%s] в editBox", value));
        }
    }

}
