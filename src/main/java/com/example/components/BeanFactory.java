package com.example.components;


import com.example.engine.ApplicationContext;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class BeanFactory {

    private final ApplicationContext context;
    private final List<BeanConfigurator> configurators = new ArrayList<>();

    public BeanFactory(ApplicationContext context) {
        this.context = context;
        for (Class<? extends BeanConfigurator> aClass : context.getScanner().getSubTypesOf(BeanConfigurator.class)) {
            try {
                configurators.add(aClass.getDeclaredConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    public <T> T createBean(Class<T> implClass) {
        T t = null;
        try {
            t = create(implClass);
            configure(t);
        } catch (InstantiationException
                | IllegalAccessException
                | InvocationTargetException
                | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return t;
    }

    private <T> void configure(T t) {
        configurators.forEach(objectConfigurator -> objectConfigurator.configure(t, context));
    }

    private <T> T create(Class<T> implClass) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        return implClass.getDeclaredConstructor().newInstance();
    }
}
