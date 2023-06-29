package ru.ibsqa.chameleon.page_factory.locator;

import ru.ibsqa.chameleon.definitions.annotations.selenium.Field;
import ru.ibsqa.chameleon.definitions.annotations.selenium.Page;
import ru.ibsqa.chameleon.page_factory.handlers.ClassAnnotationsHandler;
import ru.ibsqa.chameleon.page_factory.handlers.ElementAnnotationsHandler;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.support.pagefactory.AjaxElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class ElementLocatorFactoryImpl extends AbstractElementLocatorFactory {

    @Autowired
    private ISearchStrategy searchStrategy;

    @Override
    public ElementLocator createLocator(java.lang.reflect.Field field) {
        return new ElementLocatorImpl(getSearchContext(), getTimeOut(field), new ElementAnnotationsHandler(getDriverId(), searchStrategy, field));
    }

    @Override
    public ElementLocator createLocator(Class clazz) {
        return new AjaxElementLocator(getSearchContext(), getTimeOut(clazz), new ClassAnnotationsHandler(getDriverId(), clazz));
    }

    public int getTimeOut(java.lang.reflect.Field field) {
        if (field.isAnnotationPresent(Field.class)) {
            if (field.getAnnotation(Field.class).waitTimeOut() == -1)
                return getDefaultWaitTimeOut();
            else
                return field.getAnnotation(Field.class).waitTimeOut();

        }
        if (field.getGenericType() instanceof Class) {
            return getTimeOut((Class) field.getGenericType());
        }
        return getTimeOut((Class) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0]);
    }

    public int getTimeOut(Class clazz) {
        try {
            Method method = Page.class.getMethod("timeout");
            do {
                if (clazz.isAnnotationPresent(Page.class)) {
                    return (Integer) method.invoke(clazz.getAnnotation(Page.class));
                }
                clazz = clazz.getSuperclass();
            } while (clazz != Object.class && clazz != null);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
        }

        return getDefaultWaitTimeOut();
    }
}
