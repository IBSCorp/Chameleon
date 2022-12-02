package ru.ibsqa.qualit.steps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ibsqa.qualit.i18n.ILocaleManager;
import ru.ibsqa.qualit.selenium.driver.IDriverManager;
import ru.ibsqa.qualit.selenium.driver.WebDriverFacade;
import ru.ibsqa.qualit.utils.spring.SpringUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Component
public class ScreenshotManagerImpl implements IScreenshotManager {

    private List<IScreenshotSteps> implementations;

    @Autowired
    private ILocaleManager localeManager;

    @Autowired
    private void collectResolvers(List<IScreenshotSteps> implementations) {
        this.implementations = implementations;
        this.implementations.sort(Comparator.comparing(IScreenshotSteps::getPriority));
    }

    @Override
    public void takeScreenshotToReport(String name, IScreenshotSteps.SeverityLevel level) {
        WebDriverFacade webDriver =  SpringUtils.getBean(IDriverManager.class).getLastDriver();

        // Найти подходящие имплементации для формирования скриншота
        IScreenshotSteps screenshotSteps = implementations.stream()
                .filter(i -> Objects.isNull(i.getSupportedDrivers()) ||
                                (
                                        Objects.nonNull(webDriver) && Objects.nonNull(webDriver.getConfiguration()) &&
                                    i.getSupportedDrivers().contains(webDriver.getConfiguration().getDriverType())
                                )
                )
                .findFirst()
                .orElseThrow(() -> new RuntimeException(localeManager.getMessage("screenshotStepsNotFound")));

        screenshotSteps.takeScreenshotToReport(name, level);
    }
}
