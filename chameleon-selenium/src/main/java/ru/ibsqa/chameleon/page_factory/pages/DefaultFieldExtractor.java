package ru.ibsqa.chameleon.page_factory.pages;

import ru.ibsqa.chameleon.elements.IFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
@Slf4j
public class DefaultFieldExtractor implements IFieldExtractor {

    @Override
    @SuppressWarnings("unchecked")
    public <FACADE extends IFacade> FACADE getField(IPageObject pageObject, String fieldName) {
        FACADE field = (FACADE)searchField(pageObject, fieldName);
        if (field == null){
            field = searchFieldFromBlocks(pageObject, fieldName);
        }
        return field;
    }
    private <FACADE extends IFacade> FACADE searchField(IPageObject pageObject, String fieldName){
        //поиск в полях класса
        for (Field field: pageObject.getClass().getDeclaredFields()) {
            ru.ibsqa.chameleon.definitions.annotations.selenium.Field annotation = field.getAnnotation(ru.ibsqa.chameleon.definitions.annotations.selenium.Field.class);
            if (annotation != null && annotation.name().equals(fieldName)){
                return getFieldInstance(pageObject, field);
            }
        }
        return null;
    }
    private <FACADE extends IFacade> FACADE searchFieldFromBlocks(IPageObject pageObject, String fieldName){
        //поиск в полях, которые являются блоками
        for (Field field: pageObject.getClass().getDeclaredFields()){
            if (IPageObject.class.isAssignableFrom(field.getType())){
                try {
                    FACADE facade = getField((IPageObject) field.get(pageObject), fieldName);
                    if (facade != null){
                        return facade;
                    }

                } catch (IllegalAccessException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        return null;
    }
    @SuppressWarnings("unchecked")
    private <FACADE extends IFacade> FACADE getFieldInstance(IPageObject pageObject, Field field){
        try {
            return (FACADE) field.get(pageObject);
        } catch (IllegalAccessException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
}
