package ru.ibsqa.qualit.elements.uia;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsElement;
import org.openqa.selenium.interactions.Locatable;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.springframework.stereotype.Component;
import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;
import ru.ibsqa.qualit.elements.collections.AbstractCollection;
import ru.ibsqa.qualit.elements.selenium.WebElementFacade;
import ru.ibsqa.qualit.exceptions.ElementCreationError;
import ru.ibsqa.qualit.page_factory.PageFactoryUtils;
import ru.ibsqa.qualit.page_factory.decorator.AbstractDecorateExecutor;
import ru.ibsqa.qualit.page_factory.decorator.IDecorateExecutor;
import ru.ibsqa.qualit.page_factory.handlers.ElementProxyHandler;
import ru.ibsqa.qualit.page_factory.pages.IPageObject;
import ru.ibsqa.qualit.uia.driver.UiaSupportedDriver;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Objects;

@Component
public class UiaDecorateExecutor extends AbstractDecorateExecutor implements IDecorateExecutor {

    @Override
    public ConfigurationPriority getPriority() {
        return ConfigurationPriority.NORMAL;
    }

    @Override
    @SuppressWarnings("unchecked")
    public  <T extends AbstractCollection> T decorateCollection(ElementLocatorFactory elementLocatorFactory, ClassLoader loader, Field field) {
        if (!isSupportedDriver(elementLocatorFactory, UiaSupportedDriver.UI_AUTOMATION)) {
            return null; // Использовать стандартную имплементацию
        }
        ElementLocator elementLocator = elementLocatorFactory.createLocator(field);
        Class<T> collectionClass = (Class<T>) field.getType();
        return PageFactoryUtils.createCollection(
                collectionClass,
                PageFactoryUtils.getElementNameAsString(field),
                PageFactoryUtils.getElementWaitTimeOut(field),
                elementLocator, new String[0],
                PageFactoryUtils.getGenericParameterClass(field)
        );
    }

    @Override
    public <T extends WebElementFacade> List<T> decorateList(ElementLocatorFactory elementLocatorFactory, ClassLoader loader, Field field) {
        return null; // Использовать стандартную имплементацию
    }

    @Override
    public <T extends IPageObject> T decorateBlock(ElementLocatorFactory elementLocatorFactory, ClassLoader loader, Field field) {
        return null; // Использовать стандартную имплементацию
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends WebElementFacade> T decorateElement(ElementLocatorFactory elementLocatorFactory, ClassLoader loader, Field field) {
        if (!isSupportedDriver(elementLocatorFactory, UiaSupportedDriver.UI_AUTOMATION)) {
            return null; // Использовать стандартную имплементацию
        }

        ElementLocator locator = elementLocatorFactory.createLocator(field);
        InvocationHandler handler = new ElementProxyHandler(
                locator,
                PageFactoryUtils.getElementNameAsString(field),
                PageFactoryUtils.getElementWaitTimeOut(field)
        );
        IUiaElement elementToWrap = createUiaElementProxy(loader, handler);
        return (T) createUiaElement(
                field.getType(),
                elementToWrap,
                PageFactoryUtils.getElementNameAsString(field),
                PageFactoryUtils.getElementWaitTimeOut(field),
                getDriverId(elementLocatorFactory)
        );
    }

    @SuppressWarnings("unchecked")
    private static <T extends IUiaElement> T createUiaElement(Class<?> elementClass, IUiaElement elementToWrap, String elementName, int waitTimeout, String driverId) {
//        try {
//            return (T) PageFactoryUtils.newInstance(
//                    elementClass,
//                    elementToWrap,
//                    elementName,
//                    waitTimeOut,
//                    driverId
//            );
//        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
//            throw new ElementCreationError(e);
//        }
        WebElementFacade instance = (WebElementFacade) PageFactoryUtils.newInstance(elementClass);
        if (Objects.nonNull(instance)) {
            instance.pushArguments(elementToWrap, elementName, waitTimeout, driverId);
        }
        return (T)instance;
    }

    private static <T extends WebElement> IUiaElement createUiaElementProxy(ClassLoader loader, InvocationHandler handler) {
        Class<?>[] interfaces = new Class[] {
                WebElement.class,
                WrapsElement.class,
                Locatable.class,
                IUiaElement.class
        };
        return (IUiaElement) Proxy.newProxyInstance(loader, interfaces, handler);
    }
}
