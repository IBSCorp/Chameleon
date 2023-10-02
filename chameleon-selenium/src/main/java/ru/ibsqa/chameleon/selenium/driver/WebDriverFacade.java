package ru.ibsqa.chameleon.selenium.driver;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.interactions.*;
import ru.ibsqa.chameleon.selenium.driver.configuration.IDriverConfiguration;
import ru.ibsqa.chameleon.utils.spring.SpringUtils;
import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.*;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Slf4j
public class WebDriverFacade implements IDriverFacade {

    @Getter @Setter
    private boolean defaultDriver;

    // Значение реального драйвера изолированное для конкретного потока
    private ThreadLocal<WebDriver> threadWrappedDriver = new InheritableThreadLocal<>();
    // Полный список всех реальных драйверов (используется только в методе quitAll)
    private final List<WebDriver> allWrappedDrivers = new ArrayList<>();

    @Getter
    protected IDriverFactory driverFactory;

    @Getter
    private IDriverConfiguration configuration;

    private final ThreadLocal<Boolean> timeoutsSupport = new InheritableThreadLocal<>();

    private final ThreadLocal<Boolean> framesSupport = new InheritableThreadLocal<>();

    private int configImplicitlyWait;
    private final ThreadLocal<Integer> implicitlyWait = new InheritableThreadLocal<>();

    private int configDefaultWaitTimeOut;
    private final ThreadLocal<Integer> defaultWaitTimeOut = new InheritableThreadLocal<>();;

    public WebDriverFacade() {
        initWrappedDrivers();
    }

    protected void initWrappedDrivers() {
        this.allWrappedDrivers.clear();
        this.threadWrappedDriver = new InheritableThreadLocal<>();
    }

    @Override
    public void setConfiguration(IDriverConfiguration configuration) {
        this.configuration = configuration;
        if (Objects.nonNull(configuration)) {
            configImplicitlyWait = getDriverFactory().getConfiguration().getImplicitlyWait();
            configDefaultWaitTimeOut = getDriverFactory().getConfiguration().getDefaultWaitTimeOut();
        }
    }

    @Override
    public void setDriverFactory(IDriverFactory driverFactory) {
        this.driverFactory = driverFactory;
        setConfiguration(driverFactory.getConfiguration());
    }

    private boolean isCloseDriverAfterTest() {
        return Objects.nonNull(getConfiguration()) && getConfiguration().isCloseDriverAfterTest();
    }

    @Override
    public String getId() {
        return SpringUtils.getBeanName(this);
    }

    @Override
    public boolean hasWrappedDriver() {
        return Objects.nonNull(threadWrappedDriver.get());
    }

    protected WebDriver getPureWrappedDriver() {
        return threadWrappedDriver.get();
    }

    @Override
    public WebDriver getWrappedDriver() {
        synchronized (this) {
            if (Objects.isNull(threadWrappedDriver.get())) {
                setWrappedDriver(newProxyDriver());
            }
            return getPureWrappedDriver();
        }
    }

    protected void setWrappedDriver(WebDriver webDriver) {
        synchronized (this) {
            if (Objects.nonNull(webDriver)) {
                threadWrappedDriver.set(webDriver);
                if (!allWrappedDrivers.contains(webDriver)) {
                    allWrappedDrivers.add(webDriver);
                }
            } else {
                WebDriver old = threadWrappedDriver.get();
                if (Objects.nonNull(old)) {
                    threadWrappedDriver.remove();
                    allWrappedDrivers.remove(old);
                }
            }
        }
    }

    private void setImplicitlyWait(WebDriver webDriver, int implicitlyWait) {
        if (isTimeoutsSupport()) {
            try {
                webDriver.manage().timeouts().implicitlyWait(implicitlyWait, TimeUnit.SECONDS);
            } catch (org.openqa.selenium.UnsupportedCommandException e) {
                setTimeoutsSupport(false);
            }
        }
    }

    @Override
    public void switchToDefaultContent() {
        if (isFramesSupport() && Objects.nonNull(getPureWrappedDriver())) {
            try {
                if (Objects.nonNull(getPureWrappedDriver().switchTo())) {
                    getPureWrappedDriver().switchTo().defaultContent();
                }
            } catch (org.openqa.selenium.UnsupportedCommandException e) {
                setFramesSupport(false);
            }
        }
    }

    protected WebDriver newProxyDriver() {
        WebDriver driver = getDriverFactory().newInstance(getId());
        setImplicitlyWait(driver, getImplicitlyWait());
        return driver;
    }

