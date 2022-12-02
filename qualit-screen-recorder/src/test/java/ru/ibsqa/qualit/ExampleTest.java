package ru.ibsqa.qualit;

import ru.ibsqa.qualit.pages.YandexMainPage;
import ru.ibsqa.qualit.selenium.driver.IDriverManager;
import ru.ibsqa.qualit.steps.RecordingVideoSteps;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@ExtendWith(SpringExtension.class)
@ContextConfiguration("classpath:spring.xml")
@TestExecutionListeners(inheritListeners = false, listeners =
        {DependencyInjectionTestExecutionListener.class})
public class ExampleTest {

    @Autowired
    RecordingVideoSteps recordingVideoSteps;

    @Autowired
    IDriverManager driverManager;

    @Autowired
    YandexMainPage yandexMainPage;

    @Test
    public void searchRepositoryTest(){
        yandexMainPage.isLoaded();
        recordingVideoSteps.startVideo();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
        File file =  recordingVideoSteps.stopVideo(true);
        assertNotNull(file);
    }

    @AfterEach
    public void closeBrowser(){
        driverManager.closeAllDrivers();
    }
}
