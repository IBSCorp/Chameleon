package ru.ibsqa.chameleon.page_factory.handlers;

import ru.ibsqa.chameleon.elements.selenium.WebElementFacade;
import ru.ibsqa.chameleon.page_factory.PageFactoryUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * InvocationHandler элемента коллекции
 */
public class ElementListProxyHandler<T extends WebElementFacade> implements InvocationHandler {

    private final Class<T> elementClass;
    private final ElementLocator locator;
    private final String name;
    private final int waitTimeOut;
    private final String driverId;

    public ElementListProxyHandler(Class<T> elementClass, ElementLocator locator, String name, int waitTimeOut, String driverId) {
        this.elementClass = elementClass;
        this.locator = locator;
        this.name = name;
        this.waitTimeOut = waitTimeOut;
        this.driverId = driverId;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        if ("toString".equals(method.getName())) {
            return name;
        }

        List<T> elements = new LinkedList<>();
        int elementNumber = 0;
        for (WebElement element : locator.findElements()) {
            String newName = String.format("%s [%d]", name, elementNumber++);
            elements.add(PageFactoryUtils.createElement(elementClass, element, newName, waitTimeOut, driverId));
        }

        try {
            return method.invoke(elements, objects);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }
}