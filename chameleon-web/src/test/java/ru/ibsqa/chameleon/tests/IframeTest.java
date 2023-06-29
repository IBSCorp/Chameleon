package ru.ibsqa.chameleon.tests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import ru.ibsqa.chameleon.Constants;
import ru.ibsqa.chameleon.selenium.driver.IDriverManager;
import ru.ibsqa.chameleon.steps.PageSteps;
import ru.ibsqa.chameleon.steps.SeleniumFieldSteps;
import ru.ibsqa.chameleon.utils.spring.ChameleonSpringExtension;

@Slf4j
@ExtendWith(ChameleonSpringExtension.class)
@ContextConfiguration("classpath:spring.xml")
@TestExecutionListeners(inheritListeners = false, listeners =
        {DependencyInjectionTestExecutionListener.class})
public class IframeTest {
    @Autowired
    private PageSteps pageSteps;

    @Autowired
    private SeleniumFieldSteps fieldSteps;

    @Autowired
    private IDriverManager driverManager;

    @Test
    public void iframeTest() {
        log.info("iframeTest");
        driverManager.getDriver(null).get(Constants.URL);
        pageSteps.stepLoadedPage("Главная страница");
        fieldSteps.clickField("Песочница");
        fieldSteps.clickField("Тест Iframe");
        pageSteps.stepLoadedPage("Тест Iframe");
        fieldSteps.fieldIsDisplayed("Заголовок 1");
        fieldSteps.fieldIsDisplayed("Заголовок 2");
        fieldSteps.fieldIsDisplayed("Заголовок 3");
    }
}