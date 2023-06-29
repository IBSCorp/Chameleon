package ru.ibsqa.chameleon.steps;

import org.openqa.selenium.Alert;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ibsqa.chameleon.selenium.driver.IDriverManager;
import ru.ibsqa.chameleon.selenium.driver.WebDriverFacade;

import java.time.Duration;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.fail;

@Component
public class AlertSteps extends AbstractSteps {

    @Autowired
    private IDriverManager driverManager;

    @UIStep
    @TestStep("присутствует alert")
    public void stepAlertIsPresent() {
        Alert alert = getAlert();
        if (Objects.isNull(alert)) {
            fail(message("alertIsNotPresent"));
        }
    }

    @UIStep
    @TestStep("присутствует alert с текстом \"${text}\"")
    public void stepAlertIsPresentWithText(String text) {
        Alert alert = getAlert();
        if (Objects.isNull(alert)) {
            fail(message("alertIsNotPresent"));
        }
        if (!(""+text).equals(alert.getText())) {
            fail(message("alertWithTextIsNotPresent", text));
        }
    }

    @UIStep
    @TestStep("присутствует alert, содержащий текст \"${text}\"")
    public void stepAlertIsPresentWithSubstring(String text) {
        Alert alert = getAlert();
        if (Objects.isNull(alert)) {
            fail(message("alertIsNotPresent"));
        }
        if (!(""+text).equals(alert.getText())) {
            fail(message("alertWithSubstringIsNotPresent", text));
        }
    }

    @UIStep
    @TestStep("отсутствует alert")
    public void stepAlertIsNotPresent() {
        Alert alert = getAlert();
        if (Objects.nonNull(alert)) {
            fail(message("alertIsPresent"));
        }
    }

    @UIStep
    @TestStep("отменён alert")
    public void stepAlertDismiss() {
        Alert alert = getAlert();
        if (Objects.isNull(alert)) {
            fail(message("alertIsNotPresent"));
        }
        alert.dismiss();
    }

    @UIStep
    @TestStep("подтверждён alert")
    public void stepAlertAccept() {
        Alert alert = getAlert();
        if (Objects.isNull(alert)) {
            fail(message("alertIsNotPresent"));
        }
        alert.accept();
    }

    @UIStep
    @TestStep("в alert введен текст \"${text}\"")
    public void stepAlertSendKeys(String text) {
        Alert alert = getAlert();
        if (Objects.isNull(alert)) {
            fail(message("alertIsNotPresent"));
        }
        alert.sendKeys(text);
    }

    public Alert getAlert() {
        WebDriverFacade driver = driverManager.getLastDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(driver.getDefaultWaitTimeOut()));
        try {
            return wait.until(ExpectedConditions.alertIsPresent());
        } catch (TimeoutException e) {
            return null;
        }
    }
}
