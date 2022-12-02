package ru.ibsqa.qualit.steps;

import org.jbehave.core.annotations.Then;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SeleniumCollectionStorySteps extends AbstractSteps {

    @Autowired
    private SeleniumCollectionSteps seleniumCollectionSteps;

    @Then("элементы коллекции \"$collectionName\" содержат поле \"$fieldName\"")
    public void stepCheckFieldsDisplayed(String collectionName, String fieldName) {
        flow(()->
                seleniumCollectionSteps.checkAllItemsDisplayed(collectionName, fieldName)
        );
    }
}
