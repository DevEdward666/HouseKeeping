package com.tuo.housekeeping.model;

import java.util.List;

public class JobOrdersResponse {
    private boolean success;
    private String message;
    private String ids;
    private List<JobOrders> data;

    public JobOrdersResponse(boolean success, String message, String ids, List<JobOrders> data) {
        this.success = success;
        this.message = message;
        this.ids = ids;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getIds() {
        return ids;
    }

    public List<JobOrders> getData() {
        return data;
    }
}
