package com.lnbinfotech.msplfootwear.model;

import com.google.gson.annotations.SerializedName;

public class SchemeMasterClass {

    @SerializedName("Auto")
    public String Auto;
    @SerializedName("Id")
    public String Id;
    @SerializedName("CustId")
    public String CustId;
    @SerializedName("HOCode")
    public String HOCode;
    @SerializedName("BranchId")
    public String BranchId;
    @SerializedName("ArealineId")
    public String ArealineId;
    @SerializedName("AreaId")
    public String AreaId;
    @SerializedName("Cat")
    public String Cat;
    @SerializedName("ImgName")
    public String ImgName;
    @SerializedName("Status")
    public String Status;
    @SerializedName("StartDate")
    public String StartDate;
    @SerializedName("EndDate")
    public String EndDate;
    @SerializedName("CrDate")
    public String CrDate;
    @SerializedName("CrTime")
    public String CrTime;

    public String getAuto() {
        return Auto;
    }

    public void setAuto(String auto) {
        Auto = auto;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getCustId() {
        return CustId;
    }

    public void setCustId(String custId) {
        CustId = custId;
    }

    public String getHOCode() {
        return HOCode;
    }

    public void setHOCode(String HOCode) {
        this.HOCode = HOCode;
    }

    public String getBranchId() {
        return BranchId;
    }

    public void setBranchId(String branchId) {
        BranchId = branchId;
    }

    public String getArealineId() {
        return ArealineId;
    }

    public void setArealineId(String arealineId) {
        ArealineId = arealineId;
    }

    public String getAreaId() {
        return AreaId;
    }

    public void setAreaId(String areaId) {
        AreaId = areaId;
    }

    public String getCat() {
        return Cat;
    }

    public void setCat(String cat) {
        Cat = cat;
    }

    public String getImgName() {
        return ImgName;
    }

    public void setImgName(String imgName) {
        ImgName = imgName;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public String getCrDate() {
        return CrDate;
    }

    public void setCrDate(String crDate) {
        CrDate = crDate;
    }

    public String getCrTime() {
        return CrTime;
    }

    public void setCrTime(String crTime) {
        CrTime = crTime;
    }
}
