package com.example.components;

public class BeanDefinition {

    private String id;
    private Object clazz;
    private String scope;
    private String initMethodName;
    private String destroyMethodName;

    public BeanDefinition() {
    }

    public BeanDefinition(String id, Class<?> clazz, String scope, String initMethodName, String destroyMethodName) {
        this.id = id;
        this.clazz = clazz;
        this.scope = scope;
        this.initMethodName = initMethodName;
        this.destroyMethodName = destroyMethodName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getClazz() {
        return clazz;
    }

    public void setClazz(Object clazz) {
        this.clazz = clazz;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getInitMethodName() {
        return initMethodName;
    }

    public void setInitMethodName(String initMethodName) {
        this.initMethodName = initMethodName;
    }

    public String getDestroyMethodName() {
        return destroyMethodName;
    }

    public void setDestroyMethodName(String destroyMethodName) {
        this.destroyMethodName = destroyMethodName;
    }

}
