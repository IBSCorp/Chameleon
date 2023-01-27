package ru.ibsqa.qualit.page_factory.handlers;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.lang.reflect.Method;
import java.time.Instant;

/**
 * InvocationHandler для страницы, у которой указан локатор
 */
public class ElementBlockProxyHandler extends AbstractElementHandler {

    public ElementBlockProxyHandler(ElementLocator locator, String name, int waitTimeOut) {
        super(locator, name, waitTimeOut);
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        if ("toString".equals(method.getName())) {
            return getName();
        }

        Instant start = getCurrent();

        StaleElementReferenceException lastException;
        do {
            try {
                return super.invoke(o, method, objects);
            } catch (StaleElementReferenceException e) {
                lastException = e;
                this.waitFor();
            }
        } while (inTimeout(start));
        return throwInvokeFieldException(lastException);
    }

}