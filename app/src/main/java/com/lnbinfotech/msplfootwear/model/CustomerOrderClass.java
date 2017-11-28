package com.lnbinfotech.msplfootwear.model;

//Created by ANUP on 11/25/2017.

public class CustomerOrderClass {

    private int auto, branchId, productid, perPackQty, qty, looseQty, actLooseQty;
    private double rate, mrp, amount, totalamt, gstper,cgstamt,sgstamt,igstamt, cgstper, sgstper, cessper, cessamt;
    private String sizeGroup, requiredSize, color, hashCode, loosePackTyp;


    public int getAuto() {
        return auto;
    }

    public void setAuto(int auto) {
        this.auto = auto;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public int getProductid() {
        return productid;
    }

    public void setProductid(int productid) {
        this.productid = productid;
    }

    public int getPerPackQty() {
        return perPackQty;
    }

    public void setPerPackQty(int perPackQty) {
        this.perPackQty = perPackQty;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getLooseQty() {
        return looseQty;
    }

    public void setLooseQty(int looseQty) {
        this.looseQty = looseQty;
    }

    public int getActLooseQty() {
        return actLooseQty;
    }

    public void setActLooseQty(int actLooseQty) {
        this.actLooseQty = actLooseQty;
    }

    public String getSizeGroup() {
        return sizeGroup;
    }

    public void setSizeGroup(String sizeGroup) {
        this.sizeGroup = sizeGroup;
    }

    public String getRequiredSize() {
        return requiredSize;
    }

    public void setRequiredSize(String requiredSize) {
        this.requiredSize = requiredSize;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getLoosePackTyp() {
        return loosePackTyp;
    }

    public void setLoosePackTyp(String loosePackTyp) {
        this.loosePackTyp = loosePackTyp;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getMrp() {
        return mrp;
    }

    public void setMrp(double mrp) {
        this.mrp = mrp;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getTotalamt() {
        return totalamt;
    }

    public void setTotalamt(double totalamt) {
        this.totalamt = totalamt;
    }

    public double getGstper() {
        return gstper;
    }

    public void setGstper(double gstper) {
        this.gstper = gstper;
    }

    public double getCgstamt() {
        return cgstamt;
    }

    public void setCgstamt(double cgstamt) {
        this.cgstamt = cgstamt;
    }

    public double getSgstamt() {
        return sgstamt;
    }

    public void setSgstamt(double sgstamt) {
        this.sgstamt = sgstamt;
    }

    public double getIgstamt() {
        return igstamt;
    }

    public void setIgstamt(double igstamt) {
        this.igstamt = igstamt;
    }

    public double getCgstper() {
        return cgstper;
    }

    public void setCgstper(double cgstper) {
        this.cgstper = cgstper;
    }

    public double getSgstper() {
        return sgstper;
    }

    public void setSgstper(double sgstper) {
        this.sgstper = sgstper;
    }

    public double getCessper() {
        return cessper;
    }

    public void setCessper(double cessper) {
        this.cessper = cessper;
    }

    public double getCessamt() {
        return cessamt;
    }

    public void setCessamt(double cessamt) {
        this.cessamt = cessamt;
    }

    public String getHashCode() {
        return hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }
}
