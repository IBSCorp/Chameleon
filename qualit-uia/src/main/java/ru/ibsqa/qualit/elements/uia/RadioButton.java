package ru.ibsqa.qualit.elements.uia;

import lombok.extern.slf4j.Slf4j;
import mmarquee.automation.AutomationException;
import mmarquee.automation.controls.AutomationBase;
import mmarquee.automation.pattern.PatternNotFoundException;
import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;
import ru.ibsqa.qualit.elements.MetaElement;
import ru.ibsqa.qualit.definitions.repository.uia.MetaRadioButton;
import ru.ibsqa.qualit.uia.driver.UiaSupportedDriver;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
@MetaElement(value = MetaRadioButton.class, supportedDriver = UiaSupportedDriver.class, priority = ConfigurationPriority.LOW)
public class RadioButton extends UiaElementFacade {

    @Override
    public void sendKeys(CharSequence... keysToSend) {

        if (Boolean.valueOf(keysToSend[0].toString())) {
            this.click();
        } else {
            fail("Невозможно установить значение false у RadioButton, для этого выберите альтернативное значение");
        }

    }

    @Override
    public void click() {
        try {
            UiaElement uiaElement = getUiaElement();
            AutomationBase automationBase = uiaElement.getAutomationBase();

            if (automationBase.getClass().isAssignableFrom(mmarquee.automation.controls.RadioButton.class)) {
                mmarquee.automation.controls.RadioButton radioButton = (mmarquee.automation.controls.RadioButton) automationBase;
                try {
                    automationBase.invoke();
                } catch (PatternNotFoundException e) {
                    radioButton.select();
                }
                return;
            }
        } catch (AutomationException e) {
            log.error(e.getMessage(), e);
            fail(Objects.nonNull(e.getMessage()) ? e.getMessage() : e.toString());
        }

        super.click();
    }

    @Override
    public String getText() {
        try {
            UiaElement uiaElement = getUiaElement();
            AutomationBase automationBase = uiaElement.getAutomationBase();

            if (automationBase.getClass().isAssignableFrom(mmarquee.automation.controls.RadioButton.class)) {
                mmarquee.automation.controls.RadioButton radioButton = (mmarquee.automation.controls.RadioButton) automationBase;
                return Boolean.valueOf(radioButton.isSelected()).toString();
            }

            return uiaElement.getElement().getName();
        } catch (AutomationException e) {
            log.error(e.getMessage(), e);
            fail(Objects.nonNull(e.getMessage()) ? e.getMessage() : e.toString());
        }

        return super.getText();
    }
}
