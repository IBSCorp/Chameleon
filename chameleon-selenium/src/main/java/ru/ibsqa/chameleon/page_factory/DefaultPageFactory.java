package ru.ibsqa.chameleon.page_factory;

import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.SignatureAttribute;
import javassist.bytecode.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ibsqa.chameleon.definitions.annotations.selenium.Field;
import ru.ibsqa.chameleon.definitions.annotations.selenium.Page;
import ru.ibsqa.chameleon.definitions.repository.IRepositoryManager;
import ru.ibsqa.chameleon.definitions.repository.selenium.*;
import ru.ibsqa.chameleon.definitions.repository.selenium.elements.MetaPage;
import ru.ibsqa.chameleon.elements.IFacadeCollection;
import ru.ibsqa.chameleon.elements.selenium.IFacadeSelenium;
import ru.ibsqa.chameleon.elements.selenium.WebElementFacade;
import ru.ibsqa.chameleon.i18n.ILocaleManager;
import ru.ibsqa.chameleon.page_factory.pages.ICollectionSuperClassManager;
import ru.ibsqa.chameleon.page_factory.pages.IPageObject;
import ru.ibsqa.chameleon.page_factory.pages.IPageSuperClassManager;
import ru.ibsqa.chameleon.selenium.driver.IDriverManager;
import ru.ibsqa.chameleon.selenium.driver.IDriverFacade;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.fail;

@Component
@Slf4j
public class DefaultPageFactory implements IPageFactory {

    protected static ClassPool CLASS_POOL = ClassPool.getDefault();

    @Autowired
    protected ILocaleManager localeManager;

    @Autowired
    protected IPageSuperClassManager pageSuperClassManager;

    @Autowired
    protected ICollectionSuperClassManager collectionSuperClassManager;

    @Autowired
    protected IRepositoryManager repositoryManager;

    @Autowired
    private IDriverManager driverManager;

    private void addFrames(IMetaElement meta, ConstPool constpool, Annotation annotation) {
        String frames[] = meta.getFrames();
        ArrayMemberValue arrayMemberValue = new ArrayMemberValue(constpool);
        MemberValue[] memberValues = new StringMemberValue[frames.length];
        for (int i = 0; i < frames.length; i++) {
            memberValues[i] = new StringMemberValue(frames[i], constpool);
        }
        arrayMemberValue.setValue(memberValues);
        annotation.addMemberValue("frames", arrayMemberValue);
    }

    /**
     * Сформировать класс на основе метаданных
     *
     * @param pageName
     * @param <PAGE>
     * @return
     */
    @Override
    public synchronized <PAGE extends IPageObject> Class<PAGE> generatePage(String pageName) {

        MetaPage metaPage = repositoryManager.pickElement(pageName, MetaPage.class);

        if (null != metaPage.getPageObjectClass()) {
            return (Class<PAGE>) metaPage.getPageObjectClass();
        }

        IDriverFacade driver = driverManager.getDriver(metaPage.getDriver());

        String className = toTranslit(metaPage.getName());
        if (CLASS_POOL.getOrNull(className) != null) {
            CtClass ctClass = CLASS_POOL.getOrNull(className);
            ctClass.defrost();
            removeAllDeclaredMethods(className);
            removeAllDeclaredFields(className);
            try {
                CtClass cc = CLASS_POOL.makeClass(className);
                setSuperClass(metaPage, cc, driver);

                generateClassPage(cc, className, metaPage, pageName, driver);
                Class<PAGE> aClass = (Class<PAGE>) new PageClassLoader().loadClass(className);
                metaPage.setPageObjectClass((Class<IPageObject>) aClass);
                return aClass;
            } catch (CannotCompileException | ClassNotFoundException | NotFoundException e) {
                log.error(e.getMessage(), e);
                fail(e.getMessage());
            }

        }
        CtClass cc = CLASS_POOL.makeClass(className);
        try {
            setSuperClass(metaPage, cc, driver);

            generateClassPage(cc, className, metaPage, pageName, driver);

            Class aClass = cc.toClass();
            metaPage.setPageObjectClass((Class<IPageObject>) aClass);

            return aClass;
        } catch (CannotCompileException | NotFoundException | ClassNotFoundException e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        }
        return null;
    }

