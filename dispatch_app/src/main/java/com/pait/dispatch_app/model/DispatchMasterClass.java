package com.pait.dispatch_app.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DispatchMasterClass implements Serializable {

    @SerializedName("Auto")
    public String Auto;
    @SerializedName("DispatchBy")
    public String DispatchBy;
    @SerializedName("PONO")
    public String PONO;
    @SerializedName("TotalQty")
    public String TotalQty;
    @SerializedName("Emp_Id")
    public String Emp_Id;
    @SerializedName("Emp_Name")
    public String Emp_Name;
    @SerializedName("Transporter")
    public String Transporter;
    @SerializedName("TransId")
    public String TransId;
    @SerializedName("CustId")
    public String CustId;
    @SerializedName("PartyName")
    public String PartyName;
    @SerializedName("branchid")
    public String branchid;
    @SerializedName("DispatchCmp")
    public String DispatchCmp;
    @SerializedName("DispatchDate")
    public String DispatchDate;
    @SerializedName("DcNo")
    public String DcNo;
    @SerializedName("DCdate")
    public String DCdate;
    @SerializedName("DPTotal")
    public String DPTotal;
    @SerializedName("PSImage")
    public String PSImage;

    public String getAuto() {
        return Auto;
    }

    public void setAuto(String auto) {
        Auto = auto;
    }

    public String getDispatchBy() {
        return DispatchBy;
    }

    public void setDispatchBy(String dispatchBy) {
        DispatchBy = dispatchBy;
    }

    public String getPONO() {
        return PONO;
    }

    public void setPONO(String PONO) {
        this.PONO = PONO;
    }

    public String getTotalQty() {
        return TotalQty;
    }

    public void setTotalQty(String totalQty) {
        TotalQty = totalQty;
    }

    public String getEmp_Id() {
        return Emp_Id;
    }

    public void setEmp_Id(String emp_Id) {
        Emp_Id = emp_Id;
    }

    public String getEmp_Name() {
        return Emp_Name;
    }

    public void setEmp_Name(String emp_Name) {
        Emp_Name = emp_Name;
    }

    public String getTransporter() {
        return Transporter;
    }

    public void setTransporter(String transporter) {
        Transporter = transporter;
    }

    public String getTransId() {
        return TransId;
    }

    public void setTransId(String transId) {
        TransId = transId;
    }

    public String getCustId() {
        return CustId;
    }

    public void setCustId(String custId) {
        CustId = custId;
    }

    public String getPartyName() {
        return PartyName;
    }

    public void setPartyName(String partyName) {
        PartyName = partyName;
    }

    public String getBranchid() {
        return branchid;
    }

    public void setBranchid(String branchid) {
        this.branchid = branchid;
    }

    public String getDispatchCmp() {
        return DispatchCmp;
    }

    public void setDispatchCmp(String dispatchCmp) {
        DispatchCmp = dispatchCmp;
    }

    public String getDispatchDate() {
        return DispatchDate;
    }

    public void setDispatchDate(String dispatchDate) {
        DispatchDate = dispatchDate;
    }

    public String getDcNo() {
        return DcNo;
    }

    public void setDcNo(String dcNo) {
        DcNo = dcNo;
    }

    public String getDCdate() {
        return DCdate;
    }

    public void setDCdate(String DCdate) {
        this.DCdate = DCdate;
    }

    public String getDPTotal() {
        return DPTotal;
    }

    public void setDPTotal(String DPTotal) {
        this.DPTotal = DPTotal;
    }

    public String getPSImage() {
        return PSImage;
    }

    public void setPSImage(String PSImage) {
        this.PSImage = PSImage;
    }
}
