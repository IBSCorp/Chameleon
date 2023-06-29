package ru.ibsqa.chameleon.elements.uia;

import lombok.extern.slf4j.Slf4j;
import mmarquee.automation.AutomationException;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;
import ru.ibsqa.chameleon.elements.MetaElement;
import ru.ibsqa.chameleon.definitions.repository.uia.MetaTextBox;
import ru.ibsqa.chameleon.uia.driver.UiaSupportedDriver;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
@MetaElement(value = MetaTextBox.class, supportedDriver = UiaSupportedDriver.class, priority = ConfigurationPriority.LOW)
public class TextBox extends UiaElementFacade {

    @Override
    public void sendKeys(CharSequence... keysToSend) {
        fail(String.format("TextBox[%s] не поддерживает присваивание, это нередактируемый текст", getElementName()));
    }

    @Override
    public String getText() {
        try {
            UiaElement uiaElement = getUiaElement();

            if (uiaElement.getAutomationBase().getClass().isAssignableFrom(mmarquee.automation.controls.TextBox.class)) {
                mmarquee.automation.controls.TextBox textBox = ((mmarquee.automation.controls.TextBox) uiaElement.getAutomationBase());
                String value = textBox.getValueFromIAccessible();
                return (Objects.isNull(value) || value.equals("<Empty>")) ? textBox.getName() : value;
            }
        } catch (AutomationException e) {
            log.error(e.getMessage(), e);
            fail(Objects.nonNull(e.getMessage()) ? e.getMessage() : e.toString());
        }

        return super.getText();
    }
}
