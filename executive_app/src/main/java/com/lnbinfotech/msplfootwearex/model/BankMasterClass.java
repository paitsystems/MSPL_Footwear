package com.lnbinfotech.msplfootwearex.model;

/**
 * Created by SNEHA on 10-10-2017.
 */
public class BankMasterClass {
    public String bankId,branchId,bankName,accountNo,status,IFSC,MICR,CustType,HoCode;

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getIFSC() {
        return IFSC;
    }

    public void setIFSC(String IFSC) {
        this.IFSC = IFSC;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMICR() {
        return MICR;
    }

    public void setMICR(String MICR) {
        this.MICR = MICR;
    }

    public String getCustType() {
        return CustType;
    }

    public void setCustType(String custType) {
        CustType = custType;
    }

    public String getHoCode() {
        return HoCode;
    }

    public void setHoCode(String hoCode) {
        HoCode = hoCode;
    }
}
