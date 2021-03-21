package com.tuo.housekeeping.model;

public class UpdateJobOrder {
    public String joborderstatus;
    public String hkjoborderid;

    public UpdateJobOrder(String joborderstatus, String hkjoborderid) {
        this.joborderstatus = joborderstatus;
        this.hkjoborderid = hkjoborderid;
    }

    public String getJoborderstatus() {
        return joborderstatus;
    }

    public void setJoborderstatus(String joborderstatus) {
        this.joborderstatus = joborderstatus;
    }

    public String getHkjoborderid() {
        return hkjoborderid;
    }

    public void setHkjoborderid(String hkjoborderid) {
        this.hkjoborderid = hkjoborderid;
    }
}
