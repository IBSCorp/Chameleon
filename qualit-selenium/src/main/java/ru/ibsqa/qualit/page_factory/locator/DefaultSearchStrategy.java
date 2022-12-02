package ru.ibsqa.qualit.page_factory.locator;

import ru.ibsqa.qualit.context.IContextExplorer;
import ru.ibsqa.qualit.definitions.annotations.selenium.Collection;
import ru.ibsqa.qualit.definitions.annotations.selenium.Field;
import ru.ibsqa.qualit.evaluate.IEvaluateManager;
import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class DefaultSearchStrategy implements ISearchStrategy {

    @Autowired
    private IEvaluateManager evaluateManager;

    @Autowired
    private IContextExplorer contextExplorer;

    /**
     * Вычисления в локаторе
     * @param locator
     * @return
     */
    private String evalLocator(String locator) {
        return evaluateManager.evalVariable(contextExplorer.getPickElementScope(), locator);
    }

    @Override
    public By getLocator(String locator) {

        locator = evalLocator(locator);

        Pattern typeExtractor = Pattern.compile("^(id|xpath|class|link|css|tag|name|partialLink)\\s*\\=\\s*(.*)$");
        Matcher matcher = typeExtractor.matcher(locator);

        if (!matcher.find())
            return By.xpath(locator);
        else {
            String type = matcher.group(1);
            String expr = matcher.group(2).trim();

            if (type.contains("id")) {
                return By.id(expr);
            } else if (type.contains("xpath")) {
                return By.xpath(expr);
            } else if (type.contains("class")) {
                return By.className(expr);
            } else if (type.contains("link")) {
                return By.linkText(expr);
            } else if (type.contains("css")) {
                return By.cssSelector(expr);
            } else if (type.contains("partialLink")) {
                return By.partialLinkText(expr);
            } else if (type.contains("tag")) {
                return By.tagName(expr);
            } else if (type.contains("name")) {
                return By.name(expr);
            }
        }
        return By.xpath(locator);
    }

    private String getLocatorStrValue(java.lang.reflect.Field field) {
        if (field.getAnnotation(Field.class) != null) {
            return field.getAnnotation(Field.class).locator();
        } else if (field.getAnnotation(Collection.class) != null){
            return field.getAnnotation(Collection.class).locator();
        }
        return null;
    }

    @Override
    public By getLocator(java.lang.reflect.Field field) {
        String locatorStrValue = getLocatorStrValue(field);
        if (!locatorStrValue.isEmpty()) {
            return getLocator(locatorStrValue);
        }
        return null;
    }

    @Override
    public boolean isDynamic(java.lang.reflect.Field field) {
        String locatorStrValue = getLocatorStrValue(field);
        return locatorStrValue.contains("#{");
    }
}