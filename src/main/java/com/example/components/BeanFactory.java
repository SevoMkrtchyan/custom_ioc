package com.example.components;

import com.example.attribute.BeanDefinition;
import com.example.engine.XmlBeanDefinitionReader;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BeanFactory {

    private final Reflections scanner;
    private final List<BeanConfigurator> configurators = new ArrayList<>();
    private final List<BeanDefinition> beanDefinitions;

    public BeanFactory(String packageToScan) {
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader();
        this.beanDefinitions = reader.getBeanDefinitions();
        this.scanner = new Reflections(packageToScan);
        for (Class<? extends BeanConfigurator> aClass : scanner.getSubTypesOf(BeanConfigurator.class)) {
            try {
                configurators.add(aClass.getDeclaredConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    public <T> T createBean(Class<T> implClass, String beanId) {
        T t;
        BeanDefinition beanDefinition = null;
        for (BeanDefinition definition : beanDefinitions) {
            if (definition.getId().endsWith(beanId)) {
                beanDefinition = definition;
                break;
            }
        }
        try {
            t = create(implClass);
            if (beanDefinition != null) {
                beanDefinition.setConfigured(true);
                if (beanDefinition.getBeanAttribute() != null) {
                    beanDefinition.setClazz(t);
                    beanDefinition = configureBean(beanDefinition);
                }
                beanDefinition.setClazz(t);

            }
        } catch (InstantiationException
                | IllegalAccessException
                | InvocationTargetException
                | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return (T) beanDefinition.getClazz();
    }

    public BeanDefinition configureBean(BeanDefinition beanDefinition) {
        for (BeanConfigurator configurator : configurators) {
            beanDefinition = configurator.configure(beanDefinition, this);
        }
        return beanDefinition;
    }

    private <T> T create(Class<T> implClass) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        return implClass.getDeclaredConstructor().newInstance();
    }

    public <T> T getBean(Class<T> type) throws ClassNotFoundException {
        Class<? extends T> implClass = type;
        if (!beanDefinitions.isEmpty()) {
            for (BeanDefinition beanDefinition : beanDefinitions) {
                if (beanDefinition.isConfigured() && beanDefinition.getId().equals(type.getName())) {
                    return (T) beanDefinition.getClazz();
                }
            }
        }
        if (type.isInterface()) {
            implClass = getImplClass(type);
        }
        return createBean(implClass, type.getSimpleName());
    }

    public <T> Class<? extends T> getImplClass(Class<T> parentInterface) {
        Set<Class<? extends T>> classes = scanner.getSubTypesOf(parentInterface);
        if (classes.size() != 1) {
            throw new RuntimeException(parentInterface + " has 0 or more than one impl please update your config");
        }
        return classes.iterator().next();
    }

}
