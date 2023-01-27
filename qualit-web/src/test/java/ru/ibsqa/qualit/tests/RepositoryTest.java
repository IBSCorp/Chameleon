package ru.ibsqa.qualit.tests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import ru.ibsqa.qualit.Constants;
import ru.ibsqa.qualit.definitions.repository.IRepositoryManager;
import ru.ibsqa.qualit.definitions.repository.selenium.elements.MetaPage;
import ru.ibsqa.qualit.evaluate.IEvaluateManager;
import ru.ibsqa.qualit.selenium.driver.IDriverManager;
import ru.ibsqa.qualit.steps.CollectionSteps;
import ru.ibsqa.qualit.steps.CoreUtilSteps;
import ru.ibsqa.qualit.steps.PageSteps;
import ru.ibsqa.qualit.steps.SeleniumFieldSteps;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Slf4j
@ExtendWith(SpringExtension.class)
@ContextConfiguration("classpath:spring.xml")
@TestExecutionListeners(inheritListeners = false, listeners =
        {DependencyInjectionTestExecutionListener.class})
public class RepositoryTest {

    @Autowired
    private IDriverManager driverManager;

    @Autowired
    private SeleniumFieldSteps fieldSteps;

    @Autowired
    private PageSteps pageSteps;

    @Autowired
    private CollectionSteps collectionSteps;

    @Autowired
    private CoreUtilSteps coreUtilSteps;

    @Autowired
    private IEvaluateManager evaluateManager;

    @Autowired
    private IRepositoryManager repositoryManager;

    @Test
    public void useRepositoryTest() {
        driverManager.getDriver(null).get(Constants.URL);
        pageSteps.stepLoadedPage("Главная страница");
        fieldSteps.clickField("Песочница");
        fieldSteps.clickField("Сброс данных");
        fieldSteps.clickField("Песочница");
        fieldSteps.clickField("Товары");
        pageSteps.stepLoadedPage("Список товаров");

        log.info("Содержимое всей страницы (JSON):");
        log.info(evaluateManager.evalVariable("#json{currentPage}").toString());
        log.info("Таблица (JSON):");
        log.info(fieldSteps.getFieldValue("Таблица"));

        collectionSteps.stepCheckItemCount("Таблица", 4);
        fieldSteps.clickField("Добавить");
        pageSteps.stepLoadedPage("Добавление товара");
        coreUtilSteps.stopExecutedMs(500);
        fieldSteps.fillField("Наименование", Constants.NAME);
        fieldSteps.fillField("Тип", Constants.TYPE);
        fieldSteps.fillField("Экзотический", Constants.EXOTIC);
        fieldSteps.clickField("Сохранить");
        coreUtilSteps.stopExecutedMs(500);
        pageSteps.stepLoadedPage("Список товаров");
        collectionSteps.waitCollectionByConditions("Таблица", 10, Arrays.asList(
                CollectionSteps.FindCondition.builder().fieldName("Наименование").value(Constants.NAME).build(),
                CollectionSteps.FindCondition.builder().fieldName("Тип").value(Constants.TYPE).build(),
                CollectionSteps.FindCondition.builder().fieldName("Экзотический").value(Constants.EXOTIC).build()
        ));
        collectionSteps.stepCheckItemCount("Таблица", 5);
    }

    @Test
    public void searchInRepositoryTest() {
        assertNotEquals(repositoryManager.pickAllElements().size(),0);
        log.info(repositoryManager.pickElement("Главная страница", MetaPage.class).toString());
    }

}