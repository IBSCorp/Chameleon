package ru.ibsqa.chameleon.tests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import ru.ibsqa.chameleon.Constants;
import ru.ibsqa.chameleon.definitions.repository.IRepositoryManager;
import ru.ibsqa.chameleon.definitions.repository.selenium.elements.MetaPage;
import ru.ibsqa.chameleon.evaluate.IEvaluateManager;
import ru.ibsqa.chameleon.selenium.driver.IDriverManager;
import ru.ibsqa.chameleon.steps.CollectionSteps;
import ru.ibsqa.chameleon.steps.CoreUtilSteps;
import ru.ibsqa.chameleon.steps.PageSteps;
import ru.ibsqa.chameleon.steps.SeleniumFieldSteps;
import ru.ibsqa.chameleon.utils.spring.ChameleonSpringExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Slf4j
@ExtendWith(ChameleonSpringExtension.class)
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
        assertNotEquals(repositoryManager.pickAllElements().size(), 0);
        log.info(repositoryManager.pickElement("Главная страница", MetaPage.class).toString());
    }

}