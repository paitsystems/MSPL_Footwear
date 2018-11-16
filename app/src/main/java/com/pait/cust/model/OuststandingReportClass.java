package com.pait.cust.model;

/**
 * Created by SNEHA on 12/11/2017.
 */
public class OuststandingReportClass {
    public String date,dcno,type;
    public double total;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDcno() {
        return dcno;
    }

    public void setDcno(String dcno) {
        this.dcno = dcno;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
