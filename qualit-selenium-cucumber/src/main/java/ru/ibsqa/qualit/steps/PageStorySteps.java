package ru.ibsqa.qualit.steps;

import ru.ibsqa.qualit.context.Context;
import ru.ibsqa.qualit.context.ContextChange;
import ru.ibsqa.qualit.context.ContextType;
import ru.ibsqa.qualit.steps.roles.Driver;
import ru.ibsqa.qualit.steps.roles.Page;
import ru.ibsqa.qualit.steps.roles.Value;
import ru.ibsqa.qualit.steps.roles.Variable;
import io.cucumber.java.ru.Дано;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Тогда;
import org.springframework.beans.factory.annotation.Autowired;

public class PageStorySteps extends AbstractSteps {

    @Autowired
    private PageSteps pageSteps;

    @StepDescription(action = "UI->Драйвер->Установить драйвер по умолчанию"
            , parameters = {"driverId - идентификатор драйвера"}
            , expertView = true)
    @Тогда("^драйвер с идентификатором \"([^\"]*)\" установлен по умолчанию")
    public void stepSelectCurrentDefaultDriverById(
            @Driver String driverId
    ) {
        flow(()->
                pageSteps.stepSelectDefaultDriverById(driverId)
        );
    }

    @StepDescription(action = "UI->Страницы->Страница загружена"
            , parameters = {"page - наименование страницы, для которой будет выполнена проверка"})
    @Тогда("^страница \"([^\"]*)\" загружена")
    @Context(type = ContextType.PAGE, change = ContextChange.AFTER, parameter = "page")
    public void stepLoadedPage(
            @Page String page
    ) {
        flow(()->
                pageSteps.stepLoadedPage(page)
        );
    }

    @StepDescription(action = "UI->Страницы->Переключиться к предыдущей странице")
    @Когда("^переключились к предыдущей странице|вернулись к предыдущей странице")
    @Context(type = ContextType.PAGE, change = ContextChange.AFTER, previous = true)
    public void stepSwitchToPreviousPage() {
        flow(()->
                pageSteps.switchToPreviousPage()
        );
    }

    @StepDescription(action = "UI->Страницы->Ждать окончание загрузки страницы"
            , parameters = {"page - наименование страницы, для которой будет выполнена проверка"})
    @Тогда("^ожидается окончание загрузки страницы$")
    public void stepWaitLoadedPage(){
        flow(()->
                pageSteps.currentPageShouldBeLoaded()
        );
    }

    @StepDescription(action = "UI->Страницы->Сохранить JSON страницы"
            , parameters = {"variable - наименование переменной, в которую будет сохранена страница"}
            , expertView = true)
    @Тогда("^в переменной \"([^\"]*)\" сохранен json текущей страницы$")
    @Context(change = ContextChange.NONE, type = ContextType.NONE, variables = {"variable"})
    public void stepExportPageToJson(
            @Variable String variable
    ) {
        flow(()->
                pageSteps.exportPageToJson(variable)
        );
    }

}