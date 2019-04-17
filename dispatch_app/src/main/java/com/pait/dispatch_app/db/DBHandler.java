package com.pait.dispatch_app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.pait.dispatch_app.constant.Constant;
import com.pait.dispatch_app.model.CompanyMasterClass;
import com.pait.dispatch_app.model.DispatchMasterClass;
import com.pait.dispatch_app.model.EmployeeMasterClass;
import com.pait.dispatch_app.parse.UserClass;

import java.util.ArrayList;
import java.util.List;

// Created by lnb on 8/11/2016.

public class DBHandler extends SQLiteOpenHelper {

    public static final String Database_Name = "SmartGST.db";
    //TODO: Check DB Version
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
    public static final String CM_PartyName = "PartyName";
    public static final String CM_ImagePath = "ImagePath";
    public static final String CM_HOCode = "HOCode";
    public static final String CM_GSTNo = "GSTNo";
    public static final String CM_IMEINo = "IMEINo";
    public static final String CM_isRegistered = "isRegistered";
    public static final String CM_AadhaarNo = "AadhaarNo";
    public static final String CM_PIN = "PIN";
    public static final String CM_Discount = "Discount";

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
    public static final String PM_Cat10 = "Cat10";
    public static final String PM_HKHO = "HKHO";
    public static final String PM_HKRD = "HKRD";
    public static final String PM_HANR = "HANR";
    public static final String PM_MarkUp = "SalesMarkUp";
    public static final String PM_MarkDown = "SalesMarkDown";

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
    public static final String Company_Company_Add = "Company_Add";
    public static final String Company_Company_Phno = "Company_Phno";
    public static final String Company_Company_Email = "Company_Email";
    public static final String Company_MobileNo = "MobileNo";
    public static final String Company_Company_Phone2 = "Company_Phone2";
    public static final String Company_Mobileno2 = "Mobileno2";

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
    public static final String UM_PartyName = "PartyName";
    public static final String UM_ImagePath = "ImagePath";
    public static final String UM_HOCode = "HOCode";
    public static final String UM_GSTNo = "GSTNo";
    public static final String UM_IMEINo = "IMEINo";
    public static final String UM_isRegistered = "isRegistered";
    public static final String UM_AadhaarNo = "AadhaarNo";
    public static final String UM_PIN = "PIN";
    public static final String UM_PINText = "PINTEXT";

    public static final String Table_AllRequiredSizesDesigns = "AllRequiredSizesdesigns";
    public static final String ARSD_Productid = "Productid";
    public static final String ARSD_Cat1 = "Cat1";
    public static final String ARSD_Cat2 = "Cat2";
    public static final String ARSD_Cat3 = "Cat3";
    public static final String ARSD_Cat4 = "Cat4";
    public static final String ARSD_Cat5 = "Cat5";
    public static final String ARSD_Cat6 = "Cat6";
    public static final String ARSD_Final_prod = "Final_prod";
    public static final String ARSD_Uom = "Uom";
    public static final String ARSD_Vat = "Vat";
    public static final String ARSD_DesignNo = "DesignNo";
    public static final String ARSD_Colour = "Colour";
    public static final String ARSD_SizeGroup = "SizeGroup";
    public static final String ARSD_typ = "typ";
    public static final String ARSD_SizeFrom = "SizeFrom";
    public static final String ARSD_SizeTo = "SizeTo";
    public static final String ARSD_ActualInw = "ActualInw";
    public static final String ARSD_GSTGroup = "GSTGroup";
    public static final String ARSD_InOutType = "InOutType";
    public static final String ARSD_Total = "Total";
    public static final String ARSD_HashCode = "HashCode";
    public static final String ARSD_ImageName = "ImageName";

