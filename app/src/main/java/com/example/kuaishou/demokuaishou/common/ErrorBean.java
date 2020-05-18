package com.example.kuaishou.demokuaishou.common;

//错误的封装
public class ErrorBean {
    private String code;//错误码
    private String message;//错误描述

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
}
