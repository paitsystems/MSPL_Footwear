package com.lnbinfotech.msplfootwearex.model;

//Created by ANUP on 1/3/2018.

public class CheckAvailStockClass {

    private String color,sizegroup,Rate,Alias,Stat,Hashcode;
    private int productid,AvailQty,Branchid;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSizegroup() {
        return sizegroup;
    }

    public void setSizegroup(String sizegroup) {
        this.sizegroup = sizegroup;
    }

    public String getRate() {
        return Rate;
    }

    public void setRate(String rate) {
        Rate = rate;
    }

    public String getAlias() {
        return Alias;
    }

    public void setAlias(String alias) {
        Alias = alias;
    }

    public String getStat() {
        return Stat;
    }

    public void setStat(String stat) {
        Stat = stat;
    }

    public String getHashcode() {
        return Hashcode;
    }

    public void setHashcode(String hashcode) {
        Hashcode = hashcode;
    }

    public int getProductid() {
        return productid;
    }

    public void setProductid(int productid) {
        this.productid = productid;
    }

    public int getAvailQty() {
        return AvailQty;
    }

    public void setAvailQty(int availQty) {
        AvailQty = availQty;
    }

    public int getBranchid() {
        return Branchid;
    }

    public void setBranchid(int branchid) {
        Branchid = branchid;
    }
}