    public static final String Table_CustomerOrder = "CustomerOrder";
    public static final String CO_Auto = "Auto";
    public static final String CO_BranchId = "BranchId";
    public static final String CO_Productid = "Productid";
    public static final String CO_SizeGroup = "SizeGroup";
    public static final String CO_RequiredSize = "RequiredSize";
    public static final String CO_PerPackQty = "PerPackQty";
    public static final String CO_Color = "Color";
    public static final String CO_HashCode = "HashCode";
    public static final String CO_Rate = "Rate";
    public static final String CO_MRP = "MRP";
    public static final String CO_Qty = "Qty";
    public static final String CO_LooseQty = "LooseQty";
    public static final String CO_ActLooseQty = "ActLooseQty";
    public static final String CO_Amount = "Amount";
    public static final String CO_LoosePackTyp = "LoosePackTyp";
    public static final String CO_TotalAmt = "TotalAmt";
    public static final String CO_PendingLooseQty = "PendingLooseQty";
    public static final String CO_NetAmt = "NetAmt";
    public static final String CO_AmtAfterDisc = "AmtAfterDisc";
    public static final String CO_GSTPer = "GSTPer";
    public static final String CO_GSTAmt = "GSTAmnt";
    public static final String CO_CGSTAmt = "CGSTAmt";
    public static final String CO_SGSTAmt = "SGSTAmt";
    public static final String CO_IGSTAmt = "IGSTAmt";
    public static final String CO_CGSTPer = "CGSTPer";
    public static final String CO_SGSTPer = "SGSTPer";
    public static final String CO_CESSPer = "CESSPer";
    public static final String CO_CESSAmt = "CESSAmt";
    public static final String CO_DiscPer = "DiscPer";
    public static final String CO_DiscAmt = "DiscAmt";
    public static final String CO_OrderType = "OrderType";
    public static final String CO_AvailQty = "AvailQty";
    public static final String CO_Prodid = "ProdId";

    public static final String Table_GSTMASTER = "GSTMASTER";
    public static final String GST_Auto = "Auto";
    public static final String GST_GroupNm = "GroupNm";
    public static final String GST_Status = "Status";
    public static final String GST_GSTPer = "GSTPer";
    public static final String GST_CGSTPer = "CGSTPer";
    public static final String GST_SGSTPer = "SGSTPer";
    public static final String GST_CESSPer = "CESSPer";
    public static final String GST_CGSTSHARE = "CGSTSHARE";
    public static final String GST_SGSTSHARE = "SGSTSHARE";

    public static final String Table_TrackCustomerOrder = "TrackCustomerOrder";
    public static final String TCO_Auto = "Auto";
    public static final String TCO_Productid = "Productid";
    public static final String TCO_SizeGroup = "SizeGroup";
    public static final String TCO_Color = "Color";
    public static final String TCO_HashCode = "HashCode";
    public static final String TCO_Rate = "Rate";
    public static final String TCO_MRP = "MRP";
    public static final String TCO_OrderQty = "OrderQty";
    public static final String TCO_LoosePackTyp = "LoosePackTyp";
    public static final String TCO_CreditApp = "CreditApp";
    public static final String TCO_AllotedToPck = "AllotedToPck";
    public static final String TCO_TaxInvMade = "TaxInvMade";
    public static final String TCO_InvNo = "InvNo";
    public static final String TCO_InvDate = "InvDate";
    public static final String TCO_InvAmt = "InvAmnt";
    public static final String TCO_Transporter = "Transporter";
    public static final String TCO_TransporterNo = "TransporterNo";
    public static final String TCO_Prodid = "ProdId";
    public static final String TCO_InvQty = "InvQty";
    public static final String TCO_CancelQty = "CancelQty";
    public static final String TCO_Status = "Status";

    public static final String Table_SizeDesignMastDet = "SizeDesignMastDet";
    public static final String SDMD_Auto = "Auto";
    public static final String SDMD_ProductId = "Productid";
    public static final String SDMD_DesignNo = "DesignNo";
    public static final String SDMD_SizeGroupFrom = "SizeGroupFrom";
    public static final String SDMD_SizeGroupTo = "SizeGroupTo";
    public static final String SDMD_Total = "Total";
    public static final String SDMD_SizeGroup = "SizeGroup";
    public static final String SDMD_Colour = "Colour";
    public static final String SDMD_Size = "Size";
    public static final String SDMD_Qty = "Qty";

    public static final String Table_DispatchMaster = "DispatchMaster";
    public static final String DM_Auto = "Auto";
    public static final String DM_DispatchBy = "DispatchBy";
    public static final String DM_PONO = "PONO";
    public static final String DM_TotalQty = "TotalQty";
    public static final String DM_Emp_Id = "Emp_Id";
    public static final String DM_Emp_Name = "Emp_Name";
    public static final String DM_TransId = "TransId";
    public static final String DM_Transporter = "Transporter";
    public static final String DM_CustId = "CustId";
    public static final String DM_PartyName = "PartyName";
    public static final String DM_branchid = "branchid";
    public static final String DM_DispatchCmp = "DispatchCmp";
    public static final String DM_DispatchDate = "DispatchDate";


