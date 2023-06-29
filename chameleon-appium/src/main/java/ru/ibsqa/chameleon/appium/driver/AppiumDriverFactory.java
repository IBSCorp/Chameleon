package ru.ibsqa.chameleon.appium.driver;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;
import ru.ibsqa.chameleon.i18n.ILocaleManager;
import ru.ibsqa.chameleon.selenium.driver.DriverFactory;
import ru.ibsqa.chameleon.selenium.driver.AbstractDriverFactory;
import ru.ibsqa.chameleon.selenium.driver.configuration.IDriverConfiguration;
import ru.ibsqa.chameleon.selenium.driver.exceptions.NoSupportedDriverException;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;

import java.lang.reflect.Constructor;
import java.net.URL;

@DriverFactory(priority = ConfigurationPriority.LOW)
@NoArgsConstructor
@Slf4j
@Component
public class AppiumDriverFactory extends AbstractDriverFactory<AppiumSupportedDriver> {

    private AppiumDriverLocalService service;

    public AppiumDriverFactory(IDriverConfiguration configuration) {
        super(configuration);
    }

    // TODO реализовать сохранение драйвера в файл по аналогии с SeleniumDriverFactory
    @Override
    public WebDriver newInstance(String driverId) {
        AppiumDriver driver;
//        initDriver();
        try {
            Class<? extends AppiumDriver> aClass = getConfiguration().getDriverType().getAsClass().asSubclass(AppiumDriver.class);
            Constructor<? extends AppiumDriver> constructor = aClass.getConstructor(URL.class, Capabilities.class);
            driver = constructor.newInstance(new URL("http://127.0.0.1:4723/wd/hub"), getConfiguration().getCapabilities());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new NoSupportedDriverException(ILocaleManager.message("noSupportedDriverException", getConfiguration().getDriverType().name()));
        }

        return driver;
    }

    /*
    private void initDriver() {
        int port = 4723;
        if(!checkIfServerIsRunning(port)) {
            startServer();
        } else {
            log.info("Appium Server already running on Port - " + port);
        }
    }

    private void startServer() {
        AppiumServiceBuilder builder;
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability("noReset", "false");

        //Build the Appium service
        builder = new AppiumServiceBuilder();
        builder.withIPAddress("127.0.0.1");
        builder.usingPort(4723);
        builder.withCapabilities(cap);
        builder.withArgument(GeneralServerFlag.SESSION_OVERRIDE);
        builder.withArgument(GeneralServerFlag.LOG_LEVEL,"error");

        //Start the server with the builder
        service = AppiumDriverLocalService.buildService(builder);
        service.start();
    }

    private void stopServer() {
        service.stop();
    }

    private boolean checkIfServerIsRunning(int port) {
        boolean isServerRunning = false;
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.close();
        } catch (IOException e) {
            isServerRunning = true;
        }
        return isServerRunning;
    }
    */
}