package ru.ibsqa.chameleon.page_factory.pages;

public abstract class DefaultCollectionObject extends DefaultPageObject implements ICollectionItemObject {

//    public DefaultCollectionObject(SearchContext searchContext){
//        super(searchContext);
//    }

//    public DefaultCollectionObject(){
//        super();
//    }

//    public DefaultCollectionObject(boolean initElements) {
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
