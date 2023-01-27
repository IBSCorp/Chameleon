package ru.ibsqa.qualit.uia.driver;

import bsh.EvalError;
import bsh.Interpreter;
import ru.ibsqa.qualit.selenium.driver.WebDriverFacade;
import ru.ibsqa.qualit.uia.launcher.IAppLauncher;
import ru.ibsqa.qualit.uia.search_context.UiaSearchContext;
import ru.ibsqa.qualit.utils.waiting.WaitingUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import mmarquee.automation.AutomationException;
import mmarquee.automation.UIAutomation;
import org.openqa.selenium.Point;
import org.openqa.selenium.*;
import org.openqa.selenium.logging.Logs;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
public class UiaDriver<T extends WebElement> extends WebDriverFacade implements UiaSearchContext {

    private DesiredCapabilities capabilities;

    @Autowired
    private UiaSearchBy searchBy;

    @Autowired
    private WaitingUtils waitingUtils;

    @Autowired
    private IAppLauncher appLauncher;

    private mmarquee.automation.controls.Window automationWindow;

    @Getter
    @Setter
    private boolean temporaryMainWindow;

    @Getter
    @Setter
    private String mainWindowTitle;

    @Getter
    @Setter
    private String mainWindowTitleRegexp;

    @Getter
    @Setter
    private String mainWindowLocator;

    public void resetAutomationWindow() {
        automationWindow = null;
    }

    public mmarquee.automation.controls.Window getAutomationWindow() {
        if (Objects.isNull(automationWindow) || isTemporaryMainWindow()) { // Если окно не найдено, то дождемся
            waitAutomationWindow();
        }
        return automationWindow;
    }

    private mmarquee.automation.controls.Window waitAutomationWindow() {
        long timeout = Long.parseLong(capabilities.asMap().get("app.load.timeout").toString()) * 1000;

        waitingUtils.waiting(timeout, () -> {
            try {
                if (Objects.nonNull(getMainWindowLocator())) {
                    automationWindow = findAutomationWindow(getMainWindowLocator());
                } else if (Objects.nonNull(getMainWindowTitle())) {
                    automationWindow = findAutomationWindow(getMainWindowTitle(), false);
                } else if (Objects.nonNull(getMainWindowTitleRegexp())) {
                    automationWindow = findAutomationWindow(getMainWindowTitleRegexp(), true);
                }
                if (Objects.nonNull(automationWindow)) {
                    automationWindow.focus();
                    if (getConfiguration().isMaximizeWindow()) {
                        automationWindow.maximize();
                    }
                }
            } catch (NoWindowFoundException | AutomationException ignore) {
            }
            return Objects.nonNull(automationWindow);
        });

        assertNotNull(automationWindow, "Не найдено окно приложения");

        return automationWindow;
    }

    private Robot robot;

    public UiaDriver(DesiredCapabilities desiredCapabilities) {
        capabilities = desiredCapabilities;
    }

    @PostConstruct
    private void initUiaDriver() {
        if (!getConfiguration().isConnectToRunningApp()) {
            appLauncher.start();
        }
    }

    public UIAutomation getAutomation() {
        return UIAutomation.getInstance();
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

    private mmarquee.automation.controls.Window findAutomationWindow(String title, boolean isRegexp) {
        mmarquee.automation.controls.Window window = null;
        try {
            for (mmarquee.automation.controls.Window active : getAutomation().getDesktopWindows()) {
                String windowName = "";
                try {
                    windowName = active.getElement().getName();
                    if (windowName.isEmpty() || windowName == null) {
                        windowName = active.getClassName();
                    }
                } catch (AutomationException e) {
                    log.warn(e.getMessage(), e);
                }
                if ((!isRegexp && windowName.equals(title)) ||
                        (isRegexp && windowName.matches(title))) {
                    window = active;
                    break;
                }
            }
        } catch (AutomationException e) {
            log.error(Objects.nonNull(e.getMessage()) ? e.getMessage() : e.toString(), e);
        }
        if (Objects.isNull(window)) {
            throw new NoWindowFoundException(String.format("Не найдено окно [%s]", title));
        }
        return window;
    }

    private mmarquee.automation.controls.Window findAutomationWindow(String locator) {
        mmarquee.automation.controls.Window window = null;
        try {
            locator = locator.replaceAll("\\\\", "\\\\\\\\");
            locator = locator.replaceAll("'", "\"");
            Locator desktop = Locator.of(this.getAutomation().getDesktop());
            Interpreter interpreter = new Interpreter();
            interpreter.set("locator", desktop);
            Locator temp = (Locator) interpreter.eval(locator);

            //TODO: вынести этот алгоритм в отдельный метод
            if (Locator.NOT_FOUND.equals(temp) || Objects.isNull(temp)) {
                throw new NoWindowFoundException(String.format("Не найдено окно с локатором [%s]", locator));
            }

            window = (mmarquee.automation.controls.Window) temp.get();
        } catch (EvalError e) {
            e.printStackTrace();
        }

        return window;
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
        return (T) searchBy.create(this, by);
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
    public Set<String> getWindowHandles() {
        return null;
    }

    @Override
    public String getWindowHandle() {
        return null;
    }

    @Override
    public TargetLocator switchTo() {
        return new UiaTargetLocator();
    }

    @Override
    public Navigation navigate() {
        return null;
    }

    @Override
    public UiaOptions manage() {
        return new UiaOptions();
    }

    @Override
    public UiaDriver getUiaDriver() {
        return this;
    }

    class UiaTargetLocator implements TargetLocator {

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

    class UiaWindow implements Window {

        @Override
        public void setSize(org.openqa.selenium.Dimension targetSize) {

        }

        @Override
        public void setPosition(Point targetPosition) {

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

    class UiaOptions implements Options {


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
            return null;
        }

        @Override
        public UiaWindow window() {
            return new UiaWindow();
        }

        @Override
        public Logs logs() {
            return null;
        }

    }

    private Long implicitlyWait;

    @Override
    public void setImplicitlywait(long seconds) {
        implicitlyWait = seconds;
    }

    public Long getImplicitlywait() {
        return Objects.nonNull(implicitlyWait) ? implicitlyWait : getConfiguration().getImplicitlyWait();
    }

}
