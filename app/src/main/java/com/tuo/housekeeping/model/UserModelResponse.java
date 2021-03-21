package com.tuo.housekeeping.model;

public class UserModelResponse {
    private boolean success;
    private String message;
    private String errors;
    private String file;
    private UserModel data;

    public UserModelResponse(boolean success, String message, String errors, String file, UserModel data) {
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

    public UserModel getData() {
        return data;
    }
}
