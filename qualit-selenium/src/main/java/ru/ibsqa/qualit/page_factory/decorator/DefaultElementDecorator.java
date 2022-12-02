package ru.ibsqa.qualit.page_factory.decorator;

import org.springframework.beans.factory.annotation.Autowired;
import ru.ibsqa.qualit.page_factory.PageFactoryUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.fail;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DefaultElementDecorator extends AbstractFieldDecorator {

    @Autowired
    private IDecorateManager decorateManager;

    @Override
    public Object decorate(ClassLoader classLoader, Field field) {
        try {
            if (PageFactoryUtils.isCollection(field)) {
                return decorateManager.decorateCollection(getElementLocatorFactory(), classLoader, field);
            }
            if (PageFactoryUtils.isList(field)) {
                return decorateManager.decorateList(getElementLocatorFactory(), classLoader, field);
            }
            if (PageFactoryUtils.isElement(field)) {
                return decorateManager.decorateElement(getElementLocatorFactory(), classLoader, field);
            }
            if (PageFactoryUtils.isBlock(field)) {
                return decorateManager.decorateBlock(getElementLocatorFactory(), classLoader, field);
            }

            return null;
        } catch (ClassCastException ignore) {
            fail("Не удалось создать прокси для элемента: " + PageFactoryUtils.getElementNameAsString(field));
            return null;
        }
    }

}
