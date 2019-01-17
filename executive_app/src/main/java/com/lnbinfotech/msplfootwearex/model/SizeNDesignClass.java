package com.lnbinfotech.msplfootwearex.model;

//Created by ANUP on 11/4/2017.

import com.google.gson.annotations.SerializedName;

public class SizeNDesignClass {

    private String Cat1;
    private String Cat2;
    private String Cat3;
    private String Cat4;
    private String Cat5;
    private String Cat6;
    private String Final_prod;
    private String Uom;
    private String Vat;
    @SerializedName("DesignNo")
    private String DesignNo;
    @SerializedName("Colour")
    private String Colour;
    @SerializedName("SizeGroup")
    private String SizeGroup;
    @SerializedName("typ")
    private String typ;
    private String ActualInw;
    @SerializedName("GSTGroup")
    private String GSTGroup;
    @SerializedName("InOutType")
    private String InOutType;
    @SerializedName("HashCode")
    private String HashCode;
    @SerializedName("ImageName")
    private String ImageName;

    @SerializedName("Productid")
    private int Productid;
    @SerializedName("SizeFrom")
    private int SizeFrom;
    @SerializedName("SizeTo")
    private int SizeTo;
    @SerializedName("Total")
    private int total;


    public String getCat1() {
        return Cat1;
    }

    public void setCat1(String cat1) {
        Cat1 = cat1;
    }

    public String getCat2() {
        return Cat2;
    }

    public void setCat2(String cat2) {
        Cat2 = cat2;
    }

    public String getCat3() {
        return Cat3;
    }

    public void setCat3(String cat3) {
        Cat3 = cat3;
    }

    public String getCat4() {
        return Cat4;
    }

    public void setCat4(String cat4) {
        Cat4 = cat4;
    }

    public String getCat5() {
        return Cat5;
    }

    public void setCat5(String cat5) {
        Cat5 = cat5;
    }

    public String getCat6() {
        return Cat6;
    }

    public void setCat6(String cat6) {
        Cat6 = cat6;
    }

    public String getFinal_prod() {
        return Final_prod;
    }

    public void setFinal_prod(String final_prod) {
        Final_prod = final_prod;
    }

    public String getUom() {
        return Uom;
    }

    public void setUom(String uom) {
        Uom = uom;
    }

    public String getVat() {
        return Vat;
    }

    public void setVat(String vat) {
        Vat = vat;
    }

    public String getDesignNo() {
        return DesignNo;
    }

    public void setDesignNo(String designNo) {
        DesignNo = designNo;
    }

    public String getColour() {
        return Colour;
    }

    public void setColour(String colour) {
        Colour = colour;
    }

    public String getSizeGroup() {
        return SizeGroup;
    }

    public void setSizeGroup(String sizeGroup) {
        SizeGroup = sizeGroup;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public String getActualInw() {
        return ActualInw;
    }

    public void setActualInw(String actualInw) {
        ActualInw = actualInw;
    }

    public String getGSTGroup() {
        return GSTGroup;
    }

    public void setGSTGroup(String GSTGroup) {
        this.GSTGroup = GSTGroup;
    }

    public int getProductid() {
        return Productid;
    }

    public void setProductid(int productid) {
        Productid = productid;
    }

    public int getSizeFrom() {
        return SizeFrom;
    }

    public void setSizeFrom(int sizeFrom) {
        SizeFrom = sizeFrom;
    }

    public int getSizeTo() {
        return SizeTo;
    }

    public void setSizeTo(int sizeTo) {
        SizeTo = sizeTo;
    }

    public String getInOutType() {
        return InOutType;
    }

    public void setInOutType(String inOutType) {
        InOutType = inOutType;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getHashCode() {
        return HashCode;
    }

    public void setHashCode(String hashCode) {
        HashCode = hashCode;
    }

    public String getImageName() {
        return ImageName;
    }

    public void setImageName(String imageName) {
        ImageName = imageName;
    }
}
