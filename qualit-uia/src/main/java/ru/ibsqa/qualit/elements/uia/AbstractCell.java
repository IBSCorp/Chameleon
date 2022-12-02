package ru.ibsqa.qualit.elements.uia;

import lombok.extern.slf4j.Slf4j;
import mmarquee.automation.AutomationException;
import mmarquee.automation.controls.DataGridCell;
import mmarquee.automation.pattern.PatternNotFoundException;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
public class AbstractCell extends UiaElementFacade {

    @Override
    public String getText() {
        try {
            DataGridCell cell = this.getUiaElement().getCell();
            if (Objects.nonNull(cell)) {
                return cell.getValue();
            }
        } catch (PatternNotFoundException e) {
            return "";
        } catch (AutomationException e) {
            log.error(e.getMessage(), e);
            fail(Objects.nonNull(e.getMessage()) ? e.getMessage() : e.toString());
        }
        return super.getText();
    }
}
