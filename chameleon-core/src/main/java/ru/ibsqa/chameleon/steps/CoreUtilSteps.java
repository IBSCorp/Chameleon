package ru.ibsqa.chameleon.steps;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import static org.junit.jupiter.api.Assertions.fail;

@Component
@Primary
@Slf4j
public class CoreUtilSteps extends AbstractSteps {

    @TestStep("приостановлено выполнение на \"${seconds}\" сек")
    public void stopExecuted(int seconds){
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }

    @TestStep("приостановлено выполнение на \"${ms}\" мс")
    public void stopExecutedMs(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }

    @TestStep("шаг пропущен")
    public void stepIgnore() {}

    public Double parseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            log.error(e.getMessage(), e);
            fail(message("numberFormatErrorMessage", value));
        }
        return null;
    }
}