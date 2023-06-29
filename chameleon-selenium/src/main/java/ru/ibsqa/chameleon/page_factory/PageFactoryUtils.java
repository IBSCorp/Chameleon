package ru.ibsqa.chameleon.page_factory;

import ru.ibsqa.chameleon.definitions.annotations.selenium.Page;
import ru.ibsqa.chameleon.elements.IFacadeCollection;
import ru.ibsqa.chameleon.elements.collections.AbstractCollection;
import ru.ibsqa.chameleon.elements.selenium.WebElementFacade;
import ru.ibsqa.chameleon.page_factory.pages.ICollectionItemObject;
import ru.ibsqa.chameleon.page_factory.pages.IPageObject;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.lang.reflect.*;
import java.util.List;
import java.util.Objects;

import static org.apache.commons.lang3.reflect.ConstructorUtils.invokeConstructor;
import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
public class PageFactoryUtils {

    public static boolean isElement(Field field) {
        return !field.getType().isAssignableFrom(List.class) && isElement(field.getType());
    }

    public static boolean isBlock(Field field) {
        return IPageObject.class.isAssignableFrom(field.getType());
    }

    public static boolean isCollection(Field field) {
        return IFacadeCollection.class.isAssignableFrom(field.getType()) && ICollectionItemObject.class.isAssignableFrom(getGenericParameterClass(field));
    }

    public static boolean isList(Field field) {
        return field.getType().isAssignableFrom(List.class) && WebElementFacade.class.isAssignableFrom(getGenericParameterClass(field));
    }

    public static boolean isElement(Class<?> clazz) {
        return WebElementFacade.class.isAssignableFrom(clazz);
    }

    @SuppressWarnings("rawtypes")
    public static Class getGenericParameterClass(Field field) {
        Type genericType = field.getGenericType();
        return (Class) ((ParameterizedType) genericType).getActualTypeArguments()[0];
    }

    public static <T extends WebElementFacade> T createElement(Class<T> elementClass, WebElement elementToWrap,
                                                               String elementName, int waitTimeOut, String driverId) {
        T instance = newInstance(elementClass);
        if (Objects.nonNull(instance)) {
            instance.pushArguments(elementToWrap, elementName, waitTimeOut, driverId);
        }
        return instance;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T extends AbstractCollection> T createCollection(
            Class<T> collectionClass,
            String collectionName,
            int waitTimeOut,
            ElementLocator elementLocator,
            String[] frames,
            Class<?> collectionObjectClass
    ) {
        T instance = newInstance(collectionClass);
        if (Objects.nonNull(instance)) {
            instance.pushArguments(collectionName, waitTimeOut, elementLocator, frames, collectionObjectClass);
        }
        return instance;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> T newInstance(Class<T> clazz, Object... args)
            throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        if (clazz.isMemberClass() && !Modifier.isStatic(clazz.getModifiers())) {
            Class outerClass = clazz.getDeclaringClass();
            Object outerObject = outerClass.getConstructor().newInstance();
            return invokeConstructor(clazz, Lists.asList(outerObject, args).toArray());
        }
        return invokeConstructor(clazz, args);
    }

    public static <T> T newInstance(Class<T> clazz) {
        try {
            return invokeConstructor(clazz);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            log.error(e.getMessage(), e);
        }
        fail("Не удалось создать экземпляр класса: " + clazz.getName());
        return null;
    }

    @SuppressWarnings("rawtypes")
    public static String getElementNameAsString(Class clazz) {
        if(clazz.isAnnotationPresent(Page.class)){
            Page page = (Page) clazz.getAnnotation(Page.class);
            return ArrayUtils.toString(page);
        }
        return "";
    }

    public static String getElementNameAsString(Field field) {
        ru.ibsqa.chameleon.definitions.annotations.selenium.Field element =
                field.getAnnotation(ru.ibsqa.chameleon.definitions.annotations.selenium.Field.class);
        return element.name();
    }

    public static String[] getElementFrames(Field field) {
        ru.ibsqa.chameleon.definitions.annotations.selenium.Field element =
                field.getAnnotation(ru.ibsqa.chameleon.definitions.annotations.selenium.Field.class);
        return element.frames();
    }

    public static int getElementWaitTimeOut(Field field) {
        ru.ibsqa.chameleon.definitions.annotations.selenium.Field element =
                field.getAnnotation(ru.ibsqa.chameleon.definitions.annotations.selenium.Field.class);
        return element.waitTimeOut();
    }

    @SuppressWarnings("rawtypes")
    public static int getPageWaitTimeOut(Class clazz) {
        if (clazz.isAnnotationPresent(Page.class)) {
            Page page = (Page) clazz.getAnnotation(Page.class);
            return page.waitTimeOut();
        }
        return -1;
    }

    public static String[] getCollectionFrames(Field field) {
        ru.ibsqa.chameleon.definitions.annotations.selenium.Field element =
                field.getAnnotation(ru.ibsqa.chameleon.definitions.annotations.selenium.Field.class);
        return element.frames();
    }

}
