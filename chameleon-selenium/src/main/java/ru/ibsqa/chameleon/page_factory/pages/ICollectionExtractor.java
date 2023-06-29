package ru.ibsqa.chameleon.page_factory.pages;

public interface ICollectionExtractor {
    Iterable<? extends IPageObject> getField(IPageObject pageObject, String fieldName);
}
