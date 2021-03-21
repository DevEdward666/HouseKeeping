package com.tuo.housekeeping.model;

public class TotalJobs {
    public String total_pending_JO;
    public String total_orderly_pending;

    public TotalJobs(String total_pending_JO, String total_orderly_pending) {
        this.total_pending_JO = total_pending_JO;
        this.total_orderly_pending = total_orderly_pending;
    }

    public String getTotal_pending_JO() {
        return total_pending_JO;
    }

    public String getTotal_orderly_pending() {
        return total_orderly_pending;
    }
}
