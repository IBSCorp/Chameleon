package ru.ibsqa.qualit.page_factory.locator;

import ru.ibsqa.qualit.elements.selenium.IFacadeSelenium;
import ru.ibsqa.qualit.selenium.driver.IDriverManager;
import ru.ibsqa.qualit.selenium.driver.WebDriverFacade;
import ru.ibsqa.qualit.utils.spring.SpringUtils;
import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

/**
 * Фабрика локаторов. Она знает SearchContext драйвера (драйвер сам устанавливает его после инициализации).
 * На каждый драйвер создается своя фабрика, со своим контекстом.
 * Кроме того, у драйвера функция getElementLocatorFactory(SearchContext searchContext) умеет создавать новую фабрику,
 * не на основе контекста драйвера, а на основе любого заданного контекста, а если контекст null, то возвращается контекст драйвера.
 * Это используется для поиска относительно найденного элемента.
 *
* Имплементации фабрики могут содержать знания о стратегии обработки локаторов
 */
public abstract class AbstractElementLocatorFactory implements ElementLocatorFactory {

    @Setter @Getter
    private SearchContext searchContext;

    protected int getDefaultWaitTimeOut() {
        return getDriver().getDefaultWaitTimeOut();
    }

    @Setter
    private String driverId;

    public String getDriverId() {
        if (null == driverId){
            if (IFacadeSelenium.class.isAssignableFrom(searchContext.getClass())){
                driverId = ((IFacadeSelenium)searchContext).getDriver().getId();
            } else {
                driverId = SpringUtils.getBean(IDriverManager.class).getLastDriver().getId();
            }
        }
        return driverId;
    }
    public abstract ElementLocator createLocator(Class clazz);

    public WebDriverFacade getDriver() {
        return SpringUtils.getBean(getDriverId());
    }
}
