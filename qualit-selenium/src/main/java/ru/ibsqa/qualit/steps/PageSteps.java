package ru.ibsqa.qualit.steps;

import ru.ibsqa.qualit.page_factory.pages.IContextManagerPage;
import ru.ibsqa.qualit.page_factory.pages.IPageObject;
import ru.ibsqa.qualit.page_factory.pages.Page;
import ru.ibsqa.qualit.selenium.driver.IDriverManager;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PageSteps extends AbstractSteps {

    @Autowired
    private IDriverManager driverManager;

    @Autowired
    private IContextManagerPage contextManager;

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

    public void currentPageShouldBeLoaded() {
        Assertions.assertTrue(contextManager.getCurrentPage().isLoaded(), getLocaleManager().getMessage("pageNotLoadedAssertMessage", contextManager.getCurrentPage().getName()));
    }

    @UIStep
    @TestStep("страница \"${page}\" загружена")
    public IPageObject stepLoadedPage(IPageObject page) {
        setPage(page);
        pageShouldBeLoaded(page.getClass().getCanonicalName());
        return contextManager.getCurrentPage();
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

    @TestStep("драйвер с идентификатором \"${driverId}\" установлен по умолчанию")
    public void stepSelectDefaultDriverById(String driverId) {
        driverManager.setCurrentDefaultDriverId(driverId);
    }

    @UIStep
    @TestStep("переключились к предыдущей странице")
    public IPageObject switchToPreviousPage() {
        IPageObject page = contextManager.switchToPreviousPage();
        currentPageShouldBeLoaded();
        return contextManager.getCurrentPage();
    }

    @TestStep("в переменной \"${variable}\" сохранен json текущей страницы")
    public void exportPageToJson(String variable) {
        setVariable(variable, contextManager.getCurrentPage().getFieldValue());
    }

}
