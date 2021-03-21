package com.tuo.housekeeping.model;

public class JobOrders {
    public String area_sts_desc;
    public String spot_name;
    public String start_time;
    public String area_id;
    public String routine_id;
    public String emp_id;
    public String date_id;
    public String area_name;
    public String spot_id;
    public String start_date;
    public String routine_job_id;
    public String notes;

    public JobOrders(String area_sts_desc, String spot_name, String start_time, String area_id, String routine_id, String emp_id, String date_id, String area_name, String spot_id, String start_date, String routine_job_id, String notes) {
        this.area_sts_desc = area_sts_desc;
        this.spot_name = spot_name;
        this.start_time = start_time;
        this.area_id = area_id;
        this.routine_id = routine_id;
        this.emp_id = emp_id;
        this.date_id = date_id;
        this.area_name = area_name;
        this.spot_id = spot_id;
        this.start_date = start_date;
        this.routine_job_id = routine_job_id;
        this.notes = notes;
    }

    public String getArea_sts_desc() {
        return area_sts_desc;
    }

    public String getSpot_name() {
        return spot_name;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getArea_id() {
        return area_id;
    }

    public String getRoutine_id() {
        return routine_id;
    }

    public String getEmp_id() {
        return emp_id;
    }

    public String getDate_id() {
        return date_id;
    }

    public String getArea_name() {
        return area_name;
    }

    public String getSpot_id() {
        return spot_id;
    }

    public String getStart_date() {
        return start_date;
    }

    public String getRoutine_job_id() {
        return routine_job_id;
    }

    public String getNotes() {
        return notes;
    }
}
