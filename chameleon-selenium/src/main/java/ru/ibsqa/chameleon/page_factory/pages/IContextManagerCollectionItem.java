package ru.ibsqa.chameleon.page_factory.pages;

import ru.ibsqa.chameleon.context.IContextManager;

public interface IContextManagerCollectionItem extends IContextManager<ICollectionItemObject> {

    IPageObject getCurrentCollectionItem();

    void setCurrentCollectionItem(Class<? extends ICollectionItemObject> item);

    void setCurrentCollectionItem(ICollectionItemObject item);

    void setCurrentCollectionItem(String itemName);

    void clearCurrentCollectionItem();
}
