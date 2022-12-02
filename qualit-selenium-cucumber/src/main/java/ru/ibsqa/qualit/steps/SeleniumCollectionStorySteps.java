package ru.ibsqa.qualit.steps;

import ru.ibsqa.qualit.context.Context;
import ru.ibsqa.qualit.context.ContextChange;
import ru.ibsqa.qualit.context.ContextType;
import ru.ibsqa.qualit.steps.roles.Collection;
import ru.ibsqa.qualit.steps.roles.Exists;
import io.cucumber.java.ru.Тогда;
import org.springframework.beans.factory.annotation.Autowired;

public class SeleniumCollectionStorySteps extends AbstractSteps {

    @Autowired
    private SeleniumCollectionSteps seleniumCollectionSteps;

    @StepDescription(action = "UI->Коллекции->Элементы коллекции содержат поле"
            , parameters = {"collectionName - наименование коллекции", "fieldName - наименование поля, наличие которого будет проверяться"}
            , expertView = true)
    @Тогда("^элементы коллекции \"([^\"]*)\" содержат поле \"([^\"]*)\"$")
    @Context(type = ContextType.COLLECTION, change = ContextChange.USE, parameter = "collectionName", onlyStepContext = true)
    public void stepCheckFieldsDisplayed(
            @Collection String collectionName,
            @Exists String fieldName
    ) {
        flow(()->
                seleniumCollectionSteps.checkAllItemsDisplayed(collectionName, fieldName)
        );
    }
}
