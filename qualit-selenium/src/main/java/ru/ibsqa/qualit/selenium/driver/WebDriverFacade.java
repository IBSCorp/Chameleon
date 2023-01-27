package ru.ibsqa.qualit.selenium.driver;

import org.openqa.selenium.interactions.*;
import ru.ibsqa.qualit.page_factory.locator.AbstractElementLocatorFactory;
import ru.ibsqa.qualit.selenium.driver.configuration.IDriverConfiguration;
import ru.ibsqa.qualit.utils.spring.SpringUtils;
import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class WebDriverFacade implements WebDriver, TakesScreenshot, JavascriptExecutor, HasCapabilities, Interactive {

    @Getter @Setter
    private boolean defaultDriver;

    // Значение реального драйвера изолированное для конкретного потока
    private ThreadLocal<WebDriver> threadWrappedDriver = new ThreadLocal<>();
    // Полный список всех реальных драйверов (используется только в методе quit)
    private final List<WebDriver> allWrappedDrivers = new ArrayList<>();

    @Getter
    protected IDriverFactory driverFactory;

    @Getter
    private IDriverConfiguration configuration;

    @Getter
    private boolean timeoutsSupport = true;

    @Getter
    private boolean framesSupport = true;

    public void setConfiguration(IDriverConfiguration configuration) {
        this.configuration = configuration;
        if (Objects.nonNull(configuration)) {
            if (implicitlyWait <= 0) {
                setImplicitlyWait(getDriverFactory().getConfiguration().getImplicitlyWait());
            }
            if (defaultWaitTimeOut <= 0) {
                setDefaultWaitTimeOut(getDriverFactory().getConfiguration().getDefaultWaitTimeOut());
            }
        }
    }

    public void setDriverFactory(IDriverFactory driverFactory) {
        this.driverFactory = driverFactory;
        setConfiguration(driverFactory.getConfiguration());
    }

    private boolean isCloseDriverAfterTest() {
        return Objects.nonNull(getConfiguration()) && getConfiguration().isCloseDriverAfterTest();
    }

    @Autowired
    @Getter
    private AbstractElementLocatorFactory elementLocatorFactory;

    public AbstractElementLocatorFactory getElementLocatorFactory(SearchContext searchContext) {
        if (null == searchContext) {
            return elementLocatorFactory;
        }
        AbstractElementLocatorFactory contextualElementLocatorFactory = SpringUtils.getBean(AbstractElementLocatorFactory.class);
        contextualElementLocatorFactory.setSearchContext(searchContext);
        return contextualElementLocatorFactory;
    }

    @PostConstruct
    public void initialize(){
        elementLocatorFactory.setSearchContext(this);
        elementLocatorFactory.setDriverId(getId());
    }

    public String getId(){
        return SpringUtils.getBeanName(this);
    }

    protected WebDriver getPureWrappedDriver() {
        return threadWrappedDriver.get();
    }

    public WebDriver getWrappedDriver() {
        if (Objects.isNull(threadWrappedDriver.get())) {
            setWrappedDriver(newProxyDriver());
        }
        return getPureWrappedDriver();
    }

    protected void setWrappedDriver(WebDriver webDriver) {
        if (Objects.nonNull(webDriver)) {
            threadWrappedDriver.set(webDriver);
            if (!allWrappedDrivers.contains(webDriver)) {
                allWrappedDrivers.add(webDriver);
            }
        } else {
            threadWrappedDriver.remove();
            allWrappedDrivers.remove(webDriver);
        }
    }

    private void setImplicitlyWait(WebDriver webDriver, int implicitlyWait) {
        if (timeoutsSupport) {
            try {
                webDriver.manage().timeouts().implicitlyWait(implicitlyWait, TimeUnit.SECONDS);
            } catch (org.openqa.selenium.UnsupportedCommandException e) {
                timeoutsSupport = false;
            }
        }
    }

    public void switchToDefaultContent() {
        if (framesSupport && Objects.nonNull(getPureWrappedDriver())) {
            try {
                if (Objects.nonNull(getPureWrappedDriver().switchTo())) {
                    getPureWrappedDriver().switchTo().defaultContent();
                }
            } catch (org.openqa.selenium.UnsupportedCommandException e) {
                framesSupport = false;
            }
        }
    }

    protected WebDriver newProxyDriver() {
        WebDriver driver = getDriverFactory().newInstance(getId());
        //driver.manage().timeouts().implicitlyWait(getImplicitlyWait(), TimeUnit.SECONDS);
        setImplicitlyWait(driver, getImplicitlyWait());
        return driver;
    }

    @Getter
    private int implicitlyWait;

    public void setImplicitlyWait(int seconds) {
        implicitlyWait = seconds;
        if (Objects.nonNull(getPureWrappedDriver())) {
            //getWrappedDriver().manage().timeouts().implicitlyWait(implicitlyWait, TimeUnit.SECONDS);
            setImplicitlyWait(getPureWrappedDriver(), implicitlyWait);
        }
    }

    @Deprecated
    public void setImplicitlywait(long seconds) { // Для обратной совместимости
        setImplicitlyWait((int) seconds);
    }

    public <R> R withImplicitlyWait(int seconds, Function<WebDriverFacade, R> webDriverFacadeConsumer) {
        int implicitlyWait = this.getImplicitlyWait();
        try {
            this.setImplicitlyWait(seconds);
            return webDriverFacadeConsumer.apply(this);
        } finally {
            this.setImplicitlyWait(implicitlyWait);
        }
    }

    @Getter
    @Setter
    private int defaultWaitTimeOut;

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
        if (allWrappedDrivers.size()>0 && isCloseDriverAfterTest()) {
            allWrappedDrivers.forEach(WebDriver::quit);
            allWrappedDrivers.clear();
            threadWrappedDriver = new ThreadLocal<>();
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
}
