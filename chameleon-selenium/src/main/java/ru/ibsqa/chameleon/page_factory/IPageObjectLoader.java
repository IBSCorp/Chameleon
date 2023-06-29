package ru.ibsqa.chameleon.page_factory;

import org.openqa.selenium.SearchContext;
import ru.ibsqa.chameleon.page_factory.pages.IPageObject;

public interface IPageObjectLoader {

    void load(IPageObject pageObject, SearchContext searchContext);

}
