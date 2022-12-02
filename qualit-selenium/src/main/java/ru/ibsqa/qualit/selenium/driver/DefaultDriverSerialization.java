package ru.ibsqa.qualit.selenium.driver;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.*;
import org.openqa.selenium.remote.codec.jwp.JsonHttpCommandCodec;
import org.openqa.selenium.remote.codec.jwp.JsonHttpResponseCodec;
import org.openqa.selenium.remote.codec.w3c.W3CHttpCommandCodec;
import org.openqa.selenium.remote.codec.w3c.W3CHttpResponseCodec;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.springframework.stereotype.Component;
import ru.ibsqa.qualit.selenium.driver.configuration.IDriverConfiguration;
import ru.ibsqa.qualit.selenium.events.Highlighter;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.fail;

@Component
@Slf4j
public class DefaultDriverSerialization implements IDriverSerialization {

    @Override
    public void saveDriver(final WebDriver driver, final String driverId) {
        if (driver == null) {
            return;
        }
        try {
            // Создадим директорию и файл
            Path dir = Files.createDirectories(Paths.get(getInstancePath()));
            Path fileToCreatePath = dir.resolve(getInstanceFileName(driverId));
            Files.deleteIfExists(fileToCreatePath);
            Path newFilePath = Files.createFile(fileToCreatePath);

            // Определим сохраняемые параметры
            HttpCommandExecutor executor = (HttpCommandExecutor) (getRemoteWebDriver(driver)).getCommandExecutor();
            URL url = executor.getAddressOfRemoteServer();
            String sessionId = (getRemoteWebDriver(driver)).getSessionId().toString();

            DriverSession driverSession = DriverSession.builder()
                    .sessionId(sessionId)
                    .url(url.toString())
                    .build();

            // Сохраним в файл
            Files.write(newFilePath, Collections.singleton(driverSession.toJson()));
        } catch (IOException e) {
            fail("Не удалось сохранить сессию драйвера [" + driverId + "]" + e.getStackTrace());
        }
    }

    @Override
    public WebDriver createDriver(final String driverId, final Class<? extends WebDriver> aClass, final IDriverConfiguration configuration) {

        final Capabilities capabilities = configuration.getDesiredCapabilities();

        // Прочитаем из файла
        byte[] data = null;
        try {
            data = Files.readAllBytes(Paths.get(getInstanceFullName(driverId)));
        } catch (IOException e) {
            fail("Не удалось подключиться к запущенной сессии драйвера, так как не найден файл с сессией [" + driverId + "]" + e.getStackTrace());
        }
        final DriverSession driverSession = DriverSession.fromJson(new String(data, StandardCharsets.UTF_8));

        // Создадим executor для драйвера
        CommandExecutor executor = createExecutor(driverId, aClass, driverSession);

        if (configuration.isHighlightElements()) {
            return new EventFiringWebDriver(new RemoteWebDriver(executor, capabilities)).register(new Highlighter());
        } else {
            return new RemoteWebDriver(executor, capabilities);
        }
    }

    @Override
    public CommandExecutor createExecutor(final String driverId, final Class<? extends WebDriver> aClass, final DriverSession driverSession) {
        // Сформируем URL для драйвера
        URL url = null;
        try {
            url = new URL(driverSession.getUrl());
        } catch (MalformedURLException e) {
            fail("Не удалось получить URL из файла с сессией [" + driverId + "]" + e.getStackTrace());
            log.error(e.getMessage(), e);
        }

        return new HttpCommandExecutor(url) {

            @Override
            public Response execute(Command command) throws IOException {
                Response response = null;
                if (command.getName().equals("newSession")) {

                    response = new Response();
                    response.setSessionId(driverSession.getSessionId());
                    response.setStatus(0);
                    response.setValue(Collections.<String, String>emptyMap());

                    try {
                        Field commandCodec = null;
                        commandCodec = this.getClass().getSuperclass().getDeclaredField("commandCodec");
                        commandCodec.setAccessible(true);
                        if (aClass.isAssignableFrom(InternetExplorerDriver.class)
                                ||aClass.isAssignableFrom(ChromeDriver.class)
                                ||aClass.isAssignableFrom(FirefoxDriver.class)) {
                            commandCodec.set(this, new W3CHttpCommandCodec());
                        } else {
                            commandCodec.set(this, new JsonHttpCommandCodec());
                        }
                        Field responseCodec = null;
                        responseCodec = this.getClass().getSuperclass().getDeclaredField("responseCodec");
                        responseCodec.setAccessible(true);
                        if (aClass.isAssignableFrom(InternetExplorerDriver.class)) {
                            responseCodec.set(this, new W3CHttpResponseCodec());
                        } else {
                            responseCodec.set(this, new JsonHttpResponseCodec());
                        }
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        log.error(e.getMessage(), e);
                    }

                } else {
                    response = super.execute(command);
                }
                return response;
            }
        };
    }

    private String getInstancePath() {
        return String.join(File.separator, System.getProperty("user.dir"), "driver-instance");
    }

    private String getInstanceFileName(final String instanceId) {
        return String.format("%s.json", instanceId);
    }

    public String getInstanceFullName(final String instanceId) {
        return String.join(File.separator, getInstancePath(), getInstanceFileName(instanceId));
    }

    private RemoteWebDriver getRemoteWebDriver(WebDriver driver) {
        if (driver instanceof EventFiringWebDriver) {
            return (RemoteWebDriver)((EventFiringWebDriver)driver).getWrappedDriver();
        }
        return (RemoteWebDriver) driver;
    }
}
