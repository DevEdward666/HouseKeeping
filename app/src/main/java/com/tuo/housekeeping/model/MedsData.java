package com.tuo.housekeeping.model;

public class MedsData {
    public String patno;
    public String stockdesc;
    public  String freqdesc;
    public String datestarted;
    public String doctor;

    public MedsData(String patno, String stockdesc, String freqdesc, String datestarted, String doctor) {
        this.patno = patno;
        this.stockdesc = stockdesc;
        this.freqdesc = freqdesc;
        this.datestarted = datestarted;
        this.doctor = doctor;
    }

    public String getPatno() {
        return patno;
    }

    public String getStockdesc() {
        return stockdesc;
    }

    public String getFreqdesc() {
        return freqdesc;
    }

    public String getDatestarted() {
        return datestarted;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setPatno(String patno) {
        this.patno = patno;
    }

    public void setStockdesc(String stockdesc) {
        this.stockdesc = stockdesc;
    }

    public void setFreqdesc(String freqdesc) {
        this.freqdesc = freqdesc;
    }

    public void setDatestarted(String datestarted) {
        this.datestarted = datestarted;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }
}
