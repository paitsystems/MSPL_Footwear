package com.pait.exec.model;

import java.io.Serializable;

//Created by lnb on 9/14/2017.

public class CheckOtpClass implements Serializable {

    public String custId, otp, mobileno, imeino, imeino1, imeino2;

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

    public String getImeino1() {
        return imeino1;
    }

    public void setImeino1(String imeino1) {
        this.imeino1 = imeino1;
    }

    public String getImeino2() {
        return imeino2;
    }

    public void setImeino2(String imeino2) {
        this.imeino2 = imeino2;
    }
}
