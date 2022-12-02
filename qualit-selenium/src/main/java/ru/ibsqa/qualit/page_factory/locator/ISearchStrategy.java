package ru.ibsqa.qualit.page_factory.locator;

import org.openqa.selenium.By;

import java.lang.reflect.Field;

public interface ISearchStrategy {

    By getLocator(String locator);
    By getLocator(Field field);

    /**
     * Признак того, что локатор содержит плейсхолдеры #{...}. Это означает, что требуется
     * искать элемент не при создании формы, а при обращении к нему.
     * @return
     */
    boolean isDynamic(Field field);
}
