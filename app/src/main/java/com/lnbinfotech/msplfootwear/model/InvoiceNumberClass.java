package com.lnbinfotech.msplfootwear.model;

import com.google.gson.annotations.SerializedName;

public class InvoiceNumberClass {

    @SerializedName("INVNO")
    private String invNo;

    public String getInvNo() {
        return invNo;
    }

    public void setInvNo(String invNo) {
        this.invNo = invNo;
    }
}
