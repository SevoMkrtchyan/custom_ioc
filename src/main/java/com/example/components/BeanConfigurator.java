package com.example.components;

import java.util.List;
import java.util.Map;


public interface BeanConfigurator {

    <T> void configure(T bean, Map<String, List<String>> beanAttributes, BeanFactory factory);

}