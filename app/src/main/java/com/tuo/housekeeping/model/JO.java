package com.tuo.housekeeping.model;

public class JO {
    public String ns;
    public String room;
    public String bed;
    public String notes;
    public String sts_desc;
    public String date_requested;
    public String hk_jo_id;
    public String emp_id;

    public JO(String ns, String room, String bed, String notes, String sts_desc, String date_requested, String hk_jo_id, String emp_id) {
        this.ns = ns;
        this.room = room;
        this.bed = bed;
        this.notes = notes;
        this.sts_desc = sts_desc;
        this.date_requested = date_requested;
        this.hk_jo_id = hk_jo_id;
        this.emp_id = emp_id;
    }

    public String getNs() {
        return ns;
    }

    public String getRoom() {
        return room;
    }

    public String getBed() {
        return bed;
    }

    public String getNotes() {
        return notes;
    }

    public String getSts_desc() {
        return sts_desc;
    }

    public String getDate_requested() {
        return date_requested;
    }

    public String getHk_jo_id() {
        return hk_jo_id;
    }

    public String getEmp_id() {
        return emp_id;
    }
}
