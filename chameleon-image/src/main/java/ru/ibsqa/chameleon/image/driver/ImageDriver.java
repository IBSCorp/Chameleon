package ru.ibsqa.chameleon.image.driver;

import ru.ibsqa.chameleon.image.element.ImageElement;
import ru.ibsqa.chameleon.selenium.driver.WebDriverFacade;
import ru.ibsqa.chameleon.selenium.driver.configuration.IDriverConfiguration;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Point;
import org.openqa.selenium.*;
import org.openqa.selenium.logging.Logs;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ImageDriver<T extends WebElement> extends WebDriverFacade {

    private DesiredCapabilities capabilities;

    @Getter
    private IDriverConfiguration configuration;

    @Getter
    protected ImageDriverFactory driverFactory;

    @Getter
    @Setter
    private int defaultWaitTimeOut;


    private Robot robot;

    public ImageDriver(DesiredCapabilities desiredCapabilities) {
        capabilities = desiredCapabilities;
    }

    public void setConfiguration(IDriverConfiguration configuration) {
        this.configuration = configuration;
        if (implicitlyWait <= 0) {
            setImplicitlyWait(getConfiguration().getImplicitlyWait());
        }
        if (defaultWaitTimeOut <= 0) {
            setDefaultWaitTimeOut(getConfiguration().getDefaultWaitTimeOut());
        }
    }


    public void setDriverFactory(ImageDriverFactory driverFactory) {
        this.driverFactory = driverFactory;
        setConfiguration(driverFactory.getConfiguration());
    }



    public Robot getRobot() {
        if (Objects.isNull(robot)) {
            try {
                robot = new Robot();
                robot.setAutoDelay(10);
            } catch (AWTException e) {
                log.error(Objects.nonNull(e.getMessage()) ? e.getMessage() : e.toString(), e);
                throw new RuntimeException("Не удалось создать Robot");
            }
        }
        return robot;
    }



    @Override
    public void get(String url) {

    }

    @Override
    public String getCurrentUrl() {
        return null;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public List<WebElement> findElements(By by) {
        return Arrays.asList(findElement(by));
    }

    @Override
    public T findElement(By by) {
        String image = by.toString().replaceAll("By.xpath: ","");
        return (T) new ImageElement(image,  this);
    }

    @Override
    public String getPageSource() {
        return null;
    }

    @Override
    public void close() {

    }

    @Override
    public void quit() {

    }

    @Override
    public void quitAll() {

    }

    @Override
    public Set<String> getWindowHandles() {
        return null;
    }

    @Override
    public String getWindowHandle() {
        return null;
    }

    @Override
    public TargetLocator switchTo() {
        return new ImageTargetLocator();
    }

    @Override
    public Navigation navigate() {
        return null;
    }

    @Override
    public ImageOptions manage() {
        return new ImageOptions();
    }



    class ImageTargetLocator implements TargetLocator {

        @Override
        public WebDriver frame(int i) {
            return null;
        }

        @Override
        public WebDriver frame(String s) {
            return null;
        }

        @Override
        public WebDriver frame(WebElement webElement) {
            return null;
        }

        @Override
        public WebDriver parentFrame() {
            return null;
        }

        @Override
        public WebDriver window(String s) {
            return null;
        }

        @Override
        public WebDriver newWindow(WindowType typeHint) {
            return null;
        }

        @Override
        public WebDriver defaultContent() {
            return null;
        }

        @Override
        public WebElement activeElement() {
            return null;
        }

        @Override
        public Alert alert() {
            return null;
        }
    }


    class ImageTimeouts implements Timeouts{

        @Override
        public Timeouts implicitlyWait(long l, TimeUnit timeUnit) {
            return null;
        }

        @Override
        public Timeouts setScriptTimeout(long l, TimeUnit timeUnit) {
            return null;
        }

        @Override
        public Timeouts pageLoadTimeout(long l, TimeUnit timeUnit) {
            return null;
        }
    }

    class ImageWindow implements Window {

        @Override
        public void setSize(org.openqa.selenium.Dimension targetSize) {

        }

        @Override
        public void setPosition(org.openqa.selenium.Point targetPosition) {

        }


        @Override
        public org.openqa.selenium.Dimension getSize() {
            return null;
        }

        @Override
        public Point getPosition() {
            return null;
        }


        @Override
        public void maximize() {
            // TODO
        }

        @Override
        public void minimize() {

        }

        @Override
        public void fullscreen() {

        }
    }

    class ImageOptions implements Options {


        @Override
        public void addCookie(Cookie cookie) {

        }

        @Override
        public void deleteCookieNamed(String name) {

        }

        @Override
        public void deleteCookie(Cookie cookie) {

        }

        @Override
        public void deleteAllCookies() {

        }

        @Override
        public Set<Cookie> getCookies() {
            return null;
        }

        @Override
        public Cookie getCookieNamed(String name) {
            return null;
        }

        @Override
        public Timeouts timeouts() {
            return new ImageTimeouts();
        }

        @Override
        public ImageWindow window() {
            return new ImageWindow();
        }

        @Override
        public Logs logs() {
            return null;
        }

    }

    private int implicitlyWait;

    @Override
    public void setImplicitlyWait(int seconds) {
        implicitlyWait = seconds;
    }

    public int getImplicitlyWait() {
        return Objects.nonNull(implicitlyWait) ? implicitlyWait : getConfiguration().getImplicitlyWait();
    }

}
