package ru.ibsqa.qualit.page_factory.pages;

import ru.ibsqa.qualit.context.IContextManager;

public interface IContextManagerPage extends IContextManager<IPageObject> {

    IPageObject getCurrentPage();

    void setCurrentPage(Class<? extends IPageObject> page);

    void setCurrentPage(IPageObject page);

    void setCurrentPage(String pageName);

    IPageObject switchToPreviousPage();
}
