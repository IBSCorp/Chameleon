package ru.ibsqa.chameleon.steps;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.TimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ibsqa.chameleon.page_factory.locator.IFrameManager;
import ru.ibsqa.chameleon.page_factory.pages.IContextManagerPage;
import ru.ibsqa.chameleon.page_factory.pages.IPageObject;
import ru.ibsqa.chameleon.page_factory.pages.Page;
import ru.ibsqa.chameleon.selenium.driver.IDriverManager;
import ru.ibsqa.chameleon.selenium.driver.WebDriverFacade;
import ru.ibsqa.chameleon.utils.waiting.WaitingUtils;

import java.util.Optional;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@Component
public class BrowserSteps extends AbstractSteps {

    @Autowired
    private IDriverManager driverManager;

    @Autowired
    private IContextManagerPage contextManager;

    @Autowired
    private WaitingUtils waitingUtils;

    @Autowired
    private IFrameManager frameManager;

    @Autowired
    private PageSteps pageSteps;

    private void setPage(Page page) {
        contextManager.setCurrentPage(page.getAsClass());
    }

    private void setPage(IPageObject pageObject) {
        contextManager.setCurrentPage(pageObject);
    }

    private void setPage(String page) {
        contextManager.setCurrentPage(page);
    }

    public void pageShouldBeLoaded(String page) {
        Assertions.assertTrue(contextManager.getCurrentPage().isLoaded(), getLocaleManager().getMessage("pageNotLoadedAssertMessage", page));
    }

    @Deprecated
    private void pageShouldBeNotLoaded(String page) {
        try {
            Assertions.assertFalse(contextManager.getCurrentPage().isLoaded(), getLocaleManager().getMessage("pageLoadedAssertMessage", page));
        } catch (TimeoutException e) {
        }
    }

    @UIStep
    @TestStep("страница \"${page}\" загружена")
    public IPageObject stepLoadedPage(Page page) {
        setPage(page);
        pageShouldBeLoaded(page.toString());
        return contextManager.getCurrentPage();
    }

    @UIStep
    @TestStep("страница \"${page}\" загружена")
    public IPageObject stepLoadedPage(String page) {
        setPage(page);
        pageShouldBeLoaded(page);
        return contextManager.getCurrentPage();
    }

    @UIStep
    @TestStep("открыта страница по адресу \"${url}\"")
    public void stepOpenUrl(String url) {
        openUrl(driverManager.getDriver(null), url);
    }

    @UIStep
    @TestStep("драйвером \"${driver}\" открыта страница по адресу \"${url}\"")
    public void stepOpenUrl(String driver, String url) {
        openUrl(driverManager.getDriver(driver), url);
    }

    private void openUrl(WebDriverFacade driver, String url) {
        driver.get(url);
    }

    @UIStep
    @TestStep("выполнен переход к окну \"${value}\"")
    public void switchToWindow(String value) {
        if (!Optional.of(driverManager.getLastDriver())
                .filter(driver ->
                        waitingUtils.waiting(driver.getDefaultWaitTimeOut() * 1000, () -> {
                                    for (String w : driver.getWindowHandles()) {
                                        try {
                                            driver.switchTo().window(w);
                                            if (Pattern.matches(value, driver.getTitle())) {
                                                return true;
                                            }
                                        } catch (Exception e) {
                                        }
                                    }
                                    return false;
                                }
                        ))
                .isPresent()
        ) {
            fail("Не найдено окно с заголовком: " + value);
        }
    }

    @UIStep
    @TestStep("выполнен переход к окну \"${value}\" и загружена страница \"${page}\"")
    public void switchToWindowAndPageLoaded(String value, String page) {
        switchToWindow(value);
        stepLoadedPage(page);
    }

    @UIStep
    @TestStep("закрыто текущее окно")
    public void closeWindow() {
        driverManager.getLastDriver().close();
    }

    @UIStep
    @TestStep("адрес окна соответствует маске \"${value}\"")
    public void checkWindowUrl(String value) {
        String url = driverManager.getLastDriver().getCurrentUrl();
        assertTrue(Pattern.matches(value, url), "Адрес текущего окна не соответствует " + value);
    }

    @UIStep
    @TestStep("обновлена страница и ожидается окончание загрузки")
    public void refreshAndPageLoaded() {
        refreshPage();
        pageSteps.currentPageShouldBeLoaded();
    }

    @UIStep
    @TestStep("обновлена страница")
    public void refreshPage() {
        driverManager.getLastDriver().navigate().refresh();
    }

    @TestStep("выполнен переход к фрейму \"${framePath}\"")
    public void switchToFrameById(String framePath) {
        frameManager.changeFramePath(framePath);
    }

    @TestStep("выполнен переход к фрейму по умолчанию")
    public void switchToDefaultFrame() {
        frameManager.changeFramePath(null);
    }

}
