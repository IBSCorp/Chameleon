package ru.ibsqa.qualit.elements.uia;

import ru.ibsqa.qualit.page_factory.decorator.DefaultElementDecorator;

import static org.junit.jupiter.api.Assertions.fail;

//@Component
//@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
//@Primary
@Deprecated
public class UiaElementDecorator extends DefaultElementDecorator {

//    @Override
//    public Object decorate(ClassLoader classLoader, Field field) {
//        try {
//            if (PageFactoryUtils.isCollection(field) || PageFactoryUtils.isList(field)) {
//                if (field.getType().getSuperclass().isAssignableFrom(WebElementFacade.class)) {
//                    return super.decorate(classLoader, field);
//                } else {
//                    return decorateUiaElementCollection(classLoader, field);
//                }
//            } else if (PageFactoryUtils.isElement(field)) {
//                if (field.getType().getSuperclass().isAssignableFrom(WebElementFacade.class)) {
//                    return super.decorate(classLoader, field);
//                } else {
//                    return decorateUiaElement(classLoader, field);
//                }
//            } else {
//                return PageFactoryUtils.isBlock(field) ? this.decorateElementBlock(classLoader, field) : null;
//            }
//        } catch (ClassCastException e) {
//            fail("Не удалось создать прокси для элемента: " + PageFactoryUtils.getElementNameAsString(field));
//            return null;
//        }
//    }
//
//    protected <T extends IUiaElement> T decorateUiaElement(ClassLoader loader, Field field) {
//        ElementLocator locator = this.getElementLocatorFactory().createLocator(field);
//        InvocationHandler handler = new ElementProxyHandler(locator, PageFactoryUtils.getElementNameAsString(field), PageFactoryUtils.getElementWaitTimeOut(field));
//        IUiaElement elementToWrap = createUiaElementProxy(loader, handler);
//        return (T) createUiaElement(field.getType(), elementToWrap, PageFactoryUtils.getElementNameAsString(field), PageFactoryUtils.getElementWaitTimeOut(field), this.getDriverId());
//    }
//
//    private static <T extends IUiaElement> T createUiaElement(Class<?> elementClass, IUiaElement elementToWrap, String elementName, int waitTimeOut, String driverId) {
//        try {
//            T instance = (T) PageFactoryUtils.newInstance(elementClass, elementToWrap, elementName, waitTimeOut, driverId);
//            return instance;
//        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
//            throw new ElementCreationError(e);
//        }
//    }
//
//    private static <T extends WebElement> IUiaElement createUiaElementProxy(ClassLoader loader, InvocationHandler handler) {
//        Class<?>[] interfaces = new Class[]{WebElement.class, WrapsElement.class, Locatable.class, IUiaElement.class};
//        return (IUiaElement) Proxy.newProxyInstance(loader, interfaces, handler);
//    }
//
//    protected <T extends AbstractCollection> T decorateUiaElementCollection(ClassLoader loader, Field field) {
//        ElementLocator elementLocator = this.getElementLocatorFactory().createLocator(field);
//        Class<T> collectionClass = (Class<T>) field.getType();
//        return PageFactoryUtils.createCollection(collectionClass, PageFactoryUtils.getElementNameAsString(field), PageFactoryUtils.getElementWaitTimeOut(field), elementLocator, new String[0], PageFactoryUtils.getGenericParameterClass(field));
//    }

}
