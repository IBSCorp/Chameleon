package ru.ibsqa.qualit.utils.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.WeakHashMap;

/**
 * Object which resolves bean names.
 * <p>
 * It process newly create beans and save them with name to WeakHashMap.
 * Because it is weak, it does not prevent garbage collection
 *
 */
@Component
@Lazy(false)
public class SpringBeanNameResolver implements BeanPostProcessor {

    private WeakHashMap<Object,String> beanNames = new WeakHashMap<Object,String>();

    /**
     * returns bean name if bean was created by Spring framework
     *
     * @param bean
     * @return bean name
     */
    public String getBeanName(Object bean){
        return beanNames.get(bean);
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        //put bean and its name to weak hash map
        beanNames.put(bean,beanName);
        return bean;
    }

}
