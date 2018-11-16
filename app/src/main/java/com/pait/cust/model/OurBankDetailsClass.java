package com.pait.cust.model;

/**
 * Created by SNEHA on 12/16/2017.
 */
public class OurBankDetailsClass {
    public  String bankName,IFSC,MICR,accNo;
    public int branchId,bankId,HOCode;

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getIFSC() {
        return IFSC;
    }

    public void setIFSC(String IFSC) {
        this.IFSC = IFSC;
    }

    public String getMICR() {
        return MICR;
    }

    public void setMICR(String MICR) {
        this.MICR = MICR;
    }

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public int getBankId() {
        return bankId;
    }

    public void setBankId(int bankId) {
        this.bankId = bankId;
    }

    public int getHOCode() {
        return HOCode;
    }

    public void setHOCode(int HOCode) {
        this.HOCode = HOCode;
    }
}
