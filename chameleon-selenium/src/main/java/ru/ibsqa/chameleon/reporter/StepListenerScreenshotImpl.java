package ru.ibsqa.chameleon.reporter;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.openqa.selenium.WebDriverException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.ibsqa.chameleon.selenium.driver.IDriverManager;
import ru.ibsqa.chameleon.selenium.driver.WebDriverFacade;
import ru.ibsqa.chameleon.steps.IScreenshotManager;
import ru.ibsqa.chameleon.steps.IScreenshotSteps;
import ru.ibsqa.chameleon.steps.aspect.AbstractStepListener;
import ru.ibsqa.chameleon.steps.aspect.StepType;

import java.util.Objects;

@Slf4j
@Component
@Lazy
public class StepListenerScreenshotImpl extends AbstractStepListener {

    @Autowired
    private IScreenshotManager screenshotManager;

    @Autowired
    private IDriverManager driverManager;

    private long lastScreenshotTime = 0;

    @Override
    public void stepBefore(JoinPoint joinPoint, StepType stepType) {
        if (stepType.isUiStep()) {
            WebDriverFacade webDriver = driverManager.getLastDriver();
            if (Objects.nonNull(webDriver) && Objects.nonNull(webDriver.getConfiguration())) {
                if (webDriver.getConfiguration().getScreenshotConfiguration().equals(ScreenshotConfiguration.BEFORE_AND_AFTER_EACH_STEP)) {
                    screenshotManager.takeScreenshotToReport(createReportName("Скриншот перед шагом", joinPoint, stepType), IScreenshotSteps.SeverityLevel.INFO);
                }
            }
        }
    }

    @Override
    public void stepAfterReturning(JoinPoint joinPoint, Object data, StepType stepType) {
        if (stepType.isUiStep()) {
            WebDriverFacade webDriver = driverManager.getLastDriver();
            if (Objects.nonNull(webDriver) && Objects.nonNull(webDriver.getConfiguration())) {
                ScreenshotConfiguration screenshotConfiguration = webDriver.getConfiguration().getScreenshotConfiguration();
                if (screenshotConfiguration.equals(ScreenshotConfiguration.BEFORE_AND_AFTER_EACH_STEP) || screenshotConfiguration.equals(ScreenshotConfiguration.FOR_EACH_STEP)) {
                    screenshotManager.takeScreenshotToReport(createReportName("Скриншот после шага", joinPoint, stepType), IScreenshotSteps.SeverityLevel.INFO);
                }
            }
        }
    }

    @Override
    public void stepAfterThrowing(JoinPoint joinPoint, Throwable throwable, StepType stepType) {
        if (lastScreenshotTime < System.currentTimeMillis() - 1000) {
            WebDriverFacade webDriver = driverManager.getLastDriver();
            if (Objects.nonNull(webDriver) && Objects.nonNull(webDriver.getConfiguration())) {
                if (!webDriver.getConfiguration().getScreenshotConfiguration().equals(ScreenshotConfiguration.DISABLED)) {
                    try {
                        screenshotManager.takeScreenshotToReport(createReportName("Скриншот в момент ошибки на шаге", joinPoint, stepType), IScreenshotSteps.SeverityLevel.ERROR);
                    } catch (WebDriverException e) {
                        log.error(e.getMessage(), e);
                    }
                    lastScreenshotTime = System.currentTimeMillis();
                }
            }
        }
    }

    protected String createReportName(String reportCase, JoinPoint joinPoint, StepType stepType) {
        return String.format("%s <%s>", reportCase, stepInformerManager.getDescription(joinPoint, stepType));
    }
}
