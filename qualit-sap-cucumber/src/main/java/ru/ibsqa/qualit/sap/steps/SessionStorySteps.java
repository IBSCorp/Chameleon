package ru.ibsqa.qualit.sap.steps;

import io.cucumber.java.ru.Когда;
import org.springframework.beans.factory.annotation.Autowired;
import ru.ibsqa.qualit.sap.driver.SapDriver;
import ru.ibsqa.qualit.selenium.driver.IDriverManager;
import ru.ibsqa.qualit.steps.AbstractSteps;
import ru.ibsqa.qualit.steps.StepDescription;

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
