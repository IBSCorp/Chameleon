package ru.ibsqa.qualit.page_factory.pages;

import ru.ibsqa.qualit.elements.IFacade;

public interface IFieldExtractor {
    <FACADE extends IFacade> FACADE getField(IPageObject pageObject, String fieldName);
}