    protected void setSuperClass(MetaPage metaPage, CtClass cc, IDriverFacade driver) throws CannotCompileException, NotFoundException, ClassNotFoundException {
        cc.setSuperclass(getPageSuperClass(metaPage, driver));
    }

    protected void generatePageAnnotation(String pageName, MetaPage metaPage, CtClass cc) {
        ConstPool constpool = cc.getClassFile().getConstPool();
        AnnotationsAttribute attr = new AnnotationsAttribute(constpool, AnnotationsAttribute.visibleTag);
        Annotation annotation = new Annotation(Page.class.getCanonicalName(), constpool);
        if (null != pageName && !pageName.isEmpty()) {
            annotation.addMemberValue("name", new StringMemberValue(pageName, constpool));
        }
        if (null != metaPage.getLocator() && !metaPage.getLocator().isEmpty()) {
            annotation.addMemberValue("locator", new StringMemberValue(metaPage.getLocator(), constpool));
        }
        if (null != metaPage.getDriver() && !metaPage.getDriver().isEmpty()) {
            annotation.addMemberValue("driver", new StringMemberValue(metaPage.getDriver(), constpool));
        }
        int index = constpool.addIntegerInfo(metaPage.getWaitTimeOut());
        annotation.addMemberValue("waitTimeOut", new IntegerMemberValue(index, constpool));

        addFrames(metaPage, constpool, annotation);

        if (annotation.getMemberNames().size() > 0) {
            attr.addAnnotation(annotation);
            cc.getClassFile().addAttribute(attr);
        }
    }

    private CtClass getPageSuperClass(MetaPage metaPage, IDriverFacade driver) throws NotFoundException, ClassNotFoundException {
        String customType = metaPage.getCustomType();
        Class<? extends IPageObject> aClass;
        if (Objects.nonNull(customType) && !customType.isEmpty()) {
            aClass = (Class<? extends IPageObject>) Class.forName(customType);
        } else {
            aClass = pageSuperClassManager.getSuperClass(metaPage, driver);
        }
        ClassPool.getDefault().appendClassPath(new LoaderClassPath(aClass.getClassLoader()));
        return resolveCtClass(aClass);
    }

    private CtClass getCollectionSuperClass(IMetaCollection metaCollection, IDriverFacade driver) throws NotFoundException {
        Class<? extends IPageObject> aClass = collectionSuperClassManager.getSuperClass(metaCollection, driver);
        ClassPool.getDefault().appendClassPath(new LoaderClassPath(aClass.getClassLoader()));
        return resolveCtClass(aClass);
    }


    protected CtClass resolveCtClass(Class clazz) throws NotFoundException {
        ClassPool pool = ClassPool.getDefault();
        return pool.get(clazz.getName());
    }

    private CtClass resolveCtClass(String name) throws NotFoundException {
        ClassPool pool = ClassPool.getDefault();
        return pool.get(name);
    }

    /**
     * Транслитерировать русское название
     *
     * @param src
     * @return
     */
    protected String toTranslit(String src) {
        // строгий порядок символов
        String f = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЧЦШЩЭЮЯЫЪЬабвгдеёжзийклмнопрстуфхчцшщэюяыъь0123456789ABCDEFGHIJKLMNOPQRSTUVWXQZabcdefghijklmnopqrstuvwxyz";

        String[] t = {"A", "B", "V", "G", "D", "E", "Jo", "Zh", "Z", "I", "J", "K", "L", "M", "N", "O", "P", "R", "S", "T", "U", "F",
                "H", "Ch", "C", "Sh", "Csh", "E", "Ju", "Ja", "Y", "`", "'", "a", "b", "v", "g", "d", "e", "jo", "zh", "z", "i", "j", "k",
                "l", "m", "n", "o", "p", "r", "s", "t", "u", "f", "h", "ch", "c", "sh", "csh", "e", "ju", "ja", "y", "`", "'",
                "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
                "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
                "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "r", "u", "w", "w", "x", "y", "z"};

        String[] words = src.split("\\s+");

        StringBuilder srcWithOutSpace = new StringBuilder();
        for (String word : words) {
            srcWithOutSpace.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1));
        }

        StringBuilder res = new StringBuilder(srcWithOutSpace.length());
        for (char c : srcWithOutSpace.toString().toCharArray()) {
            int index = f.indexOf(c);
            if (-1 == index) {
                res.append('_');
                continue;
            }
            res.append(t[index]);
        }

