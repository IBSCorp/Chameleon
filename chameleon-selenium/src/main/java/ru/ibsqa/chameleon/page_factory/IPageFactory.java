package ru.ibsqa.chameleon.page_factory;

import ru.ibsqa.chameleon.page_factory.pages.IPageObject;

import java.util.List;

public interface IPageFactory {
    <T extends IPageObject> Class<T> generatePage(String pageName);
    List<String> getFieldNames(IPageObject pageObject);
    List<String> getCollectionNames(IPageObject pageObject);
}
