package ru.ibsqa.chameleon.selenium.driver.configuration;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ibsqa.chameleon.i18n.ILocaleManager;
import ru.ibsqa.chameleon.reporter.ScreenshotConfiguration;
import ru.ibsqa.chameleon.selenium.driver.ISupportedDriver;
import ru.ibsqa.chameleon.utils.spring.IClassPathScanner;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
public class DefaultDriverConfiguration implements IDriverConfiguration {

    @Autowired
    private ILocaleManager localeManager;

    @Builder.Default
    @Getter
    @Setter
    private String applicationUrl = System.getProperty("applicationUrl", null);

    @Builder.Default
    @Getter
    @Setter
    private boolean closeDriverAfterTest = Boolean.parseBoolean(System.getProperty("closeDriverAfterTest", "true"));

    @Builder.Default
    @Getter
    @Setter
    private boolean connectToRunningApp = Boolean.parseBoolean(System.getProperty("connectToRunningApp", "false"));

    @Builder.Default
    @Getter
    @Setter
    private boolean advancedScreenshotMethod = Boolean.parseBoolean(System.getProperty("advancedScreenshotMethod", "false"));

    @Builder.Default
    @Getter
    @Setter
    private boolean fullScreenshot = Boolean.parseBoolean(System.getProperty("fullScreenshot", "false"));

    @Builder.Default
    @Getter
    @Setter
    private ScreenshotConfiguration screenshotConfiguration = Enum.valueOf(ScreenshotConfiguration.class, System.getProperty("screenshotConfiguration", "FOR_FAILURES"));

    @Builder.Default
    @Getter
    @Setter
    private boolean maximizeWindow = Boolean.parseBoolean(System.getProperty("maximizeWindow", "true"));

    @Builder.Default
    @Getter
    @Setter
    private int implicitlyWait = Integer.parseInt(System.getProperty("implicitlyWait", "0"));

    @Builder.Default
    @Getter
    @Setter
    private int defaultWaitTimeOut = Integer.parseInt(System.getProperty("defaultWaitTimeOut", "10"));

    @Builder.Default
    @Getter
    @Setter
    private boolean highlightElements = Boolean.parseBoolean(System.getProperty("highlightElements", "false"));

    @Builder.Default
    @Setter
    private ISupportedDriver driverType = null;

    @Override
    public ISupportedDriver getDriverType() {
        // Если тип драйвера не был проинициализирован, но используется (такое происходит при создании драйвера)
        // то выдать ошибку
        if (Objects.isNull(driverType)) {
            throw new RuntimeException(localeManager.getMessage("driverTypeEmpty"));
        }
        return driverType;
    }

    @Builder.Default
    @Getter
    @Setter
    private String driverPath = System.getProperty("driverPath", null);

    @Builder.Default
    @Getter
    @Setter
    private Object options = null;

    @Autowired
    private List<IDriverConfigurationAppender> appenderList;

    @PostConstruct
    private void postConstruct() {
        if (Objects.isNull(driverType)) {
            Optional<String> driverTypeProperty = Optional.ofNullable(System.getProperty("driverType"));
            if (!driverTypeProperty.isEmpty() && !StringUtils.isEmpty(driverTypeProperty.get())) {
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

}
