package com.pait.dispatch_app.model;

import com.google.gson.annotations.SerializedName;

public class StockTakeClass {

    @SerializedName("Auto")
    private String Auto;
    @SerializedName("AllotDate")
    private String AllotDate;
    @SerializedName("Branchid")
    private String Branchid;
    @SerializedName("Itemid")
    private String Itemid;
    @SerializedName("Checker")
    private String Checker;
    @SerializedName("Packer")
    private String Packer;
    @SerializedName("CrBy")
    private String CrBy;
    @SerializedName("CrDate")
    private String CrDate;
    @SerializedName("CrTime")
    private String CrTime;
    @SerializedName("productId")
    private String ProductId;
    @SerializedName("packQty")
    private String PackQty;
    @SerializedName("looseQty")
    private String LooseQty;
    @SerializedName("totalQty")
    private String TotalQty;
    @SerializedName("stockQty")
    private String StockQty;
    @SerializedName("hoCode")
    private String HOCode;
    @SerializedName("NoOfPices")
    private String NoOfPices;
    @SerializedName("Round")
    private String Round;
    private String stockCheckDate;

    public String getAuto() {
        return Auto;
    }

    public void setAuto(String auto) {
        Auto = auto;
    }

    public String getAllotDate() {
        return AllotDate;
    }

    public void setAllotDate(String allotDate) {
        AllotDate = allotDate;
    }

    public String getBranchid() {
        return Branchid;
    }

    public void setBranchid(String branchid) {
        Branchid = branchid;
    }

    public String getItemid() {
        return Itemid;
    }

    public void setItemid(String itemid) {
        Itemid = itemid;
    }

    public String getChecker() {
        return Checker;
    }

    public void setChecker(String checker) {
        Checker = checker;
    }

    public String getPacker() {
        return Packer;
    }

    public void setPacker(String packer) {
        Packer = packer;
    }

    public String getCrBy() {
        return CrBy;
    }

    public void setCrBy(String crBy) {
        CrBy = crBy;
    }

    public String getCrDate() {
        return CrDate;
    }

    public void setCrDate(String crDate) {
        CrDate = crDate;
    }

    public String getCrTime() {
        return CrTime;
    }

    public void setCrTime(String crTime) {
        CrTime = crTime;
    }

    public String getPackQty() {
        return PackQty;
    }

    public void setPackQty(String packQty) {
        PackQty = packQty;
    }

    public String getLooseQty() {
        return LooseQty;
    }

    public void setLooseQty(String looseQty) {
        LooseQty = looseQty;
    }

    public String getTotalQty() {
        return TotalQty;
    }

    public void setTotalQty(String totalQty) {
        TotalQty = totalQty;
    }

    public String getStockQty() {
        return StockQty;
    }

    public void setStockQty(String stockQty) {
        StockQty = stockQty;
    }

    public String getHOCode() {
        return HOCode;
    }

    public void setHOCode(String HOCode) {
        this.HOCode = HOCode;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getStockCheckDate() {
        return stockCheckDate;
    }

    public void setStockCheckDate(String stockCheckDate) {
        this.stockCheckDate = stockCheckDate;
    }

    public String getNoOfPices() {
        return NoOfPices;
    }

    public void setNoOfPices(String noOfPices) {
        NoOfPices = noOfPices;
    }

    public String getRound() {
        return Round;
    }

    public void setRound(String round) {
        Round = round;
    }
}
