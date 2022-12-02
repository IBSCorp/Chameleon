package ru.ibsqa.qualit.page_factory.handlers;

import ru.ibsqa.qualit.page_factory.locator.IFrameManager;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.internal.LocatingElementHandler;

import java.lang.reflect.Method;
import java.time.Clock;
import java.time.Instant;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * InvocationHandler элемента страницы (кроме коллекций)
 */
@Slf4j
public class ElementProxyHandler extends LocatingElementHandler {

    private final java.time.Duration timeOutInSeconds;
    private final int waitTimeOut;

    private final Clock clock;

    private final String name;

    private final String[] frames;

    public ElementProxyHandler(ElementLocator locator, String name, int waitTimeOut) {
        super(locator);
        this.name = name;
        this.frames = new String[0];
        this.clock = Clock.systemDefaultZone();
        this.timeOutInSeconds = java.time.Duration.ofSeconds(60);
        this.waitTimeOut = waitTimeOut;
    }

    public ElementProxyHandler(ElementLocator locator, String name, int waitTimeOut, String[] frames) {
        super(locator);
        this.name = name;
        this.frames = frames;
        this.clock = Clock.systemDefaultZone();
        this.timeOutInSeconds = java.time.Duration.ofSeconds(60);
        this.waitTimeOut = waitTimeOut;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        if ("toString".equals(method.getName())) {
            return name;
        }
        IFrameManager.elementFrames(frames);
        log.debug(String.format("getField(\"%s\").%s(%s)", name, method.getName(), Objects.isNull(objects) ? "" : Stream.of(objects).map(Object::toString).collect(Collectors.joining(", "))));

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