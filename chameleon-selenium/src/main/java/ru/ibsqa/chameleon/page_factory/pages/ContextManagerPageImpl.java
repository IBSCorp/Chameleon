package ru.ibsqa.chameleon.page_factory.pages;

import ru.ibsqa.chameleon.context.IContextManager;
import ru.ibsqa.chameleon.context.IContextObject;
import ru.ibsqa.chameleon.context.IContextRegistrator;
import ru.ibsqa.chameleon.context.PickElementResult;
import ru.ibsqa.chameleon.definitions.annotations.selenium.Page;
import ru.ibsqa.chameleon.elements.IFacade;
import ru.ibsqa.chameleon.elements.IFacadeCollection;
import ru.ibsqa.chameleon.i18n.ILocaleManager;
import ru.ibsqa.chameleon.page_factory.IPageFactory;
import ru.ibsqa.chameleon.page_factory.configuration.IPageObjectConfiguration;
import ru.ibsqa.chameleon.reflections.ReflectionHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

@Component
@Slf4j
public class ContextManagerPageImpl implements IContextManagerPage, IContextRegistrator {

    @Override
    public String getContextCode() {
        return localeManager.getMessage("pageContextCode");
    }

    @Override
    public String getContextName() {
        return localeManager.getMessage("pageContextName", getPageName());
    }

    @Autowired
    private ILocaleManager localeManager;

    @Autowired
    private IPageFactory pageFactory;

    @Autowired
    private IContextManagerCollectionItem contextManagerCollectionItem;

    @Autowired
    protected IPageObjectConfiguration pageObjectConfiguration;

    private ThreadLocal<IPageObject> currentPage = new ThreadLocal<>();

    private List<IPageObject> pageStack = new ArrayList<>();

    @Override
    public IPageObject getCurrentPage() {
        return currentPage.get();
    }

    @Override
    public void setCurrentPage(IPageObject page) {
        currentPage.set(page);
        contextManagerCollectionItem.clearCurrentCollectionItem();
        pageStack.add(page);
    }

    @Override
    public void setCurrentPage(Class<? extends IPageObject> page) {
        try {
            IPageObject pageObject = (IPageObject) page.getConstructor().newInstance();
            setCurrentPage(pageObject);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.error(e.getMessage(), e);
            fail("Не удалось создать экземпляр страницы: " + page.getSimpleName() + " : " + e.getMessage());
        }
    }

    @Override
    public void setCurrentPage(String pageName) {
        Class<? extends IPageObject> aClass;
        if (pageObjectConfiguration.getPagesPackage() != null){
            aClass = searchPageObjectClass(pageObjectConfiguration.getPagesPackage(), pageName);
        }else {
            aClass = pageFactory.generatePage(pageName);
        }
        setCurrentPage(aClass);
    }

    private  <PAGE extends IPageObject> Class<PAGE> searchPageObjectClass(String packageName, String pageName){
        List<Class<?>> pageClasses = ReflectionHelper.findClassesImpmenenting(IPageObject.class, packageName);
        return (Class<PAGE>) pageClasses.stream()
                .filter(page -> page.isAnnotationPresent(ru.ibsqa.chameleon.definitions.annotations.selenium.Page.class))
                .filter(page -> page.getAnnotation(Page.class).name().equals(pageName))
                .findFirst()
                .orElse(null);
    }

    @Override
    public IPageObject switchToPreviousPage() {
        IPageObject prevPage = null;
        if (pageStack.size()>1) {
            int lastIndex = pageStack.size()-1;
            int prevIndex = lastIndex-1;
            prevPage = pageStack.get(prevIndex);
            pageStack.remove(lastIndex);
        }
        assertNotNull(prevPage, localeManager.getMessage("prevPageNotFoundAssertMessage"));
        currentPage.set(prevPage);
        contextManagerCollectionItem.clearCurrentCollectionItem();
        return prevPage;
    }

    @Override
    public void setContextCollectionItem(IContextObject contextObject) {
        if (contextObject instanceof ICollectionItemObject) {
            contextManagerCollectionItem.setCurrentCollectionItem((ICollectionItemObject) contextObject);
        } else {
            fail(localeManager.getMessage("elements.incorrectPageContextErrorMessage"));
        }
    }

    @Override
    public <FACADE extends IFacade> PickElementResult pickElement(String fullPathName, Class<FACADE> fieldType) {

        IPageObject currentPage = getCurrentPage();

        // Если страница не была установлена, то не ищем на ней
        if (null == currentPage) {
            return null;
        }

        // Сформируем ответ
        PickElementResult<FACADE, IPageObject> pickElementResult = PickElementResult
                .builder()
                .contextManager((IContextManager) this) // Ссылка на наш менеджер контекстов, который нашел
                .contextObject(currentPage) // Ссылка на страницу, где найден элемент
                .build();

        if (IFacadeCollection.class.isAssignableFrom(fieldType)) {
            pickElementResult.setElement((FACADE) currentPage.getCollection(fullPathName));  // Ссылка на найденный элемент
        } else {
            pickElementResult.setElement((FACADE) currentPage.getField(fullPathName));  // Ссылка на найденный элемент
        }

        if (null == pickElementResult.getElement()) {
            return null;
        }

        return pickElementResult;
    }

    private String getPageName() {
        if (null == getCurrentPage()){
            return "";
        }else{
            return getCurrentPage().getName();
        }
    }
}
