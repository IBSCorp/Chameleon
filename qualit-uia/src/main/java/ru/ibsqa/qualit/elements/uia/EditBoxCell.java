package ru.ibsqa.qualit.elements.uia;

import lombok.extern.slf4j.Slf4j;
import mmarquee.automation.AutomationException;
import mmarquee.automation.ControlType;
import mmarquee.automation.controls.EditBox;
import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;
import ru.ibsqa.qualit.elements.MetaElement;
import ru.ibsqa.qualit.definitions.repository.uia.MetaEditBoxCell;
import ru.ibsqa.qualit.uia.driver.UiaSupportedDriver;

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
