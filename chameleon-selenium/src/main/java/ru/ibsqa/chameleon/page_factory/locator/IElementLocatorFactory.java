package ru.ibsqa.chameleon.page_factory.locator;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

public interface IElementLocatorFactory extends ElementLocatorFactory {
    void setDriverId(String driverId);
    void setSearchContext(SearchContext searchContext);
    ElementLocator createLocator(Class<?> clazz);
}
