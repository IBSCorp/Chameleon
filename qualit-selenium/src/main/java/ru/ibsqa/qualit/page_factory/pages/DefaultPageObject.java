package ru.ibsqa.qualit.page_factory.pages;

import ru.ibsqa.qualit.definitions.annotations.selenium.Page;
import ru.ibsqa.qualit.elements.IFacade;
import ru.ibsqa.qualit.elements.IFacadeExportToJson;
import ru.ibsqa.qualit.elements.IFacadeReadable;
import ru.ibsqa.qualit.elements.selenium.WebElementFacade;
import ru.ibsqa.qualit.page_factory.IPageFactory;
import ru.ibsqa.qualit.page_factory.IPageObjectLoader;
import ru.ibsqa.qualit.page_factory.locator.IFrameManager;
import ru.ibsqa.qualit.page_factory.locator.ISearchStrategy;
import ru.ibsqa.qualit.selenium.driver.IDriverManager;
import ru.ibsqa.qualit.selenium.driver.WebDriverFacade;
import ru.ibsqa.qualit.selenium.enums.KeyEnum;
import ru.ibsqa.qualit.utils.spring.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.SearchContext;

import java.awt.*;
import java.util.List;

/**
 * Умеет быть основой для определения страниц в java коде
 * Порождается как спринговый бин
 */
@Slf4j
public abstract class DefaultPageObject implements IPageObject {

    private IDriverManager driverManager = SpringUtils.getBean(IDriverManager.class);

    private IFieldExtractor fieldExtractor = SpringUtils.getBean(IFieldExtractor.class);

    private ICollectionExtractor collectionExtractor = SpringUtils.getBean(ICollectionExtractor.class);

    private IContextManagerPage contextManagerSelenium = SpringUtils.getBean(IContextManagerPage.class);

    private ISearchStrategy searchStrategy = SpringUtils.getBean(ISearchStrategy.class);

    private IPageObjectLoader pageObjectLoader = SpringUtils.getBean(IPageObjectLoader.class);

    private IPageFactory pageFactory =  SpringUtils.getBean(IPageFactory.class);

    @Override
    public void beforePageLoaded() {
    }

    @Override
    public void afterPageLoaded() {
    }

    @Override
    public void switchFrames() {
        ru.ibsqa.qualit.definitions.annotations.selenium.Page aPage = this.getClass().getAnnotation(ru.ibsqa.qualit.definitions.annotations.selenium.Page.class);
        if (null != aPage) {
            IFrameManager.pageFrames(aPage.frames());
        }
        log.debug(String.format("loadPage(\"%s\")", getName()));
    }

    @Override
    public void loadPage() {
        contextManagerSelenium.setCurrentPage(this);
    }

    public WebDriverFacade getDriver() {
        ru.ibsqa.qualit.definitions.annotations.selenium.Page aPage = this.getClass().getAnnotation(Page.class);
        if (null != aPage) {
            return driverManager.getDriver(aPage.driver());
        } else if (this instanceof ICollectionItemObject) {
            return driverManager.getLastDriver();
        } else {
            return driverManager.getDriver(null);
        }
    }

    @Override
    public void initElements(SearchContext searchContext) {
        pageObjectLoader.load(this,searchContext);
    }

    public DefaultPageObject(SearchContext searchContext){
        initElements(searchContext);
    }

    public DefaultPageObject(){
        SearchContext searchContext = null;
        //TODO сделать работу с локатором страницы
/*        Page aPage = this.getClass().getAnnotation(Page.class);
        if (null != aPage) {
            String locator = aPage.locator();
            int waitTimeOut = aPage.waitTimeOut();
            if (null != locator && !locator.isEmpty()) {
                final By by = searchStrategy.getLocator(locator);
                final WebDriver driver = getDriver();
                if (waitTimeOut>=0) {
                    final Wait<WebDriver> wait = new WebDriverWait(driver, waitTimeOut);
                    searchContext = wait.until(ExpectedConditions.presenceOfElementLocated(by));
                } else {
                    try {
                        searchContext = driver.findElement(by);
                    }catch (NoSuchElementException e){
                        fail("Не найден элемент по локатору: " + locator);
                    }

                }
            }
        }*/
        initElements(searchContext);
    }

    public DefaultPageObject(boolean initElements) {
        if (initElements) {
            initElements(null);
        }
    }

    @Override
    public <FACADE extends IFacade> FACADE getField(String fieldName) {
        return fieldExtractor.getField(this, fieldName);
    }

    @Override
    public <FACADE extends WebElementFacade> FACADE getSeleniumField(String fieldName) {
        return getField(fieldName);
    }

    @Override
    public Iterable<? extends IPageObject> getCollection(String collectionName) {
       return collectionExtractor.getField(this, collectionName);
       /* if (null != field) {
            try {
                return (Iterable<? extends IPageObject>) field.get(this);
            } catch (IllegalAccessException e) {
                log.error(e.getMessage(), e);
                fail(e.getMessage());
            }
        }
        return null;
*/
    }

    @Override
    public void pressKey(KeyEnum key) {
        try {
            Robot robot = new Robot();
            robot.setAutoDelay(250);
            robot.keyPress(key.getValue());
            robot.keyRelease(key.getValue());
        }catch (Exception e){}
    }

    @Override
    public JSONObject exportToJson() throws JSONException {
        // Пройти по всем полям
        JSONObject jsonObject = new JSONObject();

        List<String> fieldNames = pageFactory.getFieldNames(this);

        if (null != fieldNames) {
            for (String filedName : fieldNames) {
                IFacadeReadable field = getField(filedName);
                jsonObject.put(filedName, field.getFieldValue());
            }
        }

        List<String> collectionNames = pageFactory.getCollectionNames(this);

        if (null != collectionNames) {
            for (String collectionName : collectionNames) {
                Object collection = getCollection(collectionName);
                if (collection instanceof IFacadeExportToJson) {
                    jsonObject.put(collectionName, ((IFacadeExportToJson)collection).exportToJson());
                }
            }
        }

        return jsonObject;
    }

}
