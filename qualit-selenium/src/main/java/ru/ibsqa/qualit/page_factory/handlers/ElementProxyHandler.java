package ru.ibsqa.qualit.page_factory.handlers;

import ru.ibsqa.qualit.page_factory.locator.IFrameManager;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.internal.LocatingElementHandler;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.ibsqa.qualit.elements.selenium.WebElementFacade.UNKNOWN_WEB_ELEMENT;

/**
 * InvocationHandler элемента страницы
 */
@Slf4j
public class ElementProxyHandler extends AbstractElementHandler {

    private final String[] frames;
    private final boolean collectionElement;

    public ElementProxyHandler(ElementLocator locator, String name, int waitTimeOut) {
        this(locator, name, waitTimeOut, new String[0], false);
    }

    public ElementProxyHandler(ElementLocator locator, String name, int waitTimeOut, String[] frames, boolean collectionElement) {
        super(locator, name, waitTimeOut);
        this.frames = frames;
        this.collectionElement = collectionElement;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        if ("toString".equals(method.getName())) {
            return getName();
        }
        IFrameManager.elementFrames(frames);
        log.debug(String.format("getField(\"%s\").%s(%s)", getName(), method.getName(), Objects.isNull(objects) ? "" : Stream.of(objects).map(Object::toString).collect(Collectors.joining(", "))));

        // В этом блоке производится быстрая проверка отсутствия поля на странице
        if (method.getName().equals("equals") && UNKNOWN_WEB_ELEMENT.equals(objects[0])) {
            Field field = LocatingElementHandler.class.getDeclaredField("locator");
            field.setAccessible(true);
            ElementLocator elementLocator = (ElementLocator)field.get(this);
            return elementLocator.findElements().size() == 0;
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
        } while (!collectionElement && inTimeout(start));
        return throwInvokeFieldException(lastException);
    }

}