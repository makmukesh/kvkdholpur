package com.vpipl.kvkdholpur.Model;

public class StackHelperCategory1 {
    String code, name;

    public StackHelperCategory1() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStateName() {
        return name;
    }

    public void setStateName(String name) {
        this.name = name;
    }

    @Override
    public String toString () {
        return name;
    }}