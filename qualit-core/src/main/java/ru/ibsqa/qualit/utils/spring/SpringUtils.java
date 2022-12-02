package ru.ibsqa.qualit.utils.spring;

import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import ru.ibsqa.qualit.i18n.ILocaleManager;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.fail;

@Component
@Lazy(false)
@Slf4j
public class SpringUtils implements ApplicationContextAware {

    @Getter
    private static ApplicationContext context;

    private static ResourcePatternResolver resourceResolver;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static <T> T getBean(Class<T> beanClass) throws BeansException {
        return context.getBean(beanClass);
    }

    public static <T> T getBean(String value) throws BeansException {
        return (T)context.getBean(value);
    }

    public static String getBeanName(Object bean){
        SpringBeanNameResolver springBeanNameResolver = getBean(SpringBeanNameResolver.class);
        return springBeanNameResolver.getBeanName(bean);
    }

    private static Resource[] getResourcesInternal(String locationPattern) throws IOException {
        if (Objects.nonNull(context)) {
            return context.getResources(locationPattern);
        } else {
            if (Objects.isNull(resourceResolver)) {
                resourceResolver = new PathMatchingResourcePatternResolver(SpringUtils.class.getClassLoader());
            }
            return resourceResolver.getResources(locationPattern);
        }
    }

    public static Resource[] getResources(String locationPattern) {
        try {
            return getResourcesInternal(locationPattern);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            ILocaleManager localeManager = getBean(ILocaleManager.class);
            fail(localeManager.getMessage("resourceIOException", locationPattern));
        }
        return null;
    }

    public static Resource getResource(String locationPattern) {

        Resource[] resources = getResources(locationPattern);
        checkResourceCount(resources, locationPattern);

        return resources[0];
    }

    public static InputStream openResourceOrFile(String locationPattern) throws IOException {
        Resource[] resources = null;
        try {
            resources = getResourcesInternal(locationPattern);
        } catch (IOException e) {
        }

        if (resources != null) {
            checkResourceCount(resources, locationPattern);
            try {
                return resources[0].getInputStream();
            } catch (FileNotFoundException e) {
            }
        }

        return new FileInputStream(new File(locationPattern));
    }

    private static void checkResourceCount(Resource[] resources, String locationPattern) {
        if (0 == resources.length) {
            ILocaleManager localeManager = getBean(ILocaleManager.class);
            fail(localeManager.getMessage("resourceNotFound", locationPattern));
        } else if (1 < resources.length) {
            ILocaleManager localeManager = getBean(ILocaleManager.class);
            fail(localeManager.getMessage("resourceNotUnique", locationPattern));
        }
    }

    public static boolean isClassPathResource(String value) {
        return null != value && value.trim().matches("(?s)(?<classpath>classpath.*)");
    }
}
