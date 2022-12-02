package tests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import ru.ibsqa.qualit.evaluate.IEvaluateManager;
import ru.ibsqa.qualit.selenium.driver.IDriverManager;
import ru.ibsqa.qualit.steps.CollectionSteps;
import ru.ibsqa.qualit.steps.PageSteps;
import ru.ibsqa.qualit.steps.SeleniumFieldSteps;

@Slf4j
@ExtendWith(SpringExtension.class)
@ContextConfiguration("classpath:spring.xml")
@TestExecutionListeners(inheritListeners = false, listeners =
        {DependencyInjectionTestExecutionListener.class})
public class YandexTestRepositoryTest {

    @Autowired
    IDriverManager driverManager;

    @Autowired
    SeleniumFieldSteps seleniumFieldSteps;

    @Autowired
    PageSteps pageSteps;

    @Autowired
    CollectionSteps collectionSteps;

    @Autowired
    IEvaluateManager evaluateManager;

    @Test
    public void yandexTestRepositoryTest(){
        driverManager.getDriver(null).get("https://www.yandex.ru/");

        pageSteps.stepLoadedPage("Стартовая страница Яндекс");

        log.info("Содержимое всей страницы:");
        //pageSteps.exportPageToJson("json");
        //log.info(evaluateManager.evalVariable("#{json}").toString());
        log.info(evaluateManager.evalVariable("#json{currentPage}").toString());

        collectionSteps.stepSetCollectionByIndex("Сейчас в СМИ", 1);

        log.info("Сейчас в СМИ (JSON):");
        log.info(seleniumFieldSteps.getFieldValue("Сейчас в СМИ"));

        log.info("Текст первой новости:");
        log.info(seleniumFieldSteps.getFieldValue("Текст новости"));

        pageSteps.stepLoadedPage("Стартовая страница Яндекс");

        seleniumFieldSteps.fillField("Поиск", "Selenium repository");

        seleniumFieldSteps.clickField("Найти");
    }

}