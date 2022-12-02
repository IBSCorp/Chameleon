package ru.ibsqa.qualit.sap.pages;

import ru.ibsqa.qualit.page_factory.pages.DefaultPageObject;
import ru.ibsqa.qualit.page_factory.pages.ICollectionItemObject;
import org.openqa.selenium.SearchContext;

public class SapCollectionObject extends DefaultPageObject implements ICollectionItemObject {
    public SapCollectionObject(SearchContext searchContext) {
        super(searchContext);
    }

    public SapCollectionObject() {
        super();
    }

    public SapCollectionObject(boolean initElements) {
        super(initElements);
    }

    @Override
    public void beforePageLoaded() {
    }

    @Override
    public void afterPageLoaded() {
    }

    @Override
    public void switchFrames() {
    }

    @Override
    public boolean isLoaded() {
        return true;
    }
}
