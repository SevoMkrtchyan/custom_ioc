package com.example.components;

import com.example.annotation.Inject;
import com.example.engine.ApplicationContext;

import java.lang.reflect.Field;

public class FieldInjectWithAnnotationConfig implements BeanConfigurator {

    @Override
    public <T> void configure(T t, ApplicationContext context) {
        for (Field field : t.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Inject.class)) {
                field.setAccessible(true);
                Object object = context.getBean(field.getType());
                try {
                    field.set(t, object);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

