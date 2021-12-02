package com.example.attribute;

import java.util.List;

public class BeanAttribute {

    private AttributeType type;
    List<String> attributes;

    public BeanAttribute() {
    }

    public AttributeType getType() {
        return type;
    }

    public void setType(AttributeType type) {
        this.type = type;
    }

    public List<String> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<String> attributes) {
        this.attributes = attributes;
    }
}
