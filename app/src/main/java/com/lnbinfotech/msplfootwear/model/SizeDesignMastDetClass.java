package com.lnbinfotech.msplfootwear.model;

//Created by ANUP on 12/29/2017.

import com.google.gson.annotations.SerializedName;

public class SizeDesignMastDetClass {

    @SerializedName("Auto")
    private int auto;
    @SerializedName("Productid")
    private int productid;
    @SerializedName("SizeGroupFrom")
    private int sizeGroupFrom;
    @SerializedName("SizeGroupTo")
    private int sizeGroupTo;
    @SerializedName("Total")
    private int total;
    @SerializedName("Size")
    private int size;
    @SerializedName("Qty")
    private int qty;
    @SerializedName("DesignNo")
    private String designNo;
    @SerializedName("SizeGroup")
    private String sizeGroup;
    @SerializedName("Colour")
    private String colour;

    public int getAuto() {
        return auto;
    }

    public void setAuto(int auto) {
        this.auto = auto;
    }

    public int getProductid() {
        return productid;
    }

    public void setProductid(int productid) {
        this.productid = productid;
    }

    public int getSizeGroupFrom() {
        return sizeGroupFrom;
    }

    public void setSizeGroupFrom(int sizeGroupFrom) {
        this.sizeGroupFrom = sizeGroupFrom;
    }

    public int getSizeGroupTo() {
        return sizeGroupTo;
    }

    public void setSizeGroupTo(int sizeGroupTo) {
        this.sizeGroupTo = sizeGroupTo;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getDesignNo() {
        return designNo;
    }

    public void setDesignNo(String designNo) {
        this.designNo = designNo;
    }

    public String getSizeGroup() {
        return sizeGroup;
    }

    public void setSizeGroup(String sizeGroup) {
        this.sizeGroup = sizeGroup;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }
}
