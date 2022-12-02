package ru.ibsqa.qualit.steps;

import ru.ibsqa.qualit.driver.WebDriverVideoFacade;
import ru.ibsqa.qualit.selenium.driver.IDriverManager;
import ru.ibsqa.qualit.selenium.driver.WebDriverFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

import static org.junit.jupiter.api.Assertions.fail;

@Component
public class RecordingVideoSteps extends AbstractSteps {

    @Autowired
    private IDriverManager driverManager;

    @TestStep("выполняется запись видео")
    public void startVideo(){
        WebDriverFacade driverFacade = driverManager.getLastDriver();
        if (driverFacade instanceof WebDriverVideoFacade){
            ((WebDriverVideoFacade)driverFacade).startVideo();
        }else{
            fail("Драйвер не поддерживает запись видео. Необходимо использовать WebDriverVideoFacade");
        }

    }

    @TestStep("запись видео остановлена")
    public File stopVideo(boolean attachToReport){
        WebDriverFacade driverFacade = driverManager.getLastDriver();
        if (driverFacade instanceof WebDriverVideoFacade){
            return ((WebDriverVideoFacade)driverFacade).stopVideo(attachToReport);
        }else{
            fail("Драйвер не поддерживает запись видео. Необходимо использовать WebDriverVideoFacade");
        }
        return null;
    }
}
