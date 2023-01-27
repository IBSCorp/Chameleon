package ru.ibsqa.qualit.tests;

import com.google.common.collect.Iterators;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import ru.ibsqa.qualit.Constants;
import ru.ibsqa.qualit.page_objects.FormPage;
import ru.ibsqa.qualit.page_objects.ListPage;
import ru.ibsqa.qualit.page_objects.MainPage;
import ru.ibsqa.qualit.selenium.driver.IDriverManager;
import ru.ibsqa.qualit.steps.CollectionSteps;
import ru.ibsqa.qualit.steps.CoreUtilSteps;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ExtendWith(SpringExtension.class)
@ContextConfiguration("classpath:spring.xml")
@TestExecutionListeners(inheritListeners = false, listeners =
        {DependencyInjectionTestExecutionListener.class})
public class PageObjectTest {

    @Autowired
    private IDriverManager driverManager;

    @Autowired
    private MainPage mainPage;

    @Autowired
    private ListPage listPage;

    @Autowired
    private FormPage formPage;

    @Autowired
    CollectionSteps collectionSteps;

    @Autowired
    private CoreUtilSteps utilSteps;

    @Test
    public void pageObjectTest() {

        driverManager.getDriver(null).get(Constants.URL);

        mainPage.loadPage();
        mainPage.menu.sandbox.click();
        mainPage.menu.reset.click();
        listPage.menu.sandbox.click();
        listPage.menu.goods.click();
        listPage.loadPage();
        assertEquals(4, Iterators.size(listPage.items.iterator()));
        listPage.addBtn.click();
        utilSteps.stopExecutedMs(500);
        formPage.loadPage();
        formPage.name.type(Constants.NAME);
        formPage.type.type(Constants.TYPE);
        formPage.exotic.type(Constants.EXOTIC);
        formPage.saveBtn.click();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        listPage.loadPage();
        collectionSteps.waitCollectionByConditions("Таблица", 10, Arrays.asList(
                CollectionSteps.FindCondition.builder().fieldName("Наименование").value(Constants.NAME).build(),
                CollectionSteps.FindCondition.builder().fieldName("Тип").value(Constants.TYPE).build(),
                CollectionSteps.FindCondition.builder().fieldName("Экзотический").value(Constants.EXOTIC).build()
        ));
        assertEquals(5, Iterators.size(listPage.items.iterator()));

    }

}