package com.example.components;


import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class XmlBeanDefinitionsConfigurator implements BeanConfigurator {

    @Override
    public <T> void configure(T bean, Map<String, List<String>> beanAttributes, BeanFactory factory) {
        try {
            if (!beanAttributes.isEmpty()) {
                if (!beanAttributes.get("fields").isEmpty()) {
                    configureField(bean, beanAttributes.get("fields"), factory);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private <T> void configureConstructor(T bean, Map<String, List<String>> dependencies) throws ClassNotFoundException {
        // TODO: 28.11.21 do inject with constructor by xml config attributes
    }

    private <T> void configureField(T bean, List<String> dependencies, BeanFactory factory) {

        for (String injectedBeanName : dependencies) {
            Field declaredField = null;
            try {
                declaredField = bean.getClass().getDeclaredField(injectedBeanName);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            Objects.requireNonNull(declaredField).setAccessible(true);
            Object object = factory.getBean(declaredField.getType());
            try {
                declaredField.set(bean, object);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
