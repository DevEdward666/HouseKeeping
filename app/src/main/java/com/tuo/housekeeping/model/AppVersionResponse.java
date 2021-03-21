package com.tuo.housekeeping.model;

import java.util.List;

public class AppVersionResponse {
    private boolean success;
    private String message;
    private List<AppVersionModel> data;

    public AppVersionResponse(boolean success, String message, List<AppVersionModel> data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<AppVersionModel> getData() {
        return data;
    }
}
