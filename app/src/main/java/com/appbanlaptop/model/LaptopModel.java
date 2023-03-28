package com.appbanlaptop.model;

import java.util.List;

public class LaptopModel {
    private boolean success;
    private String messenge;
    private List<Laptop> result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessenge() {
        return messenge;
    }

    public void setMessenge(String messenge) {
        this.messenge = messenge;
    }

    public List<Laptop> getResult() {
        return result;
    }

    public void setResult(List<Laptop> result) {
        this.result = result;
    }
}
