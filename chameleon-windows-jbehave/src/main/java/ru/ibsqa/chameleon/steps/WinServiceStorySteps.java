package ru.ibsqa.chameleon.steps;

import ru.ibsqa.chameleon.converters.Variable;
import org.jbehave.core.annotations.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WinServiceStorySteps extends AbstractSteps {

    @Autowired
    private WinServiceSteps winServiceSteps;

    @When("запущена служба \"$serviceName\"")
    public void startServiceStep(Variable serviceName) {
        flow(()->
                winServiceSteps.startServiceStep(serviceName.getValue())
        );

    }

    @When("остановлена служба \"$serviceName\"")
    public void stopServiceStep(Variable serviceName) {
        flow(()->
                winServiceSteps.stopServiceStep(serviceName.getValue())
        );
    }

}