    public DBHandler(Context context) {
        super(context, Database_Name, null, Database_Version);
    }

    public DBHandler(Context context, String path) {
        super(context, path, null, Database_Version);
    }

    private String create_cust_master = "create table if not exists " + Table_Customermaster + "(" +
            CM_RetailCustID + " int," + CM_Name + " text," + CM_Address + " text," + CM_MobileNo + " text," + CM_Status + " text," +
            CM_BranchId + " int," + CM_Email + " text," + CM_District + " text," + CM_Taluka + " text," + CM_CityId + " int," +
            CM_AreaId + " int," + CM_PANNo + " text," + CM_PartyName + " text," + CM_ImagePath + " text," + CM_HOCode + " int," + CM_GSTNo + " text," + CM_IMEINo + " text," +
            CM_isRegistered + " text," + CM_AadhaarNo + " text," + CM_PIN + " int," + CM_Discount + " float)";

    private String create_prod_master = "create table if not exists " + Table_ProductMaster + "(" +
            PM_ProductID + " int," + PM_Cat1 + " text," + PM_Cat2 + " text," + PM_Cat3 + " text," + PM_Cat4 + " text," + PM_Cat5 + " text," + PM_Cat6 + " text," +
            PM_Finalprod + " text," + PM_UOM + " text," + PM_SRate + " text," + PM_PRate + " text," + PM_BranchId + " int," + PM_Status + " text," + PM_NoOfPieces + " int," +
            PM_CompanyId + " int," + PM_MRPRate + " text," + PM_ProdId + " text," + PM_Cat7 + " text," + PM_Cat8 + " text," + PM_MinStkQty + " int," +
            PM_MaxStkQty + " int," + PM_GSTGroup + " text," + PM_HSNCode + " text," + PM_Cat9 + " text," + PM_Cat10 + " text," + PM_HKHO + " int," + PM_HKRD + " int," + PM_HANR + " int,"+
            PM_MarkUp+" text,"+ PM_MarkDown+" text)";

    private String create_si_master = "create table if not exists " + Table_StockInfo + "(" +
            SI_Company + " text," + SI_ProdId + " text," + SI_Color + " text," + SI_Size + " text," + SI_Rate + " text," +
            SI_LQty + " int," + SI_PQty + " int," + SI_PackUnpack + " text," + SI_PerPackQty + " int," + SI_SaleRate + " text," +
            SI_ProductID + " int)";

    private String create_emp_master = "create table if not exists " + Table_Employee + "(" +
            EMP_EmpId + " int," + EMP_Name + " text," + EMP_MobNo + " text," + EMP_Adress + " text," + EMP_DesignId + " int," + EMP_BranchId + " int," +
            EMP_Status + " text," + EMP_DesignName + " text," + EMP_CompName + " text," + EMP_CompInit + " text," + EMP_HoCode + " int," + EMP_PIN + " text)";

    private String create_ho_master = "create table if not exists " + Table_HOMaster + "(" +
            HO_Auto + " int," + HO_Id + " int," + HO_Name + " text," + HO_City + " int," + HO_State + " int," + HO_ini + " text)";

    private String create_area_master = "create table if not exists " + Table_AreaMaster + "(" +
            Area_Auto + " int," + Area_Id + " int," + Area_Area + " text," + Area_Cityid + " int)";

    private String create_city_master = "create table if not exists " + Table_CityMaster + "(" +
            City_Auto + " int," + City_Id + " int," + City_City + " text," + City_Stateid + " int)";

    private String create_company_master = "create table if not exists " + Table_CompanyMaster + "(" + Company_Id + " int," + Company_Name + " text,"
            + Company_Initial + " text," + Company_Pan + " text," + Company_DisplayCmp + " text," + Company_GSTNo + " text," + Company_HOCode + " text,"+
            Company_Company_Add + " text," + Company_Company_Phno + " text," + Company_Company_Email + " text," +
            Company_MobileNo + " text," + Company_Company_Phone2 + " text," + Company_Mobileno2 + " text)";