        return res.toString();
    }

    /**
     * Создать в формируемом классе метод getName
     *
     * @param evalClass
     * @return
     * @throws CannotCompileException
     */
    protected CtMethod makeGetNameMethod(CtClass evalClass, IMetaContainer container) throws CannotCompileException {
        CtClass STRING = null;
        try {
            STRING = CLASS_POOL.get("java.lang.String");
        } catch (NotFoundException e) {
            log.error(e.getMessage(), e);
        }
        CtMethod mtGetName = new CtMethod(STRING, "getName", new CtClass[]{}, evalClass);
        mtGetName.setBody(String.format("return \"%s\";", container.getName().replace("\"", "\\\"")));
        ConstPool constpool = evalClass.getClassFile().getConstPool();
        AnnotationsAttribute attr2 = new AnnotationsAttribute(constpool, AnnotationsAttribute.visibleTag);
        Annotation fieldAnnotation2 = new Annotation(Override.class.getCanonicalName(), constpool);
        attr2.addAnnotation(fieldAnnotation2);
        mtGetName.getMethodInfo2().addAttribute(attr2);
        return mtGetName;
    }

    /**
     * Создать в формируемом классе метод isLoaded
     *
     * @param evalClass
     * @param page
     * @return
     * @throws CannotCompileException
     */
    protected CtMethod makeIsLoadedMethod(CtClass evalClass, MetaPage page) throws CannotCompileException {
        CtMethod mtIsLoaded = new CtMethod(CtClass.booleanType, "isLoaded", new CtClass[]{}, evalClass);
        mtIsLoaded.setBody(String.format("{%s; %s; boolean r = %s; if (r) {%s;} return r;}", makeBeforePageLoaded(), makeFrameManager(), makeIsLoadedFields(page), makeAfterPageLoaded()));
        ConstPool constpool = evalClass.getClassFile().getConstPool();
        AnnotationsAttribute attr2 = new AnnotationsAttribute(constpool, AnnotationsAttribute.visibleTag);
        Annotation fieldAnnotation2 = new Annotation(Override.class.getCanonicalName(), constpool);
        attr2.addAnnotation(fieldAnnotation2);
        mtIsLoaded.getMethodInfo2().addAttribute(attr2);
        return mtIsLoaded;
    }

    protected String makeIsLoadedFields(MetaPage page) {
        StringBuilder method = new StringBuilder();
        for (IMetaField field : page.getFields()) {
            if (field.isLoaded()) {
                if (method.length() > 0) {
                    method.append(" && ");
                }
                method.append("getSeleniumField(\"").append(field.getName()).append("\").waitToDisplayed()");
            }
        }
        if (method.length() == 0) {
            method.append("true");
        }
        return method.toString();
    }

    protected String makeFrameManager() {
        return "switchFrames()";
    }

    /**
     * Предназначена для переопределения в проектах. Здесь можно указать действия, которые выполняются до загрузки страницы
     *
     * @return
     */
    protected String makeBeforePageLoaded() {
        return "beforePageLoaded()";
    }

    /**
     * Предназначена для переопределения в проектах. Здесь можно указать действия, которые выполняются после загрузки страницы
     *
     * @return
     */
    protected String makeAfterPageLoaded() {
        return "afterPageLoaded()";
    }


    /**
     * Формирование поля типа блок в классе на основании метаданных.
     *
     * @param pageClass
     * @param metaBlock
     */
    protected void generateBlock(String pageClassName, CtClass pageClass, IMetaBlock metaBlock) {
        String className = toTranslit(metaBlock.getName());
        CtClass innerClass = null;
        if (CLASS_POOL.getOrNull(className) == null) {
            generatePage(metaBlock.getName());

        }
        innerClass = getCtClass(className);
        try {
            CtField field = new CtField(innerClass, className, pageClass);
            field.setModifiers(Modifier.PUBLIC);
            pageClass.addField(field);
        } catch (CannotCompileException e) {
            log.error(e.getMessage(), e);
            //TODO вынести в константы в файл
            fail("Не удалось создать поле: " + innerClass.getName());
        }
    }

    private CtClass getCtClass(String className) {
        try {
            return CLASS_POOL.getCtClass(className);
        } catch (NotFoundException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * Формирование поля в классе на основании метаданных. Добавляются аннотации
     *
     * @param pageClass
     * @param metaField
     */
    protected void generateField(String pageClassName, CtClass pageClass, IMetaField metaField, IDriverFacade driver) {
        try {
            // Создать аннотацию для поля
            ConstPool constpool = pageClass.getClassFile().getConstPool();
            AnnotationsAttribute attr = new AnnotationsAttribute(constpool, AnnotationsAttribute.visibleTag);
            Annotation annotation = new Annotation(Field.class.getCanonicalName(), constpool);
            annotation.addMemberValue("name", new StringMemberValue(metaField.getName(), constpool));
            annotation.addMemberValue("locator", new StringMemberValue(metaField.getLocator(), constpool));
            int index = constpool.addIntegerInfo(metaField.getWaitTimeOut());
            annotation.addMemberValue("waitTimeOut", new IntegerMemberValue(index, constpool));
            addFrames(metaField, constpool, annotation);
            attr.addAnnotation(annotation);

            // Создать поле с аннотацией
            String elementType = metaField.getFacadeClassName(driver.getConfiguration().getDriverType().getClass());
            Class<? extends WebElementFacade> elType = (Class<? extends WebElementFacade>) Class.forName(elementType);
            ClassPool.getDefault().appendClassPath(new LoaderClassPath(elType.getClassLoader()));
            CtClass facadeClass = ClassPool.getDefault().get(elementType);
            CtField field = new CtField(facadeClass, toTranslit(metaField.getName()), pageClass);
            field.getFieldInfo().addAttribute(attr);
            field.setModifiers(Modifier.PUBLIC);
            pageClass.addField(field);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    /**
     * Формирование коллекции в классе на основании метаданных. Добавляется статический класс и аннотации
     *
     * @param pageClass
     * @param metaCollection
     */
    protected void generateCollection(String pageClassName, CtClass pageClass, IMetaCollection metaCollection, IDriverFacade driver) {
        try {

            // Создать класс для элемента коллекции
            Class itemClass = null;
            String subClassName = pageClassName + "." + toTranslit(metaCollection.getName());
            if (CLASS_POOL.getOrNull(subClassName) != null) {
                CtClass ctClass = CLASS_POOL.getOrNull(subClassName);
                ctClass.defrost();
                removeAllDeclaredMethods(subClassName);
                removeAllDeclaredFields(subClassName);
                try {
                    generateClassCollection(ctClass, metaCollection, subClassName, driver);
                    itemClass = new PageClassLoader().loadClass(subClassName);

                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            } else {
                CtClass cc = CLASS_POOL.makeClass(subClassName);
                try {
                    cc.setSuperclass(getCollectionSuperClass(metaCollection, driver));

                    generateClassCollection(cc, metaCollection, subClassName, driver);
                    itemClass = cc.toClass();
                } catch (CannotCompileException | NotFoundException e) {
                    log.error(e.getMessage(), e);
                    fail(e.getMessage());
                }
            }

            // Создать аннотацию для коллекции
            ConstPool constpool = pageClass.getClassFile().getConstPool();
            AnnotationsAttribute attr = new AnnotationsAttribute(constpool, AnnotationsAttribute.visibleTag);
            Annotation annotation = new Annotation(Field.class.getCanonicalName(), constpool);
            annotation.addMemberValue("name", new StringMemberValue(metaCollection.getName(), constpool));
            annotation.addMemberValue("locator", new StringMemberValue(metaCollection.getLocator(), constpool));
            int index = constpool.addIntegerInfo(metaCollection.getWaitTimeOut());
            annotation.addMemberValue("waitTimeOut", new IntegerMemberValue(index, constpool));
            addFrames(metaCollection, constpool, annotation);
            attr.addAnnotation(annotation);

            // Создать поле коллекция
            String elementType = metaCollection.getFacadeClassName(driver.getConfiguration().getDriverType().getClass());
            Class<? extends WebElementFacade> elType = (Class<? extends WebElementFacade>) Class.forName(elementType);
            ClassPool.getDefault().appendClassPath(new LoaderClassPath(elType.getClassLoader()));
            CtClass facadeClass = ClassPool.getDefault().get(elementType);
            CtField field = new CtField(facadeClass, toTranslit(metaCollection.getName()), pageClass);

            // Указать женерик. Это нужно, чтобы итератор знал какого класса создавать объекты
            String fieldSignature = "L" + elType.getCanonicalName().replace(".", "/") + "<L" + itemClass.getCanonicalName().replace(".", "/") + ";>;";
            field.getFieldInfo().addAttribute(new SignatureAttribute(constpool, fieldSignature));

            // Указать аннотацию
            field.getFieldInfo().addAttribute(attr);

            // Сделать поле публичным
            field.setModifiers(Modifier.PUBLIC);

            // Добавить поле на страницу
            pageClass.addField(field);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    /**
     * Получить список имен полей на странице
     *
     * @param pageObject
     * @return
     */
    @Override
    public List<String> getFieldNames(IPageObject pageObject) {
        return getNames(pageObject, IFacadeSelenium.class);
    }

    /**
     * Получить список имен коллекций на странице
     *
     * @param pageObject
     * @return
     */
    @Override
    public List<String> getCollectionNames(IPageObject pageObject) {
        return getNames(pageObject, IFacadeCollection.class);
    }

    /**
     * Получить список имен полей или коллекций на странице
     *
     * @param pageObject
     * @param clazz      класс объекта (поле или коллекция)
     * @return
     */
    private List<String> getNames(IPageObject pageObject, Class clazz) {
        return Optional.ofNullable(pageObject)
                .map(page -> getNames(page.getClass(), clazz, 0)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()))
                .orElse(null);
    }

    private Stream<String> getNames(Class pageClass, Class clazz, int deep) {
        return Arrays.stream(pageClass.getDeclaredFields())
                .flatMap(field -> {
                    Class type = field.getType();
                    if (IPageObject.class.isAssignableFrom(type) && deep < 15) { // Блоки
                        return getNames(type, clazz, deep+1);
                    } else if (clazz.isAssignableFrom(type)) {
                        return Arrays.stream(field.getAnnotations())
                                .filter((annotation) -> Field.class.isAssignableFrom(annotation.annotationType()) )
                                .map((annotation) -> ((Field) annotation).name());
                    } else {
                        return Stream.empty();
                    }
                });
    }

    private void removeAllDeclaredMethods(String className) {
        CtClass ctClass = getCtClass(className);
        CtMethod[] methods = ctClass.getDeclaredMethods();
        Arrays.stream(methods).forEach(ctMethod -> {
            try {
                ctClass.removeMethod(ctMethod);
            } catch (NotFoundException e) {
                log.error(e.getMessage(), e);
            }
        });
    }

    private void removeAllDeclaredFields(String className) {
        CtClass ctClass = getCtClass(className);
        CtField[] fields = ctClass.getDeclaredFields();
        Arrays.stream(fields).forEach(ctField -> {
            try {
                ctClass.removeField(ctField);
            } catch (NotFoundException e) {
                log.error(e.getMessage(), e);
            }
        });
    }

    private void generateClassPage(CtClass cc, String className, MetaPage metaPage, String pageName, IDriverFacade driver) throws CannotCompileException {
        // Создать метод для проверки загрузки страницы
        CtMethod ctIsLoadedMethod = makeIsLoadedMethod(cc, metaPage);
        cc.addMethod(ctIsLoadedMethod);

        // Создать метод возвращающий имя страницы
        CtMethod ctGetNameMethod = makeGetNameMethod(cc, metaPage);
        cc.addMethod(ctGetNameMethod);

        // Генерация блоков
        for (IMetaBlock block : metaPage.getBlocks()) {
            generateBlock(className, cc, block);
        }

        // Генерация полей
        for (IMetaField field : metaPage.getFields()) {
            generateField(className, cc, field, driver);
        }

        // Генерация коллекций
        for (IMetaCollection collection : metaPage.getCollections()) {
            generateCollection(className, cc, collection, driver);
        }

        // Добавить аннотации класса (имя страницы, локатор, драйвер)
        generatePageAnnotation(pageName, metaPage, cc);
    }

    private void generateClassCollection(CtClass cc, IMetaCollection metaCollection, String subClassName, IDriverFacade driver) throws CannotCompileException {
        // Создать метод возвращающий имя коллекции
        CtMethod ctGetNameMethod = makeGetNameMethod(cc, metaCollection);
        cc.addMethod(ctGetNameMethod);

        // Генерация полей
        for (IMetaField field : metaCollection.getFields()) {
            generateField(subClassName, cc, field, driver);
        }
    }
}