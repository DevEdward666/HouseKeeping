package com.tuo.housekeeping.model;

public class TotalJobsFinished {
    public String total_pending_routine;

    public TotalJobsFinished(String total_pending_routine) {
        this.total_pending_routine = total_pending_routine;
    }

    public String getTotal_pending_routine() {
        return total_pending_routine;
    }
}
