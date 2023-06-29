package io.cucumber.spring;

import io.cucumber.core.backend.CucumberBackendException;
import io.cucumber.core.backend.ObjectFactory;
import io.cucumber.core.resource.ClasspathSupport;
import lombok.SneakyThrows;
import org.apiguardian.api.API;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;
import org.springframework.test.context.TestContextManager;

import java.util.Collection;
import java.util.HashSet;

import static ru.ibsqa.chameleon.utils.spring.SpringUtils.getRootException;

@API(status = API.Status.STABLE)
public final class ChameleonFactory implements ObjectFactory {
    private final Collection<Class<?>> stepClasses = new HashSet<>();
    private Class<?> withCucumberContextConfiguration = null;
    private TestContextAdaptor testContextAdaptor;

    @Override
    public boolean addClass(final Class<?> stepClass) {
        if (stepClasses.contains(stepClass)) {
            return true;
        }

        checkNoComponentAnnotations(stepClass);
        if (hasCucumberContextConfiguration(stepClass)) {
            checkOnlyOneClassHasCucumberContextConfiguration(stepClass);
            withCucumberContextConfiguration = stepClass;
        }
        stepClasses.add(stepClass);
        return true;
    }

    @SneakyThrows
    private static void checkNoComponentAnnotations(Class<?> type) {
        if (AnnotatedElementUtils.isAnnotated(type, Component.class)) {
            try {
                throw new CucumberBackendException(String.format("" +
                                "Glue class %1$s was (meta-)annotated with @Component; marking it as a candidate for auto-detection by "
                                +
                                "Spring. Glue classes are detected and registered by Cucumber. Auto-detection of glue classes by "
                                +
                                "spring may lead to duplicate bean definitions. Please remove the @Component (meta-)annotation",
                        type.getName()));
            } catch (CucumberBackendException ex) {
                throw getRootException(ex);
            }
        }
    }

    static boolean hasCucumberContextConfiguration(Class<?> stepClass) {
        return AnnotatedElementUtils.isAnnotated(stepClass, CucumberContextConfiguration.class);
    }

    @SneakyThrows
    private void checkOnlyOneClassHasCucumberContextConfiguration(Class<?> stepClass) {
        if (withCucumberContextConfiguration != null) {
            try {
                throw new CucumberBackendException(String.format("" +
                                "Glue class %1$s and %2$s are both (meta-)annotated with @CucumberContextConfiguration.\n" +
                                "Please ensure only one class configures the spring context\n" +
                                "\n" +
                                "By default Cucumber scans the entire classpath for context configuration.\n" +
                                "You can restrict this by configuring the glue path.\n" +
                                ClasspathSupport.configurationExamples(),
                        stepClass,
                        withCucumberContextConfiguration));
            } catch (CucumberBackendException ex) {
                throw getRootException(ex);
            }
        }
    }

    @SneakyThrows
    @Override
    public void start() {
        if (withCucumberContextConfiguration == null) {
            try {
                throw new CucumberBackendException("" +
                        "Please annotate a glue class with some context configuration.\n" +
                        "\n" +
                        "For example:\n" +
                        "\n" +
                        "   @CucumberContextConfiguration\n" +
                        "   @SpringBootTest(classes = TestConfig.class)\n" +
                        "   public class CucumberSpringConfiguration { }" +
                        "\n" +
                        "Or: \n" +
                        "\n" +
                        "   @CucumberContextConfiguration\n" +
                        "   @ContextConfiguration( ... )\n" +
                        "   public class CucumberSpringConfiguration { }");
            } catch (CucumberBackendException ex) {
                throw getRootException(ex);
            }
        }

        // The application context created by the TestContextManager is
        // a singleton and reused between scenarios and shared between
        // threads.
        TestContextManager testContextManager = new TestContextManager(withCucumberContextConfiguration);

        try {
            testContextAdaptor = new TestContextAdaptor(testContextManager, stepClasses);
            testContextAdaptor.start();
        } catch (Exception ex) {
            throw getRootException(ex);
        }
    }

    @Override
    public void stop() {
        if (testContextAdaptor != null) {
            testContextAdaptor.stop();
        }
    }

    @SneakyThrows
    @Override
    public <T> T getInstance(final Class<T> type) {
        try {
            return testContextAdaptor.getInstance(type);
        } catch (Exception ex) {
            throw getRootException(ex);
        }
    }
}