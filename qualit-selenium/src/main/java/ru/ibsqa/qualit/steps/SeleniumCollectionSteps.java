package ru.ibsqa.qualit.steps;

import org.junit.jupiter.api.Assertions;
import ru.ibsqa.qualit.context.IContextObject;
import ru.ibsqa.qualit.elements.selenium.IFacadeSelenium;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Component
public class SeleniumCollectionSteps extends AbstractSteps {

    @Autowired
    private CollectionSteps collectionSteps;

    @UIStep
    @TestStep("все элементы \"${collectionName}\" содержат поле \"${fieldName}\"")
    public void checkAllItemsDisplayed(String collectionName, String fieldName) {
        String errMsg = "Не найдено поле [%s] у элемента коллекции [%s]";
        List<IContextObject> items = collectionSteps.getItems(collectionName);
        for (IContextObject item : items) {
            Assertions.assertTrue(((IFacadeSelenium) item.getField(fieldName)).isDisplayed(), String.format(errMsg, fieldName, collectionName));
        }
    }

}