    @Override
    public <R> R withImplicitlyWait(int seconds, Function<IDriverFacade, R> consumer) {
        int implicitlyWait = this.getImplicitlyWait();
        try {
            this.setImplicitlyWait(seconds);
            return consumer.apply(this);
        } finally {
            this.setImplicitlyWait(implicitlyWait);
        }
    }

    @Override
    public void maximizeWindow(){
        getWrappedDriver().manage().window().maximize();
    }

    @Override
    public void get(String url) {
        getWrappedDriver().get(url);
    }

    @Override
    public String getCurrentUrl() {
        return getWrappedDriver().getCurrentUrl();
    }

    @Override
    public String getTitle() {
        return getWrappedDriver().getTitle();
    }

    @Override
    public List<WebElement> findElements(By by) {
        return getWrappedDriver().findElements(by);
    }

    @Override
    public WebElement findElement(By by) {
        return getWrappedDriver().findElement(by);
    }

    @Override
    public String getPageSource() {
        return getWrappedDriver().getPageSource();
    }

    @Override
    public void close() {
        getWrappedDriver().close();
    }

    @Override
    public void quit() {
        synchronized (this) {
            WebDriver driver = getPureWrappedDriver();
            if (Objects.nonNull(driver)) {
                driver.quit();
                allWrappedDrivers.remove(driver);
                threadWrappedDriver.remove();
            }
        }
    }

    /**
     * Закрытие WebDriver-ов для всех потоков. Вызывать как destroy-method.
     * Закрытие не выполняется, если не установлено свойство closeDriverAfterTest=true
     */
    @Override
    public void quitAll() {
        synchronized (this) {
            if (allWrappedDrivers.size() > 0 && isCloseDriverAfterTest()) {
                allWrappedDrivers.forEach(WebDriver::quit);
                initWrappedDrivers();
            }
        }
    }

    @Override
    public Set<String> getWindowHandles() {
        return getWrappedDriver().getWindowHandles();
    }

    @Override
    public String getWindowHandle() {
        return getWrappedDriver().getWindowHandle();
    }

    @Override
    public TargetLocator switchTo() {
        return getWrappedDriver().switchTo();
    }

    @Override
    public Navigation navigate() {
        return getWrappedDriver().navigate();
    }

    @Override
    public Options manage() {
        return getWrappedDriver().manage();
    }

    @Override
    public Capabilities getCapabilities() {
        return ((HasCapabilities) getWrappedDriver()).getCapabilities();
    }

    @Override
    public Object executeScript(String script, Object... parameters) {
        return ((JavascriptExecutor) getWrappedDriver()).executeScript(script, parameters);
    }

    @Override
    public Object executeAsyncScript(String script, Object... parameters) {
        return ((JavascriptExecutor) getWrappedDriver()).executeAsyncScript(script, parameters);
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> outputType) throws WebDriverException {
        return ((TakesScreenshot) getWrappedDriver()).getScreenshotAs(outputType);
    }

    @Override
    public void showBrowser(){
        throw new UnsupportedOperationException();
    }

    @Override
    public void perform(Collection<Sequence> actions) {
        ((Interactive) getWrappedDriver()).perform(actions);
    }

    @Override
    public void resetInputState() {
        ((Interactive) getWrappedDriver()).resetInputState();
    }

    @Override
    public boolean isTimeoutsSupport() {
        return Optional.ofNullable(this.timeoutsSupport.get()).orElse(true);
    }

    @Override
    public void setTimeoutsSupport(boolean timeoutsSupport) {
        this.timeoutsSupport.set(timeoutsSupport);
    }

    @Override
    public boolean isFramesSupport() {
        return Optional.ofNullable(this.framesSupport.get()).orElse(true);
    }

    @Override
    public void setFramesSupport(boolean framesSupport) {
        this.framesSupport.set(framesSupport);
    }

    @Override
    public int getImplicitlyWait() {
        return Optional.ofNullable(this.implicitlyWait.get()).orElse(configImplicitlyWait);
    }

    @Override
    public void setImplicitlyWait(int seconds) {
        this.implicitlyWait.set(seconds);
        if (Objects.nonNull(getPureWrappedDriver())) {
            setImplicitlyWait(getPureWrappedDriver(), this.implicitlyWait.get());
        }
    }

    @Override
    public int getDefaultWaitTimeOut() {
        return Optional.ofNullable(this.defaultWaitTimeOut.get()).orElse(configDefaultWaitTimeOut);
    }

    @Override
    public void setDefaultWaitTimeOut(int seconds) {
        this.defaultWaitTimeOut.set(seconds);
    }

}
