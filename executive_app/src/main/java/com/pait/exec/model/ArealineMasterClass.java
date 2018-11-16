package com.pait.exec.model;

//Created by ANUP on 1/16/2018.

import java.io.Serializable;

public class ArealineMasterClass implements Serializable{

    private int auto,areaid,custcount;
    private String area;

    public int getAuto() {
        return auto;
    }

    public void setAuto(int auto) {
        this.auto = auto;
    }

    public int getAreaid() {
        return areaid;
    }

    public void setAreaid(int areaid) {
        this.areaid = areaid;
    }

    public int getCustcount() {
        return custcount;
    }

    public void setCustcount(int custcount) {
        this.custcount = custcount;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
