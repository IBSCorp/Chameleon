package ru.ibsqa.chameleon.elements.uia;

import mmarquee.automation.AutomationException;
import mmarquee.automation.controls.AutomationBase;
import mmarquee.automation.controls.Tab;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;
import ru.ibsqa.chameleon.elements.MetaElement;
import ru.ibsqa.chameleon.definitions.repository.uia.MetaRibbonTabs;
import ru.ibsqa.chameleon.uia.driver.UiaSupportedDriver;

@MetaElement(value = MetaRibbonTabs.class, supportedDriver = UiaSupportedDriver.class, priority = ConfigurationPriority.LOW)
public class RibbonTabs extends AbstractTabs {

    @Override
    public void type(String value) {
        UiaElement element = this.getUiaElement();
        AutomationBase base = element.getAutomationBase();
        if (base instanceof Tab) {
            Tab tab = (Tab) base;
            try {
                tab.selectTabPage(value);
            } catch (AutomationException e) {
                e.printStackTrace();
                super.type(value);
            }
        } else {
            super.type(value);
        }
    }

}
