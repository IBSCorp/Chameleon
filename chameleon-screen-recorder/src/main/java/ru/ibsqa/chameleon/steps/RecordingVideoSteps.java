package ru.ibsqa.chameleon.steps;

import ru.ibsqa.chameleon.driver.WebDriverVideoFacade;
import ru.ibsqa.chameleon.selenium.driver.IDriverFacade;
import ru.ibsqa.chameleon.selenium.driver.IDriverManager;
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
        IDriverFacade driverFacade = driverManager.getLastDriver();
        if (driverFacade instanceof WebDriverVideoFacade){
            ((WebDriverVideoFacade)driverFacade).startVideo();
        }else{
            fail("Драйвер не поддерживает запись видео. Необходимо использовать WebDriverVideoFacade");
        }

    }

    @TestStep("запись видео остановлена")
    public File stopVideo(boolean attachToReport){
        IDriverFacade driverFacade = driverManager.getLastDriver();
        if (driverFacade instanceof WebDriverVideoFacade){
            return ((WebDriverVideoFacade)driverFacade).stopVideo(attachToReport);
        }else{
            fail("Драйвер не поддерживает запись видео. Необходимо использовать WebDriverVideoFacade");
        }
        return null;
    }
}
