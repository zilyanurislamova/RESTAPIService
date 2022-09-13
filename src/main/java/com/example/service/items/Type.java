package com.example.service.items;

public enum Type {
    FILE("FILE"),
    FOLDER("FOLDER");

    private final String value;

    Type(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
