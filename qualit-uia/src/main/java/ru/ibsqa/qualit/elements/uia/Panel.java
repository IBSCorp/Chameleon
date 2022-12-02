package ru.ibsqa.qualit.elements.uia;

import lombok.extern.slf4j.Slf4j;
import mmarquee.automation.AutomationException;
import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;
import ru.ibsqa.qualit.elements.MetaElement;
import ru.ibsqa.qualit.definitions.repository.uia.MetaPanel;
import ru.ibsqa.qualit.uia.driver.UiaSupportedDriver;

@Slf4j
@MetaElement(value = MetaPanel.class, supportedDriver = UiaSupportedDriver.class, priority = ConfigurationPriority.LOW)
public class Panel extends UiaElementFacade {

    @Override
    public String getText() {
        UiaElement uiaElement = getUiaElement();
        try {
            return uiaElement.getAutomationBase().getName();
        } catch (AutomationException e) {
            log.error(e.getMessage(), e);
        }
        return super.getText();
    }

}
