package ru.ibsqa.chameleon.page_factory.decorator;

import ru.ibsqa.chameleon.page_factory.locator.IElementLocatorFactory;

public abstract class AbstractFieldDecorator implements IFieldDecorator {

    private final ThreadLocal<IElementLocatorFactory> elementLocatorFactory = new InheritableThreadLocal<>();

    @Override
    public IElementLocatorFactory getElementLocatorFactory() {
        return this.elementLocatorFactory.get();
    }

    @Override
    public void setElementLocatorFactory(IElementLocatorFactory elementLocatorFactory) {
        this.elementLocatorFactory.set(elementLocatorFactory);
    }

}
