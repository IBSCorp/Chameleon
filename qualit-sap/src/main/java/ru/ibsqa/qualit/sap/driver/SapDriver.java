package ru.ibsqa.qualit.sap.driver;


import ru.ibsqa.qualit.sap.search_context.SapSearchContext;
import ru.ibsqa.qualit.selenium.driver.WebDriverFacade;
import ru.ibsqa.qualit.utils.spring.SpringUtils;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.logging.Logs;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
public class SapDriver<T extends WebElement> extends WebDriverFacade implements SapSearchContext {

    private ActiveXComponent SAPROTWr;
    private ActiveXComponent GUIApp;

    @Getter
    private ActiveXComponent session;

    private Variant mVariant;
    private Dispatch ROTEntry;
    private Variant ScriptEngine;
    private DesiredCapabilities mDesiredCapabilities;
    private final SapSearchBy searchBy = SpringUtils.getBean(SapSearchBy.class);

    @Getter
    protected SapDriverFactory driverFactory;

    @Getter
    private ActiveXComponent connection;

    public SapDriver(DesiredCapabilities desiredCapabilities) {
        if (!existSapLogon()){
            launchApp(desiredCapabilities.asMap().get("app.path").toString());
        }
        this.mDesiredCapabilities = desiredCapabilities;
        ComThread.InitSTA();

        //-Set SapGuiAuto = GetObject("SAPGUI")-----------------------------
        this.SAPROTWr = new ActiveXComponent("SapROTWr.SapROTWrapper");
        this.ROTEntry = SAPROTWr.invoke("GetROTEntry", "SAPGUI").toDispatch();
        //-Set application = SapGuiAuto.GetScriptingEngine----------------
        this.ScriptEngine = Dispatch.call(ROTEntry, "GetScriptingEngine");
        this.GUIApp = new ActiveXComponent(ScriptEngine.toDispatch());
        ActiveXComponent newSession;
//        try{
//            connection = new ActiveXComponent(GUIApp.invoke("Children", 0).toDispatch());
//            newSession = new ActiveXComponent(connection.invoke("Children", 0).toDispatch());
//        }catch (Exception e){
//            newSession = new ActiveXComponent(openConnections().invoke("Children", 0).toDispatch());
//        }
        Variant variant = openConnections().invoke("Children", 0);
        this.mVariant = variant;
        newSession = new ActiveXComponent(variant.toDispatch());
        Utils.delayInsec(3);
        this.session = newSession;
        activateCurrentWindowSap();
    }

