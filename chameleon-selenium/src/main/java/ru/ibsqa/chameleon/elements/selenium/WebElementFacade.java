package ru.ibsqa.chameleon.elements.selenium;

import ru.ibsqa.chameleon.selenium.driver.IDriverManager;
import ru.ibsqa.chameleon.selenium.driver.WebDriverFacade;
import ru.ibsqa.chameleon.selenium.enums.KeyEnum;
import ru.ibsqa.chameleon.utils.spring.SpringUtils;
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
        } catch (Exception ignore) {
        }
    }

    public void doubleClick() {
        wait.until(ExpectedConditions.elementToBeClickable(getWrappedElement()));
        Actions action = new Actions(getDriver());
        action.doubleClick(getWrappedElement()).perform();
    }

    public void type(String value) {
        clear();
        sendKeys(value);
    }

    @Override
    public String getPlaceholder() {
        return getAttribute("placeholder").trim().replaceAll("\u00A0", " ");
    }

    @Override
    public void click() {
        wait.until(ExpectedConditions.elementToBeClickable(getWrappedElement()));
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
    public boolean isAbsent() {
        return getWrappedElement().equals(UNKNOWN_WEB_ELEMENT);
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
// Ожидание выполняется в шагах checkFieldExists, checkFieldNotExists, fieldIsNotDisplayed, fieldIsDisplayed и в методе waitToDisplayed
// Данный метод начиная с версии 3.3 возвращает моментальное значение видимости поля
//        try {
//            if (getWrappedElement().isDisplayed()) return true;
//            wait.until(ExpectedConditions.visibilityOf(getWrappedElement()));
//            return getWrappedElement().isDisplayed();
//        } catch (Exception e) {
//            log.debug(e.getMessage(), e);
//            return false;
//        }
        try {
            return getWrappedElement().isDisplayed();
        } catch (WebDriverException e) {
            log.debug(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean waitToDisplayed() {
        try {
            if (getWrappedElement().isDisplayed()) return true;
            wait.until(ExpectedConditions.visibilityOf(getWrappedElement()));
            return getWrappedElement().isDisplayed();
        } catch (WebDriverException e) {
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
            return getWrappedElement().isEnabled();
        } catch (NoSuchElementException ignored) {
            return false;
        }
    }

    public boolean exists() {
        if (isAbsent()) { // Здесь происходит быстрая проверка отсутствия поля на странице
            return false;
        }
        try {
            getWrappedElement().isDisplayed();
        } catch (NoSuchElementException ignored) {
            return false;
        }
        return true;
    }


    public final static WebElement UNKNOWN_WEB_ELEMENT = new WebElement() {
        @Override
        public void click() {

        }

        @Override
        public void submit() {

        }

        @Override
        public void sendKeys(CharSequence... keysToSend) {

        }

        @Override
        public void clear() {

        }

        @Override
        public String getTagName() {
            return null;
        }

        @Override
        public String getAttribute(String name) {
            return null;
        }

        @Override
        public boolean isSelected() {
            return false;
        }

        @Override
        public boolean isEnabled() {
            return false;
        }

        @Override
        public String getText() {
            return null;
        }

        @Override
        public List<WebElement> findElements(By by) {
            return null;
        }

        @Override
        public WebElement findElement(By by) {
            return null;
        }

        @Override
        public boolean isDisplayed() {
            return false;
        }

        @Override
        public Point getLocation() {
            return null;
        }

        @Override
        public Dimension getSize() {
            return null;
        }

        @Override
        public Rectangle getRect() {
            return null;
        }

        @Override
        public String getCssValue(String propertyName) {
            return null;
        }

        @Override
        public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
            return null;
        }
    };
}
