package com.example.components;

import com.example.attribute.BeanDefinition;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class InitMethodStart implements BeanConfigurator {

    @Override
    public void initMethod(BeanDefinition beanDefinition) {
        if (beanDefinition.getInitMethodName() != null) {
            try {
                Method declaredMethod = beanDefinition.getCreatedInstance().getClass().getDeclaredMethod(beanDefinition.getInitMethodName());
                declaredMethod.setAccessible(true);
                declaredMethod.invoke(beanDefinition.getCreatedInstance(), (Object[]) declaredMethod.getParameterTypes());
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
