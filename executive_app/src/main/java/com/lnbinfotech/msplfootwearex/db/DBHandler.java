package com.lnbinfotech.msplfootwearex.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.model.AreaMasterClass;
import com.lnbinfotech.msplfootwearex.model.BankBranchMasterClass;
import com.lnbinfotech.msplfootwearex.model.BankMasterClass;
import com.lnbinfotech.msplfootwearex.model.CityMasterClass;
import com.lnbinfotech.msplfootwearex.model.CompanyMasterClass;
import com.lnbinfotech.msplfootwearex.model.CustomerDetailClass;
import com.lnbinfotech.msplfootwearex.model.DocumentMasterClass;
import com.lnbinfotech.msplfootwearex.model.EmployeeMasterClass;
import com.lnbinfotech.msplfootwearex.model.HOMasterClass;
import com.lnbinfotech.msplfootwearex.model.ProductMasterClass;
import com.lnbinfotech.msplfootwearex.model.StockInfoMasterClass;
import com.lnbinfotech.msplfootwearex.model.UserClass;

import java.util.ArrayList;
import java.util.List;

// Created by lnb on 8/11/2016.

public class DBHandler extends SQLiteOpenHelper {

    public static final String Database_Name = "SmartGST.db";
    public static final int Database_Version = 1;

    public static final String Table_Customermaster = "CustomerMaster";
    public static final String CM_RetailCustID = "CustID";
    public static final String CM_Name = "Name";
    public static final String CM_Address = "Address";
    public static final String CM_MobileNo = "MobileNo";
    public static final String CM_Status = "Status";
    public static final String CM_BranchId = "BranchId";
    public static final String CM_Email = "Email";
    public static final String CM_District = "District";
    public static final String CM_Taluka = "Taluka";
    public static final String CM_CityId = "CityId";
    public static final String CM_AreaId = "AreaId";
    public static final String CM_PANNo = "PANNo";
    public static final String CM_ImagePath = "ImagePath";
    public static final String CM_HOCode = "HOCode";
    public static final String CM_GSTNo = "GSTNo";
    public static final String CM_IMEINo = "IMEINo";
    public static final String CM_isRegistered = "isRegistered";
    public static final String CM_AadhaarNo = "AadhaarNo";
    public static final String CM_PIN = "PIN";

    public static final String Table_ProductMaster = "ProductMaster";
    public static final String PM_ProductID = "Product_id";
    public static final String PM_Cat1 = "Cat1";
    public static final String PM_Cat2 = "Cat2";
    public static final String PM_Cat3 = "Cat3";
    public static final String PM_Cat4 = "Cat4";
    public static final String PM_Cat5 = "Cat5";
    public static final String PM_Cat6 = "Cat6";
    public static final String PM_Finalprod = "Final_Prod";
    public static final String PM_UOM = "UOM";
    public static final String PM_SRate = "SRate";
    public static final String PM_PRate = "PRate";
    public static final String PM_BranchId = "BranchId";
    public static final String PM_Status = "Status";
    public static final String PM_NoOfPieces = "NoOfPieces";
    public static final String PM_CompanyId = "Company_id";
    public static final String PM_MRPRate = "MRPRate";
    public static final String PM_ProdId = "ProductID";
    public static final String PM_Cat7 = "Cat7";
    public static final String PM_Cat8 = "Cat8";
    public static final String PM_MinStkQty = "MinStkQty";
    public static final String PM_MaxStkQty = "MaxStkQty";
    public static final String PM_GSTGroup = "GSTGroup";
    public static final String PM_HSNCode = "HSNCode";
    public static final String PM_Cat9 = "Cat9";

    public static final String Table_StockInfo = "StockInfo";
    public static final String SI_Company = "Company";
    public static final String SI_ProdId = "ProductID";
    public static final String SI_Color = "Color";
    public static final String SI_Size = "Size";
    public static final String SI_Rate = "Rate";
    public static final String SI_LQty = "LQty";
    public static final String SI_PQty = "PQty";
    public static final String SI_PackUnpack = "PackUnpack";
    public static final String SI_PerPackQty = "PerPackQTy";
    public static final String SI_SaleRate = "SaleRate";
    public static final String SI_ProductID = "Product_id";

