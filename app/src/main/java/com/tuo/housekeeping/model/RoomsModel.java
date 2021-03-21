package com.tuo.housekeeping.model;

public class RoomsModel {
    public String roomcode;
    public String roombedno;
    public String moddesc;
    public String listroomstatus;
    public String patno;
    public String patientname;

    public RoomsModel(String roomcode, String roombedno, String moddesc, String listroomstatus, String patno, String patientname) {
        this.roomcode = roomcode;
        this.roombedno = roombedno;
        this.moddesc = moddesc;
        this.listroomstatus = listroomstatus;
        this.patno = patno;
        this.patientname = patientname;
    }

    public String getRoomcode() {
        return roomcode;
    }

    public String getRoombedno() {
        return roombedno;
    }

    public String getModdesc() {
        return moddesc;
    }

    public String getListroomstatus() {
        return listroomstatus;
    }

    public String getPatno() {
        return patno;
    }

    public String getPatientname() {
        return patientname;
    }
}
