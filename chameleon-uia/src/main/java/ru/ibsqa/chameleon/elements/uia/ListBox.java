package ru.ibsqa.chameleon.elements.uia;

import lombok.extern.slf4j.Slf4j;
import mmarquee.automation.AutomationException;
import mmarquee.automation.ControlType;
import mmarquee.automation.controls.ListItem;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;
import ru.ibsqa.chameleon.elements.MetaElement;
import ru.ibsqa.chameleon.definitions.repository.uia.MetaListBox;
import ru.ibsqa.chameleon.uia.driver.UiaSupportedDriver;

import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
@MetaElement(value = MetaListBox.class, supportedDriver = UiaSupportedDriver.class, priority = ConfigurationPriority.LOW)
public class ListBox extends ComboBox {

    @Override
    public void type(String value) {
        super.click();
        // найдем выпадающий список
        ListItem listItem = findElement(ListItem.class, value, ControlType.ListItem);
        // кликнем на пункт списка
        try {
            listItem.click();
        } catch (AutomationException e) {
            fail(String.format("Ошибка при выборе пункта [%s]", value));
        }
    }
}
