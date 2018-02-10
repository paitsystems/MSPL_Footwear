package com.lnbinfotech.msplfootwearex.model;

//Created by SNEHA on 12/11/2017.

public class OuststandingReportClass {

    private String date,dcno,type;
    private double total, paidAmnt, outAmnt, recAmnt, netAmt;
    private boolean isChecked;

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

    public double getPaidAmnt() {
        return paidAmnt;
    }

    public void setPaidAmnt(double paidAmnt) {
        this.paidAmnt = paidAmnt;
    }

    public double getOutAmnt() {
        return outAmnt;
    }

    public void setOutAmnt(double outAmnt) {
        this.outAmnt = outAmnt;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public double getRecAmnt() {
        return recAmnt;
    }

    public void setRecAmnt(double recAmnt) {
        this.recAmnt = recAmnt;
    }

    public double getNetAmt() {
        return netAmt;
    }

    public void setNetAmt(double netAmt) {
        this.netAmt = netAmt;
    }
}
