package com.tuo.housekeeping.model;

public class MedsSpecificData {
    public String patno;
    public String stockdesc;
    public  String freqdesc;

    public MedsSpecificData(String patno, String stockdesc, String freqdesc) {
        this.patno = patno;
        this.stockdesc = stockdesc;
        this.freqdesc = freqdesc;
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

    public void setPatno(String patno) {
        this.patno = patno;
    }

    public void setStockdesc(String stockdesc) {
        this.stockdesc = stockdesc;
    }

    public void setFreqdesc(String freqdesc) {
        this.freqdesc = freqdesc;
    }
}
