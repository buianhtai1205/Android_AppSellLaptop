package com.appbanlaptop.model;

import java.util.List;

public class BrandModel {
    boolean success;
    String messenge;
    List<Brand> result;

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

    public List<Brand> getResult() {
        return result;
    }

    public void setResult(List<Brand> result) {
        this.result = result;
    }
}
