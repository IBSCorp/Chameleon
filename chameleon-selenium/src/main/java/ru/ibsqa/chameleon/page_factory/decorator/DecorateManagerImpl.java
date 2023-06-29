package ru.ibsqa.chameleon.page_factory.decorator;

import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ibsqa.chameleon.elements.collections.AbstractCollection;
import ru.ibsqa.chameleon.elements.selenium.WebElementFacade;
import ru.ibsqa.chameleon.i18n.ILocaleManager;
import ru.ibsqa.chameleon.page_factory.pages.IPageObject;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Component
public class DecorateManagerImpl implements IDecorateManager {

    @Autowired
    private ILocaleManager localeManager;

    private List<IDecorateExecutor> executors;

    @Autowired
    private void collectResolvers(List<IDecorateExecutor> executors) {
        this.executors = executors;
        this.executors.sort(Comparator.comparing(IDecorateExecutor::getPriority));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends WebElementFacade> T decorateElement(ElementLocatorFactory elementLocatorFactory, ClassLoader loader, Field field) {
        return decorate((e) -> (T) e.decorateElement(elementLocatorFactory, loader, field));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends AbstractCollection> T decorateCollection(ElementLocatorFactory elementLocatorFactory, ClassLoader loader, Field field) {
        return decorate((e) -> (T) e.decorateCollection(elementLocatorFactory, loader, field));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends WebElementFacade> List<T> decorateList(ElementLocatorFactory elementLocatorFactory, ClassLoader loader, Field field) {
        return decorate((e) -> (List<T>) e.decorateList(elementLocatorFactory, loader, field));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends IPageObject> T decorateBlock(ElementLocatorFactory elementLocatorFactory, ClassLoader loader, Field field) {
        return decorate((e) -> (T) e.decorateBlock(elementLocatorFactory, loader, field));
    }

    private <T> T decorate(Function<IDecorateExecutor, T> execution) {
        return executors.stream()
                .map(e -> (T) execution.apply(e))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new RuntimeException(localeManager.getMessage("decorateExecutorNotFound")));
    }

}
