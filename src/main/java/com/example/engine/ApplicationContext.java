package com.example.engine;

import com.example.components.BeanFactory;
import org.reflections.Reflections;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ApplicationContext {

    private BeanFactory factory;
    private Map<Class, Object> cache = new ConcurrentHashMap<>();
    private Reflections scanner;

    public ApplicationContext(String scanPackage) {
        scanner = new Reflections(scanPackage);
    }

    public <T> T getBean(Class<T> type) {
        if (cache.containsKey(type)) {
            return (T) cache.get(type);
        }
        Class<? extends T> implClass = type;
        if (type.isInterface()) {
            implClass = getImplClass(type);
        }
        return factory.createBean(implClass);
    }

    public <T> Class<? extends T> getImplClass(Class<T> parentInterface) {
        Set<Class<? extends T>> classes = scanner.getSubTypesOf(parentInterface);
        if (classes.size() != 1) {
            throw new RuntimeException(parentInterface + " has 0 or more than one impl please update your config");
        }
        return classes.iterator().next();
    }

    public void setFactory(BeanFactory factory) {
        this.factory = factory;
    }

    public Reflections getScanner() {
        return scanner;
    }

}