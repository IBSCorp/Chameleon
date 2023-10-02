package ru.ibsqa.chameleon.page_factory.handlers;

import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.internal.LocatingElementHandler;
import ru.ibsqa.chameleon.elements.InvokeFieldException;
import ru.ibsqa.chameleon.i18n.ILocaleManager;
import ru.ibsqa.chameleon.selenium.driver.IDriverFacade;
import ru.ibsqa.chameleon.selenium.driver.IDriverManager;
import ru.ibsqa.chameleon.utils.spring.SpringUtils;

import java.time.Clock;
import java.time.Instant;

public abstract class AbstractElementHandler extends LocatingElementHandler {

    private final int waitTimeOut;
    private final String name;
    private final Clock clock;

    public AbstractElementHandler(ElementLocator locator, String name, int waitTimeOut) {
        super(locator);
        this.name = name;
        this.clock = Clock.systemDefaultZone();
        this.waitTimeOut = waitTimeOut;
    }

    protected String getName() {
        return this.name;
    }

    protected Instant getCurrent() {
        return this.clock.instant();
    }

    protected boolean inTimeout(Instant start) {
        return this.clock.instant().isBefore(start.plus(java.time.Duration.ofSeconds(getWaitTimeOut())));
    }

    protected int getWaitTimeOut() {
        if (this.waitTimeOut>=0) {
            return this.waitTimeOut;
        }
        IDriverManager driverManager = SpringUtils.getBean(IDriverManager.class);
        IDriverFacade driverFacade = driverManager.getLastDriver();
        return driverFacade.getDefaultWaitTimeOut();
    }

    protected long sleepFor() {
        return 200L;
    }

    protected void waitFor() throws InterruptedException {
        Thread.sleep(this.sleepFor());
    }

    protected Object throwInvokeFieldException(Throwable cause) {
        ILocaleManager localeManager = SpringUtils.getBean(ILocaleManager.class);
        throw new InvokeFieldException(localeManager.getMessage("invokeFieldException", getName()), cause);
    }
}
