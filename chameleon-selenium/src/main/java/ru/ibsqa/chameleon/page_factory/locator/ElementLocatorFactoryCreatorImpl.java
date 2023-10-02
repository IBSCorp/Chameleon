package ru.ibsqa.chameleon.page_factory.locator;

import org.openqa.selenium.SearchContext;
import org.springframework.stereotype.Component;
import ru.ibsqa.chameleon.selenium.driver.IDriverFacade;
import ru.ibsqa.chameleon.utils.spring.SpringUtils;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ElementLocatorFactoryCreatorImpl implements IElementLocatorFactoryCreator {

    private final Map<String, IElementLocatorFactory> cache = new ConcurrentHashMap<>();

    @Override
    public IElementLocatorFactory createElementLocatorFactory(IDriverFacade driverFacade, SearchContext searchContext) {
        final String driverId = driverFacade.getId();
        if (Objects.isNull(searchContext)) {
            synchronized (this) {
                return Optional.ofNullable(cache.get(driverId)).orElseGet(() -> {
                    IElementLocatorFactory elementLocatorFactory = createElementLocatorFactory();
                    elementLocatorFactory.setDriverId(driverId);
                    cache.put(driverId, elementLocatorFactory);
                    return elementLocatorFactory;
                });
            }
        } else {
            IElementLocatorFactory elementLocatorFactory = createElementLocatorFactory();
            elementLocatorFactory.setDriverId(driverId);
            elementLocatorFactory.setSearchContext(searchContext);
            return elementLocatorFactory;
        }
    }

    protected IElementLocatorFactory createElementLocatorFactory() {
        return SpringUtils.getBean(IElementLocatorFactory.class);
    }

}
