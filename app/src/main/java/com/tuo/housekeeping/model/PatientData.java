package com.tuo.housekeeping.model;

public class PatientData {
    public String orderly_jo_id;
    public String emp_id;
    public String ns;
    public String room;
    public String bed;
    public String notes;
    public String pat_no;
    public String pat_name;
    public String sts_id;
    public String sts_desc;
    public String date_requested;

    public PatientData(String orderly_jo_id, String emp_id, String ns, String room, String bed, String notes, String pat_no, String pat_name, String sts_id, String sts_desc, String date_requested) {
        this.orderly_jo_id = orderly_jo_id;
        this.emp_id = emp_id;
        this.ns = ns;
        this.room = room;
        this.bed = bed;
        this.notes = notes;
        this.pat_no = pat_no;
        this.pat_name = pat_name;
        this.sts_id = sts_id;
        this.sts_desc = sts_desc;
        this.date_requested = date_requested;
    }

    public String getOrderly_jo_id() {
        return orderly_jo_id;
    }

    public String getEmp_id() {
        return emp_id;
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

    public String getPat_no() {
        return pat_no;
    }

    public String getPat_name() {
        return pat_name;
    }

    public String getSts_id() {
        return sts_id;
    }

    public String getSts_desc() {
        return sts_desc;
    }

    public String getDate_requested() {
        return date_requested;
    }
}
