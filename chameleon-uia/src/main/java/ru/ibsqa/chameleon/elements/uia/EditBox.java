package ru.ibsqa.chameleon.elements.uia;

import lombok.extern.slf4j.Slf4j;
import mmarquee.automation.AutomationException;
import mmarquee.automation.ControlType;
import mmarquee.automation.Element;
import mmarquee.automation.ElementNotFoundException;
import mmarquee.automation.controls.AutomationBase;
import mmarquee.automation.controls.ElementBuilder;
import mmarquee.automation.controls.Panel;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;
import ru.ibsqa.chameleon.elements.MetaElement;
import ru.ibsqa.chameleon.definitions.repository.uia.MetaEditBox;
import ru.ibsqa.chameleon.uia.driver.UiaSupportedDriver;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
@MetaElement(value = MetaEditBox.class, supportedDriver = UiaSupportedDriver.class, priority = ConfigurationPriority.LOW)
public class EditBox extends UiaElementFacade {

    @Override
    public void sendKeys(CharSequence... keysToSend) {
        try {
            UiaElement uiaElement = getUiaElement();
            AutomationBase automationBase = uiaElement.getAutomationBase();
            Element element = uiaElement.getElement();

            if (automationBase.getClass().isAssignableFrom(Panel.class)) {
                String name = element.getName();
                element.setFocus();
                uiaElement.click();
                getRobot().delay(100);
                Element ae = ((Panel) automationBase).getControlByControlType(name, ControlType.Edit).getElement();
                mmarquee.automation.controls.EditBox editBox = new mmarquee.automation.controls.EditBox(new ElementBuilder(ae));
                editBox.setValue(keysToSend[0].toString());
                getRobot().delay(100);
                editBox.getElement().setFocus();
                return;
            } else if (automationBase.getClass().isAssignableFrom(mmarquee.automation.controls.EditBox.class)) {
                mmarquee.automation.controls.EditBox editBox = (mmarquee.automation.controls.EditBox) automationBase;
                editBox.setValue(keysToSend[0].toString());
                return;
            }
        } catch (AutomationException e) {
            log.error(e.getMessage(), e);
            fail(Objects.nonNull(e.getMessage()) ? e.getMessage() : e.toString());
        }

        super.
                sendKeys(keysToSend);
    }

    @Override
    public String getText() {
        try {
            UiaElement uiaElement = getUiaElement();
            AutomationBase automationBase = uiaElement.getAutomationBase();
            Element element = uiaElement.getElement();

            if (automationBase.getClass().isAssignableFrom(Panel.class)) {
                String name = element.getName();
                element.setFocus();
                getRobot().delay(100);
                Element ae;
                try {
                    ae = ((Panel) automationBase).getControlByControlType(name, ControlType.Edit).getElement();
                } catch (ElementNotFoundException e) {
                    ae = ((Panel) automationBase).getControlByControlType("", ControlType.Edit).getElement();
                }
                mmarquee.automation.controls.EditBox editBox = new mmarquee.automation.controls.EditBox(new ElementBuilder(ae));
                return editBox.getValue();
            } else if (automationBase.getClass().isAssignableFrom(mmarquee.automation.controls.EditBox.class)) {
                mmarquee.automation.controls.EditBox editBox = (mmarquee.automation.controls.EditBox) automationBase;
                return editBox.getValue();
            }

        } catch (AutomationException e) {
            log.error(e.getMessage(), e);
            fail(Objects.nonNull(e.getMessage()) ? e.getMessage() : e.toString());
        }

        return super.getText();
    }

}
