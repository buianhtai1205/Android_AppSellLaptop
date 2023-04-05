package com.appbanlaptop.model;

import java.util.List;

public class LaptopModel {
    private boolean success;
    private String message;
    private List<Laptop> result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Laptop> getResult() {
        return result;
    }

    public void setResult(List<Laptop> result) {
        this.result = result;
    }
}
