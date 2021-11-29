package com.example.engine;

public interface ApplicationContext {

    <T> T getBean(Class<T> type);

}