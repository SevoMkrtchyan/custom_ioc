package com.example.components;

import com.example.attribute.AttributeType;
import com.example.attribute.BeanDefinition;
import com.example.engine.BeanFactory;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
                declaredField = beanDefinition.getCreatedInstance().getClass().getDeclaredField(beanName);
            } catch (NoSuchFieldException e) {
                List<String> collect = Arrays.stream(e.getStackTrace()).map(line -> line + "\n").collect(Collectors.toList());
                throw new RuntimeException(e.getMessage() + " field not found please check your config " +
                        " injected bean name would be the same as field" + collect);
            }
            Objects.requireNonNull(declaredField).setAccessible(true);
            Object object = factory.getBean(declaredField.getType());
            try {
                declaredField.set(beanDefinition.getCreatedInstance(), object);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

}
