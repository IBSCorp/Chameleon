package ru.ibsqa.qualit.selenium.driver.configuration;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ibsqa.qualit.i18n.ILocaleManager;
import ru.ibsqa.qualit.reporter.ScreenshotConfiguration;
import ru.ibsqa.qualit.selenium.driver.ISupportedDriver;
import ru.ibsqa.qualit.utils.spring.IClassPathScanner;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
public class DefaultDriverConfiguration implements IDriverConfiguration {

    @Autowired
    private ILocaleManager localeManager;

    @Builder.Default
    private String applicationUrl = System.getProperty("applicationUrl", null);

    @Builder.Default
    private boolean closeDriverAfterTest = Boolean.parseBoolean(System.getProperty("closeDriverAfterTest", "true"));

    @Builder.Default
    private boolean connectToRunningApp = Boolean.parseBoolean(System.getProperty("connectToRunningApp", "false"));

    @Builder.Default
    private boolean advancedScreenshotMethod = Boolean.parseBoolean(System.getProperty("advancedScreenshotMethod", "false"));

    @Builder.Default
    private boolean fullScreenshot = Boolean.parseBoolean(System.getProperty("advancedScreenshotMethod", "false"));

    @Builder.Default
    private ScreenshotConfiguration screenshotConfiguration = Enum.valueOf(ScreenshotConfiguration.class, System.getProperty("screenshotConfiguration", "FOR_FAILURES"));

    @Builder.Default
    private boolean maximizeWindow = Boolean.parseBoolean(System.getProperty("maximizeWindow", "true"));

    @Builder.Default
    private int implicitlyWait = Integer.parseInt(System.getProperty("webdriver.timeouts.implicitlywait", "0"));

    @Builder.Default
    private int defaultWaitTimeOut = Integer.parseInt(System.getProperty("defaultWaitTimeOut", "10"));

    @Builder.Default
    private boolean highlightElements = Boolean.parseBoolean(System.getProperty("highlightElements", "false"));

    @Builder.Default
    private ISupportedDriver driverType = null;

    @Builder.Default
    private String driverPath = null;

    @Builder.Default
    private DesiredCapabilities desiredCapabilities = null;

    private List<IDriverConfigurationAppender> appenderList;

    @Autowired
    private void collectDesiredCapabilitiesAppenderList(List<IDriverConfigurationAppender> appenderList) {
        this.appenderList = appenderList;
    }

    @PostConstruct
    private void postConstruct() {
        if (Objects.isNull(driverType)) {
            Optional<String> driverTypeProperty = Optional.ofNullable(System.getProperty("driverType"));
            if (driverTypeProperty.isEmpty()) {
                throw new RuntimeException(localeManager.getMessage("driverTypeEmpty"));
            }
            driverType = driverTypeProperty
                            .map(this::findDriverTypeByName)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            localeManager.getMessage(
                                                    "noSupportedDriverException",
                                                    driverTypeProperty.get()
                                            )
                                    )
                            );
        }
        if (Objects.isNull(desiredCapabilities)) {
            desiredCapabilities = new DesiredCapabilities();
        }
        appenderList.forEach(appender -> appender.appendToConfiguration(this));
    }

    @Autowired
    private IClassPathScanner classPathScanner;
    protected ISupportedDriver findDriverTypeByName(String driverTypeName) {
        return classPathScanner.findCandidates(ISupportedDriver.class)
                .filter(c-> Objects.nonNull(c.getEnumConstants()))
                .flatMap(c -> Arrays.stream(c.getEnumConstants()))
                .filter(e -> e.name().equalsIgnoreCase(driverTypeName))
                .findAny()
                .orElseThrow(() -> new RuntimeException(
                        localeManager.getMessage("driverTypeNotFound", driverTypeName))
                );
    }

    public boolean isManagedDriver() {
        return Objects.isNull(driverPath);
    }

    @Override
    public void initDriver() {
        if (null != driverType) {
            if (isManagedDriver()) {
                WebDriverManager.getInstance(driverType.getAsClass()).setup();
            } else {
                driverType.initDriver(this);
            }
        }
    }
//    @Getter
//    @Setter
//    private ISupportedDriver driverType;
//
//    @Getter
//    @Setter
//    private String driverPath;
//
//    @Getter
//    @Setter
//    private String applicationUrl;
//
//    @Getter
//    @Setter
//    private int implicitlyWait;
//
//    @Getter
//    @Setter
//    private int defaultWaitTimeOut;
//
//    public DefaultDriverConfiguration() {
//        implicitlyWait = Integer.parseInt(System.getProperty("webdriver.timeouts.implicitlywait", "0"));
//        defaultWaitTimeOut = 10;
//    }
//
//    @Getter
//    @Setter
//    private boolean maximizeWindow = true;
//
//    @Getter
//    @Setter
//    private DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
//
//    @Getter
//    @Setter
//    private boolean connectToRunningApp = false;
//
//    @Getter
//    @Setter
//    private boolean closeDriverAfterTest = true;
//
//    @Getter
//    @Setter
//    private boolean advancedScreenshotMethod = false;
//
//    @Getter
//    @Setter
//    private boolean fullScreenshot = false;
//
//    @Getter
//    @Setter
//    private ScreenshotConfiguration screenshotConfiguration = FOR_FAILURES;
//
//	@Getter
//	@Setter
//	private boolean highlightElements = false;
//
//    @Override
//    public void initDriver() {
//        if (null != driverType) {
//            driverType.initDriver(this);
//        }
//    }

}
