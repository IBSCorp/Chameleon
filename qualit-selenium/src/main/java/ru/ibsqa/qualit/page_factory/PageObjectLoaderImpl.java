package ru.ibsqa.qualit.page_factory;

import lombok.val;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ibsqa.qualit.definitions.annotations.selenium.Page;
import ru.ibsqa.qualit.elements.selenium.BlockElement;
import ru.ibsqa.qualit.elements.selenium.WebElementFacade;
import ru.ibsqa.qualit.page_factory.decorator.AbstractFieldDecorator;
import ru.ibsqa.qualit.page_factory.handlers.ElementBlockProxyHandler;
import ru.ibsqa.qualit.page_factory.locator.IFrameManager;
import ru.ibsqa.qualit.page_factory.pages.IPageObject;
import ru.ibsqa.qualit.selenium.driver.WebDriverFacade;

import java.lang.reflect.InvocationHandler;

import static ru.ibsqa.qualit.page_factory.PageFactoryUtils.getElementNameAsString;

@Component
public class PageObjectLoaderImpl implements IPageObjectLoader {

    @Autowired
    private IFrameManager frameSelector;

    @Autowired
    private AbstractFieldDecorator fieldDecorator;

    @Override
    public void load(IPageObject pageObject, SearchContext searchContext){
        if (isClassBlock(pageObject)){//если для элементов этого класса надо создать родительский элемент
            WebElementFacade blockElement = createBlockElement(pageObject,  searchContext);
            initElements(blockElement, pageObject);
        }else {
            initElements(searchContext, pageObject);
        }
    }

    private boolean isClassBlock(IPageObject pageObject){
        return (pageObject.getClass().isAnnotationPresent(Page.class)
                && !pageObject.getClass().getAnnotation(Page.class).locator().isEmpty());
    }

    private void initElements(SearchContext searchContext, IPageObject pageObject){
        fieldDecorator.setElementLocatorFactory(pageObject.getDriver().getElementLocatorFactory(searchContext));
        PageFactory.initElements(fieldDecorator, pageObject);
    }

    public <T extends WebElementFacade> WebElementFacade createBlockElement(IPageObject pageObject, SearchContext searchContext) {
        ElementLocator locator = pageObject.getDriver().getElementLocatorFactory(searchContext).createLocator(pageObject.getClass());
        String elementName = PageFactoryUtils.getElementNameAsString(pageObject.getClass());
        int waitTimeOut = PageFactoryUtils.getPageWaitTimeOut(pageObject.getClass());
        if (waitTimeOut < 0) {
            waitTimeOut = Math.max(pageObject.getDriver().getImplicitlyWait(), pageObject.getDriver().getDefaultWaitTimeOut());
        }

        InvocationHandler handler = new ElementBlockProxyHandler(locator, elementName, waitTimeOut);
        WebElement elementToWrap = ProxyFactory.createWebElementProxy(pageObject.getClass().getClassLoader(), handler);
        return createBlockElement(pageObject.getDriver(), elementToWrap, elementName, waitTimeOut);
    }

    public <T extends WebElementFacade> T createBlockElement(WebDriverFacade driver, WebElement elementToWrap, String elementName, int waitTimeOut) {
        val blockElement = (T) new BlockElement();
        blockElement.pushArguments(elementToWrap, elementName, waitTimeOut, driver.getId());
        return blockElement;
    }
}
