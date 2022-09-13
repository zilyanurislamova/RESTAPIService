package com.example.service.response;

public class Error {

    public static final String VALIDATION_FAILED = "Validation Failed";
    public static final String ITEM_NOT_FOUND = "Item not found";

    private int code;

    private String message;

    public Error(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
