package com.pait.dispatch_app.model;

import com.google.gson.annotations.SerializedName;

public class StockTakeClass {

    @SerializedName("Auto")
    private String Auto;
    @SerializedName("Product_id")
    private String Product_id;
    @SerializedName("ProductId")
    private String ProductId;
    @SerializedName("ArticleName")
    private String ArticleName;
    @SerializedName("PackQty")
    private String PackQty;
    @SerializedName("LooseQty")
    private String LooseQty;
    @SerializedName("TotalQty")
    private String TotalQty;
    @SerializedName("StockQty")
    private String StockQty;
    @SerializedName("Avail")
    private String Avail;
    @SerializedName("Round")
    private String Round;
    @SerializedName("MRP")
    private String MRP;
    @SerializedName("Colour")
    private String Colour;
    @SerializedName("HashCode")
    private String HashCode;
    @SerializedName("HOCode")
    private String HOCode;
    @SerializedName("BranchId")
    private String BranchId;
    @SerializedName("CrBy")
    private String CrBy;
    @SerializedName("CrDate")
    private String CrDate;
    @SerializedName("CrTime")
    private String CrTime;
    @SerializedName("SizeGroup")
    private String SizeGroup;
    @SerializedName("GSTGroup")
    private String GSTGroup;
    @SerializedName("DesignNo")
    private String DesignNo;
    @SerializedName("InOutType")
    private String InOutType;
    @SerializedName("Typ")
    private String Typ;
    @SerializedName("Stock_Check_Date")
    private String Stock_Check_Date;

    public String getAuto() {
        return Auto;
    }

    public void setAuto(String auto) {
        Auto = auto;
    }

    public String getProduct_id() {
        return Product_id;
    }

    public void setProduct_id(String product_id) {
        Product_id = product_id;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getArticleName() {
        return ArticleName;
    }

    public void setArticleName(String articleName) {
        ArticleName = articleName;
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

    public String getAvail() {
        return Avail;
    }

    public void setAvail(String avail) {
        Avail = avail;
    }

    public String getRound() {
        return Round;
    }

    public void setRound(String round) {
        Round = round;
    }

    public String getMRP() {
        return MRP;
    }

    public void setMRP(String MRP) {
        this.MRP = MRP;
    }

    public String getColour() {
        return Colour;
    }

    public void setColour(String colour) {
        Colour = colour;
    }

    public String getHashCode() {
        return HashCode;
    }

    public void setHashCode(String hashCode) {
        HashCode = hashCode;
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

    public String getSizeGroup() {
        return SizeGroup;
    }

    public void setSizeGroup(String sizeGroup) {
        SizeGroup = sizeGroup;
    }

    public String getGSTGroup() {
        return GSTGroup;
    }

    public void setGSTGroup(String GSTGroup) {
        this.GSTGroup = GSTGroup;
    }

    public String getDesignNo() {
        return DesignNo;
    }

    public void setDesignNo(String designNo) {
        DesignNo = designNo;
    }

    public String getInOutType() {
        return InOutType;
    }

    public void setInOutType(String inOutType) {
        InOutType = inOutType;
    }

    public String getTyp() {
        return Typ;
    }

    public void setTyp(String typ) {
        Typ = typ;
    }

    public String getStock_Check_Date() {
        return Stock_Check_Date;
    }

    public void setStock_Check_Date(String stock_Check_Date) {
        Stock_Check_Date = stock_Check_Date;
    }

    public String getHOCode() {
        return HOCode;
    }

    public void setHOCode(String HOCode) {
        this.HOCode = HOCode;
    }

    public String getBranchId() {
        return BranchId;
    }

    public void setBranchId(String branchId) {
        BranchId = branchId;
    }
}
