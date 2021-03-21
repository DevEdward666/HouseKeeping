package com.tuo.housekeeping.model;

import java.util.List;

public class RoomsResponse {
    private boolean success;
    private String message;
    private String errors;
    private String file;
    private List<RoomsModel> data;

    public RoomsResponse(boolean success, String message, String errors, String file, List<RoomsModel> data) {
        this.success = success;
        this.message = message;
        this.errors = errors;
        this.file = file;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getErrors() {
        return errors;
    }

    public String getFile() {
        return file;
    }

    public List<RoomsModel> getData() {
        return data;
    }
}
