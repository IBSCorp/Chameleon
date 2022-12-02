package ru.ibsqa.qualit.elements.selenium;

import ru.ibsqa.qualit.selenium.driver.IDriverManager;
import ru.ibsqa.qualit.selenium.driver.WebDriverFacade;
import ru.ibsqa.qualit.selenium.enums.KeyEnum;
import ru.ibsqa.qualit.utils.spring.SpringUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.Coordinates;
import org.openqa.selenium.interactions.Locatable;
import org.openqa.selenium.interactions.MoveTargetOutOfBoundsException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.time.Duration;
import java.util.List;

@Slf4j
public abstract class WebElementFacade implements IFacadeSelenium {

    protected IDriverManager driverManager = SpringUtils.getBean(IDriverManager.class);

    private String driverId;

    private WebElement element;

    @Getter
    private Wait<WebDriver> wait;

    @Getter
    private String elementName;

    @Getter
    private int waitTimeOut;

    public WebElementFacade() {
    }

    public void pushArguments(final WebElement element, final String elementName, final int waitTimeOut, final String driverId) {
        this.element = element;
        this.elementName = elementName;
        this.driverId = driverId;
        final WebDriverFacade driver = getDriver();
        this.waitTimeOut = waitTimeOut < 0 ? getDriver().getDefaultWaitTimeOut() : waitTimeOut;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(getWaitTimeOut()), Duration.ofMillis(200)).ignoring(StaleElementReferenceException.class).ignoring(NoSuchElementException.class).ignoring(MoveTargetOutOfBoundsException.class);
    }

    public void pressKey(KeyEnum key) {
        try {
            Robot robot = new Robot();
            robot.setAutoDelay(250);
            robot.keyPress(key.getValue());
            robot.keyRelease(key.getValue());
        } catch (Exception e) {
        }
    }

    public void doubleClick() {
        Actions action = new Actions(getDriver());
        action.doubleClick(getWrappedElement()).perform();
    }

    public void type(String value) {
        clear();
        sendKeys(value);
    }

    @Override
    public String getPlaceholder() {
        return getAttribute("placeholder");
    }

    @Override
    public void click() {
        getWrappedElement().click();
    }

    @Override
    public void submit() {
        getWrappedElement().submit();
    }

    @Override
    public void sendKeys(CharSequence... keysToSend) {
        getWrappedElement().sendKeys(keysToSend);
    }

    @Override
    public void clear() {
        getWrappedElement().clear();
    }

    @Override
    public String getTagName() {
        return getWrappedElement().getTagName();
    }

    @Override
    public String getAttribute(String name) {
        return getWrappedElement().getAttribute(name);
    }

    @Override
    public boolean isSelected() {
        return getWrappedElement().isSelected();
    }

    @Override
    public boolean isEnabled() {
        return getWrappedElement().isEnabled();
    }

    @Override
    public String getText() {
        String text = getWrappedElement().getText();
        if (text.isEmpty() && getWrappedElement().getAttribute("value") != null) {
            return getWrappedElement().getAttribute("value");
        }
        return text;
    }

    @Override
    public List<WebElement> findElements(By by) {
        return getWrappedElement().findElements(by);
    }

    @Override
    public WebElement findElement(By by) {
        return getWrappedElement().findElement(by);
    }

    @Override
    public boolean isDisplayed() {
        try {
            if (getWrappedElement().isDisplayed()) return true;
            wait.until(ExpectedConditions.visibilityOf(getWrappedElement()));
            return getWrappedElement().isDisplayed();
        } catch (Exception e) {
            log.debug(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public Point getLocation() {
        return getWrappedElement().getLocation();
    }

    @Override
    public Dimension getSize() {
        return getWrappedElement().getSize();
    }

    @Override
    public Rectangle getRect() {
        return getWrappedElement().getRect();
    }

    @Override
    public String getCssValue(String propertyName) {
        return getWrappedElement().getCssValue(propertyName);
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
        return getWrappedElement().getScreenshotAs(target);
    }

    @Override
    public Coordinates getCoordinates() {
        if (element instanceof Locatable) {
            return ((Locatable) element).getCoordinates();
        }
        return null;
    }

    @Override
    public WebElement getWrappedElement() {
        return element;
    }

    @Override
    public WebDriverFacade getDriver() {
        return driverManager.getDriver(driverId);
    }

    @Override
    public String getErrorMsg() {
        return null;
    }

    @Override
    public String getLabel() {
        return null;
    }

    @Override
    public boolean isEditable() {
        try {
            getWrappedElement().isEnabled();
        } catch (NoSuchElementException ignored) {
            return false;
        }
        return true;
    }

    public boolean exists() {
        try {
            getWrappedElement().isDisplayed();
        } catch (NoSuchElementException ignored) {
            return false;
        }
        return true;
    }
}
