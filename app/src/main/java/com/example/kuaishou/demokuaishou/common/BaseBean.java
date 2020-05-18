package com.example.kuaishou.demokuaishou.common;

public class BaseBean<T> {
    private String code;
    private String message;
    private T result;//它的类型是多种

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