    public static final String Table_Employee = "Employee_Master";
    public static final String EMP_EmpId = "EmpID";
    public static final String EMP_Name = "Name";
    public static final String EMP_MobNo = "MobileNo";
    public static final String EMP_Adress = "Adress";
    public static final String EMP_DesignId = "DesignId";
    public static final String EMP_BranchId = "BranchId";
    public static final String EMP_Status = "Status";
    public static final String EMP_DesignName = "DesignName";
    public static final String EMP_CompName = "CompanyName";
    public static final String EMP_CompInit = "CompanyInitial";
    public static final String EMP_HoCode = "HoCode";
    public static final String EMP_PIN = "PIN";

    public static final String Table_HOMaster = "HOMaster";
    public static final String HO_Auto = "Auto";
    public static final String HO_Id = "Id";
    public static final String HO_Name = "Name";
    public static final String HO_City = "City";
    public static final String HO_State = "State";
    public static final String HO_ini = "ini";

    public static final String Table_AreaMaster = "AreaMaster";
    public static final String Area_Auto = "Auto";
    public static final String Area_Id = "Id";
    public static final String Area_Area = "Area";
    public static final String Area_Cityid = "Cityid";

    public static final String Table_CityMaster = "CityMaster";
    public static final String City_Auto = "Auto";
    public static final String City_Id = "Id";
    public static final String City_City = "City";
    public static final String City_Stateid = "StateId";

    public static final String Table_CompanyMaster = "CompanyMaster";
    public static final String Company_Id = "Id";
    public static final String Company_Name = "Name";
    public static final String Company_Initial = "Initial";
    public static final String Company_Pan = "Pan";
    public static final String Company_DisplayCmp = "DisplayCmp";
    public static final String Company_HOCode = "HOCode";
    public static final String Company_GSTNo = "GSTNo";

    public static final String Table_BankMaster = "BankMaster";
    public static final String Bank_Id = "Id";
    public static final String Bank_BranchId = "BranchId";
    public static final String Bank_Name = "Name";
    public static final String Bank_AccountNo = "AccountNo";
    public static final String Bank_Status = "Status";
    public static final String Bank_IFSC = "IFSC";
    public static final String Bank_MICR = "MICR";
    public static final String Bank_CustType = "CustType";
    public static final String Bank_HoCode = "HoCode";

    public static final String Table_BankBranchMaster = "BankBranchMaster";
    public static final String Branch_AutoId = "AutoId";
    public static final String Branch_Id = "Id";
    public static final String Branch_Branch = "Branch";
    public static final String Branch_CustId = "CustId";
    public static final String Branch_AccountNo = "AccountNo";
    public static final String Branch_CBankId = "CBankId";
    public static final String Branch_CBranch = "CBranch";

    //id,DocName,Alias,ForWhom,Compulsary
    public static final String Table_DocumentMaster = "DocumentMaster";
    public static final String Document_Id = "Id";
    public static final String Document_DocName = "DocName";
    public static final String Document_ForWhom = "ForWhom";
    public static final String Document_Compulsary = "Compulsary";

    public static final String Table_Usermaster = "UserMaster";
    public static final String UM_RetailCustID = "CustID";
    public static final String UM_Name = "Name";
    public static final String UM_Address = "Address";
    public static final String UM_MobileNo = "MobileNo";
    public static final String UM_Status = "Status";
    public static final String UM_BranchId = "BranchId";
    public static final String UM_Email = "Email";
    public static final String UM_District = "District";
    public static final String UM_Taluka = "Taluka";
    public static final String UM_CityId = "CityId";
    public static final String UM_AreaId = "AreaId";
    public static final String UM_PANNo = "PANNo";
    public static final String UM_ImagePath = "ImagePath";
    public static final String UM_HOCode = "HOCode";
    public static final String UM_GSTNo = "GSTNo";
    public static final String UM_IMEINo = "IMEINo";
    public static final String UM_isRegistered = "isRegistered";
    public static final String UM_AadhaarNo = "AadhaarNo";
    public static final String UM_PIN = "PIN";


