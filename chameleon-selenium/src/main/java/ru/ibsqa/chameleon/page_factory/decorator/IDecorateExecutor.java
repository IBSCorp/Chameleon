package ru.ibsqa.chameleon.page_factory.decorator;

import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;
import ru.ibsqa.chameleon.elements.collections.AbstractCollection;
import ru.ibsqa.chameleon.elements.selenium.WebElementFacade;
import ru.ibsqa.chameleon.page_factory.pages.IPageObject;

import java.lang.reflect.Field;
import java.util.List;

public interface IDecorateExecutor {
    <T extends WebElementFacade> T decorateElement (
            ElementLocatorFactory elementLocatorFactory,
            ClassLoader loader,
            Field field
    );
    <T extends AbstractCollection> T decorateCollection (
            ElementLocatorFactory elementLocatorFactory,
            ClassLoader loader,
            Field field
    );
    <T extends WebElementFacade> List<T> decorateList (
            ElementLocatorFactory elementLocatorFactory,
            ClassLoader loader,
            Field field
    );
    <T extends IPageObject> T decorateBlock (
            ElementLocatorFactory elementLocatorFactory,
            ClassLoader loader,
            Field field
    );

    default ConfigurationPriority getPriority() {
        return ConfigurationPriority.HIGH;
    }
}
