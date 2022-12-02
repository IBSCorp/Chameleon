package tests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import pages.web.yandex.YandexMainPage;
import pages.web.yandex.YandexSearchResultPage;
import ru.ibsqa.qualit.selenium.driver.IDriverManager;
import ru.ibsqa.qualit.steps.CollectionSteps;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ExtendWith(SpringExtension.class)
@ContextConfiguration("classpath:spring.xml")
@TestExecutionListeners(inheritListeners = false, listeners =
        {DependencyInjectionTestExecutionListener.class})
public class YandexTestJavaclassTest {

    @Autowired
    IDriverManager driverManager;

    @Autowired
    YandexMainPage searchPage;

    @Autowired
    YandexSearchResultPage resultPage;

    @Autowired
    CollectionSteps collectionSteps;

    @Test
    public void yandexTestJavaclassTest() {
        driverManager.getDriver(null).get("https://www.yandex.ru/");

        searchPage.loadPage();

        searchPage.news.forEach(item ->
            {
                log.info(item.newsText.getText());
                assertNotNull(item.newsText.getText());
            }
        );
        YandexMainPage.NewsItem item1 = collectionSteps.searchItemByIndex("Сейчас в СМИ", 1);
        log.info(item1.newsText.getText());

        //searchPage.searchInputs.get(0).type("TEST!");

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        searchPage.searchInput.type("Selenium java class");

        searchPage.yandexBlockPage.searchBtn.click();

    }

}