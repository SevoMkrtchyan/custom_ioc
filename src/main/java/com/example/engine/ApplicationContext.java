package com.example.engine;

import com.example.components.BeanFactory;

public class ApplicationContext {

    private BeanFactory factory;

    public ApplicationContext() {
    }

    public <T> T getBean(Class<T> type) {
        return factory.getBean(type);
    }

    public void setFactory(BeanFactory factory) {
        this.factory = factory;
    }

}