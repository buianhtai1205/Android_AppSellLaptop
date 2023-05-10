package com.appbanlaptop.model;

import java.util.List;

public class FeedbackModel {
    private boolean success;
    private String message;
    private List<Feedback> result;

    public FeedbackModel(boolean success, String message, List<Feedback> result) {
        this.success = success;
        this.message = message;
        this.result = result;
    }

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

    public List<Feedback> getResult() {
        return result;
    }

    public void setResult(List<Feedback> result) {
        this.result = result;
    }
}