    private String create_bank_master = "create table if not exists " + Table_BankMaster + "(" + Bank_Id + " int," + Bank_BranchId + " int," + Bank_Name + " text,"
            + Bank_AccountNo + " text," + Bank_Status + " text," + Bank_IFSC + " text," + Bank_MICR + " text," + Bank_CustType + " text," + Bank_HoCode + " text)";

    private String create_bank_branch_master = "create table if not exists " + Table_BankBranchMaster + "(" + Branch_AutoId + " int," + Branch_Id + " int,"
            + Branch_Branch + " text," + Branch_CustId + " int," + Branch_AccountNo + " text," + Branch_CBankId + " int," + Branch_CBranch + " text)";

    private String create_document_master = "create table if not exists " + Table_DocumentMaster + "(" + Document_Id + " int,"
            + Document_DocName + " text," + Document_ForWhom + " text," + Document_Compulsary + " text)";

    private String create_user_master = "create table if not exists " + Table_Usermaster + "(" +
            UM_RetailCustID + " int," + UM_Name + " text," + UM_Address + " text," + UM_MobileNo + " text," + UM_Status + " text," +
            UM_BranchId + " int," + UM_Email + " text," + UM_District + " text," + UM_Taluka + " text," + UM_CityId + " int," +
            UM_AreaId + " int," + UM_PANNo + " text," + UM_PartyName + " text," + UM_ImagePath + " text," + UM_HOCode + " int," + UM_GSTNo + " text," + UM_IMEINo + " text," +
            UM_isRegistered + " text," + UM_AadhaarNo + " text," + UM_PIN + " int,"+UM_PINText+" text)";

    private String create_arsd_master = "create table if not exists " + Table_AllRequiredSizesDesigns + "(" +
            ARSD_Productid + " int," + ARSD_Cat1 + " text," + ARSD_Cat2 + " text," + ARSD_Cat3 + " text," +
            ARSD_Cat4 + " text," + ARSD_Cat5 + " text," + ARSD_Cat6 + " text," + ARSD_Final_prod + " text," +
            ARSD_Uom + " text," + ARSD_Vat + " text," + ARSD_DesignNo + " text," + ARSD_Colour + " text," +
            ARSD_SizeGroup + " text," + ARSD_typ + " text," + ARSD_SizeFrom + " int," + ARSD_SizeTo + " int," +
            ARSD_ActualInw + " text," + ARSD_GSTGroup + " text," + ARSD_InOutType + " text," + ARSD_Total + " int," +
            ARSD_HashCode + " text," + ARSD_ImageName + " text)";

    private String create_custorder_table = "create table if not exists " + Table_CustomerOrder + "(" + CO_Auto + " int," +
            CO_BranchId + " int," + CO_Productid + " int," + CO_SizeGroup + " text," + CO_RequiredSize + " text," +
            CO_PerPackQty + " int," + CO_Color + " text," + CO_HashCode + " text," + CO_Rate + " text," + CO_MRP + " text," + CO_Qty + " int," +
            CO_LooseQty + " int," + CO_ActLooseQty + " int," + CO_Amount + " text," + CO_LoosePackTyp + " text," + CO_PendingLooseQty + " int," + CO_TotalAmt + " text," +
            CO_NetAmt + " text," + CO_AmtAfterDisc + " text," + CO_GSTPer + " text," + CO_GSTAmt + " text," + CO_CGSTAmt + " text," +
            CO_SGSTAmt + " text," + CO_IGSTAmt + " text," + CO_CGSTPer + " text," +CO_SGSTPer + " text," + CO_CESSPer + " text," +
            CO_CESSAmt + " text," + CO_DiscPer + " text," + CO_DiscAmt + " text," + CO_OrderType + " text," + CO_AvailQty + " int,"+CO_Prodid+" text)";

    private String create_gstmaster_table = "create table if not exists " + Table_GSTMASTER + "(" + GST_Auto + " int," +
            GST_GroupNm + " text," + GST_Status + " text," + GST_GSTPer + " text," + GST_CGSTPer + " text," +
            GST_SGSTPer + " text," + GST_CESSPer + " text," + GST_CGSTSHARE + " text," + GST_SGSTSHARE + " text)";

