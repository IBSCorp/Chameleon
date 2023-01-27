package ru.ibsqa.qualit.page_factory.handlers;

import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.internal.LocatingElementHandler;
import ru.ibsqa.qualit.elements.InvokeFieldException;
import ru.ibsqa.qualit.i18n.ILocaleManager;
import ru.ibsqa.qualit.selenium.driver.IDriverManager;
import ru.ibsqa.qualit.selenium.driver.WebDriverFacade;
import ru.ibsqa.qualit.utils.spring.SpringUtils;

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
        WebDriverFacade webDriverFacade = driverManager.getLastDriver();
        return webDriverFacade.getDefaultWaitTimeOut();
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
