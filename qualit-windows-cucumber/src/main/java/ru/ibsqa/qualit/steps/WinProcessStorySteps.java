package ru.ibsqa.qualit.steps;

import io.cucumber.java.ru.Тогда;
import org.springframework.beans.factory.annotation.Autowired;

public class WinProcessStorySteps extends AbstractSteps {

    @Autowired
    private WinProcessSteps winProcessSteps;

    @StepDescription(action = "Windows->Процессы->Завершение процесса", parameters = {"process - имя процесса"})
    @Тогда("^завершен процесс \"([^\"]*)\"$")
    public void killProcess(String process) {
        flow(() ->
                winProcessSteps.killProcess(process)
        );
    }
}
