package com.example.components;

import com.example.attribute.BeanDefinition;
import com.example.attribute.Scope;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InitMethod {

    private final Map<Object, Integer> counter;

    public InitMethod() {
        this.counter = new ConcurrentHashMap<>();
    }

    public void callInitMethod(BeanDefinition beanDefinition) {
        if (beanDefinition.getScope() == Scope.SINGLETON && !counter.containsKey(beanDefinition.getCreatedInstance())) {
            counter.put(beanDefinition.getCreatedInstance(), 1);
            start(beanDefinition);
        } else if (beanDefinition.getScope() == Scope.PROTOTYPE) {
            start(beanDefinition);
        }
    }

    private void start(BeanDefinition beanDefinition) {
        try {
            Method declaredMethod = beanDefinition.getCreatedInstance().getClass().getDeclaredMethod(beanDefinition.getInitMethodName());
            declaredMethod.setAccessible(true);
            declaredMethod.invoke(beanDefinition.getCreatedInstance(), (Object[]) declaredMethod.getParameterTypes());
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
