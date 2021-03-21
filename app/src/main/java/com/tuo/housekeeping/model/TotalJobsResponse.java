package com.tuo.housekeeping.model;

import java.util.List;

public class TotalJobsResponse {
    private boolean success;
    private String message;
    private String ids;
    private List<TotalJobs> data;

    public TotalJobsResponse(boolean success, String message, String ids, List<TotalJobs> data) {
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

    public List<TotalJobs> getData() {
        return data;
    }
}
