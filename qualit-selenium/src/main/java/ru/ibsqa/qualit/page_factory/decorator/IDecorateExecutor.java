package ru.ibsqa.qualit.page_factory.decorator;

import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;
import ru.ibsqa.qualit.elements.collections.AbstractCollection;
import ru.ibsqa.qualit.elements.selenium.WebElementFacade;
import ru.ibsqa.qualit.page_factory.pages.IPageObject;

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
