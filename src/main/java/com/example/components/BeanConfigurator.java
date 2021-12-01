package com.example.components;

import com.example.attribute.BeanDefinition;
import com.example.engine.BeanFactory;

public interface BeanConfigurator {

     BeanDefinition configure(BeanDefinition beanDefinition, BeanFactory factory);

}