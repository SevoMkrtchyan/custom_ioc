package com.example.components;

import com.example.attribute.BeanDefinition;

public interface BeanConfigurator {

    default BeanDefinition configure(BeanDefinition beanDefinition, BeanFactory factory) {
        return beanDefinition;
    }

    default void initMethod(BeanDefinition beanDefinition){}

}