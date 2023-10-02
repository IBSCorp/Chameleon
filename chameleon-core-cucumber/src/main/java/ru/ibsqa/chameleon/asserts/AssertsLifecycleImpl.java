package ru.ibsqa.chameleon.asserts;

import io.cucumber.java.Scenario;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AssertsLifecycleImpl implements IAssertsLifecycle {

    @Autowired
    private IAssertManager assertManager;

    @Autowired
    private IThrowableMixer throwableMixer;

    @Override
    public void beforeScenario(Scenario scenario) {
        assertManager.openLayer();
    }

    @Override
    public void afterScenario(Scenario scenario) {
        AssertLayer assertLayer = assertManager.closeLayer();
        // Если в процессе выполнения были обработаны и собранны SoftAssert-ошибки,
        // то сгенерировать исключение на основе первого из них
        throwableMixer.mixThrowableFromList(assertLayer.getErrors())
                .ifPresent((throwable) -> Assertions.fail(throwable.getMessage(), throwable));
    }
}
