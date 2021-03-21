package com.tuo.housekeeping.model;

import java.util.List;

public class UpdateJobOrderResponse {
    private boolean success;
    private String message;
    private String data;
    private List<UpdateJobOrder> ids;

    public UpdateJobOrderResponse(boolean success, String message, String data, List<UpdateJobOrder> ids) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.ids = ids;
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public List<UpdateJobOrder> getIds() {
        return ids;
    }

    public void setIds(List<UpdateJobOrder> ids) {
        this.ids = ids;
    }
}
