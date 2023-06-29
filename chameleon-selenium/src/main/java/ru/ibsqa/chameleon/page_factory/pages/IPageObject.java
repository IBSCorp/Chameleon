package ru.ibsqa.chameleon.page_factory.pages;

import ru.ibsqa.chameleon.context.IContextObject;
import ru.ibsqa.chameleon.elements.IFacadeExportToJson;
import ru.ibsqa.chameleon.elements.selenium.WebElementFacade;
import ru.ibsqa.chameleon.selenium.driver.WebDriverFacade;
import ru.ibsqa.chameleon.selenium.enums.KeyEnum;
import org.json.JSONObject;
import org.openqa.selenium.SearchContext;

public interface IPageObject extends IContextObject, IFacadeExportToJson<JSONObject> {

    WebDriverFacade getDriver();

    void initElements(SearchContext searchContext);

    <T extends WebElementFacade> T getSeleniumField(String fieldName);

    Iterable<? extends IPageObject> getCollection(String collectionName);

    void beforePageLoaded();

    void afterPageLoaded();

    void switchFrames();

    boolean isLoaded();

    void loadPage();

    void pressKey(KeyEnum key);

    default String getName() {
        return getClass().getSimpleName();
    }
}
