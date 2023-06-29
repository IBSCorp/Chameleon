package ru.ibsqa.chameleon.steps;

import org.jbehave.core.annotations.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CoreUtilStorySteps extends AbstractSteps {

    @Autowired
    private CoreUtilSteps utilSteps;

    @Autowired
    private CoreVariableSteps variableSteps;

    @When("приостановлено выполнение на \"$seconds\" {секунд|секунды|секунду}")
    public void stepStopExecuted(String seconds){
        flow(()->
            utilSteps.stopExecuted(Integer.valueOf(evalVariable(seconds)))
        );
    }

    @When("приостановлено выполнение на \"$ms\" мс")
    public void stepStopExecutedMs(String ms) {
        flow(() ->
                utilSteps.stopExecutedMs(Integer.valueOf(evalVariable(ms)))
        );
    }

    @When("в переменной \"$variable\" сохранено значение \"$value\"")
    public void createVariable(String variable, String value) {
        flow(()->
                variableSteps.createVariable(variable, evalVariable(value))
        );
    }

}
