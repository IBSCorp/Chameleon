package ru.ibsqa.qualit.elements.uia;

import lombok.extern.slf4j.Slf4j;
import mmarquee.automation.AutomationException;
import mmarquee.automation.controls.AutomationBase;
import mmarquee.automation.controls.EditBox;
import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;
import ru.ibsqa.qualit.elements.MetaElement;
import ru.ibsqa.qualit.definitions.repository.uia.MetaComboBox;
import ru.ibsqa.qualit.uia.driver.UiaSupportedDriver;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
@MetaElement(value = MetaComboBox.class, supportedDriver = UiaSupportedDriver.class, priority = ConfigurationPriority.LOW)
public class ComboBox extends UiaElementFacade {

    @Override
    public void sendKeys(CharSequence... keysToSend) {
        try {
            UiaElement uiaElement = getUiaElement();
            AutomationBase automationBase = uiaElement.getAutomationBase();

            if (automationBase.getClass().isAssignableFrom(mmarquee.automation.controls.ComboBox.class)) {
                ((mmarquee.automation.controls.ComboBox) automationBase).setText(keysToSend[0].toString());
                getRobot().delay(100);
                return;
            }
        } catch (AutomationException e) {
            log.error(e.getMessage(), e);
            fail(Objects.nonNull(e.getMessage()) ? e.getMessage() : e.toString());
        }

        super.sendKeys(keysToSend);
    }

    @Override
    public String getText() {
        try {
            UiaElement uiaElement = getUiaElement();

            if (uiaElement.getAutomationBase().getClass().isAssignableFrom(mmarquee.automation.controls.ComboBox.class)) {
                return ((mmarquee.automation.controls.ComboBox) uiaElement.getAutomationBase()).getValue();
            } else if (uiaElement.getAutomationBase().getClass().isAssignableFrom(EditBox.class)) {
                return ((EditBox) uiaElement.getAutomationBase()).getValue();
            }
        } catch (AutomationException e) {
            log.error(e.getMessage(), e);
            fail(Objects.nonNull(e.getMessage()) ? e.getMessage() : e.toString());
        }

        return super.getText();
    }
}
