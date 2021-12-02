package com.example.components;

import com.example.attribute.BeanDefinition;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DestroyMethod {

    private final Map<Object, Integer> counter;

    public DestroyMethod() {
        counter = new ConcurrentHashMap<>();
    }

    public void callDestroyMethod(List<BeanDefinition> beanDefinitions) {
        Thread thread = new Thread() {
            public void run() {
                startDestroy(beanDefinitions);
            }
        };
        Runtime.getRuntime().addShutdownHook(thread);
    }

    private void startDestroy(List<BeanDefinition> beanDefinitions) {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            if (beanDefinition.getDestroyMethodName() != null) {
                if (!counter.containsKey(beanDefinition.getCreatedInstance()))
                    try {
                        Method declaredMethod = beanDefinition.getCreatedInstance().getClass().getDeclaredMethod(beanDefinition.getDestroyMethodName());
                        declaredMethod.setAccessible(true);
                        declaredMethod.invoke(beanDefinition.getCreatedInstance(), (Object[]) declaredMethod.getParameterTypes());
                        counter.put(beanDefinition.getCreatedInstance(), 1);
                    } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
            }
        }
    }
}