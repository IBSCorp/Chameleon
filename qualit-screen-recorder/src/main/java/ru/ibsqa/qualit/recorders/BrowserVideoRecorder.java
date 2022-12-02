package ru.ibsqa.qualit.recorders;

import ru.ibsqa.qualit.ApplicationUtilSteps;
import ru.ibsqa.qualit.selenium.driver.IDriverManager;
import com.google.common.io.Files;
import ru.ibsqa.qualit.reporter.TestAttachment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
public class BrowserVideoRecorder extends AbstractVideoRecorder {

    @Autowired
    private IDriverManager driverManager;

    @Autowired
    private ApplicationUtilSteps applicationUtilSteps;

    @Override
    public void start() {
        applicationUtilSteps.showWindow(driverManager.getLastDriver());
        try {
            Robot robot = new Robot();
            robot.setAutoDelay(250);
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_SHIFT);
            robot.keyPress(KeyEvent.VK_7);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyRelease(KeyEvent.VK_SHIFT);
            robot.keyRelease(KeyEvent.VK_7);
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public File stop(boolean save) {
        String script = "var editorExtensionId = \"" + getVideoRecorderConfiguration().getExtensionId() + "\";\n" +
                "var callback = arguments[arguments.length - 1];\n" +
                "chrome.runtime.sendMessage(editorExtensionId, {command: 'stop_video'},\n" +
                "function(response) {\n" +
                "callback(response.path);\n" +
                "  } \n" +
                ");";
        String path = (String) driverManager.getLastDriver().executeAsyncScript(script);
        waitForFileDownloaded(path);
        File file = new File(path);
        if (save){
            attachVideo(file);
        }
        return file;
    }

    private void waitForFileDownloaded(String path){
        int count=0;
        log.info(Boolean.valueOf(new File(path).exists()).toString());
        while (!new File(path).exists()){
            if (count == 180){
                fail("Файл с видео не загружен");
                break;
            }
            try {
                Thread.sleep(1000);
                count++;
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    @Override
    @TestAttachment(value = "VIDEO", mimeType = "video/webm")
    protected byte[] attachVideo(File file) {
        try {
            return Files.toByteArray(file);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return new byte[0];
        }
    }
}
