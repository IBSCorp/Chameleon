package ru.ibsqa.chameleon.selenium.driver;

import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Interactive;
import ru.ibsqa.chameleon.selenium.driver.configuration.IDriverConfiguration;

import java.util.function.Function;

public interface IDriverFacade extends WebDriver, TakesScreenshot, JavascriptExecutor, HasCapabilities, Interactive {
    String getId();
    boolean isDefaultDriver();
    IDriverConfiguration getConfiguration();
    void setConfiguration(IDriverConfiguration configuration);
    IDriverFactory getDriverFactory();
    void setDriverFactory(IDriverFactory driverFactory);
    boolean hasWrappedDriver();
    WebDriver getWrappedDriver();
    void switchToDefaultContent();
    void maximizeWindow();
    void showBrowser();
    void quitAll();
    boolean isTimeoutsSupport();
    void setTimeoutsSupport(boolean timeoutsSupport);
    boolean isFramesSupport();
    void setFramesSupport(boolean framesSupport);
    int getImplicitlyWait();
    void setImplicitlyWait(int seconds);
    int getDefaultWaitTimeOut();
    void setDefaultWaitTimeOut(int seconds);
    <R> R withImplicitlyWait(int seconds, Function<IDriverFacade, R> consumer);
}
