package ru.ibsqa.chameleon.elements.collections;

import ru.ibsqa.chameleon.page_factory.PageFactoryUtils;
import ru.ibsqa.chameleon.page_factory.pages.IPageObject;
import org.openqa.selenium.SearchContext;

import java.util.Iterator;
import java.util.Objects;

public interface ICollectionIterator<PAGE extends IPageObject> extends Iterator<PAGE> {
    /**
     * Завернуть найденный элемент в класс-страницу
     *
     * @param collectionObjectClass
     * @param searchContext
     * @return
     */
    default PAGE createPageObject(SearchContext searchContext, Class<PAGE> collectionObjectClass) {
        PAGE pageObject = PageFactoryUtils.newInstance(collectionObjectClass);
        if (Objects.nonNull(pageObject)) {
            pageObject.initElements(searchContext);
        }
        return pageObject;
    }
//    default PAGE createPageObject(SearchContext searchContext, Class<PAGE> collectionObjectClass) {
//        PAGE pageObject = null;
//        try {
//            return PageFactoryUtils.newInstance(collectionObjectClass, searchContext);
//        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
//            Logger log = LoggerFactory.getLogger(ICollectionIterator.class);
//            log.error(e.getMessage(), e);
//            fail("Не удалось создать экземпляр класса: " + collectionObjectClass.getName());
//        }
//        return null;
//    }
}
