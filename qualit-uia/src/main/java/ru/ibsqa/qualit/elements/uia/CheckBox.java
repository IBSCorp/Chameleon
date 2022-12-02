package ru.ibsqa.qualit.elements.uia;

import lombok.extern.slf4j.Slf4j;
import mmarquee.automation.AutomationException;
import mmarquee.automation.controls.AutomationBase;
import mmarquee.uiautomation.ToggleState;
import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;
import ru.ibsqa.qualit.elements.MetaElement;
import ru.ibsqa.qualit.definitions.repository.uia.MetaCheckBox;
import ru.ibsqa.qualit.uia.driver.UiaSupportedDriver;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
@MetaElement(value = MetaCheckBox.class, supportedDriver = UiaSupportedDriver.class, priority = ConfigurationPriority.LOW)
public class CheckBox extends UiaElementFacade {

    @Override
    public void sendKeys(CharSequence... keysToSend) {
        try {
            UiaElement uiaElement = getUiaElement();
            AutomationBase automationBase = uiaElement.getAutomationBase();

            if (automationBase.getClass().isAssignableFrom(mmarquee.automation.controls.CheckBox.class)) {
                mmarquee.automation.controls.CheckBox checkBox = (mmarquee.automation.controls.CheckBox) automationBase;
                if (Boolean.valueOf(keysToSend[0].toString()) != (ToggleState.ON == checkBox.getToggleState())) {
                    checkBox.toggle();
                }
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
            AutomationBase automationBase = uiaElement.getAutomationBase();

            if (automationBase.getClass().isAssignableFrom(mmarquee.automation.controls.CheckBox.class)) {
                mmarquee.automation.controls.CheckBox checkBox = (mmarquee.automation.controls.CheckBox) automationBase;
                return Boolean.valueOf(ToggleState.ON == checkBox.getToggleState()).toString();
            }

            return uiaElement.getElement().getName();
        } catch (AutomationException e) {
            log.error(e.getMessage(), e);
            fail(Objects.nonNull(e.getMessage()) ? e.getMessage() : e.toString());
        }

        return super.getText();
    }

}
