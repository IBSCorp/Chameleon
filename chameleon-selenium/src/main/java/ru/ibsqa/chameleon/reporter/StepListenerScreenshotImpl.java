package ru.ibsqa.chameleon.reporter;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.openqa.selenium.WebDriverException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.ibsqa.chameleon.selenium.driver.IDriverManager;
import ru.ibsqa.chameleon.selenium.driver.IDriverFacade;
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

    private final ThreadLocal<Boolean> hasBdd = new InheritableThreadLocal<>();
    private final ThreadLocal<Boolean> stepErrorScreenshot = new InheritableThreadLocal<>();

    @Override
    public void stepBefore(JoinPoint joinPoint, StepType stepType) {
        if (stepType.isBddStep()) {
            hasBdd.set(true);
        }

        IDriverFacade driverFacade = driverManager.getLastDriver();
        if (Objects.nonNull(driverFacade) && Objects.nonNull(driverFacade.getConfiguration())) {
            ScreenshotConfiguration screenshotConfiguration = driverFacade.getConfiguration().getScreenshotConfiguration();

            if ( stepType.isUiStep()
                        || !screenshotConfiguration.equals(ScreenshotConfiguration.FOR_UI_FAILURES)
            ) {
                stepErrorScreenshot.remove();
            }

            if (stepType.isUiStep()) {
                if (screenshotConfiguration.equals(ScreenshotConfiguration.BEFORE_AND_AFTER_EACH_STEP)) {
                    screenshotManager.takeScreenshotToReport(createReportName("Скриншот перед шагом", joinPoint, stepType), IScreenshotSteps.SeverityLevel.INFO);
                }
            }
        }
    }

    @Override
    public void stepAfterReturning(JoinPoint joinPoint, Object data, StepType stepType) {
        if (stepType.isUiStep()) {
            IDriverFacade driverFacade = driverManager.getLastDriver();
            if (Objects.nonNull(driverFacade) && Objects.nonNull(driverFacade.getConfiguration())) {
                ScreenshotConfiguration screenshotConfiguration = driverFacade.getConfiguration().getScreenshotConfiguration();
                if (screenshotConfiguration.equals(ScreenshotConfiguration.BEFORE_AND_AFTER_EACH_STEP) || screenshotConfiguration.equals(ScreenshotConfiguration.FOR_EACH_STEP)) {
                    screenshotManager.takeScreenshotToReport(createReportName("Скриншот после шага", joinPoint, stepType), IScreenshotSteps.SeverityLevel.INFO);
                }
            }
        }
    }

    @Override
    public void stepAfterThrowing(JoinPoint joinPoint, Throwable throwable, StepType stepType) {
        if (Objects.isNull(hasBdd.get()) || Objects.isNull(stepErrorScreenshot.get())) {
            IDriverFacade driverFacade = driverManager.getLastDriver();
            if (Objects.nonNull(driverFacade) && Objects.nonNull(driverFacade.getConfiguration())) {
                if (!driverFacade.getConfiguration().getScreenshotConfiguration().equals(ScreenshotConfiguration.DISABLED)) {
                    try {
                        screenshotManager.takeScreenshotToReport(createReportName("Скриншот в момент ошибки на шаге", joinPoint, stepType), IScreenshotSteps.SeverityLevel.ERROR);
                    } catch (WebDriverException e) {
                        log.error(e.getMessage(), e);
                    }
                    stepErrorScreenshot.set(true);
                }
            }
        }
    }

    protected String createReportName(String reportCase, JoinPoint joinPoint, StepType stepType) {
        return String.format("%s <%s>", reportCase, stepInformerManager.getDescription(joinPoint, stepType));
    }
}
