package ru.ibsqa.qualit.page_factory.handlers;

import ru.ibsqa.qualit.definitions.annotations.selenium.Field;
import ru.ibsqa.qualit.page_factory.locator.ISearchStrategy;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;

import static ru.ibsqa.qualit.page_factory.PageFactoryUtils.*;

public class ElementAnnotationsHandler extends AbstractElementAnnotationsHandler {

    private final ISearchStrategy searchStrategy;

    @Getter
    private final boolean dynamic;

    public ElementAnnotationsHandler(String driverId, ISearchStrategy searchStrategy, java.lang.reflect.Field field) {
        super(driverId, field);
        this.searchStrategy = searchStrategy;
        this.dynamic = searchStrategy.isDynamic(field);
    }

    @Override
    public By buildBy() {
        if (isElement(getField()) || isCollection(getField()) || isList(getField())) {
            return buildByFromHtmlElementAnnotations();
        }
        return super.buildBy();
    }

    private By buildByFromFindAnnotations() {
        if (getField().isAnnotationPresent(FindBys.class)) {
            FindBys findBys = getField().getAnnotation(FindBys.class);
            return new FindBys.FindByBuilder().buildIt(findBys, getField());
        }

        if (getField().isAnnotationPresent(FindAll.class)) {
            FindAll findAll = getField().getAnnotation(FindAll.class);
            return new FindAll.FindByBuilder().buildIt(findAll, getField());
        }

        if (getField().isAnnotationPresent(FindBy.class)) {
            FindBy findBy = getField().getAnnotation(FindBy.class);
            return new FindBy.FindByBuilder().buildIt(findBy, getField());
        }

        if (getField().isAnnotationPresent(Field.class)) {
            return searchStrategy.getLocator(getField());
        }
        return null;
    }

    private By buildByFromHtmlElementAnnotations() {
        assertValidAnnotations();

        By result = buildByFromFindAnnotations();
        if (result != null) {
            return result;
        }

        Class<?> fieldClass = getField().getType();
        while (fieldClass != Object.class) {
            if (fieldClass.isAnnotationPresent(FindBy.class)) {
                return new FindBy.FindByBuilder().buildIt(fieldClass.getAnnotation(FindBy.class), getField());
            }
            fieldClass = fieldClass.getSuperclass();
        }
        return buildByFromDefault();
    }
}