    private String create_trackcustomerorder_table = "create table if not exists "+Table_TrackCustomerOrder+"("+TCO_Auto+" int,"+TCO_Productid+" int,"+TCO_SizeGroup+" text,"+
            TCO_Color+" text,"+TCO_HashCode+" text,"+TCO_Rate+" text,"+TCO_MRP+" text,"+TCO_OrderQty+" int,"+TCO_LoosePackTyp+" text,"+TCO_CreditApp+" text,"+TCO_AllotedToPck+" text,"+TCO_TaxInvMade+" text,"+
            TCO_InvNo+" text,"+TCO_InvDate+" text,"+TCO_InvAmt+" text,"+TCO_Transporter+" text,"+TCO_TransporterNo+" text,"+TCO_Prodid+" text,"+TCO_InvQty+" int,"+TCO_CancelQty+" int,"+TCO_Status+" text)";

    private String create_sizedesignmastdet_table = "create table if not exists "+Table_SizeDesignMastDet+"("+SDMD_Auto+" int,"+SDMD_ProductId+" int,"+SDMD_DesignNo+" text,"+SDMD_SizeGroupFrom+" int,"
            +SDMD_SizeGroupTo+" int,"+SDMD_Total+" int,"+SDMD_SizeGroup+" text,"+SDMD_Colour+" text,"+SDMD_Size+" int,"+SDMD_Qty+" int)";

    private String create_dispatchmaster_table = "create table if not exists "+Table_DispatchMaster
            +"("+ DM_Auto + " int,"+ DM_DispatchBy + " int," + DM_PONO + " text,"+ DM_TotalQty + " int,"+
            DM_Emp_Id + " int,"+ DM_Emp_Name + " text," +DM_TransId + " int,"+ DM_Transporter + " text,"+ DM_CustId + " int,"+
            DM_PartyName + " text,"+ DM_branchid + " int,"+ DM_DispatchCmp + " int," +
            DM_DispatchDate + " text)";

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Constant.showLog(create_cust_master);
        //db.execSQL(create_cust_master);
        //Constant.showLog(create_prod_master);
        //db.execSQL(create_prod_master);
        //Constant.showLog(create_si_master);
        //db.execSQL(create_si_master);
        Constant.showLog(create_emp_master);
        db.execSQL(create_emp_master);
        Constant.showLog(create_ho_master);
        db.execSQL(create_ho_master);
        //Constant.showLog(create_area_master);
        //db.execSQL(create_area_master);
        //Constant.showLog(create_city_master);
        //db.execSQL(create_city_master);
        Constant.showLog(create_company_master);
        db.execSQL(create_company_master);
        //Constant.showLog(create_bank_master);
        //db.execSQL(create_bank_master);
        //Constant.showLog(create_bank_branch_master);
        //db.execSQL(create_bank_branch_master);
        //Constant.showLog(create_document_master);
        //db.execSQL(create_document_master);
        Constant.showLog(create_user_master);
        db.execSQL(create_user_master);
        //Constant.showLog(create_arsd_master);
        //db.execSQL(create_arsd_master);
        //Constant.showLog(create_custorder_table);
        //db.execSQL(create_custorder_table);
        //Constant.showLog(create_gstmaster_table);
        //db.execSQL(create_gstmaster_table);
        //Constant.showLog(create_trackcustomerorder_table);
        //db.execSQL(create_trackcustomerorder_table);
        //Constant.showLog(create_sizedesignmastdet_table);
        //db.execSQL(create_sizedesignmastdet_table);
        Constant.showLog(create_dispatchmaster_table);
        db.execSQL(create_dispatchmaster_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.disableWriteAheadLogging();
    }

    public void addUserDetail(UserClass user) {
        ContentValues cv = new ContentValues();
        cv.put(UM_RetailCustID, user.getCustID());
        cv.put(UM_Name, user.getName());
        cv.put(UM_Address, user.getAddress());
        cv.put(UM_MobileNo, user.getMobile());
        cv.put(UM_Email, user.getEmail());
        cv.put(UM_PANNo, user.getPANno());
        cv.put(UM_PartyName, user.getPartyName());
        cv.put(UM_GSTNo, user.getGSTNo());
        cv.put(UM_ImagePath, user.getImagePath());
        cv.put(UM_Status, user.getStatus());
        cv.put(UM_District, user.getDistrict());
        cv.put(UM_Taluka, user.getTaluka());
        cv.put(UM_CityId, user.getCityId());
        cv.put(UM_AreaId, user.getAreaId());
        cv.put(UM_HOCode, user.getHOCode());
        cv.put(UM_BranchId, user.getBranchId());
        cv.put(UM_IMEINo, user.getIMEINo());
        cv.put(UM_isRegistered, user.getIsRegistered());
        cv.put(UM_AadhaarNo, user.getAadharNo());
        cv.put(UM_PIN, user.getPIN());
        cv.put(UM_PINText, user.getPintext());
        getWritableDatabase().insert(Table_Usermaster, null, cv);
    }

