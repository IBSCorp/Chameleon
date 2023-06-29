package ru.ibsqa.chameleon.sap.pages;

import ru.ibsqa.chameleon.page_factory.pages.DefaultPageObject;
import ru.ibsqa.chameleon.page_factory.pages.ICollectionItemObject;

public class SapCollectionObject extends DefaultPageObject implements ICollectionItemObject {
//    public SapCollectionObject(SearchContext searchContext) {
//        super(searchContext);
//    }
//
    public SapCollectionObject() {
        super();
    }
//
//    public SapCollectionObject(boolean initElements) {
//        super(initElements);
//    }

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
