package com.lnbinfotech.msplfootwear.model;

//Created by lnb on 9/15/2017.

import java.io.Serializable;

public class CustomerDetailClass implements Serializable{

    public String name,address,mobile,PANno,GSTNo,ImagePath, email;
    public int custID;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPANno() {
        return PANno;
    }

    public void setPANno(String PANno) {
        this.PANno = PANno;
    }

    public String getGSTNo() {
        return GSTNo;
    }

    public void setGSTNo(String GSTNo) {
        this.GSTNo = GSTNo;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public int getCustID() {
        return custID;
    }

    public void setCustID(int custID) {
        this.custID = custID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
