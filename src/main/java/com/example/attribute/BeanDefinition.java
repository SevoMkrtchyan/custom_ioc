package com.example.attribute;

public class BeanDefinition {

    private String id;
    private Object createdInstance;
    private String scope;
    private String initMethodName;
    private String destroyMethodName;
    private BeanAttribute beanAttribute;
    private boolean configured = false;

    public BeanDefinition() {
    }

    public BeanDefinition(String id, Class<?> clazz, String scope, String initMethodName, String destroyMethodName, BeanAttribute beanAttribute) {
        this.id = id;
        this.createdInstance = clazz;
        this.scope = scope;
        this.initMethodName = initMethodName;
        this.destroyMethodName = destroyMethodName;
        this.beanAttribute = beanAttribute;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getCreatedInstance() {
        return createdInstance;
    }

    public void setCreatedInstance(Object createdInstance) {
        this.createdInstance = createdInstance;
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

    public BeanAttribute getBeanAttribute() {
        return beanAttribute;
    }

    public void setBeanAttribute(BeanAttribute beanAttribute) {
        this.beanAttribute = beanAttribute;
    }

    public boolean isConfigured() {
        return configured;
    }

    public void setConfigured(boolean configured) {
        this.configured = configured;
    }

}
