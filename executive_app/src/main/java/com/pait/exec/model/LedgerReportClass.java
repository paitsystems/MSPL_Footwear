package com.pait.exec.model;

/**
 * Created by SNEHA on 12/11/2017.
 */
public class LedgerReportClass {
    public String date,against,transno;
    public double opnbal,clsbal,debit,credit;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAgainst() {
        return against;
    }

    public void setAgainst(String against) {
        this.against = against;
    }

    public String getTransno() {
        return transno;
    }

    public void setTransno(String transno) {
        this.transno = transno;
    }

    public double getOpnbal() {
        return opnbal;
    }

    public void setOpnbal(double opnbal) {
        this.opnbal = opnbal;
    }

    public double getClsbal() {
        return clsbal;
    }

    public void setClsbal(double clsbal) {
        this.clsbal = clsbal;
    }

    public double getDebit() {
        return debit;
    }

    public void setDebit(double debit) {
        this.debit = debit;
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }
}
