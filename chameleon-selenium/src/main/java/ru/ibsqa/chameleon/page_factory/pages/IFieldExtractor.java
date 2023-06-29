package ru.ibsqa.chameleon.page_factory.pages;

import ru.ibsqa.chameleon.elements.IFacade;

public interface IFieldExtractor {
    <FACADE extends IFacade> FACADE getField(IPageObject pageObject, String fieldName);
}
