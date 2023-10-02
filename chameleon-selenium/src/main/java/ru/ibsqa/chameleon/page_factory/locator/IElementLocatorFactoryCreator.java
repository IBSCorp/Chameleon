package ru.ibsqa.chameleon.page_factory.locator;

import org.openqa.selenium.SearchContext;
import ru.ibsqa.chameleon.selenium.driver.IDriverFacade;

public interface IElementLocatorFactoryCreator {
    IElementLocatorFactory createElementLocatorFactory(IDriverFacade driverFacade, SearchContext searchContext);
}
