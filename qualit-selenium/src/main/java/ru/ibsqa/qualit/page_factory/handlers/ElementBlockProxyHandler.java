package ru.ibsqa.qualit.page_factory.handlers;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.internal.LocatingElementHandler;

import java.lang.reflect.Method;
import java.time.Clock;
import java.time.Instant;

/**
 * InvocationHandler для страницы, у которой указан локатор
 */
public class ElementBlockProxyHandler extends LocatingElementHandler {

    private final java.time.Duration timeOutInSeconds;

    private final Clock clock;

    private final String name;

    public ElementBlockProxyHandler(ElementLocator locator, String name, int timeOutInSeconds) {
        super(locator);
        this.name = name;
        this.clock = Clock.systemDefaultZone();
        this.timeOutInSeconds = java.time.Duration.ofSeconds(timeOutInSeconds);
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        if ("toString".equals(method.getName())) {
            return name;
        }

        Instant end = clock.instant().plus(this.timeOutInSeconds);

        StaleElementReferenceException lastException;
        do {
            try {
                return super.invoke(o, method, objects);
            } catch (StaleElementReferenceException e) {
                lastException = e;
                this.waitFor();
            }
        } while (end.isBefore(clock.instant()));
        throw lastException;
    }

    protected long sleepFor() {
        return 500L;
    }

    private void waitFor() throws InterruptedException {
        Thread.sleep(this.sleepFor());
    }
}