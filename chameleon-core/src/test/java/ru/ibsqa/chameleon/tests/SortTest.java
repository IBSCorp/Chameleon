package ru.ibsqa.chameleon.tests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import ru.ibsqa.chameleon.steps.CollectionSteps;
import ru.ibsqa.chameleon.utils.spring.ChameleonSpringExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@ExtendWith(ChameleonSpringExtension.class)
@ContextConfiguration("classpath:spring.xml")
@TestExecutionListeners(inheritListeners = false, listeners =
        {DependencyInjectionTestExecutionListener.class})
public class SortTest {

    @Autowired
    private CollectionSteps collectionSteps;

    @Test
    public void checkSortTest() {
        log.info("-= Тест проверки сортировки коллекции =-");

        Map<String, CollectionSteps.SortParams> params = new HashMap<>();

        CollectionSteps.SortParams param1 = new CollectionSteps.SortParams();
        params.put("field1", param1);
        param1.setClazz(String.class);
        param1.setDesc(true);

        CollectionSteps.SortParams param2 = new CollectionSteps.SortParams();
        params.put("field2", param2);
        param2.setClazz(Long.class);
        param2.setDesc(false);

        List<java.util.Map<String, String>> array = new ArrayList<>();
        Map<String, String> item;

        item = new HashMap<>();
        item.put("field1", "BBBB");
        item.put("field2", "22");
        array.add(item);

        item = new HashMap<>();
        item.put("field1", "AAAA");
        item.put("field2", "11");
        array.add(item);

        item = new HashMap<>();
        item.put("field1", "AAAA");
        item.put("field2", "12");
        array.add(item);

        collectionSteps.checkSortArray("Наша коллекция", array, params);
    }
}
