package ru.ibsqa.qualit.steps;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;
import ru.ibsqa.qualit.reporter.IReporterManager;
import ru.ibsqa.qualit.selenium.driver.IDriverManager;
import ru.ibsqa.qualit.selenium.driver.ISupportedDriver;
import ru.ibsqa.qualit.selenium.driver.WebDriverFacade;
import ru.ibsqa.qualit.selenium.driver.configuration.IDriverConfiguration;
import ru.ibsqa.qualit.utils.spring.SpringUtils;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

@Component
@Slf4j
public class DefaultScreenshotSteps extends AbstractSteps implements IScreenshotSteps {

    private final static String ATTACHMENT_TYPE = "image/png";
    private final static String ATTACHMENT_EXTENSION = "png";
    private final static int SCROLL_TIMEOUT = 1000;

    @Autowired
    private IReporterManager reporterManager;

    /*
    private List<ILogRender> logRenders;

    @Autowired
    public void initLogRenders(List<ILogRender> logRenders) {
        this.logRenders = logRenders;
    }
    */

    @Override
    public void takeScreenshotToReport(String name, SeverityLevel level) {
        WebDriverFacade webDriver = SpringUtils.getBean(IDriverManager.class).getLastDriver();
        if (null != webDriver) {
            IDriverConfiguration configuration = webDriver.getDriverFactory().getConfiguration();
            if (configuration.isAdvancedScreenshotMethod()) {
                addScreenshot(name, () -> takeWebDriverAdvancedScreenshot(webDriver));
            } else {
                addScreenshot(name, () -> takeWebDriverSimpleScreenshot(webDriver));
            }
            if (configuration.isFullScreenshot()) {
                addScreenshot(name + " (полноэкранный)", () -> getFullScreenshot());
            }
        }
    }

    /**
     * Скриншот видимой части web-страницы
     *
     * @param webDriver
     * @return
     */
    public InputStream takeWebDriverSimpleScreenshot(WebDriverFacade webDriver) {
        File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
        if (null != file) {
            Path content = Paths.get(file.getPath());
            try {
                return Files.newInputStream(content);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return null;
    }

    /**
     * Скриншот полной web-страницы с вертикальной прокруткой
     *
     * @param webDriver
     * @return
     */
    public InputStream takeWebDriverAdvancedScreenshot(WebDriverFacade webDriver) {
        Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(SCROLL_TIMEOUT)).takeScreenshot(webDriver);
        if (null != screenshot) {
            try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
                ImageIO.write(screenshot.getImage(), ATTACHMENT_EXTENSION, os);
                try {
                    return new ByteArrayInputStream(os.toByteArray());
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        return null;
    }

    /**
     * Скриншот экрана
     *
     * @return
     */
    public InputStream getFullScreenshot() {
        try {
            Dimension resolution = Toolkit.getDefaultToolkit().getScreenSize();
            Rectangle rectangle = new Rectangle(resolution);
            Robot robot = new Robot();
            hideLogRenders(); // Спрячем лог
            BufferedImage bufferedImage = robot.createScreenCapture(rectangle);
            showLogRenders(); // Покажем лог

            try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
                ImageIO.write(bufferedImage, ATTACHMENT_EXTENSION, os);
                try {
                    return new ByteArrayInputStream(os.toByteArray());
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public void addScreenshot(String name, Supplier<InputStream> supplier) {
        try (InputStream is = supplier.get()) {
            if (Objects.nonNull(is)) {
                reporterManager.createAttachment(null != name ? name : message("errorScreenshot"), is, ATTACHMENT_TYPE, ATTACHMENT_EXTENSION);
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void showLogRenders() {
        /*
        if (Objects.nonNull(logRenders)) {
            logRenders.forEach(r -> r.show());
        }
        */
    }

    public void hideLogRenders() {
        /*
        if (Objects.nonNull(logRenders)) {
            logRenders.forEach(r -> r.hide());
        }
        */
    }

    @Override
    public ConfigurationPriority getPriority() {
        return ConfigurationPriority.LOW;
    }

    @Override
    public List<ISupportedDriver> getSupportedDrivers() {
        return null; // Все типы драйверов
    }

}
