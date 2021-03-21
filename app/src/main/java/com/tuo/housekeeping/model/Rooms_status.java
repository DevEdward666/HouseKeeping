package com.tuo.housekeeping.model;

public class Rooms_status {

    public String statcode;
    public String statdesc;

    public Rooms_status(String statcode, String statdesc) {
        this.statcode = statcode;
        this.statdesc = statdesc;
    }

    public String getStatcode() {
        return statcode;
    }

    public String getStatdesc() {
        return statdesc;
    }
}
