package ru.ibsqa.qualit.page_factory.decorator;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.springframework.stereotype.Component;
import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;
import ru.ibsqa.qualit.elements.collections.AbstractCollection;
import ru.ibsqa.qualit.elements.selenium.WebElementFacade;
import ru.ibsqa.qualit.page_factory.PageFactoryUtils;
import ru.ibsqa.qualit.page_factory.ProxyFactory;
import ru.ibsqa.qualit.page_factory.handlers.ElementListProxyHandler;
import ru.ibsqa.qualit.page_factory.handlers.ElementProxyHandler;
import ru.ibsqa.qualit.page_factory.pages.ICollectionItemObject;
import ru.ibsqa.qualit.page_factory.pages.IPageObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.util.List;

@Component
public class DefaultDecorateExecutor extends AbstractDecorateExecutor implements IDecorateExecutor {

    @Override
    @SuppressWarnings("unchecked")
    public <T extends WebElementFacade> T decorateElement(ElementLocatorFactory elementLocatorFactory, ClassLoader loader, Field field) {
        ElementLocator locator = elementLocatorFactory.createLocator(field);
        InvocationHandler handler = new ElementProxyHandler(
                locator,
                PageFactoryUtils.getElementNameAsString(field),
                PageFactoryUtils.getElementWaitTimeOut(field),
                PageFactoryUtils.getElementFrames(field),
                ICollectionItemObject.class.isAssignableFrom(field.getDeclaringClass())
        );
        WebElement elementToWrap = ProxyFactory.createWebElementProxy(loader, handler);
        return PageFactoryUtils.createElement(
                (Class<T>) field.getType(),
                elementToWrap,
                PageFactoryUtils.getElementNameAsString(field),
                PageFactoryUtils.getElementWaitTimeOut(field),
                getDriverId(elementLocatorFactory)
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends AbstractCollection> T decorateCollection(ElementLocatorFactory elementLocatorFactory, ClassLoader loader, Field field) {
        ElementLocator elementLocator = elementLocatorFactory.createLocator(field);
        Class<T> collectionClass = (Class<T>) field.getType();
        return PageFactoryUtils.createCollection(
                collectionClass,
                PageFactoryUtils.getElementNameAsString(field),
                PageFactoryUtils.getElementWaitTimeOut(field),
                elementLocator,
                PageFactoryUtils.getCollectionFrames(field),
                PageFactoryUtils.getGenericParameterClass(field)
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends WebElementFacade> List<T> decorateList(ElementLocatorFactory elementLocatorFactory, ClassLoader loader, Field field) {
        ElementLocator locator = elementLocatorFactory.createLocator(field);
        InvocationHandler handler = new ElementListProxyHandler<>(
                PageFactoryUtils.getGenericParameterClass(field),
                locator,
                PageFactoryUtils.getElementNameAsString(field),
                PageFactoryUtils.getElementWaitTimeOut(field),
                getDriverId(elementLocatorFactory)
        );
        return ProxyFactory.createElementListProxy(loader, handler);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends IPageObject> T decorateBlock(ElementLocatorFactory elementLocatorFactory, ClassLoader loader, Field field) {
        return (T) PageFactoryUtils.newInstance(field.getType());
    }

    @Override
    public ConfigurationPriority getPriority() {
        return ConfigurationPriority.LOW;
    }

}
