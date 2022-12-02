package ru.ibsqa.qualit.tests;

import ru.ibsqa.qualit.selenium.driver.IDriverManager;
import ru.ibsqa.qualit.steps.CollectionSteps;
import ru.ibsqa.qualit.steps.CompareOperatorEnum;
import ru.ibsqa.qualit.steps.PageSteps;
import ru.ibsqa.qualit.steps.SeleniumFieldSteps;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.Collections;

@Slf4j
@ExtendWith(SpringExtension.class)
@ContextConfiguration("classpath:spring.xml")
@TestExecutionListeners(inheritListeners = false, listeners =
        {DependencyInjectionTestExecutionListener.class})
public class IframeTest {
    @Autowired
    private PageSteps pageSteps;

    @Autowired
    private SeleniumFieldSteps fieldSteps;

    @Autowired
    private CollectionSteps collectionSteps;

    @Autowired
    private IDriverManager driverManager;

    @Test
    public void iframeTest() {
        log.info("iframeTest");
        // Тест далее будет падать всегда с мутными ошибками, если не выполнить настройки IE
        // Кнопка "Сервис" -> "Свойства браузера", перейти на вкладку "Безопасность", для каждой из зон
        // проставить галочку "Включить защищенный режим (потребуется перезапуск InternetExplorer)"
        pageSteps.stepLoadedPage("Страница с фреймами");
        fieldSteps.clickField("Заголовок");
        fieldSteps.clickField("Преимущества плавающих фреймов");
        pageSteps.stepLoadedPage("Левый фрейм");
        fieldSteps.clickField("Заголовок");
        fieldSteps.clickField("Преимущества плавающих фреймов");
        pageSteps.stepLoadedPage("Правый фрейм");
        collectionSteps.stepSetCollectionByConditions("Варианты",
                Collections.singletonList(CollectionSteps.FindCondition.builder().fieldName("Ссылка").operator(CompareOperatorEnum.EQUALS).value("html-редактор").build())
        );

        pageSteps.stepLoadedPage("Страница с фреймами");
        collectionSteps.stepSetCollectionByConditions("Варианты",
                Collections.singletonList(CollectionSteps.FindCondition.builder().fieldName("Ссылка").operator(CompareOperatorEnum.EQUALS).value("Полезные скрипты для сайта").build())
        );

        pageSteps.switchToPreviousPage();
        pageSteps.stepLoadedPage("Страница с фреймами");
    }
}