    public DBHandler(Context context) {
        super(context, Database_Name, null, Database_Version);
    }

    String create_cust_master = "create table if not exists " + Table_Customermaster + "(" +
            CM_RetailCustID + " int," + CM_Name + " text," + CM_Address + " text," + CM_MobileNo + " text," + CM_Status + " text," +
            CM_BranchId + " int," + CM_Email + " text," + CM_District + " text," + CM_Taluka + " text," + CM_CityId + " int," +
            CM_AreaId + " int," + CM_PANNo + " text," + CM_ImagePath + " text," + CM_HOCode + " int," + CM_GSTNo + " text," + CM_IMEINo + " text," +
            CM_isRegistered + " text," + CM_AadhaarNo + " text," + CM_PIN + " int)";

    String create_prod_master = "create table if not exists " + Table_ProductMaster + "(" +
            PM_ProductID + " int," + PM_Cat1 + " text," + PM_Cat2 + " text," + PM_Cat3 + " text," + PM_Cat4 + " text," + PM_Cat5 + " text," + PM_Cat6 + " text," +
            PM_Finalprod + " text," + PM_UOM + " text," + PM_SRate + " text," + PM_PRate + " text," + PM_BranchId + " int," + PM_Status + " text," + PM_NoOfPieces + " int," +
            PM_CompanyId + " int," + PM_MRPRate + " text," + PM_ProdId + " text," + PM_Cat7 + " text," + PM_Cat8 + " text," + PM_MinStkQty + " int," +
            PM_MaxStkQty + " int," + PM_GSTGroup + " text," + PM_HSNCode + " text," + PM_Cat9 + " text)";

    String create_si_master = "create table if not exists " + Table_StockInfo + "(" +
            SI_Company + " text," + SI_ProdId + " text," + SI_Color + " text," + SI_Size + " text," + SI_Rate + " text," +
            SI_LQty + " int," + SI_PQty + " int," + SI_PackUnpack + " text," + SI_PerPackQty + " int," + SI_SaleRate + " text," +
            SI_ProductID + " int)";

    String create_emp_master = "create table if not exists " + Table_Employee + "(" +
            EMP_EmpId + " int," + EMP_Name + " text," + EMP_MobNo + " text," + EMP_Adress + " text," + EMP_DesignId + " int," + EMP_BranchId + " int," +
            EMP_Status + " text," + EMP_DesignName + " text," + EMP_CompName + " text," + EMP_CompInit + " text," + EMP_HoCode + " int," + EMP_PIN + " text)";

    String create_ho_master = "create table if not exists " + Table_HOMaster + "(" +
            HO_Auto + " int," + HO_Id + " int," + HO_Name + " text," + HO_City + " int," + HO_State + " int," + HO_ini + " text)";

    String create_area_master = "create table if not exists " + Table_AreaMaster + "(" +
            Area_Auto + " int," + Area_Id + " int," + Area_Area + " text," + Area_Cityid + " int)";

    String create_city_master = "create table if not exists " + Table_CityMaster + "(" +
            City_Auto + " int," + City_Id + " int," + City_City + " text," + City_Stateid + " int)";

    String create_compamy_master = "create table if not exists " + Table_CompanyMaster + "(" + Company_Id + " int," + Company_Name + " text,"
            + Company_Initial + " text," + Company_Pan + " text," + Company_DisplayCmp + " text," + Company_GSTNo + " text," + Company_HOCode + "text)";

