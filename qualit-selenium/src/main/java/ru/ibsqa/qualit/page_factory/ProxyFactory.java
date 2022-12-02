package ru.ibsqa.qualit.page_factory;

import ru.ibsqa.qualit.elements.IFacadeCollection;
import ru.ibsqa.qualit.elements.selenium.WebElementFacade;
import ru.ibsqa.qualit.page_factory.pages.IPageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Locatable;
import org.openqa.selenium.WrapsElement;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.List;

public class ProxyFactory {

    public static <T extends WebElement> T createWebElementProxy(ClassLoader loader, InvocationHandler handler) {
        Class<?>[] interfaces = new Class[]{WebElement.class, WrapsElement.class, Locatable.class};
        return (T) Proxy.newProxyInstance(loader, interfaces, handler);
    }

    public static <T extends IFacadeCollection, E extends IPageObject> T createElementCollectionProxy(ClassLoader loader, InvocationHandler handler) {
        Class<?>[] interfaces = new Class[]{IFacadeCollection.class};
        return (T) Proxy.newProxyInstance(loader, interfaces, handler);
    }

    public static <T extends WebElementFacade> List<T> createElementListProxy(ClassLoader loader, InvocationHandler handler) {
        return (List<T>) Proxy.newProxyInstance(loader, new Class[]{List.class}, handler);
    }
}
