package ru.ibsqa.qualit.uia.pages;

import ru.ibsqa.qualit.page_factory.pages.DefaultPageObject;
import ru.ibsqa.qualit.page_factory.pages.ICollectionItemObject;
import org.openqa.selenium.SearchContext;

public class UiaCollectionObject extends DefaultPageObject implements ICollectionItemObject {

    public UiaCollectionObject(SearchContext searchContext) {
        super(searchContext);
    }

    public UiaCollectionObject() {
        super();
    }

    public UiaCollectionObject(boolean initElements) {
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
