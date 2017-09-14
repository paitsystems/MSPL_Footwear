package com.lnbinfotech.msplfootwear.model;

import java.io.Serializable;

//Created by lnb on 9/14/2017.

public class CheckOtpClass implements Serializable {

    public String custId, otp, mobileno, imeino;

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getImeino() {
        return imeino;
    }

    public void setImeino(String imeino) {
        this.imeino = imeino;
    }
}