    public void deleteTable(String tableName){
        getWritableDatabase().delete(tableName,null,null);
    }

    public ArrayList<UserClass> getUserDetail() {
        ArrayList<UserClass> list = new ArrayList<>();
        String str = "select * from " + Table_Usermaster;
        Cursor res = getWritableDatabase().rawQuery(str, null);
        if (res.moveToFirst()) {
            do {
                UserClass userClass = new UserClass();
                userClass.setCustID(res.getInt(res.getColumnIndex(UM_RetailCustID)));
                userClass.setName(res.getString(res.getColumnIndex(UM_Name)));
                userClass.setAddress(res.getString(res.getColumnIndex(UM_Address)));
                userClass.setMobile(res.getString(res.getColumnIndex(UM_MobileNo)));
                userClass.setEmail(res.getString(res.getColumnIndex(UM_Email)));
                userClass.setPANno(res.getString(res.getColumnIndex(UM_PANNo)));
                userClass.setPartyName(res.getString(res.getColumnIndex(UM_PartyName)));
                userClass.setGSTNo(res.getString(res.getColumnIndex(UM_GSTNo)));
                userClass.setImagePath(res.getString(res.getColumnIndex(UM_ImagePath)));
                userClass.setStatus(res.getString(res.getColumnIndex(UM_Status)));
                userClass.setDistrict(res.getString(res.getColumnIndex(UM_District)));
                userClass.setTaluka(res.getString(res.getColumnIndex(UM_Taluka)));
                userClass.setCityId(res.getInt(res.getColumnIndex(UM_CityId)));
                userClass.setAreaId(res.getInt(res.getColumnIndex(UM_AreaId)));
                userClass.setBranchId(res.getInt(res.getColumnIndex(UM_BranchId)));
                userClass.setHOCode(res.getInt(res.getColumnIndex(UM_HOCode)));
                userClass.setIMEINo(res.getString(res.getColumnIndex(UM_IMEINo)));
                userClass.setIsRegistered(res.getString(res.getColumnIndex(UM_isRegistered)));
                userClass.setAadharNo(res.getString(res.getColumnIndex(UM_AadhaarNo)));
                userClass.setPIN(res.getString(res.getColumnIndex(UM_PIN)));
                userClass.setPintext(res.getString(res.getColumnIndex(UM_PINText)));
                list.add(userClass);
            } while (res.moveToNext());
        }
        res.close();
        return list;
    }

    public String getCustPIN(String custid) {
        String pin = "-1";
        String str = "select " + UM_PINText + " from " + Table_Usermaster + " where " + UM_RetailCustID + "=" + custid;
        Cursor res = getWritableDatabase().rawQuery(str, null);
        if (res.moveToFirst()) {
            do {
                pin = res.getString(res.getColumnIndex(UM_PINText));
            } while (res.moveToNext());
        }
        res.close();
        return pin;
    }

    public void updatePIN(String custid, String pin) {
        ContentValues cv = new ContentValues();
        cv.put(UM_PINText, pin);
        getWritableDatabase().update(Table_Usermaster, cv, UM_RetailCustID + "=?", new String[]{custid});
    }

    public List<String> checkPINUnsetID() {
        List<String> list = new ArrayList<>();
        String str = "select " + UM_PINText + " from " + Table_Usermaster + " where " + UM_PINText + "=-1";
        Cursor res = getWritableDatabase().rawQuery(str, null);
        if (res.moveToFirst()) {
            do {
                list.add(res.getString(res.getColumnIndex(UM_PINText)));
            } while (res.moveToNext());
        }
        res.close();
        return list;
    }

