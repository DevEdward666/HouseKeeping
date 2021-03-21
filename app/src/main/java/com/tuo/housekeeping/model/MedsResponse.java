package com.tuo.housekeeping.model;

import java.util.List;

public class MedsResponse {
    private boolean success;
    private String message;
    private List<MedsData> data;

    public MedsResponse(boolean success, String message, List<MedsData> data) {
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

    public List<MedsData> getData() {
        return data;
    }
}
