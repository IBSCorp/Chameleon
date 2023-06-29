package ru.ibsqa.chameleon.steps;

import io.cucumber.java.ru.Тогда;
import org.springframework.beans.factory.annotation.Autowired;
import ru.ibsqa.chameleon.context.Context;
import ru.ibsqa.chameleon.context.ContextChange;
import ru.ibsqa.chameleon.element.ElementTypeNone;

public class WinRegistryStorySteps extends AbstractSteps {

    @Autowired
    private WinRegistrySteps winRegistrySteps;

    @StepDescription(action = "Windows->Реестр->Сохранение значения из реестра в переменной",
            parameters = {"variable - наименование переменной, в которую будет сохранено значение",
                    "path - путь в реестре windows"})
    @Context(change = ContextChange.NONE, type = ElementTypeNone.class, variables = {"variable"})
    @Тогда("^в переменную \"([^\"]*)\" сохранено значение \"([^\"]*)\" из реестра windows$")
    public void registryGetValue(String variable, String path) {
        flow(() ->
                winRegistrySteps.registryGetValue(variable, path)
        );
    }

    @StepDescription(action = "Windows->Реестр->Создание в реестре раздела",
            parameters = {"path - путь в реестре windows"})
    @Тогда("^в реестре windows создан раздел \"([^\"]*)\"$")
    public void registryCreateKey(String path) {
        flow(() ->
                winRegistrySteps.registryCreateKey(path)
        );
    }

    @StepDescription(action = "Windows->Реестр->Запись в реестр строки",
            parameters = {"path - путь в реестре windows", "value - строковое значение"})
    @Тогда("^значение \"([^\"]*)\" в реестре windows заполнено строкой \"([^\"]*)\"$")
    public void registrySetStringValue(String path, String value) {
        flow(() ->
                winRegistrySteps.registrySetStringValue(path, value)
        );
    }

    @StepDescription(action = "Windows->Реестр->Запись в реестр числа",
            parameters = {"path - путь в реестре windows", "value - числовое значение"})
    @Тогда("^значение \"([^\"]*)\" в реестре windows заполнено числом \"([^\"]*)\"$")
    public void registrySetIntValue(String path, String value) {
        flow(() ->
                winRegistrySteps.registrySetIntValue(path, value)
        );
    }

    @StepDescription(action = "Windows->Реестр->Запись в реестр большого числа",
            parameters = {"path - путь в реестре windows", "value - числовое значение"})
    @Тогда("^значение \"([^\"]*)\" в реестре windows заполнено большим числом \"([^\"]*)\"$")
    public void registrySetLongValue(String path, String value) {
        flow(() ->
                winRegistrySteps.registrySetLongValue(path, value)
        );
    }

}
