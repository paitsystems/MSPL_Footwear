package com.pait.dispatch_app.model;

//Created by lnb on 10/4/2017.

import java.io.Serializable;

public class EmployeeMasterClass implements Serializable{

    public String Name,Mobno,Add,Status,DesigName,CompanyName,CompanyInit;
    public int Emp_Id,Desig_Id,Branch_Id,HoCode, PIN;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMobno() {
        return Mobno;
    }

    public void setMobno(String mobno) {
        Mobno = mobno;
    }

    public String getAdd() {
        return Add;
    }

    public void setAdd(String add) {
        Add = add;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getDesigName() {
        return DesigName;
    }

    public void setDesigName(String desigName) {
        DesigName = desigName;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getCompanyInit() {
        return CompanyInit;
    }

    public void setCompanyInit(String companyInit) {
        CompanyInit = companyInit;
    }

    public int getEmp_Id() {
        return Emp_Id;
    }

    public void setEmp_Id(int emp_Id) {
        Emp_Id = emp_Id;
    }

    public int getDesig_Id() {
        return Desig_Id;
    }

    public void setDesig_Id(int desig_Id) {
        Desig_Id = desig_Id;
    }

    public int getBranch_Id() {
        return Branch_Id;
    }

    public void setBranch_Id(int branch_Id) {
        Branch_Id = branch_Id;
    }

    public int getHoCode() {
        return HoCode;
    }

    public void setHoCode(int hoCode) {
        HoCode = hoCode;
    }

    public int getPIN() {
        return PIN;
    }

    public void setPIN(int PIN) {
        this.PIN = PIN;
    }
}
