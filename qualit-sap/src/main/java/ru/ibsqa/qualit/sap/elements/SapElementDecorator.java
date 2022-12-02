package ru.ibsqa.qualit.sap.elements;

import ru.ibsqa.qualit.page_factory.decorator.DefaultElementDecorator;

import static org.junit.jupiter.api.Assertions.fail;

//@Component
//@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
//@Primary
@Deprecated
public class SapElementDecorator extends DefaultElementDecorator {
//    @Override
//    public Object decorate(ClassLoader classLoader, Field field) {
//        try {
//            if (PageFactoryUtils.isCollection(field) || PageFactoryUtils.isList(field)) {
//                if (field.getType().getSuperclass().isAssignableFrom(WebElementFacade.class)){
//                    return super.decorate(classLoader, field);
//                }else{
//                    return decorateSapElementCollection(classLoader, field);
//                }
//            } else if (PageFactoryUtils.isElement(field)) {
//                if (field.getType().getSuperclass().isAssignableFrom(WebElementFacade.class)){
//                    return super.decorate(classLoader, field);
//                }else{
//                    return decorateSapElement(classLoader, field);
//                }
//
//            } else {
//                return PageFactoryUtils.isBlock(field) ? this.decorateElementBlock(classLoader, field) : null;
//            }
//        } catch (ClassCastException var4) {
//            fail("Не удалось создать прокси для элемента: " + PageFactoryUtils.getElementNameAsString(field));
//            return null;
//        }
//    }
//
//
//    protected <T extends ISapElement> T decorateSapElement(ClassLoader loader, Field field) {
//        ElementLocator locator = this.getElementLocatorFactory().createLocator(field);
//        InvocationHandler handler = new ElementProxyHandler(locator, PageFactoryUtils.getElementNameAsString(field), PageFactoryUtils.getElementWaitTimeOut(field));
//        ISapElement elementToWrap =  createSapElementProxy(loader, handler);
//        return (T) createSapElement(field.getType(), elementToWrap, PageFactoryUtils.getElementNameAsString(field), PageFactoryUtils.getElementWaitTimeOut(field),  this.getDriverId());
//    }
//
//    public static <T extends ISapElement> T createSapElement(Class<?> elementClass, ISapElement elementToWrap, String elementName, int waitTimeout, String driverId) {
//        try {
//            T instance = (T) PageFactoryUtils.newInstance(elementClass, elementToWrap, elementName, waitTimeout, driverId);
//            return instance;
//        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException var5) {
//            throw new ElementCreationError(var5);
//        }
//    }
//
//    public static <T extends WebElement> ISapElement createSapElementProxy(ClassLoader loader, InvocationHandler handler) {
//        Class<?>[] interfaces = new Class[]{WebElement.class, WrapsElement.class, Locatable.class, ISapElement.class};
//        return (ISapElement) Proxy.newProxyInstance(loader, interfaces, handler);
//    }
//
//    protected <T extends AbstractCollection> T decorateSapElementCollection(ClassLoader loader, Field field) {
//        ElementLocator elementLocator = this.getElementLocatorFactory().createLocator(field);
//        Class<T> collectionClass = (Class<T>) field.getType();
//        return PageFactoryUtils.createCollection(collectionClass, PageFactoryUtils.getElementNameAsString(field), PageFactoryUtils.getElementWaitTimeOut(field), elementLocator, new String[0], PageFactoryUtils.getGenericParameterClass(field));
//    }

}
