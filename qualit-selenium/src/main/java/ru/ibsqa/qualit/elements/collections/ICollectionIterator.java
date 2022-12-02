package ru.ibsqa.qualit.elements.collections;

import ru.ibsqa.qualit.page_factory.PageFactoryUtils;
import ru.ibsqa.qualit.page_factory.pages.IPageObject;
import org.openqa.selenium.SearchContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.fail;

public interface ICollectionIterator<PAGE extends IPageObject> extends Iterator<PAGE> {
    /**
     * Завернуть найденный элемент в класс-страницу
     *
     * @param collectionObjectClass
     * @param searchContext
     * @return
     */
    default PAGE createPageObject(SearchContext searchContext, Class<PAGE> collectionObjectClass) {
        PAGE pageObject = null;
        try {
            return PageFactoryUtils.newInstance(collectionObjectClass, searchContext);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            Logger log = LoggerFactory.getLogger(ICollectionIterator.class);
            log.error(e.getMessage(), e);
            fail("Не удалось создать экземпляр класса: " + collectionObjectClass.getName());
        }
        return null;
    }
}
