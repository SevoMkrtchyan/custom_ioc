package com.example.components;

import com.example.attribute.BeanDefinition;

public interface BeanConfigurator {

    BeanDefinition configure(BeanDefinition beanDefinition, BeanFactory factory);

}