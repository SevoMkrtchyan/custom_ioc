package com.example.components;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class XmlBeanDefinitionsConfigurator implements BeanConfigurator {

    @Override
    public <T> T configure(BeanDefinition beanDefinition, Map<String, List<String>> beanAttributes, BeanFactory factory) {
        try {
            if (!beanAttributes.isEmpty()) {
                if (!beanAttributes.get("fields").isEmpty()) {
                    beanDefinition = configureField(beanDefinition, beanAttributes.get("fields"), factory);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (T) beanDefinition;
    }

    private <T> void configureConstructor(T bean, Map<String, List<String>> dependencies) throws ClassNotFoundException {
        // TODO: 28.11.21 do inject with constructor by xml config attributes
    }

    private <T> T configureField(BeanDefinition beanDefinition, List<String> dependencies, BeanFactory factory) throws ClassNotFoundException {
        for (String injectedBeanName : dependencies) {
            Field declaredField = null;
            try {
                declaredField = beanDefinition.getClazz().getClass().getDeclaredField(injectedBeanName);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            Objects.requireNonNull(declaredField).setAccessible(true);
            Object object = factory.getBean(declaredField.getType());
            try {
                declaredField.set(beanDefinition.getClazz(), object);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return (T) beanDefinition;
    }

}
