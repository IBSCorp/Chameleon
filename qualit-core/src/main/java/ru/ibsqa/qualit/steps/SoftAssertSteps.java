package ru.ibsqa.qualit.steps;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ibsqa.qualit.asserts.IAssertManager;

@Component
@Slf4j
public class SoftAssertSteps extends AbstractSteps {

    @Autowired
    protected IAssertManager assertManager;

    public void softAssertOn() {
        assertManager.softAssertOn();
    }

    public void softAssertOff() {
        assertManager.softAssertOff();
    }

}
