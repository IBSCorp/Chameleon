package ru.ibsqa.qualit.steps;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ibsqa.qualit.converters.Variable;

@Component
public class BrowserStorySteps extends AbstractSteps {

    @Autowired
    private PageSteps pageSteps;

    @Autowired
    private BrowserSteps browserSteps;

    @Given("открыта страница по адресу \"$url\"")
    public void stepOpenUrl(Variable url){
        flow(()->
                browserSteps.stepOpenUrl(url.getValue())
        );
    }

    @Given("драйвером \"$driver\" открыта страница по адресу \"$url\"")
    public void stepOpenUrl(Variable driver, Variable url){
        flow(()->
                browserSteps.stepOpenUrl(driver.getValue(), url.getValue())
        );
    }

    @When("выполнен переход к окну \"$value\"")
    public void stepSwitchToWindow(Variable value) {
        flow(() ->
                browserSteps.switchToWindow(value.getValue())
        );
    }

    @When("выполнен переход к окну \"$value\" и загружена страница \"([\"]*)\"")
    public void stepSwitchToWindowAndPageLoaded(Variable value, Variable page) {
        flow(() ->
                browserSteps.switchToWindowAndPageLoaded(value.getValue(), page.getValue())
        );
    }

    @When("закрыто текущее окно")
    public void stepCloseWindow() {
        flow(() ->
                browserSteps.closeWindow()
        );
    }

    @Then("адрес окна соответствует маске \"$value\"")
    public void stepCheckWindowUrl(Variable value) {
        flow(() ->
                browserSteps.checkWindowUrl(value.getValue())
        );
    }

    @When("обновлена страница и ожидается окончание загрузки")
    public void stepRefreshAndPageLoaded() {
        flow(() ->
                browserSteps.refreshAndPageLoaded()
        );
    }

    @When("обновлена страница")
    public void stepRefreshPage() {
        flow(() ->
                browserSteps.refreshPage()
        );
    }

    @When("выполнен переход к фрейму \"$value\"")
    public void stepSwitchToFrameById(Variable framePath) {
        flow(() ->
                browserSteps.switchToFrameById(framePath.getValue())
        );
    }

    @When("выполнен переход к фрейму по умолчанию")
    public void stepSwitchToDefaultFrame() {
        flow(() ->
                browserSteps.switchToDefaultFrame()
        );
    }
}
