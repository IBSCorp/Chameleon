package ru.ibsqa.qualit.steps;

import ru.ibsqa.qualit.converters.Variable;
import org.jbehave.core.annotations.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WinProcessStorySteps extends AbstractSteps {

    @Autowired
    private WinProcessSteps winProcessSteps;

    @When("завершен процесс \"$process\"")
    public void killProcess(Variable process) {
        flow(()->
                winProcessSteps.killProcess(process.getValue())
        );
    }
}
