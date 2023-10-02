package ru.ibsqa.chameleon.elements.collections;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.StaleElementReferenceException;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;
import ru.ibsqa.chameleon.definitions.repository.selenium.elements.MetaCollection;
import ru.ibsqa.chameleon.elements.InvokeFieldException;
import ru.ibsqa.chameleon.elements.MetaElement;
import ru.ibsqa.chameleon.i18n.ILocaleManager;
import ru.ibsqa.chameleon.page_factory.pages.IPageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import ru.ibsqa.chameleon.utils.spring.SpringUtils;

import java.util.List;

/**
 * Простая коллекция, которая умеет перебирать все элементы, которые находятся на текущей странице.
 * Для скроллирования и пагинации нужны другие имплементации.
 *
 * @param <PAGE>
 */
@MetaElement(value = MetaCollection.class, priority = ConfigurationPriority.LOW)
@Slf4j
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
                try {
                    webElements = getElementLocator().findElements();
                    log.debug(String.format("find [%d] elements in collection [%s]", webElements.size(), getCollectionName()));
                } catch (StaleElementReferenceException e) {
                    log.debug(e.getLocalizedMessage(), e);
                    ILocaleManager localeManager = SpringUtils.getBean(ILocaleManager.class);
                    throw new InvokeFieldException(localeManager.getMessage("invokeFieldException", getCollectionName()), e);
                }
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
