package ru.ibsqa.qualit.elements.uia;

import lombok.extern.slf4j.Slf4j;
import mmarquee.automation.AutomationException;
import mmarquee.automation.controls.AutomationBase;
import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;
import ru.ibsqa.qualit.elements.MetaElement;
import ru.ibsqa.qualit.definitions.repository.uia.MetaButton;
import ru.ibsqa.qualit.uia.driver.UiaSupportedDriver;

@Slf4j
@MetaElement(value = MetaButton.class, supportedDriver = UiaSupportedDriver.class, priority = ConfigurationPriority.LOW)
public class Button extends UiaElementFacade {

    @Override
    public void click() {
        try {
            UiaElement uiaElement = getUiaElement();
            AutomationBase automationBase = uiaElement.getAutomationBase();

            if (automationBase.getClass().isAssignableFrom(mmarquee.automation.controls.Button.class)) {
                mmarquee.automation.controls.Button button = (mmarquee.automation.controls.Button) automationBase;
                if (button.isEnabled()) {
                    button.click();
                } else {
                    log.warn(String.format("Кнопка [%s] не может быть нажата", getElementName()));
                }
                return;
            }
        } catch (AutomationException e) {
            log.warn(String.format("Ошибка при нажатии на кнопку [%s]", getElementName()), e);
            // Если не удалось кликнуть через UIAutomation, то кликнем роботом (как реализовано в базовом классе)
        }

        super.click();
    }
}
