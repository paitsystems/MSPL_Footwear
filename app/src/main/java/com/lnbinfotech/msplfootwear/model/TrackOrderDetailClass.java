package com.lnbinfotech.msplfootwear.model;

//Created by SNEHA on 10/27/2017.

public class TrackOrderDetailClass {
    private String final_prod,size_group,color,mrp,actLooseQty,loosePackTyp;
    private int auto,productid;

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

    public String getFinal_prod() {
        return final_prod;
    }

    public void setFinal_prod(String final_prod) {
        this.final_prod = final_prod;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize_group() {
        return size_group;
    }

    public void setSize_group(String size_group) {
        this.size_group = size_group;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getActLooseQty() {
        return actLooseQty;
    }

    public void setActLooseQty(String actLooseQty) {
        this.actLooseQty = actLooseQty;
    }

    public String getLoosePackTyp() {
        return loosePackTyp;
    }

    public void setLoosePackTyp(String loosePackTyp) {
        this.loosePackTyp = loosePackTyp;
    }
}