    private void launchApp(String appPath) {
        try {
            Utils.delayInsec(5);
            new ProcessBuilder(appPath).start();
            Utils.delayInsec(5);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void activateCurrentWindowSap() {
        new ActiveXComponent(session.invoke("findById", "wnd[0]").toDispatch()).invoke("JumpBackward");
    }

    @Override
    protected WebDriver newProxyDriver() {
        WebDriver driver = driverFactory.newInstance(getId());

        return driver;
    }


    private void closeApp() {
        try {
            Runtime.getRuntime().exec("taskkill /T /F /IM \"saplogon.exe\"");
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private boolean existSapLogon(){
        try {
            String line;
            Process p = Runtime.getRuntime().exec(System.getenv("windir") + "\\system32\\" + "tasklist.exe");
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null){
                if (line.contains("saplogon.exe")){
                    log.info("Найден процесс saplogon.exe");
                    return true;
                }
            }
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public void get(String url) {

    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> outputType) throws WebDriverException {
        Variant variant = new ActiveXComponent(session.invoke("findById", "wnd[0]").toDispatch()).invoke("HardCopy", this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath().replaceFirst("/", "").replace("classes/", "").replace("/", "\\") + "screenshot");
        if (variant.getString() != null){
            try {
               return (X) Files.readAllBytes(new File(variant.getString()).toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
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
        return null;
    }

    @Override
    public T findElement(By by) {
        String locator = by.toString().replaceAll("By.xpath: ", "");
        log.debug(locator);
        return (T) searchBy.create(this, by);
    }

    @Override
    public String getPageSource() {
        return null;
    }

    @Override
    public void close() {
        closeConnections();
    }

    @Override
    public void quit() {
        closeConnections();
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
    public SapTargetLocator switchTo() {
        return new SapTargetLocator();
    }

    @Override
    public Navigation navigate() {
        return null;
    }

    @Override
    public SapOptions manage() {
        return new SapOptions();
    }


    private ActiveXComponent openConnections() {
        connection = new ActiveXComponent(GUIApp.invoke("OpenConnection",
                mDesiredCapabilities.getCapability("stand").toString()).toDispatch());
        return connection;
    }

    public void connectToStand(String stand) {
        ActiveXComponent activeXComponent =  new ActiveXComponent(GUIApp.invoke("OpenConnection",
                stand).toDispatch());
        this.session = new ActiveXComponent(activeXComponent.invoke("Children", 0).toDispatch());
    }

    public void switchToSession(String sessionName) {
        long maxWaitTime = System.currentTimeMillis() + getConfiguration().getImplicitlyWait() * 1000;
        int countSessions;
        do {
            countSessions = Integer.parseInt(new ActiveXComponent(connection.getProperty("sessions").toDispatch()).getProperty("Count").toString());
            for (int i = 0; i < countSessions; i++) {
                if (new ActiveXComponent(connection.invoke("Children", i).toDispatch()).getProperty("Name").toString().equals(sessionName)) {
                    session = new ActiveXComponent(connection.invoke("Children", i).toDispatch());
                    return;
                }
            }
        }
        while (countSessions == 1 || System.currentTimeMillis() < maxWaitTime);
        fail(String.format("Не найдена сессия с наименованием [%s]", sessionName));
    }


    public void closeConnections() {
        if (null != session) {
            try {
                int collectionCount = new ActiveXComponent(GUIApp.getProperty("Children").toDispatch()).getProperty("count").getInt();
                for (int i = 0; i < collectionCount; i++) {
                    ActiveXComponent connection = new ActiveXComponent(GUIApp.invoke("Children", i).toDispatch());
                    connection.invoke("CloseConnection");
                    Utils.delayInsec(2);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            session = null;
        }

    }

    public Variant getVariant() {
        return mVariant;
    }

    @Override
    public SapDriver getSapDriver() {
        return this;
    }


    class SapWindow implements Window {

        @Override
        public void setSize(Dimension targetSize) {

        }

        @Override
        public void setPosition(Point targetPosition) {

        }

        @Override
        public Dimension getSize() {
            ActiveXComponent mainWindow = new ActiveXComponent(session.invoke("findById", "wnd[0]").toDispatch());
            int width =  Integer.parseInt(mainWindow.getProperty("width").toString());
            int height = Integer.parseInt(mainWindow.getProperty("height").toString());
            return new Dimension(width, height);
        }

        @Override
        public Point getPosition() {
            ActiveXComponent mainWindow = new ActiveXComponent(session.invoke("findById", "wnd[0]").toDispatch());
            int x =  Integer.parseInt(mainWindow.getProperty("Top").toString());
            int y = Integer.parseInt(mainWindow.getProperty("Left").toString());
            return new Point(x, y);
        }

        @Override
        public void maximize() {
            new ActiveXComponent(session.invoke("findById", "wnd[0]").toDispatch()).invoke("maximize");
        }

        @Override
        public void minimize() {
            new ActiveXComponent(session.invoke("findById", "wnd[0]").toDispatch()).invoke("minimize");
        }

        @Override
        public void fullscreen() {

        }
    }

    class SapOptions implements Options {


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
            return new SapTimeouts();
        }

        @Override
        public SapWindow window() {
            return new SapWindow();
        }

        @Override
        public Logs logs() {
            return null;
        }
    }

    class SapTargetLocator implements TargetLocator {

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

    class SapTimeouts implements Timeouts{

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




}
