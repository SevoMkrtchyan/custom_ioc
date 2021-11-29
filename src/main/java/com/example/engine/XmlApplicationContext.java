package com.example.engine;

import com.example.components.BeanFactory;

public class XmlApplicationContext implements ApplicationContext {

    private final BeanFactory factory;

    public XmlApplicationContext(String packageToScan) {
        this.factory = new BeanFactory(packageToScan);
    }

    public <T> T getBean(Class<T> type) {
        try {
            return factory.getBean(type);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
