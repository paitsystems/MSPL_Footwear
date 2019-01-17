package com.lnbinfotech.msplfootwearex.model;

//Created by SNEHA on 11-10-2017.

import com.google.gson.annotations.SerializedName;

public class BankBranchMasterClass {

    @SerializedName("Autoid")
    public String autoid;
    @SerializedName("Branch")
    public String branch;
    @SerializedName("id")
    public String id;
    @SerializedName("Custid")
    public String custid;
    @SerializedName("AccountNo")
    public String accountNo;
    @SerializedName("CBankid")
    public String cBankid;
    @SerializedName("CBranch")
    public String cBranch;

    public String getAutoid() {
        return autoid;
    }

    public void setAutoid(String autoid) {
        this.autoid = autoid;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getCustid() {
        return custid;
    }

    public void setCustid(String custid) {
        this.custid = custid;
    }

    public String getcBankid() {
        return cBankid;
    }

    public void setcBankid(String cBankid) {
        this.cBankid = cBankid;
    }

    public String getcBranch() {
        return cBranch;
    }

    public void setcBranch(String cBranch) {
        this.cBranch = cBranch;
    }
}
