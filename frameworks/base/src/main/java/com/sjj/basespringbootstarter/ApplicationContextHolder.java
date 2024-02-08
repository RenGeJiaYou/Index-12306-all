package com.sjj.basespringbootstarter;

/*
 * 获取SpringBoot 管理的 Beans
 *
 * @Author Island_World
 */

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.annotation.Annotation;
import java.util.Map;

public class ApplicationContextHolder implements ApplicationContextAware {

    private static ApplicationContext CONTEXT;
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextHolder.CONTEXT = applicationContext;
    }

    /*
    * Get ioc container bean by type
    *
    * @param clazz
    * @param <T>
    * @return
    */
    public static <T> T getBean(Class<T> clazz){
        return CONTEXT.getBean(clazz);
    }

    /*
     * Get ioc container bean by type
     *
     * @param name
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(String name,Class<T> clazz){
        return CONTEXT.getBean(name,clazz);
    }

    /*
     * Get a set of ioc container beans by type
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> Map<String, T> getBeansOfType(Class<T> clazz){
        return CONTEXT.getBeansOfType(clazz);
    }

    /**
     * Find whether the bean has annotations.
     * 在 Spring 的 IoC 容器中查找并返回给定名称的bean上的指定类型的注解。
     * 如果找到了匹配的注解，就返回该注解的实例；如果没有找到，就返回null。
     *
     * @param beanName
     * @param annotationType
     * @param <A>
     * @return
     */
    public static <A extends Annotation> A findAnnotationOnBean(String beanName,Class<A>annotationType) {
        return CONTEXT.findAnnotationOnBean(beanName, annotationType);
    }

    /*
    * Get Application Context
    *
    * @return ApplicationContext
    * */
    public static ApplicationContext getInstance() {
        return CONTEXT;
    }
}
