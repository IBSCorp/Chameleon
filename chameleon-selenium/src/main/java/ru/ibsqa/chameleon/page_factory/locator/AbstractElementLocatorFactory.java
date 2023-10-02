package ru.ibsqa.chameleon.page_factory.locator;

import ru.ibsqa.chameleon.elements.selenium.IFacadeSelenium;
import ru.ibsqa.chameleon.selenium.driver.IDriverFacade;
import ru.ibsqa.chameleon.selenium.driver.IDriverManager;
import ru.ibsqa.chameleon.utils.spring.SpringUtils;
import org.openqa.selenium.SearchContext;

import java.util.Optional;

/**
 * Фабрика локаторов. Она знает SearchContext драйвера (драйвер сам устанавливает его после инициализации).
 * На каждый драйвер создается своя фабрика, со своим контекстом.
 * Кроме того, у драйвера функция getElementLocatorFactory(SearchContext searchContext) умеет создавать новую фабрику,
 * не на основе контекста драйвера, а на основе любого заданного контекста, а если контекст null, то возвращается контекст драйвера.
 * Это используется для поиска относительно найденного элемента.
 *
* Имплементации фабрики могут содержать знания о стратегии обработки локаторов
 */
public abstract class AbstractElementLocatorFactory implements IElementLocatorFactory {

    private SearchContext searchContext;
    private String driverId;
    private IDriverFacade driver;

    public void setSearchContext(SearchContext searchContext) {
        this.searchContext = searchContext;
    }

    public SearchContext getSearchContext() {
        return Optional.ofNullable(this.searchContext)
                .orElseGet(this::getDriver);
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public synchronized String getDriverId() {
        if (null == driverId) {
            if (IFacadeSelenium.class.isAssignableFrom(searchContext.getClass())) {
                setDriverId(((IFacadeSelenium)searchContext).getDriver().getId());
            } else {
                setDriverId(SpringUtils.getBean(IDriverManager.class).getLastDriver().getId());
            }
        }
        return driverId;
    }

    protected int getDefaultWaitTimeOut() {
        return getDriver().getDefaultWaitTimeOut();
    }

    public IDriverFacade getDriver() {
        return Optional.ofNullable(driver)
                .orElseGet(() -> driver = SpringUtils.getBean(getDriverId()));
    }
}