    public void addEmployeeMaster(List<EmployeeMasterClass> list ) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        for (EmployeeMasterClass empClass : list) {
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
            db.insert(Table_Employee, null, cv);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public int getMaxEmpId() {
        int a = 0;
        String str = "select max(" + EMP_EmpId + ") from " + Table_Employee;
        Cursor res = getWritableDatabase().rawQuery(str, null);
        if (res.moveToFirst()) {
            a = res.getInt(0);
        }
        res.close();
        return a;
    }

    public int getMaxCompId() {
        int a = 0;
        String str = "select max(" + Company_Id + ") from " + Table_CompanyMaster;
        Cursor res = getWritableDatabase().rawQuery(str, null);
        if (res.moveToFirst()) {
            a = res.getInt(0);
        }
        res.close();
        return a;
    }

    public int getMaxAuto() {
        int a = 0;
        String str = "select max(" + DM_Auto+ ") from " + Table_DispatchMaster;
        Cursor res = getWritableDatabase().rawQuery(str, null);
        if (res.moveToFirst()) {
            a = res.getInt(0);
        }
        res.close();
        return a;
    }

    public void addDispatchMaster(List<DispatchMasterClass> list ) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        for (DispatchMasterClass dm : list) {
            ContentValues cv = new ContentValues();
            cv.put(DM_Auto, dm.getAuto());
            cv.put(DM_DispatchBy, dm.getDispatchBy());
            cv.put(DM_PONO, dm.getPONO());
            cv.put(DM_TotalQty, dm.getTotalQty());
            cv.put(DM_Emp_Id, dm.getEmp_Id());
            cv.put(DM_Emp_Name, dm.getEmp_Name());
            cv.put(DM_TransId, dm.getTransId());
            cv.put(DM_Transporter, dm.getTransporter());
            cv.put(DM_CustId, dm.getCustId());
            cv.put(DM_PartyName, dm.getPartyName());
            cv.put(DM_branchid, dm.getBranchid());
            cv.put(DM_DispatchCmp, dm.getDispatchCmp());
            cv.put(DM_DispatchDate, dm.getDispatchDate());
            db.insert(Table_DispatchMaster, null, cv);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public void addCompanyMaster(CompanyMasterClass company) {
        ContentValues cv = new ContentValues();
        cv.put(Company_Id, company.getCompanyId());
        cv.put(Company_Name, company.getCompanyName());
        cv.put(Company_Initial, company.getCompanyInitial());
        cv.put(Company_Pan, company.getCompanyPan());
        cv.put(Company_DisplayCmp, company.getDisplayCmp());
        cv.put(Company_GSTNo, company.getGSTNo());
        cv.put(Company_HOCode, company.getHOCode());
        cv.put(Company_Company_Add, company.getCompany_Add());
        cv.put(Company_Company_Phno, company.getCompany_Phno());
        cv.put(Company_Company_Email, company.getCompany_Email());
        cv.put(Company_MobileNo, company.getMobileNo());
        cv.put(Company_Company_Phone2, company.getCompany_Phone2());
        cv.put(Company_Mobileno2, company.getMobileno2());
        getWritableDatabase().insert(Table_CompanyMaster, null, cv);
    }

    public Cursor getDPCenter(int hoCode){
        String str = "select * from "+Table_CompanyMaster + " where "+Company_HOCode+"="+hoCode +" order by "+Company_DisplayCmp;
        Constant.showLog(str);
        return getWritableDatabase().rawQuery(str,null);
    }

    public Cursor getPartyName(){
        String str = "select distinct "+ DM_PartyName+" from "+Table_DispatchMaster +" order by "+DM_PartyName;
        Constant.showLog(str);
        return getWritableDatabase().rawQuery(str,null);
    }

    public Cursor getPONo(String partyName){
        String str = "select * from "+Table_DispatchMaster + " where "+ DM_PartyName+"='"+partyName +"' order by "+DM_PONO;
        Constant.showLog(str);
        return getWritableDatabase().rawQuery(str,null);
    }

    public Cursor getEmpName(int hoCode) {
        String str = "select " + EMP_EmpId + "," + EMP_Name + " from " + Table_Employee + " where " + EMP_DesignId + " in (6,7) and " + EMP_HoCode + "=" + hoCode + " order by " + EMP_Name;
        Constant.showLog(str);
        return getWritableDatabase().rawQuery(str, null);
    }

}



