package com.example.components;

import java.util.List;
import java.util.Map;


public interface BeanConfigurator {

    <T> T configure(BeanDefinition beanDefinition, Map<String, List<String>> beanAttributes, BeanFactory factory);

}