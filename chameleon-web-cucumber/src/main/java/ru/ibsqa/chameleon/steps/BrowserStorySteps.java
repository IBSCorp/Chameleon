package ru.ibsqa.chameleon.steps;

import io.cucumber.java.ru.Дано;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Тогда;
import org.springframework.beans.factory.annotation.Autowired;
import ru.ibsqa.chameleon.context.Context;
import ru.ibsqa.chameleon.context.ContextChange;
import ru.ibsqa.chameleon.element.ElementTypePage;
import ru.ibsqa.chameleon.steps.roles.Driver;
import ru.ibsqa.chameleon.steps.roles.Page;
import ru.ibsqa.chameleon.steps.roles.Value;

public class BrowserStorySteps extends AbstractSteps {

    @Autowired
    private PageSteps pageSteps;

    @Autowired
    private BrowserSteps browserSteps;

    @StepDescription(action = "UI->Браузер->Открыть URL",
            subAction = "Открыть URL",
            parameters = {"url - url, который будет открыт в браузере"})
    @Дано("^открыта страница по адресу \"([^\"]*)\"")
    public void stepOpenUrl(
            @Value String url
    ) {
        flow(() ->
                browserSteps.stepOpenUrl(url)
        );
    }

    @StepDescription(action = "UI->Браузер->Открыть URL"
            , subAction = "Открыть URL с указанием драйвера"
            , parameters = {"driver - драйвер, которым будет открыт URL", "url - url, который будет открыт в браузере"}
            , expertView = true)
    @UIStep
    @Дано("^драйвером \"([^\"]*)\" открыта страница по адресу \"([^\"]*)\"")
    public void stepOpenUrl(
            @Driver String driver,
            @Value String url
    ) {
        flow(() ->
                browserSteps.stepOpenUrl(driver, url)
        );
    }

    @StepDescription(action = "UI->Страницы->Перейти к окну по заголовку"
            , parameters = {"value - заголовок окна браузера (регулярное выражение)"}
            , expertView = true)
    @Когда("^выполнен переход к окну \"([^\"]*)\"$")
    public void stepSwitchToWindow(
            @Value String value
    ) {
        flow(() ->
                browserSteps.switchToWindow(value)
        );
    }

    @StepDescription(action = "UI->Страницы->Перейти к окну по заголовку и переключиться на страницу"
            , parameters = {"value - заголовок окна браузера (регулярное выражение)",
            "page - наименование страницы, для которой будет выполнена проверка"}
            , expertView = true)
    @Когда("^выполнен переход к окну \"([^\"]*)\" и загружена страница \"([^\"]*)\"$")
    @Context(change = ContextChange.AFTER, type = ElementTypePage.class, parameter = "page")
    public void stepSwitchToWindowAndPageLoaded(
            @Value String value, @Page String page
    ) {
        flow(() ->
                browserSteps.switchToWindowAndPageLoaded(value, page)
        );
    }

    @StepDescription(action = "UI->Страницы->Закрыть текущее окно")
    @Когда("^закрыто текущее окно$")
    public void stepCloseWindow() {
        flow(() ->
                browserSteps.closeWindow()
        );
    }

    @StepDescription(action = "UI->Браузер->Проверить URL"
            , parameters = {"value - проверяемый url (регулярное выражение)",
            "page - наименование страницы, для которой будет выполнена проверка"})
    @Тогда("^адрес окна соответствует маске \"([^\"]*)\"$")
    public void stepCheckWindowUrl(
            @Value String value
    ) {
        flow(() ->
                browserSteps.checkWindowUrl(value)
        );
    }

    @StepDescription(action = "UI->Страницы->Обновить страницу"
            , subAction = "Обновить и ждать окончания загрузки"
            , expertView = true)
    @Когда("^обновлена страница и ожидается окончание загрузки$")
    public void stepRefreshAndPageLoaded() {
        flow(() ->
                browserSteps.refreshAndPageLoaded()
        );
    }

    @StepDescription(action = "UI->Страницы->Обновить страницу"
            , subAction = "Обновить страницу")
    @Когда("^обновлена страница$")
    public void stepRefreshPage() {
        flow(() ->
                browserSteps.refreshPage()
        );
    }

    @StepDescription(action = "UI->Страницы->Переключиться к фрейму"
            , parameters = {"framePath - последовательность фреймов разделенная знаком > (допускается указывать номер, id, наименование или селектор фрейма), " +
            ". - без смены фрейма, .. - вышестоящий фрейм"}, expertView = true)
    @Когда("^выполнен переход к фрейму \"([^\"]*)\"$")
    public void stepSwitchToFrameById(
            @Value String framePath
    ) {
        flow(() ->
                browserSteps.switchToFrameById(framePath)
        );
    }

    @StepDescription(action = "UI->Страницы->Переключиться к содержимому верхнего уровня (выход из фреймов)"
            , expertView = true)
    @Когда("^выполнен переход к фрейму по умолчанию$")
    public void stepSwitchToDefaultFrame() {
        flow(() ->
                browserSteps.switchToDefaultFrame()
        );
    }

}