package ru.ibsqa.qualit.steps;

import org.jbehave.core.annotations.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DebugStorySteps extends AbstractSteps {

    @Autowired
    private DebugSteps debugSteps;

    @When("DEBUG значение \"$variable\" вывести в лог")
    public void evalToLog(String variable) {
        flow(()->
                debugSteps.evalToLog(variable)
        );
    }

}

