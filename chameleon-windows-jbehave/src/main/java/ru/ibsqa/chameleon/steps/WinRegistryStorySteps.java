package ru.ibsqa.chameleon.steps;

import org.jbehave.core.annotations.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WinRegistryStorySteps extends AbstractSteps {

    @Autowired
    private WinRegistrySteps winRegistrySteps;

    @When("в переменную \"$variable\" сохранено значение \"$path\" из реестра windows")
    public void registryGetValue(String variable, String path) {
        flow(()->
                winRegistrySteps.registryGetValue(variable,evalVariable(path))
        );
    }

    @When("в реестре windows создан раздел \"$path\"")
    public void registryCreateKey(String path) {
        flow(()->
                winRegistrySteps.registryCreateKey(evalVariable(path))
        );
    }

    @When("значение \"$path\" в реестре windows заполнено строкой \"$value\"")
    public void registrySetStringValue(String path, String value) {
        flow(()->
                winRegistrySteps.registrySetStringValue(evalVariable(path),evalVariable(value))
        );
    }

    @When("значение \"$path\" в реестре windows заполнено числом \"$value\"")
    public void registrySetIntValue(String path, String value) {
        flow(()->
                winRegistrySteps.registrySetIntValue(evalVariable(path),evalVariable(value))
        );
    }

    @When("значение \"$path\" в реестре windows заполнено большим числом \"$value\"")
    public void registrySetLongValue(String path, String value) {
        flow(()->
                winRegistrySteps.registrySetLongValue(evalVariable(path),evalVariable(value))
        );
    }

}
