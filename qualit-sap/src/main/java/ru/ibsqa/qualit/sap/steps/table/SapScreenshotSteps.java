package ru.ibsqa.qualit.sap.steps.table;

import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;
import ru.ibsqa.qualit.sap.driver.SapDriver;
import ru.ibsqa.qualit.sap.driver.SapSupportedDriver;
import ru.ibsqa.qualit.selenium.driver.IDriverManager;
import ru.ibsqa.qualit.selenium.driver.ISupportedDriver;
import ru.ibsqa.qualit.steps.IScreenshotSteps;
import ru.ibsqa.qualit.steps.DefaultScreenshotSteps;
import ru.ibsqa.qualit.utils.spring.SpringUtils;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Variant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;

@Component @Slf4j
public class SapScreenshotSteps extends DefaultScreenshotSteps implements IScreenshotSteps {

    @Override
    public void takeScreenshotToReport(String name, SeverityLevel level) {
        this.addScreenshot(name, this::getFullScreenshot);
    }

    /**
     * Скриншот экрана
     *
     * @return
     */
    @Override
    public InputStream getFullScreenshot() {
        SapDriver driver = (SapDriver) SpringUtils.getBean(IDriverManager.class).getLastDriver().getWrappedDriver();
        Variant variant = new ActiveXComponent(driver.getSession().invoke("findById", "wnd[0]").toDispatch()).invoke("HardCopy", this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath().replaceFirst("/", "").replace("classes/", "").replace("/", "\\") + "screenshot");
        if (variant.getString() != null){
            try {
                return new ByteArrayInputStream(Files.readAllBytes(new File(variant.getString()).toPath()));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    @Override
    public ConfigurationPriority getPriority() {
        return ConfigurationPriority.NORMAL;
    }

    @Override
    public List<ISupportedDriver> getSupportedDrivers() {
        return Collections.singletonList(SapSupportedDriver.SAP);
    }

}