    String create_bank_master = "create table if not exists " + Table_BankMaster + "(" + Bank_Id + " int," + Bank_BranchId + " int," + Bank_Name + " text,"
            + Bank_AccountNo + " text," + Bank_Status + " text," + Bank_IFSC + " text," + Bank_MICR + "text," + Bank_CustType + " text," + Bank_HoCode + " text)";

    String create_bank_branch_master = "create table if not exists " + Table_BankBranchMaster + "(" + Branch_AutoId + " int," + Branch_Id + " int,"
            + Branch_Branch + " text," + Branch_CustId + " int," + Branch_AccountNo + " text," + Branch_CBankId + " int," + Branch_CBranch + " text)";

    String create_document_master = "create table if not exists " + Table_DocumentMaster + "(" + Document_Id + " int,"
            + Document_DocName + " text," + Document_ForWhom + " text," + Document_Compulsary + " text)";

    String create_user_master = "create table if not exists " + Table_Usermaster + "(" +
            UM_RetailCustID + " int," + UM_Name + " text," + UM_Address + " text," + UM_MobileNo + " text," + UM_Status + " text," +
            UM_BranchId + " int," + UM_Email + " text," + UM_District + " text," + UM_Taluka + " text," + UM_CityId + " int," +
            UM_AreaId + " int," + UM_PANNo + " text," + UM_ImagePath + " text," + UM_HOCode + " int," + UM_GSTNo + " text," + UM_IMEINo + " text," +
            UM_isRegistered + " text," + UM_AadhaarNo + " text," + UM_PIN + " int)";


