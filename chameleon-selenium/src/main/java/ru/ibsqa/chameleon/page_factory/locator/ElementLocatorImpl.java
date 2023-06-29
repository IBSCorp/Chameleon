package ru.ibsqa.chameleon.page_factory.locator;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.AjaxElementLocator;
import org.openqa.selenium.support.pagefactory.DefaultElementLocator;
import ru.ibsqa.chameleon.page_factory.handlers.AbstractElementAnnotationsHandler;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Переопределяем базовый механизм работы с локаторами selenium.
 * Если локатор содержит #{, значит он должен вычисляться не один раз? при создании формы, а каждый раз при поиске элемента.
 * В этом случае также следует предотвращать кэширование.
 *
 * Также особенностью данного локатора является то, что при поиске отдельного элемента срабатывает ожидание, а при поиске списка - нет
 */
@Slf4j
public class ElementLocatorImpl extends AjaxElementLocator {

    private final AbstractElementAnnotationsHandler annotations;
    private Field byField;
    private Field cachedElementField;
    private Field cachedElementListField;
    private Field timeoutField;

    private ThreadLocal<Integer> timeOutInSecondsStore = new ThreadLocal<>();

    public ElementLocatorImpl(SearchContext context, int timeOutInSeconds, AbstractElementAnnotationsHandler annotations) {
        super(context, timeOutInSeconds, annotations);
        this.annotations = annotations;

        try {
            byField = DefaultElementLocator.class.getDeclaredField("by");
            byField.setAccessible(true);

            cachedElementField = DefaultElementLocator.class.getDeclaredField("cachedElement");
            cachedElementField.setAccessible(true);

            cachedElementListField = DefaultElementLocator.class.getDeclaredField("cachedElementList");
            cachedElementListField.setAccessible(true);

            timeoutField = AjaxElementLocator.class.getDeclaredField("timeOutInSeconds");
            timeoutField.setAccessible(true);

            //Field modifiersField = Field.class.getDeclaredField("modifiers");
            //modifiersField.setAccessible(true);
            //modifiersField.setInt(byField, byField.getModifiers() & ~Modifier.FINAL);

            var lookup = MethodHandles.privateLookupIn(Field.class, MethodHandles.lookup());
            VarHandle MODIFIERS = lookup.findVarHandle(Field.class, "modifiers", int.class);
            MODIFIERS.set(byField, byField.getModifiers() & ~Modifier.FINAL);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    /**
     * Поиск единственноего элемента. Производится в пределах timeOutInSeconds, пока элемент не будет найден или не закончится таймаут
     * @return
     */
    public WebElement findElement() {
        doDynamic();
        return super.findElement();
    }

    /**
     * Поиск списка элементов, таймаут не применяется
     * @return
     */
    public List<WebElement> findElements() {
        doDynamic();
        List<WebElement> list;
        timeOutInSecondsStore.set(timeOutInSeconds);
        try {
            setTimeOutInSeconds(0);
            list = super.findElements();
        } finally {
            setTimeOutInSeconds(timeOutInSecondsStore.get());
        }
        return list;
    }

    protected void setTimeOutInSeconds(int timeOutInSeconds) {
        try {
            timeoutField.set(this, timeOutInSeconds);
        } catch (IllegalAccessException e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    protected void doDynamic() {
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
