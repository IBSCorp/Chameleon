package ru.ibsqa.qualit.steps;

import ru.ibsqa.qualit.steps.roles.Value;
import io.cucumber.java.ru.Когда;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class DebugStorySteps extends AbstractSteps {

    @Autowired
    private DebugSteps debugSteps;

    @StepDescription(action = "Переменные->Вывести значение переменной"
            , parameters = {"variable - переменная"})
    @Когда("^DEBUG значение \"([^\"]*)\" вывести в лог$")
    public void evalToLog(
            @Value String variable
    ) {
        flow(()->
                debugSteps.evalToLog(variable)
        );
    }

}

