package ru.ibsqa.chameleon.page_factory.pages;

import ru.ibsqa.chameleon.context.IContextManager;

public interface IContextManagerPage extends IContextManager<IPageObject> {

    IPageObject getCurrentPage();

    void setCurrentPage(Class<? extends IPageObject> page);

    void setCurrentPage(IPageObject page);

    void setCurrentPage(String pageName);

    IPageObject switchToPreviousPage();
}
