package com.lnbinfotech.msplfootwear.model;

//Created by ANUP on 11/26/2017.

public class GSTMasterClass {

    private int auto;
    private String groupName, Status;
    private double gstPer, cgstPer, sgstPer, cessPer, cgstShare, sgstShare;

    public int getAuto() {
        return auto;
    }

    public void setAuto(int auto) {
        this.auto = auto;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public double getGstPer() {
        return gstPer;
    }

    public void setGstPer(double gstPer) {
        this.gstPer = gstPer;
    }

    public double getCgstPer() {
        return cgstPer;
    }

    public void setCgstPer(double cgstPer) {
        this.cgstPer = cgstPer;
    }

    public double getSgstPer() {
        return sgstPer;
    }

    public void setSgstPer(double sgstPer) {
        this.sgstPer = sgstPer;
    }

    public double getCessPer() {
        return cessPer;
    }

    public void setCessPer(double cessPer) {
        this.cessPer = cessPer;
    }

    public double getCgstShare() {
        return cgstShare;
    }

    public void setCgstShare(double cgstShare) {
        this.cgstShare = cgstShare;
    }

    public double getSgstShare() {
        return sgstShare;
    }

    public void setSgstShare(double sgstShare) {
        this.sgstShare = sgstShare;
    }
}
