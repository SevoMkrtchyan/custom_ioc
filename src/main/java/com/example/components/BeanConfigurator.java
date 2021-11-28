package com.example.components;

import com.example.engine.ApplicationContext;

import java.util.List;

public interface BeanConfigurator {

    default <T> void startupBeanConfigurator(List<String> beans, ApplicationContext context) {}

    <T> void configure(T t, ApplicationContext context);
}
