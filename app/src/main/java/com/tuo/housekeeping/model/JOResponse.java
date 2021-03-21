package com.tuo.housekeeping.model;

import java.util.List;

public class JOResponse {
    private boolean success;
    private String message;
    private String ids;
    private List<JO> data;

    public JOResponse(boolean success, String message, String ids, List<JO> data) {
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

    public List<JO> getData() {
        return data;
    }
}
