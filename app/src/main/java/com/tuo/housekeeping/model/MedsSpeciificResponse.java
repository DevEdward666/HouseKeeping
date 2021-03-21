package com.tuo.housekeeping.model;

import java.util.List;

public class MedsSpeciificResponse {
    private boolean success;
    private String message;
    private List<MedsSpecificData> data;

    public MedsSpeciificResponse(boolean success, String message, List<MedsSpecificData> data) {
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

    public List<MedsSpecificData> getData() {
        return data;
    }
}
