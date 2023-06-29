package ru.ibsqa.chameleon.elements.collections;

import ru.ibsqa.chameleon.page_factory.locator.IFrameManager;
import ru.ibsqa.chameleon.page_factory.pages.IPageObject;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

@Slf4j
public abstract class AbstractCollectionIterator<PAGE extends IPageObject> implements ICollectionIterator<PAGE> {

    @Getter(AccessLevel.PUBLIC)
    private final String collectionName;

    @Getter(AccessLevel.PUBLIC)
    private final int waitTimeOut;

    @Getter(AccessLevel.PROTECTED)
    private final ElementLocator elementLocator;

    @Getter(AccessLevel.PROTECTED)
    private final String[] frames;

    @Getter(AccessLevel.PROTECTED)
    private final Class<PAGE> collectionObjectClass;

    public AbstractCollectionIterator(String collectionName, int waitTimeOut, ElementLocator elementLocator, String[] frames, Class<PAGE> collectionObjectClass) {
        this.collectionName = collectionName;
        this.waitTimeOut = waitTimeOut;
        this.elementLocator = elementLocator;
        this.frames = frames;
        this.collectionObjectClass = collectionObjectClass;
        IFrameManager.collectionFrames(frames);
        log.debug(String.format("getCollection(\"%s\")", collectionName));
    }

    protected abstract WebElement getNextWebElement();

    @Override
    public PAGE next() {
        return createPageObject(getNextWebElement(), collectionObjectClass);
    }

}
