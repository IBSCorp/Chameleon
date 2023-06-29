package ru.ibsqa.chameleon.selenium.driver;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.winium.DesktopOptions;
import org.openqa.selenium.winium.WiniumDriver;
import org.springframework.stereotype.Component;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;
import ru.ibsqa.chameleon.selenium.driver.configuration.IDriverConfiguration;
import ru.ibsqa.chameleon.utils.commandpromt.ICommandPrompt;
import ru.ibsqa.chameleon.utils.spring.SpringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

@DriverFactory(priority = ConfigurationPriority.LOW)
@NoArgsConstructor
@Slf4j
@Component
public class WiniumDriverFactory extends AbstractDriverFactory<WiniumSupportedDriver> {

    private final ICommandPrompt commandPrompt = SpringUtils.getBean(ICommandPrompt.class);

    public WiniumDriverFactory(IDriverConfiguration configuration) {
        super(configuration);
    }

    // TODO реализовать сохранение драйвера в файл по аналогии с SeleniumDriverFactory
    @Override
    public WebDriver newInstance(String driverId) {
        WebDriver driver = null;

        initDriver();
        DesktopOptions options = new DesktopOptions();
        String applicationUrl = getConfiguration().getApplicationUrl();
        if (null == applicationUrl || applicationUrl.isEmpty() || getConfiguration().isConnectToRunningApp()) {
            options.setDebugConnectToRunningApp(true);
        } else {
            options.setApplicationPath(applicationUrl);
        }
        try {
            driver = new WiniumDriver(new URL("http://localhost:9999"), options);
        } catch (MalformedURLException e) {
            log.error(e.getMessage(), e);
        }

        //driver.manage().timeouts().implicitlyWait(configuration.getImplicitlyWait(), TimeUnit.SECONDS);

        return driver;
    }

    private final String defaultWiniumDriverLocation = "/drivers";
    private final String defaultWiniumDriverName = "Winium.Desktop.Driver.exe";
    private void initDriver() {
        String applicationUrl = getConfiguration().getApplicationUrl();
        String driverPath = getConfiguration().getDriverPath();
        String appExe;
        if (!StringUtils.isEmpty(applicationUrl) && !getConfiguration().isConnectToRunningApp()) {
            String[] parts = applicationUrl.split("\\\\");
            appExe = parts[parts.length - 1];
            // Остановить приложение
            commandPrompt.runCommand(String.format("taskkill /F /IM \"%s\"", appExe));
        }
        String driverExe = defaultWiniumDriverName;
        if (!StringUtils.isEmpty(driverPath)) {
            String[] parts = driverPath.split("\\\\");
            driverExe = parts[parts.length - 1];
        }

        // Остановить драйвер
        commandPrompt.runCommand(String.format("taskkill /F /IM \"%s\"", driverExe));
        // Запустить драйвер
        if (!StringUtils.isEmpty(driverPath)) {
            commandPrompt.runCommand(driverPath);
        } else {
            try (InputStream is = SpringUtils.openResourceOrFile(String.format("classpath*:%s/%s", defaultWiniumDriverLocation, defaultWiniumDriverName))) {
                File targetFile = new File(String.format("%s/%s", defaultWiniumDriverLocation, defaultWiniumDriverName));
                if (!targetFile.exists()) {
                    FileUtils.copyInputStreamToFile(is, targetFile);
                }
                commandPrompt.runCommand(targetFile.getAbsolutePath());
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }
    }

}
