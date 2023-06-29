package ru.ibsqa.chameleon.sap.steps;

import io.cucumber.java.ru.Когда;
import org.springframework.beans.factory.annotation.Autowired;
import ru.ibsqa.chameleon.sap.driver.SapDriver;
import ru.ibsqa.chameleon.selenium.driver.IDriverManager;
import ru.ibsqa.chameleon.steps.AbstractSteps;
import ru.ibsqa.chameleon.steps.StepDescription;

public class SessionStorySteps extends AbstractSteps {

    @Autowired
    private IDriverManager driverManager;

    @StepDescription(
            action = "SAP Automation->Переключение к сессии",
            parameters = {"sessionName - наименование сессии"}
    )
    @Когда("^выполнено переключение к сессии \"(.*)\"$")
    public void stepConnectToSession(String sessionName) {
        flow(() -> {
            ((SapDriver)driverManager.getLastDriver().getWrappedDriver()).switchToSession(sessionName);
        });
    }

}
