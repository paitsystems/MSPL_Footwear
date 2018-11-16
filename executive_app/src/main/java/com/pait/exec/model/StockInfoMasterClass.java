package com.pait.exec.model;

//Created by lnb on 10/4/2017.

public class StockInfoMasterClass {

    public String Company,ProductId,Color,Size,Rate,PackUnpack,SaleRate;
    public int LQty,PQty,PerPackQty,Product_id;

    public String getCompany() {
        return Company;
    }

    public void setCompany(String company) {
        Company = company;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getColor() {
        return Color;
    }

    public void setColor(String color) {
        Color = color;
    }

    public String getSize() {
        return Size;
    }

    public void setSize(String size) {
        Size = size;
    }

    public String getRate() {
        return Rate;
    }

    public void setRate(String rate) {
        Rate = rate;
    }

    public String getPackUnpack() {
        return PackUnpack;
    }

    public void setPackUnpack(String packUnpack) {
        PackUnpack = packUnpack;
    }

    public String getSaleRate() {
        return SaleRate;
    }

    public void setSaleRate(String saleRate) {
        SaleRate = saleRate;
    }

    public int getLQty() {
        return LQty;
    }

    public void setLQty(int LQty) {
        this.LQty = LQty;
    }

    public int getPQty() {
        return PQty;
    }

    public void setPQty(int PQty) {
        this.PQty = PQty;
    }

    public int getPerPackQty() {
        return PerPackQty;
    }

    public void setPerPackQty(int perPackQty) {
        PerPackQty = perPackQty;
    }

    public int getProduct_id() {
        return Product_id;
    }

    public void setProduct_id(int product_id) {
        Product_id = product_id;
    }
}
