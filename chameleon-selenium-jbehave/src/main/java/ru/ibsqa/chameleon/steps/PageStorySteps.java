package ru.ibsqa.chameleon.steps;

import ru.ibsqa.chameleon.converters.Variable;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PageStorySteps extends AbstractSteps {

    @Autowired
    private PageSteps pageSteps;

    @Then("страница \"$page\" загружена")
    public void stepLoadedPage(Variable page){
        flow(()->
                pageSteps.stepLoadedPage(page.getValue())
        );
    }

    @Then("драйвер с идентификатором \"$driverId\" установлен по умолчанию")
    public void stepSelectCurrentDefaultDriverById(Variable driverId){
        flow(()->
                pageSteps.stepSelectDefaultDriverById(driverId.getValue())
        );
    }

    @When("{переключились|вернулись} к предыдущей странице")
    public void stepSwitchToPreviousPage() {
        flow(()->
                pageSteps.switchToPreviousPage()
        );
    }

    @When("в переменной \"$variable\" сохранен json текущей страницы")
    public void stepExportPageToJson(String variable) {
        flow(()->
                pageSteps.exportPageToJson(variable)
        );
    }

}
