package ru.ibsqa.chameleon.steps;

import io.cucumber.java.ru.Тогда;
import org.springframework.beans.factory.annotation.Autowired;
import ru.ibsqa.chameleon.context.Context;
import ru.ibsqa.chameleon.context.ContextChange;
import ru.ibsqa.chameleon.element.ElementTypeCollection;
import ru.ibsqa.chameleon.steps.roles.Collection;
import ru.ibsqa.chameleon.steps.roles.Exists;

public class SeleniumCollectionStorySteps extends AbstractSteps {

    @Autowired
    private SeleniumCollectionSteps seleniumCollectionSteps;

    @StepDescription(action = "UI->Коллекции->Элементы коллекции содержат поле"
            , parameters = {"collectionName - наименование коллекции", "fieldName - наименование поля, наличие которого будет проверяться"}
            , expertView = true)
    @Тогда("^элементы коллекции \"([^\"]*)\" содержат поле \"([^\"]*)\"$")
    @Context(type = ElementTypeCollection.class, change = ContextChange.USE, parameter = "collectionName")
    public void stepCheckFieldsDisplayed(
            @Collection String collectionName,
            @Exists String fieldName
    ) {
        flow(() ->
                seleniumCollectionSteps.checkAllItemsDisplayed(collectionName, fieldName)
        );
    }
}
