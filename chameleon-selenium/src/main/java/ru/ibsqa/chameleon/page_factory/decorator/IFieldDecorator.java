package ru.ibsqa.chameleon.page_factory.decorator;

import org.openqa.selenium.support.pagefactory.FieldDecorator;
import ru.ibsqa.chameleon.page_factory.locator.IElementLocatorFactory;

public interface IFieldDecorator extends FieldDecorator {
    IElementLocatorFactory getElementLocatorFactory();
    void setElementLocatorFactory(IElementLocatorFactory elementLocatorFactory);
}
