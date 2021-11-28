package com.example.components;

import com.example.engine.ApplicationContext;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class BeanFactory {

    private Reflections scanner;
    private Map<String, Map<String, List<String>>> beans;
    private final List<BeanConfigurator> configurators = new ArrayList<>();
    private final Map<Class<?>, Class<?>> cache;
    private ApplicationContext context;

    public BeanFactory(Reflections scanner) {
        this.scanner = scanner;
        this.cache = new ConcurrentHashMap<>();
        for (Class<? extends BeanConfigurator> aClass : scanner.getSubTypesOf(BeanConfigurator.class)) {
            try {
                configurators.add(aClass.getDeclaredConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    public <T> T createBean(Class<T> implClass, String beanName) {
        T t = null;
        try {
            t = create(implClass);
            configureBean(t, beanName);
        } catch (InstantiationException
                | IllegalAccessException
                | InvocationTargetException
                | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return t;
    }

    public <T> void configureBean(T bean, String beanName) {

        configurators.forEach(objectConfigurator -> objectConfigurator.configure(bean, beans.get(beanName), this));
    }

    private <T> T create(Class<T> implClass) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        return implClass.getDeclaredConstructor().newInstance();
    }

    public void createBeanFromXml() {
        Set<String> topBeans = beans.keySet();
        for (String topBean : topBeans) {
            try {
                Class<?> aClass = Class.forName(topBean);
                getBean(aClass);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public <T> T getBean(Class<T> type) {
        if (cache.containsKey(type)) {
            return (T) cache.get(type);
        }
        Class<? extends T> implClass = type;
        if (type.isInterface()) {
            implClass = getImplClass(type);
        }
        T bean = createBean(implClass, type.getName());
//        cache.put((Class<?>) bean, null);
        return bean;
    }


    public <T> Class<? extends T> getImplClass(Class<T> parentInterface) {
        Set<Class<? extends T>> classes = scanner.getSubTypesOf(parentInterface);
        if (classes.size() != 1) {
            throw new RuntimeException(parentInterface + " has 0 or more than one impl please update your config");
        }
        return classes.iterator().next();
    }

    public void setBeans(Map<String, Map<String, List<String>>> beans) {
        this.beans = beans;
    }

    public void setContext(ApplicationContext context) {
        this.context = context;
    }
}
