package ru.ibsqa.chameleon.page_factory.decorator;

import ru.ibsqa.chameleon.elements.collections.AbstractCollection;
import ru.ibsqa.chameleon.elements.selenium.WebElementFacade;
import ru.ibsqa.chameleon.page_factory.locator.IElementLocatorFactory;
import ru.ibsqa.chameleon.page_factory.pages.IPageObject;

import java.lang.reflect.Field;
import java.util.List;

public interface IDecorateManager {
    <T extends WebElementFacade> T decorateElement(IElementLocatorFactory elementLocatorFactory, ClassLoader loader, Field field);
    <T extends AbstractCollection> T decorateCollection(IElementLocatorFactory elementLocatorFactory, ClassLoader loader, Field field);
    <T extends WebElementFacade> List<T> decorateList(IElementLocatorFactory elementLocatorFactory, ClassLoader loader, Field field);
    <T extends IPageObject> T decorateBlock(IElementLocatorFactory elementLocatorFactory, ClassLoader loader, Field field);
}
