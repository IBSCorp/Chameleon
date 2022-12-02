package ru.ibsqa.qualit.page_factory.pages;

import ru.ibsqa.qualit.context.IContextManager;
import ru.ibsqa.qualit.context.IContextRegistrator;
import ru.ibsqa.qualit.context.PickElementResult;
import ru.ibsqa.qualit.elements.IFacade;
import ru.ibsqa.qualit.i18n.ILocaleManager;
import ru.ibsqa.qualit.page_factory.IPageFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.fail;

@Component
@Slf4j
public class ContextManagerCollectionItemImpl implements IContextManagerCollectionItem, IContextRegistrator {

    @Autowired
    private ILocaleManager localeManager;

    @Autowired
    private IPageFactory pageFactory;

    private ThreadLocal<ICollectionItemObject> currentCollectionItem = new ThreadLocal<>();

    @Override
    public ICollectionItemObject getCurrentCollectionItem() {
        return currentCollectionItem.get();
    }

    @Override
    public void setCurrentCollectionItem(ICollectionItemObject item) {
        currentCollectionItem.set(item);
    }

    @Override
    public void setCurrentCollectionItem(Class<? extends ICollectionItemObject> item) {
        try {
            ICollectionItemObject collectionItemObject = (ICollectionItemObject) item.getConstructor().newInstance();
            setCurrentCollectionItem(collectionItemObject);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.error(e.getMessage(), e);
            fail();
        }
    }

    @Override
    public void setCurrentCollectionItem(String collectionName) {
        Class<? extends ICollectionItemObject> aClass = pageFactory.generatePage(collectionName);
        setCurrentCollectionItem(aClass);
    }

    @Override
    public void clearCurrentCollectionItem() {
        currentCollectionItem.remove();
    }

    @Override
    public <FACADE extends IFacade> PickElementResult pickElement(String fullPathName, Class<FACADE> fieldType) {

        IPageObject currentCollectionItem = getCurrentCollectionItem();

        // Если страница не была установлена, то не ищем на ней
        if (null == currentCollectionItem) {
            return null;
        }

        // Сформируем ответ
        PickElementResult<FACADE, ICollectionItemObject> pickElementResult = PickElementResult
                .builder()
                .contextManager((IContextManager) this) // Ссылка на наш менеджер контекстов, который нашел
                .contextObject(currentCollectionItem) // Ссылка на страницу, где найден элемент
                .build();

        pickElementResult.setElement((FACADE) currentCollectionItem.getField(fullPathName));  // Ссылка на найденный элемент

        if (null == pickElementResult.getElement()) {
            return null;
        }

        return pickElementResult;
    }

    @Override
    public String getContextCode() {
        return localeManager.getMessage("collectionItemContextCode");
    }

    @Override
    public String getContextName() {
        return localeManager.getMessage("collectionItemContextName", getClassName());
    }

    private String getClassName(){
        if (null == getCurrentCollectionItem()){
            return "";
        }else{
            return getCurrentCollectionItem().getName();
        }
    }
}
