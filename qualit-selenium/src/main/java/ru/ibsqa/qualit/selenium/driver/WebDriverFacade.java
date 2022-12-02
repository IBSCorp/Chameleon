package ru.ibsqa.qualit.selenium.driver;

import ru.ibsqa.qualit.page_factory.locator.AbstractElementLocatorFactory;
import ru.ibsqa.qualit.selenium.driver.configuration.IDriverConfiguration;
import ru.ibsqa.qualit.utils.spring.SpringUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Keyboard;
import org.openqa.selenium.interactions.Mouse;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class WebDriverFacade implements WebDriver, TakesScreenshot, HasInputDevices, JavascriptExecutor, HasCapabilities {

    @Getter @Setter
    private boolean defaultDriver;

    @Setter(AccessLevel.PROTECTED)
    protected WebDriver wrappedDriver;

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


    public WebDriver getWrappedDriver(){
        if (wrappedDriver == null) {
            setWrappedDriver(newProxyDriver());
        }
        return wrappedDriver;
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
        if (framesSupport && Objects.nonNull(wrappedDriver)) {
            try {
                if (Objects.nonNull(wrappedDriver.switchTo())) {
                    wrappedDriver.switchTo().defaultContent();
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
        if (wrappedDriver != null) {
            //wrappedDriver.manage().timeouts().implicitlyWait(implicitlyWait, TimeUnit.SECONDS);
            setImplicitlyWait(wrappedDriver, implicitlyWait);
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
        if (null != wrappedDriver && isCloseDriverAfterTest()) {
            getWrappedDriver().quit();
            wrappedDriver = null;
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
    public Keyboard getKeyboard() {
        return ((HasInputDevices) getWrappedDriver()).getKeyboard();
    }

    @Override
    public Mouse getMouse() {
        return ((HasInputDevices) getWrappedDriver()).getMouse();
    }

    public void showBrowser(){
        throw new UnsupportedOperationException();
    }
}
