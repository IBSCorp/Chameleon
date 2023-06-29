package ru.ibsqa.chameleon.steps;

import io.cucumber.java.ru.Тогда;
import org.springframework.beans.factory.annotation.Autowired;

public class WinServiceStorySteps extends AbstractSteps {

    @Autowired
    private WinServiceSteps winServiceSteps;

    @StepDescription(action = "Windows->Службы->Запуск службы", parameters = {"serviceName - имя службы"})
    @Тогда("^запущена служба \"([^\"]*)\"$")
    public void startServiceStep(String serviceName) {
        flow(() ->
                winServiceSteps.startServiceStep(serviceName)
        );

    }

    @StepDescription(action = "Windows->Службы->Остановка службы", parameters = {"serviceName - имя службы"})
    @Тогда("^остановлена служба \"([^\"]*)\"$")
    public void stopServiceStep(String serviceName) {
        flow(() ->
                winServiceSteps.stopServiceStep(serviceName)
        );
    }

}
