package ru.ibsqa.qualit.driver;

import ru.ibsqa.qualit.WinProcessSteps;
import ru.ibsqa.qualit.Windows32Utils;
import ru.ibsqa.qualit.recorders.IVideoRecorder;
import ru.ibsqa.qualit.utils.spring.SpringUtils;
import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.CommandExecutor;
import org.openqa.selenium.remote.service.DriverCommandExecutor;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.springframework.util.ReflectionUtils;
import ru.ibsqa.qualit.selenium.driver.WebDriverFacade;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Objects;

public class WebDriverVideoFacade extends WebDriverFacade implements TakeVideo {

    @Getter @Setter
    private IVideoRecorder videoRecorder;

    private long browserPid;

    @Override
    public void startVideo() {
        videoRecorder.start();
    }

    @Override
    public File stopVideo(boolean save) {
        return videoRecorder.stop(save);
    }
    @Override
    public WebDriver getWrappedDriver(){
        if (Objects.isNull(super.getPureWrappedDriver())) {
            WebDriver wrappedDriver = super.getWrappedDriver();
            if (wrappedDriver instanceof EventFiringWebDriver) {
                wrappedDriver = ((EventFiringWebDriver) wrappedDriver).getWrappedDriver();
            }
            if (wrappedDriver instanceof ChromeDriver) {
                String driverName = driverFactory.getConfiguration().getDriverType().name().toLowerCase();
                Field executor = ReflectionUtils.findField(wrappedDriver.getClass(), "executor");
                ReflectionUtils.makeAccessible(executor);
                CommandExecutor commandExecutor = (CommandExecutor) ReflectionUtils.getField(executor, wrappedDriver);
                int port = ((DriverCommandExecutor) commandExecutor).getAddressOfRemoteServer().getPort();
                WinProcessSteps winProcessSteps = SpringUtils.getBean(WinProcessSteps.class);
                long pidDriver = winProcessSteps.getPidByPort(port);
                this.browserPid = winProcessSteps.getParentPid(pidDriver, driverName);
            }
        }

        return super.getPureWrappedDriver();
    }

    @Override
    public void showBrowser(){
        Windows32Utils.showWindow(browserPid);
    }
}
