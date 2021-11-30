package com.example.components;

import com.example.attribute.AttributeType;
import com.example.attribute.BeanDefinition;

import java.lang.reflect.Field;
import java.util.Objects;

public class XmlBeanDefinitionsConfigurator implements BeanConfigurator {

    @Override
    public BeanDefinition configure(BeanDefinition beanDefinition, BeanFactory factory) {
        try {
            if (beanDefinition.getBeanAttribute() != null &&
                    beanDefinition.getBeanAttribute().getType() == AttributeType.FIELD) {
                configureField(beanDefinition, factory);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return beanDefinition;
    }

    private void configureField(BeanDefinition beanDefinition, BeanFactory factory) throws ClassNotFoundException {
        for (String injectedBeanName : beanDefinition.getBeanAttribute().getAttributes()) {
            Field declaredField = null;
            try {
                String[] split = injectedBeanName.split("\\.");
                String beanName = split[split.length - 1];
                declaredField = beanDefinition.getClazz().getClass().getDeclaredField(beanName);
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
    }

}
