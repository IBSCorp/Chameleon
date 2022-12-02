package ru.ibsqa.qualit.page_factory.pages;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

import static ru.ibsqa.qualit.page_factory.PageFactoryUtils.isCollection;

@Component
@Slf4j
public class DefaultCollectionExtractor implements ICollectionExtractor {

    @Override
    public Iterable<? extends IPageObject> getField(IPageObject pageObject, String fieldName) {
        Iterable<? extends IPageObject> field = searchField(pageObject, fieldName);
        if (field == null){
            field = searchFieldFromBlocks(pageObject, fieldName);
        }
        return field;
    }

    private Iterable<? extends IPageObject> searchField(IPageObject pageObject, String fieldName){
        //поиск в полях класса
        for (Field field: pageObject.getClass().getDeclaredFields()) {
            if (isCollection(field)){
                if (field.getAnnotation(ru.ibsqa.qualit.definitions.annotations.selenium.Field.class).names().equals(fieldName)){
                    return getFieldInstance(pageObject, field);
                }
            }
        }
        return null;
    }

    private Iterable<? extends IPageObject> searchFieldFromBlocks(IPageObject pageObject, String fieldName){
        //поиск в полях, которые являются блоками
        for (Field field: pageObject.getClass().getDeclaredFields()){
            if (IPageObject.class.isAssignableFrom(field.getType())){
                try {
                    Iterable<? extends IPageObject> innerField = searchField((IPageObject) field.get(pageObject), fieldName);
                    if (innerField != null){
                        return innerField;
                    }

                } catch (IllegalAccessException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        return null;
    }

    private Iterable<? extends IPageObject> getFieldInstance(IPageObject pageObject, Field field){
        try {
            return (Iterable<? extends IPageObject>) field.get(pageObject);
        } catch (IllegalAccessException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
}
