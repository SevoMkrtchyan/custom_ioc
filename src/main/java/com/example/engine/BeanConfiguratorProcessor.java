package com.example.engine;

import com.example.components.BeanConfigurator;

import java.util.List;

public class BeanConfiguratorProcessor {

    private BeanConfigurator configure;


    public <T> void startupBeanConfigurator(List<String> beans, ApplicationContext context) {
        for (String bean : beans) {
            try {
                Class<?> aClass = Class.forName(bean);
                T object = (T) context.getBean(aClass);
                configure.configure(object, context);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void setConfigure(BeanConfigurator configure) {
        this.configure = configure;
    }
}