package com.example.components;

import com.example.engine.XmlBeanDefinitionReader;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BeanFactory {

    private Reflections scanner;
    private Map<String, Map<String, List<String>>> beanDefinitionsAttributes;
    private final List<BeanConfigurator> configurators = new ArrayList<>();
    private final List<BeanDefinition> beanDefinitions;
    private final XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader();

    public BeanFactory(String packageToScan) {
        this.beanDefinitionsAttributes = reader.getAttributes();
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
        T t = null;
        BeanDefinition beanDefinition = null;
        for (BeanDefinition definition : beanDefinitions) {
            if (beanId.equalsIgnoreCase(definition.getId())) {
                beanDefinition = definition;
            }
        }
        try {
            t = create(implClass);
            for (String beanDefinitionKey : beanDefinitionsAttributes.keySet()) {
                if (beanDefinitionKey.endsWith(beanId)) {
                    Map<String, List<String>> beanDefinitionAttributes = beanDefinitionsAttributes.get(beanDefinitionKey);
                    if (beanDefinition != null) {
                        beanDefinition.setClazz(t);
                    }
                    if (beanDefinitionAttributes != null) {
                        beanDefinition = configureBean(beanDefinition, beanDefinitionAttributes);
                    }
                } else {
                    if (beanDefinition != null) {
                        beanDefinition.setClazz(t);
                    }
                }
            }
            beanDefinitions.add(beanDefinition);
        } catch (InstantiationException
                | IllegalAccessException
                | InvocationTargetException
                | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return (T) beanDefinition.getClazz();
    }

    public <T> BeanDefinition configureBean(BeanDefinition beanDefinition, Map<String, List<String>> beanDefinitionAttributes) {
        BeanDefinition configuredBeanDefinition = null;
        for (BeanConfigurator configurator : configurators) {
            configuredBeanDefinition = configurator.configure(beanDefinition, beanDefinitionAttributes, this);
        }
        return configuredBeanDefinition;
    }

    private <T> T create(Class<T> implClass) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        return implClass.getDeclaredConstructor().newInstance();
    }


    public <T> T getBean(Class<T> type) throws ClassNotFoundException {
        Class<? extends T> implClass = type;
        for (BeanDefinition beanDefinition : beanDefinitions) {
            if (beanDefinition.getId().equalsIgnoreCase(type.getSimpleName())) {
                if (type.isInterface()) {
                    implClass = getImplClass(type);
                }
                T bean = (T) createBean(implClass, type.getSimpleName());
                return (T) bean;
            }
        }
        T bean = createBean(implClass, type.getSimpleName());
        return bean;
    }


    public <T> Class<? extends T> getImplClass(Class<T> parentInterface) {
        Set<Class<? extends T>> classes = scanner.getSubTypesOf(parentInterface);
        if (classes.size() != 1) {
            throw new RuntimeException(parentInterface + " has 0 or more than one impl please update your config");
        }
        return classes.iterator().next();
    }

    public List<BeanDefinition> getBeanDefinitions() {
        return beanDefinitions;
    }
}