    @Override
    public void onCreate(SQLiteDatabase db) {
        Constant.showLog(create_cust_master);
        db.execSQL(create_cust_master);
        Constant.showLog(create_prod_master);
        db.execSQL(create_prod_master);
        Constant.showLog(create_si_master);
        db.execSQL(create_si_master);
        Constant.showLog(create_emp_master);
        db.execSQL(create_emp_master);
        Constant.showLog(create_ho_master);
        db.execSQL(create_ho_master);
        Constant.showLog(create_area_master);
        db.execSQL(create_area_master);
        Constant.showLog(create_city_master);
        db.execSQL(create_city_master);
        Constant.showLog(create_compamy_master);
        db.execSQL(create_compamy_master);
        Constant.showLog(create_bank_master);
        db.execSQL(create_bank_master);
        Constant.showLog(create_bank_branch_master);
        db.execSQL(create_bank_branch_master);
        Constant.showLog(create_document_master);
        db.execSQL(create_document_master);
        Constant.showLog(create_user_master);
        db.execSQL(create_user_master);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void addCustomerDetail(CustomerDetailClass cust) {
        ContentValues cv = new ContentValues();
        cv.put(CM_RetailCustID, cust.getCustID());
        cv.put(CM_Name, cust.getName());
        cv.put(CM_Address, cust.getAddress());
        cv.put(CM_MobileNo, cust.getMobile());
        cv.put(CM_Email, cust.getEmail());
        cv.put(CM_BranchId,cust.getBranchId());
        cv.put(CM_PANNo, cust.getPANno());
        cv.put(CM_GSTNo, cust.getGSTNo());
        cv.put(CM_ImagePath, cust.getImagePath());
        cv.put(CM_PIN, "-1");
        getWritableDatabase().insert(Table_Customermaster, null, cv);
    }

    public void addAreaMaster(AreaMasterClass areaClass) {
        ContentValues cv = new ContentValues();
        cv.put(Area_Auto, areaClass.getAuto());
        cv.put(Area_Id, areaClass.getId());
        cv.put(Area_Area, areaClass.getArea());
        cv.put(Area_Cityid, areaClass.getCityid());
        getWritableDatabase().insert(Table_AreaMaster, null, cv);
    }

    public void addCityMaster(CityMasterClass cityClass) {
        ContentValues cv = new ContentValues();
        cv.put(City_Auto, cityClass.getAuto());
        cv.put(City_Id, cityClass.getId());
        cv.put(City_City, cityClass.getCity());
        cv.put(City_Stateid, cityClass.getStId());
        getWritableDatabase().insert(Table_CityMaster, null, cv);
    }

    public void addHOMaster(HOMasterClass hoClass) {
        ContentValues cv = new ContentValues();
        cv.put(HO_Auto, hoClass.getAuto());
        cv.put(HO_Id, hoClass.getId());
        cv.put(HO_Name, hoClass.getName());
        cv.put(HO_City, hoClass.getCity());
        cv.put(HO_State, hoClass.getState());
        cv.put(HO_ini, hoClass.getIni());
        getWritableDatabase().insert(Table_HOMaster, null, cv);
    }

    public void addEmployeeMaster(EmployeeMasterClass empClass) {
        ContentValues cv = new ContentValues();
        cv.put(EMP_EmpId, empClass.getEmp_Id());
        cv.put(EMP_Name, empClass.getName());
        cv.put(EMP_MobNo, empClass.getMobno());
        cv.put(EMP_Adress, empClass.getAdd());
        cv.put(EMP_DesignId, empClass.getDesig_Id());
        cv.put(EMP_BranchId, empClass.getBranch_Id());
        cv.put(EMP_Status, empClass.getStatus());
        cv.put(EMP_DesignName, empClass.getDesigName());
        cv.put(EMP_CompName, empClass.getCompanyName());
        cv.put(EMP_CompInit, empClass.getCompanyInit());
        cv.put(EMP_HoCode, empClass.getHoCode());
        cv.put(EMP_PIN, empClass.getPIN());
        getWritableDatabase().insert(Table_Employee, null, cv);
    }

    public void addStockInfo(StockInfoMasterClass stockInfo) {
        ContentValues cv = new ContentValues();
        cv.put(SI_Company, stockInfo.getCompany());
        cv.put(SI_ProdId, stockInfo.getProductId());
        cv.put(SI_Color, stockInfo.getColor());
        cv.put(SI_Size, stockInfo.getSize());
        cv.put(SI_Rate, stockInfo.getRate());
        cv.put(SI_LQty, stockInfo.getLQty());
        cv.put(SI_PQty, stockInfo.getPQty());
        cv.put(SI_PackUnpack, stockInfo.getPackUnpack());
        cv.put(SI_PerPackQty, stockInfo.getPerPackQty());
        cv.put(SI_SaleRate, stockInfo.getSaleRate());
        cv.put(SI_ProductID, stockInfo.getProduct_id());
        getWritableDatabase().insert(Table_StockInfo, null, cv);
    }

    public void addProductMaster(ProductMasterClass prodClass) {
        ContentValues cv = new ContentValues();
        cv.put(PM_ProductID, prodClass.getProduct_id());
        cv.put(PM_Cat1, prodClass.getCat1());
        cv.put(PM_Cat2, prodClass.getCat2());
        cv.put(PM_Cat3, prodClass.getCat3());
        cv.put(PM_Cat4, prodClass.getCat4());
        cv.put(PM_Cat5, prodClass.getCat5());
        cv.put(PM_Cat6, prodClass.getCat6());
        cv.put(PM_Finalprod, prodClass.getFinal_prod());
        cv.put(PM_UOM, prodClass.getUom());
        cv.put(PM_SRate, prodClass.getSrate());
        cv.put(PM_PRate, prodClass.getPrate());
        cv.put(PM_BranchId, prodClass.getBranchid());
        cv.put(PM_Status, prodClass.getStatus());
        cv.put(PM_NoOfPieces, prodClass.getNoOfPices());
        cv.put(PM_CompanyId, prodClass.getCompany_Id());
        cv.put(PM_MRPRate, prodClass.getMRPRate());
        cv.put(PM_ProdId, prodClass.getProductId());
        cv.put(PM_Cat7, prodClass.getCat7());
        cv.put(PM_Cat8, prodClass.getCat8());
        cv.put(PM_MinStkQty, prodClass.getMinStkQty());
        cv.put(PM_MaxStkQty, prodClass.getMaxStkQty());
        cv.put(PM_GSTGroup, prodClass.getGSTGroup());
        cv.put(PM_HSNCode, prodClass.getHSNCode());
        cv.put(PM_Cat9, prodClass.getCat9());
        getWritableDatabase().insert(Table_ProductMaster, null, cv);
    }

    public void addCustomerMaster(CustomerDetailClass cust) {
        ContentValues cv = new ContentValues();
        cv.put(CM_RetailCustID, cust.getCustID());
        cv.put(CM_Name, cust.getName());
        cv.put(CM_Address, cust.getAddress());
        cv.put(CM_MobileNo, cust.getMobile());
        cv.put(CM_Status, cust.getStatus());
        cv.put(CM_BranchId, cust.getBranchId());
        cv.put(CM_Email, cust.getEmail());
        cv.put(CM_District, cust.getDistrict());
        cv.put(CM_Taluka, cust.getTaluka());
        cv.put(CM_CityId, cust.getCityId());
        cv.put(CM_AreaId, cust.getAreaId());
        cv.put(CM_PANNo, cust.getPANno());
        cv.put(CM_ImagePath, cust.getImagePath());
        cv.put(CM_HOCode, cust.getHOCode());
        cv.put(CM_GSTNo, cust.getGSTNo());
        cv.put(CM_IMEINo, cust.getIMEINo());
        cv.put(CM_isRegistered, cust.getIsRegistered());
        cv.put(CM_AadhaarNo, cust.getAadharNo());
        cv.put(CM_PIN, cust.getPIN());
        getWritableDatabase().insert(Table_Customermaster, null, cv);
    }

    public void addCompanyMaster(CompanyMasterClass company) {
        ContentValues cv = new ContentValues();
        cv.put(Company_Id, company.companyId);
        cv.put(Company_Name, company.getCompanyName());
        cv.put(Company_Initial, company.getCompanyInitial());
        cv.put(Company_Pan, company.getCompanyPan());
        cv.put(Company_DisplayCmp, company.getDisplayCmp());
        cv.put(Company_GSTNo, company.getGSTNo());
        cv.put(Company_HOCode, company.getHOCode());
        getWritableDatabase().insert(Table_CompanyMaster, null, cv);
    }

    public void addBankMaster(BankMasterClass bank) {
        ContentValues cv = new ContentValues();
        cv.put(Bank_Id, bank.getBankId());
        cv.put(Bank_BranchId, bank.getBranchId());
        cv.put(Bank_Name, bank.getBankName());
        cv.put(Bank_AccountNo, bank.getAccountNo());
        cv.put(Bank_Status, bank.getStatus());
        cv.put(Bank_IFSC, bank.getIFSC());
        cv.put(Bank_MICR, bank.getMICR());
        cv.put(Bank_CustType, bank.getCustType());
        cv.put(Bank_HoCode, bank.getHoCode());
        getWritableDatabase().insert(Table_BankMaster, null, cv);
    }

    public void addBankBranchMaster(BankBranchMasterClass branchClass) {
        ContentValues cv = new ContentValues();
        cv.put(Branch_AutoId,branchClass.getAutoid());
        cv.put(Branch_Id,branchClass.getId());
        cv.put(Branch_Branch,branchClass.getBranch());
        cv.put(Branch_CustId,branchClass.getCustid());
        cv.put(Branch_AccountNo,branchClass.getAccountNo());
        cv.put(Branch_CBankId,branchClass.getcBankid());
        cv.put(Branch_CBranch,branchClass.getcBranch());
        getWritableDatabase().insert(Table_BankBranchMaster,null,cv);
    }

    public void addDocumentMaster(DocumentMasterClass documentClass) {
        ContentValues cv = new ContentValues();
        cv.put(Document_Id,documentClass.getId());
        cv.put(Document_DocName,documentClass.getDocName());
        cv.put(Document_ForWhom,documentClass.getForWhom());
        cv.put(Document_Compulsary,documentClass.getCompulsary());
        getWritableDatabase().insert(Table_DocumentMaster,null,cv);
    }

    //name,Address,Mobile,Email,Panno,ImagePath,GSTNo,AadharNo
    //TODO ALL FIELDS OF USER TO BE ADDED
    public void addUserDetail(UserClass user) {
        ContentValues cv = new ContentValues();
        cv.put(UM_RetailCustID, user.getCustID());
        cv.put(UM_Name, user.getName());
        cv.put(UM_Address, user.getAddress());
        cv.put(UM_MobileNo, user.getMobile());
        cv.put(UM_Email, user.getEmail());
        cv.put(UM_PANNo, user.getPANno());
        cv.put(UM_GSTNo, user.getGSTNo());
        cv.put(UM_ImagePath, user.getImagePath());
        cv.put(UM_PIN, "-1");
        getWritableDatabase().insert(Table_Usermaster, null, cv);
    }

    public ArrayList<CustomerDetailClass> getCustomerDetail() {
        ArrayList<CustomerDetailClass> list = new ArrayList<>();
        String str = "select * from " + Table_Usermaster;
        Cursor res = getWritableDatabase().rawQuery(str, null);
        if (res.moveToFirst()) {
            do {
                CustomerDetailClass custClass = new CustomerDetailClass();
                custClass.setCustID(res.getInt(res.getColumnIndex(UM_RetailCustID)));
                custClass.setName(res.getString(res.getColumnIndex(UM_Name)));
                custClass.setAddress(res.getString(res.getColumnIndex(UM_Address)));
                custClass.setMobile(res.getString(res.getColumnIndex(UM_MobileNo)));
                custClass.setEmail(res.getString(res.getColumnIndex(UM_Email)));
                custClass.setPANno(res.getString(res.getColumnIndex(UM_PANNo)));
                custClass.setGSTNo(res.getString(res.getColumnIndex(UM_GSTNo)));
                custClass.setImagePath(res.getString(res.getColumnIndex(UM_ImagePath)));
                list.add(custClass);
            } while (res.moveToNext());
        }
        res.close();
        return list;
    }

    public String getCustPIN(String custid) {
        String pin = "-1";
        String str = "select " + UM_PIN + " from " + Table_Usermaster + " where " + UM_RetailCustID + "=" + custid;
        Cursor res = getWritableDatabase().rawQuery(str, null);
        if (res.moveToFirst()) {
            do {
                pin = res.getString(res.getColumnIndex(UM_PIN));
            } while (res.moveToNext());
        }
        res.close();
        return pin;
    }

    public List<String> checkPINUnsetID() {
        List<String> list = new ArrayList<>();
        String str = "select " + UM_PIN + " from " + Table_Usermaster + " where " + UM_PIN + "=-1";
        Cursor res = getWritableDatabase().rawQuery(str, null);
        if (res.moveToFirst()) {
            do {
                list.add(res.getString(res.getColumnIndex(UM_PIN)));
            } while (res.moveToNext());
        }
        res.close();
        return list;
    }

    public void updatePIN(String custid, String pin) {
        ContentValues cv = new ContentValues();
        cv.put(UM_PIN, pin);
        getWritableDatabase().update(Table_Usermaster, cv, UM_RetailCustID + "=?", new String[]{custid});
    }

    // public Cursor getAreaName(String cityid){
    public Cursor getAreaName() {
        // String str = "select "+Area_Area+" from "+Table_AreaMaster+" where "+Area_Cityid+" = "+cityid;
        String str = "select " + Area_Area + " from " + Table_AreaMaster + " where " + Area_Cityid + " = 1";
        Constant.showLog(str);
        return getWritableDatabase().rawQuery(str, null);
    }

    public Cursor getAreaId(String area) {
        String str = "select " + Area_Id + " from " + Table_AreaMaster + " where " + Area_Area + " = '" + area + "'";
        Constant.showLog(str);
        return getWritableDatabase().rawQuery(str, null);
    }

    public Cursor getCustomerName(String areaid) {
        String str = "select " + CM_Name + " from " + Table_Customermaster + " where " + CM_AreaId + " = " + areaid;
        Constant.showLog(str);
        return getWritableDatabase().rawQuery(str, null);
    }

    public Cursor getBankName() {
        String str = "select " + Bank_Name + " from " + Table_BankMaster;
        Constant.showLog(str);
        return getWritableDatabase().rawQuery(str, null);
    }

    public Cursor getBranchName(){
       // String str = "select "+Branch_CBranch+" from "+Table_BankBranchMaster+" where "+Branch_CBranch+"  <> ''";
        String str = "select "+Branch_CBranch+" from "+Table_BankBranchMaster;
        Constant.showLog(str);
        return getWritableDatabase().rawQuery(str,null);
    }

    public void createBankMaster() {
        String str = "create table if not exists " + Table_BankMaster + "(" + Bank_Id + " int," + Bank_BranchId + " int," + Bank_Name + " text,"
                + Bank_AccountNo + " text," + Bank_Status + " text," + Bank_IFSC + " text" + Bank_MICR + "text," + Bank_CustType + " text," + Bank_HoCode + " text)";
        Constant.showLog(str);
        getWritableDatabase().execSQL(str);
    }
    public void createCompanyMaster() {
        String str = "create table if not exists " + Table_CompanyMaster + "(" + Company_Id + " int," + Company_Name + " text,"
                + Company_Initial + " text," + Company_Pan + " text," + Company_DisplayCmp + " text," + Company_GSTNo + " text" + Company_HOCode + "text)";
        Constant.showLog(str);
        getWritableDatabase().execSQL(str);
    }
    public void createBankBranchMaster() {
        String str = "create table if not exists " + Table_BankBranchMaster + "(" + Branch_AutoId + " int," + Branch_Id + " int,"
                + Branch_Branch + " text," + Branch_CustId + " int," + Branch_AccountNo + " text," + Branch_CBankId + " int," + Branch_CBranch + " text)";
        Constant.showLog(str);
        getWritableDatabase().execSQL(str);
    }
    public void createDocumentMaster() {
        String str = "create table if not exists " + Table_DocumentMaster + "(" + Document_Id + " int,"
                + Document_DocName + " text," + Document_ForWhom + " text," + Document_Compulsary + " text)";
        Constant.showLog(str);
        getWritableDatabase().execSQL(str);
    }

    public Cursor getSubCategory(String catName){
        String str = "select distinct "+PM_Cat2+" from "+Table_ProductMaster+" where "+PM_Cat9+"='"+catName+"' order by "+PM_Cat2;
        return getWritableDatabase().rawQuery(str,null);
    }


    public Cursor getIdOfDocType(String docName){
        String str = "select "+Document_Id+" from "+Table_DocumentMaster+" where "+Document_DocName+" = '"+docName+"'";
        return getWritableDatabase().rawQuery(str,null);
    }

    public Cursor getUserDetails(){
        String str = "select * from "+Table_Usermaster;
        return getWritableDatabase().rawQuery(str,null);
    }

    /************sneha changes25oct**********************/
    public Cursor getDocName(){
        String str = "select "+Document_DocName+" from "+Table_DocumentMaster;
        return getWritableDatabase().rawQuery(str,null);
    }

    public void deleteTable(String tableName) {
        getWritableDatabase().execSQL("delete from " + tableName);
    }

    public Cursor getFinalProduct(String cat2,String cat9) {
        String str = "select distinct "+PM_Finalprod +" from "+ Table_ProductMaster+" where "+PM_Cat2+"='"+cat2+"' and "+PM_Cat9+"='"+cat9+"'";
        return getWritableDatabase().rawQuery(str,null);
    }

}


