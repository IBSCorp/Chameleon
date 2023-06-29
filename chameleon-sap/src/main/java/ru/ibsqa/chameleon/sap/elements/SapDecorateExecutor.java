package ru.ibsqa.chameleon.sap.elements;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsElement;
import org.openqa.selenium.interactions.Locatable;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.springframework.stereotype.Component;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;
import ru.ibsqa.chameleon.elements.collections.AbstractCollection;
import ru.ibsqa.chameleon.elements.selenium.WebElementFacade;
import ru.ibsqa.chameleon.page_factory.PageFactoryUtils;
import ru.ibsqa.chameleon.page_factory.decorator.AbstractDecorateExecutor;
import ru.ibsqa.chameleon.page_factory.decorator.IDecorateExecutor;
import ru.ibsqa.chameleon.page_factory.handlers.ElementProxyHandler;
import ru.ibsqa.chameleon.page_factory.pages.IPageObject;
import ru.ibsqa.chameleon.sap.driver.SapSupportedDriver;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Objects;

@Component
public class SapDecorateExecutor extends AbstractDecorateExecutor implements IDecorateExecutor {

    @Override
    public ConfigurationPriority getPriority() {
        return ConfigurationPriority.NORMAL;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends WebElementFacade> T decorateElement(ElementLocatorFactory elementLocatorFactory, ClassLoader loader, Field field) {
        if (!isSupportedDriver(elementLocatorFactory, SapSupportedDriver.SAP)) {
            return null; // Использовать стандартную имплементацию
        }

        ElementLocator locator = elementLocatorFactory.createLocator(field);
        InvocationHandler handler = new ElementProxyHandler(
                locator,
                PageFactoryUtils.getElementNameAsString(field),
                PageFactoryUtils.getElementWaitTimeOut(field)
        );
        ISapElement elementToWrap =  createSapElementProxy(loader, handler);
        return (T) createSapElement(
                field.getType(),
                elementToWrap,
                PageFactoryUtils.getElementNameAsString(field),
                PageFactoryUtils.getElementWaitTimeOut(field),
                this.getDriverId(elementLocatorFactory)
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends AbstractCollection> T decorateCollection(ElementLocatorFactory elementLocatorFactory, ClassLoader loader, Field field) {
        if (!isSupportedDriver(elementLocatorFactory, SapSupportedDriver.SAP)) {
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

    @SuppressWarnings("unchecked")
    private static <T extends ISapElement> T createSapElement(Class<?> elementClass, ISapElement elementToWrap, String elementName, int waitTimeout, String driverId) {
        //try {
            /*return (T) PageFactoryUtils.newInstance(
                    elementClass,
                    elementToWrap,
                    elementName,
                    waitTimeout,
                    driverId
            );*/
            WebElementFacade instance = (WebElementFacade) PageFactoryUtils.newInstance(elementClass);
            if (Objects.nonNull(instance)) {
                instance.pushArguments(elementToWrap, elementName, waitTimeout, driverId);
            }
            return (T)instance;
//        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
//            throw new ElementCreationError(e);
//        }
    }

    private static <T extends WebElement> ISapElement createSapElementProxy(ClassLoader loader, InvocationHandler handler) {
        Class<?>[] interfaces = new Class[]{
                WebElement.class,
                WrapsElement.class,
                Locatable.class,
                ISapElement.class
        };
        return (ISapElement) Proxy.newProxyInstance(loader, interfaces, handler);
    }

}
