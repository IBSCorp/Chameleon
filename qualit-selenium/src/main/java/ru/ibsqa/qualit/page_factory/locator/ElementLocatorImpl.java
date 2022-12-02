package ru.ibsqa.qualit.page_factory.locator;

import ru.ibsqa.qualit.page_factory.handlers.AbstractElementAnnotationsHandler;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.AjaxElementLocator;
import org.openqa.selenium.support.pagefactory.DefaultElementLocator;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Переопределяем базовый механизм работы с локаторами selenium.
 * Если локатор содержит #{, значит он должен вычисляться не один раз? при создании формы, а каждый раз при поиске элемента.
 * В этом случае также следует предотвращать кэширование.
 */
@Slf4j
public class ElementLocatorImpl extends AjaxElementLocator {

    private final AbstractElementAnnotationsHandler annotations;
    private Field byField;
    private Field cachedElementField;
    private Field cachedElementListField;

    public ElementLocatorImpl(SearchContext context, int timeOutInSeconds, AbstractElementAnnotationsHandler annotations) {
        super(context, timeOutInSeconds, annotations);
        this.annotations = annotations;

        try {
            byField = DefaultElementLocator.class.getDeclaredField("by");
            byField.setAccessible(true);
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(byField, byField.getModifiers() & ~Modifier.FINAL);

            cachedElementField = DefaultElementLocator.class.getDeclaredField("cachedElement");
            cachedElementField.setAccessible(true);

            cachedElementListField = DefaultElementLocator.class.getDeclaredField("cachedElementList");
            cachedElementListField.setAccessible(true);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    public WebElement findElement() {
        doDynamic();
        return super.findElement();
    }

    public List<WebElement> findElements() {
        doDynamic();
        return super.findElements();
    }

    private void doDynamic() {
        if (annotations.isDynamic()) {
            try {
                byField.set(this, annotations.buildBy());
                cachedElementField.set(this, null);
                cachedElementListField.set(this, null);
            } catch (IllegalAccessException e) {
                log.error(e.getMessage(), e);
                fail(e.getMessage());
            }
        }
    }

}
