package ru.ibsqa.qualit.elements.collections;

import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;
import ru.ibsqa.qualit.definitions.repository.selenium.elements.MetaCollection;
import ru.ibsqa.qualit.elements.MetaElement;
import ru.ibsqa.qualit.page_factory.pages.IPageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.util.List;

/**
 * Простая коллекция, которая умеет перебирать все элементы, которые находятся на текущей странице.
 * Для скроллирования и пагинации нужны другие имплементации.
 *
 * @param <PAGE>
 */
@MetaElement(value = MetaCollection.class, priority = ConfigurationPriority.LOW)
public class DefaultCollection<PAGE extends IPageObject> extends AbstractCollection<PAGE> {

    @Override
    @SuppressWarnings({"rawtypes","unchecked"})
    public ICollectionIterator<PAGE> iterator() {
        return new DefaultCollectionIterator(getCollectionName(), getWaitTimeOut(), getElementLocator(), getFrames(), getCollectionObjectClass());
    }

    /**
     * Итератор расположен в этом же классе. В нем заключена основная логика обработки коллекции
     *
     * @param <PAGE>
     */
    public static class DefaultCollectionIterator<PAGE extends IPageObject> extends AbstractCollectionIterator<PAGE> {

        private List<WebElement> webElements = null;
        private int lastIndex = -1;

        public DefaultCollectionIterator(String collectionName, int waitTimeOut, ElementLocator elementLocator, String[] frames, Class<PAGE> collectionObjectClass) {
            super(collectionName, waitTimeOut, elementLocator, frames, collectionObjectClass);
        }

        private List<WebElement> getWebElements() {
            if (null == webElements && null != getElementLocator()) {
                webElements = getElementLocator().findElements();
            }
            return webElements;
        }

        protected WebElement getNextWebElement() {
            if (hasNext()) {
                return getWebElements().get(++lastIndex);
            }
            return null;
        }

        @Override
        public boolean hasNext() {
            return null != getWebElements() && getWebElements().size() > lastIndex + 1;
        }

    }

}
