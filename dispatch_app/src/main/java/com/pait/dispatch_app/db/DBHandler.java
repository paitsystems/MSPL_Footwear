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
    private static final int Database_Version = 2;

    private static final String Table_Customermaster = "CustomerMaster";
    private static final String CM_RetailCustID = "CustID";
    private static final String CM_Name = "Name";
    private static final String CM_Address = "Address";
    private static final String CM_MobileNo = "MobileNo";
    private static final String CM_Status = "Status";
    private static final String CM_BranchId = "BranchId";
    private static final String CM_Email = "Email";
    private static final String CM_District = "District";
    private static final String CM_Taluka = "Taluka";
    private static final String CM_CityId = "CityId";
    private static final String CM_AreaId = "AreaId";
    private static final String CM_PANNo = "PANNo";
    private static final String CM_PartyName = "PartyName";
    private static final String CM_ImagePath = "ImagePath";
    private static final String CM_HOCode = "HOCode";
    private static final String CM_GSTNo = "GSTNo";
    private static final String CM_IMEINo = "IMEINo";
    private static final String CM_isRegistered = "isRegistered";
    private static final String CM_AadhaarNo = "AadhaarNo";
    private static final String CM_PIN = "PIN";
    private static final String CM_Discount = "Discount";

    private static final String Table_ProductMaster = "ProductMaster";
    private static final String PM_ProductID = "Product_id";
    private static final String PM_Cat1 = "Cat1";
    private static final String PM_Cat2 = "Cat2";
    private static final String PM_Cat3 = "Cat3";
    private static final String PM_Cat4 = "Cat4";
    private static final String PM_Cat5 = "Cat5";
    private static final String PM_Cat6 = "Cat6";
    private static final String PM_Finalprod = "Final_Prod";
    private static final String PM_UOM = "UOM";
    private static final String PM_SRate = "SRate";
    private static final String PM_PRate = "PRate";
    private static final String PM_BranchId = "BranchId";
    private static final String PM_Status = "Status";
    private static final String PM_NoOfPieces = "NoOfPieces";
    private static final String PM_CompanyId = "Company_id";
    private static final String PM_MRPRate = "MRPRate";
    private static final String PM_ProdId = "ProductID";
    private static final String PM_Cat7 = "Cat7";
    private static final String PM_Cat8 = "Cat8";
    private static final String PM_MinStkQty = "MinStkQty";
    private static final String PM_MaxStkQty = "MaxStkQty";
    private static final String PM_GSTGroup = "GSTGroup";
    private static final String PM_HSNCode = "HSNCode";
    private static final String PM_Cat9 = "Cat9";
    private static final String PM_Cat10 = "Cat10";
    private static final String PM_HKHO = "HKHO";
    private static final String PM_HKRD = "HKRD";
    private static final String PM_HANR = "HANR";
    private static final String PM_MarkUp = "SalesMarkUp";
    private static final String PM_MarkDown = "SalesMarkDown";

    private static final String Table_StockInfo = "StockInfo";
    private static final String SI_Company = "Company";
    private static final String SI_ProdId = "ProductID";
    private static final String SI_Color = "Color";
    private static final String SI_Size = "Size";
    private static final String SI_Rate = "Rate";
    private static final String SI_LQty = "LQty";
    private static final String SI_PQty = "PQty";
    private static final String SI_PackUnpack = "PackUnpack";
    private static final String SI_PerPackQty = "PerPackQTy";
    private static final String SI_SaleRate = "SaleRate";
    private static final String SI_ProductID = "Product_id";

    public static final String Table_Employee = "Employee_Master";
    public static final String EMP_EmpId = "EmpID";
    public static final String EMP_Name = "Name";
    private static final String EMP_MobNo = "MobileNo";
    private static final String EMP_Adress = "Adress";
    private static final String EMP_DesignId = "DesignId";
    private static final String EMP_BranchId = "BranchId";
    private static final String EMP_Status = "Status";
    private static final String EMP_DesignName = "DesignName";
    private static final String EMP_CompName = "CompanyName";
    private static final String EMP_CompInit = "CompanyInitial";
    private static final String EMP_HoCode = "HoCode";
    private static final String EMP_PIN = "PIN";

    private static final String Table_HOMaster = "HOMaster";
    private static final String HO_Auto = "Auto";
    private static final String HO_Id = "Id";
    private static final String HO_Name = "Name";
    private static final String HO_City = "City";
    private static final String HO_State = "State";
    private static final String HO_ini = "ini";

    private static final String Table_AreaMaster = "AreaMaster";
    private static final String Area_Auto = "Auto";
    private static final String Area_Id = "Id";
    private static final String Area_Area = "Area";
    private static final String Area_Cityid = "Cityid";

    private static final String Table_CityMaster = "CityMaster";
    private static final String City_Auto = "Auto";
    private static final String City_Id = "Id";
    private static final String City_City = "City";
    private static final String City_Stateid = "StateId";

    public static final String Table_CompanyMaster = "CompanyMaster";
    public static final String Company_Id = "Id";
    private static final String Company_Name = "Name";
    private static final String Company_Initial = "Initial";
    private static final String Company_Pan = "Pan";
    public static final String Company_DisplayCmp = "DisplayCmp";
    private static final String Company_HOCode = "HOCode";
    private static final String Company_GSTNo = "GSTNo";
    private static final String Company_Company_Add = "Company_Add";
    private static final String Company_Company_Phno = "Company_Phno";
    private static final String Company_Company_Email = "Company_Email";
    private static final String Company_MobileNo = "MobileNo";
    private static final String Company_Company_Phone2 = "Company_Phone2";
    private static final String Company_Mobileno2 = "Mobileno2";

    private static final String Table_BankMaster = "BankMaster";
    private static final String Bank_Id = "Id";
    private static final String Bank_BranchId = "BranchId";
    private static final String Bank_Name = "Name";
    private static final String Bank_AccountNo = "AccountNo";
    private static final String Bank_Status = "Status";
    private static final String Bank_IFSC = "IFSC";
    private static final String Bank_MICR = "MICR";
    private static final String Bank_CustType = "CustType";
    private static final String Bank_HoCode = "HoCode";

    private static final String Table_BankBranchMaster = "BankBranchMaster";
    private static final String Branch_AutoId = "AutoId";
    private static final String Branch_Id = "Id";
    private static final String Branch_Branch = "Branch";
    private static final String Branch_CustId = "CustId";
    private static final String Branch_AccountNo = "AccountNo";
    private static final String Branch_CBankId = "CBankId";
    private static final String Branch_CBranch = "CBranch";

    private static final String Table_DocumentMaster = "DocumentMaster";
    private static final String Document_Id = "Id";
    private static final String Document_DocName = "DocName";
    private static final String Document_ForWhom = "ForWhom";
    private static final String Document_Compulsary = "Compulsary";

    public static final String Table_Usermaster = "UserMaster";
    private static final String UM_RetailCustID = "CustID";
    private static final String UM_Name = "Name";
    private static final String UM_Address = "Address";
    private static final String UM_MobileNo = "MobileNo";
    private static final String UM_Status = "Status";
    private static final String UM_BranchId = "BranchId";
    private static final String UM_Email = "Email";
    private static final String UM_District = "District";
    private static final String UM_Taluka = "Taluka";
    private static final String UM_CityId = "CityId";
    private static final String UM_AreaId = "AreaId";
    private static final String UM_PANNo = "PANNo";
    private static final String UM_PartyName = "PartyName";
    private static final String UM_ImagePath = "ImagePath";
    private static final String UM_HOCode = "HOCode";
    private static final String UM_GSTNo = "GSTNo";
    private static final String UM_IMEINo = "IMEINo";
    private static final String UM_isRegistered = "isRegistered";
    private static final String UM_AadhaarNo = "AadhaarNo";
    private static final String UM_PIN = "PIN";
    private static final String UM_PINText = "PINTEXT";

    private static final String Table_AllRequiredSizesDesigns = "AllRequiredSizesdesigns";
    private static final String ARSD_Productid = "Productid";
    private static final String ARSD_Cat1 = "Cat1";
    private static final String ARSD_Cat2 = "Cat2";
    private static final String ARSD_Cat3 = "Cat3";
    private static final String ARSD_Cat4 = "Cat4";
    private static final String ARSD_Cat5 = "Cat5";
    private static final String ARSD_Cat6 = "Cat6";
    private static final String ARSD_Final_prod = "Final_prod";
    private static final String ARSD_Uom = "Uom";
    private static final String ARSD_Vat = "Vat";
    private static final String ARSD_DesignNo = "DesignNo";
    private static final String ARSD_Colour = "Colour";
    private static final String ARSD_SizeGroup = "SizeGroup";
    private static final String ARSD_typ = "typ";
    private static final String ARSD_SizeFrom = "SizeFrom";
    private static final String ARSD_SizeTo = "SizeTo";
    private static final String ARSD_ActualInw = "ActualInw";
    private static final String ARSD_GSTGroup = "GSTGroup";
    private static final String ARSD_InOutType = "InOutType";
    private static final String ARSD_Total = "Total";
    private static final String ARSD_HashCode = "HashCode";
    private static final String ARSD_ImageName = "ImageName";

    public static final String Table_CustomerOrder = "CustomerOrder";
    private static final String CO_Auto = "Auto";
    private static final String CO_BranchId = "BranchId";
    private static final String CO_Productid = "Productid";
    private static final String CO_SizeGroup = "SizeGroup";
    private static final String CO_RequiredSize = "RequiredSize";
    private static final String CO_PerPackQty = "PerPackQty";
    private static final String CO_Color = "Color";
    private static final String CO_HashCode = "HashCode";
    private static final String CO_Rate = "Rate";
    private static final String CO_MRP = "MRP";
    private static final String CO_Qty = "Qty";
    private static final String CO_LooseQty = "LooseQty";
    private static final String CO_ActLooseQty = "ActLooseQty";
    private static final String CO_Amount = "Amount";
    private static final String CO_LoosePackTyp = "LoosePackTyp";
    private static final String CO_TotalAmt = "TotalAmt";
    private static final String CO_PendingLooseQty = "PendingLooseQty";
    private static final String CO_NetAmt = "NetAmt";
    private static final String CO_AmtAfterDisc = "AmtAfterDisc";
    private static final String CO_GSTPer = "GSTPer";
    private static final String CO_GSTAmt = "GSTAmnt";
    private static final String CO_CGSTAmt = "CGSTAmt";
    private static final String CO_SGSTAmt = "SGSTAmt";
    private static final String CO_IGSTAmt = "IGSTAmt";
    private static final String CO_CGSTPer = "CGSTPer";
    private static final String CO_SGSTPer = "SGSTPer";
    private static final String CO_CESSPer = "CESSPer";
    private static final String CO_CESSAmt = "CESSAmt";
    private static final String CO_DiscPer = "DiscPer";
    private static final String CO_DiscAmt = "DiscAmt";
    private static final String CO_OrderType = "OrderType";
    private static final String CO_AvailQty = "AvailQty";
    private static final String CO_Prodid = "ProdId";

    private static final String Table_GSTMASTER = "GSTMASTER";
    private static final String GST_Auto = "Auto";
    private static final String GST_GroupNm = "GroupNm";
    private static final String GST_Status = "Status";
    private static final String GST_GSTPer = "GSTPer";
    private static final String GST_CGSTPer = "CGSTPer";
    private static final String GST_SGSTPer = "SGSTPer";
    private static final String GST_CESSPer = "CESSPer";
    private static final String GST_CGSTSHARE = "CGSTSHARE";
    private static final String GST_SGSTSHARE = "SGSTSHARE";

    private static final String Table_TrackCustomerOrder = "TrackCustomerOrder";
    private static final String TCO_Auto = "Auto";
    private static final String TCO_Productid = "Productid";
    private static final String TCO_SizeGroup = "SizeGroup";
    private static final String TCO_Color = "Color";
    private static final String TCO_HashCode = "HashCode";
    private static final String TCO_Rate = "Rate";
    private static final String TCO_MRP = "MRP";
    private static final String TCO_OrderQty = "OrderQty";
    private static final String TCO_LoosePackTyp = "LoosePackTyp";
    private static final String TCO_CreditApp = "CreditApp";
    private static final String TCO_AllotedToPck = "AllotedToPck";
    private static final String TCO_TaxInvMade = "TaxInvMade";
    private static final String TCO_InvNo = "InvNo";
    private static final String TCO_InvDate = "InvDate";
    private static final String TCO_InvAmt = "InvAmnt";
    private static final String TCO_Transporter = "Transporter";
    private static final String TCO_TransporterNo = "TransporterNo";
    private static final String TCO_Prodid = "ProdId";
    private static final String TCO_InvQty = "InvQty";
    private static final String TCO_CancelQty = "CancelQty";
    private static final String TCO_Status = "Status";

    private static final String Table_SizeDesignMastDet = "SizeDesignMastDet";
    private static final String SDMD_Auto = "Auto";
    private static final String SDMD_ProductId = "Productid";
    private static final String SDMD_DesignNo = "DesignNo";
    private static final String SDMD_SizeGroupFrom = "SizeGroupFrom";
    private static final String SDMD_SizeGroupTo = "SizeGroupTo";
    private static final String SDMD_Total = "Total";
    private static final String SDMD_SizeGroup = "SizeGroup";
    private static final String SDMD_Colour = "Colour";
    private static final String SDMD_Size = "Size";
    private static final String SDMD_Qty = "Qty";

    public static final String Table_DispatchMaster = "DispatchMaster";
    private static final String DM_Auto = "Auto";
    private static final String DM_DispatchBy = "DispatchBy";
    public static final String DM_PONO = "PONO";
    public static final String DM_TotalQty = "TotalQty";
    public static final String DM_Emp_Id = "Emp_Id";
    public static final String DM_Emp_Name = "Emp_Name";
    private static final String DM_TransId = "TransId";
    public static final String DM_Transporter = "Transporter";
    public static final String DM_CustId = "CustId";
    public static final String DM_PartyName = "PartyName";
    private static final String DM_branchid = "branchid";
    private static final String DM_DispatchCmp = "DispatchCmp";
    private static final String DM_DispatchDate = "DispatchDate";
    public static final String DM_DCNo = "DCNo";
    public static final String DM_DCDate = "DCDate";
    public static final String DM_DPTotal = "DPTotal";
    public static final String DM_PSImage = "PSImage";


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
            DM_DispatchDate + " text," + DM_DCNo + " text,"+ DM_DCDate +" text," + DM_DPTotal + " text," +
            DM_PSImage + " text)";

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
        if(oldVersion < 2){
            String str = "alter table "+Table_DispatchMaster+" add "+DM_DPTotal+" text";
            db.execSQL(str);
            str = "alter table "+Table_DispatchMaster+" add "+DM_PSImage+" text";
            db.execSQL(str);
        }
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
            cv.put(DM_DCNo, dm.getDcNo());
            cv.put(DM_DCDate, dm.getDCdate());
            cv.put(DM_DPTotal, dm.getDPTotal());
            cv.put(DM_PSImage, dm.getPSImage());
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
        String str = "select * from "+Table_CompanyMaster + " where "+Company_HOCode+"="+hoCode +" and " +
                Company_DisplayCmp + " not like '%Damage%' and "+Company_DisplayCmp+" not like '%transfer%' " +
                " and "+Company_Id +" not in (1,7,12,13) order by "+Company_DisplayCmp;
        Constant.showLog(str);
        return getWritableDatabase().rawQuery(str,null);
    }

    public Cursor getPartyName(){
        String str = "select distinct "+ DM_PartyName+","+ DM_CustId +" from "+Table_DispatchMaster +" order by "+DM_PartyName;
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

    public String getDPINIT(int hoCode, int dpId){
        String init = "";
        String str = "select "+Company_Initial+" from "+Table_CompanyMaster + " where "+Company_HOCode+"="+hoCode +
                " and "+Company_Id +"="+ dpId;
        Constant.showLog(str);
        Cursor res = getWritableDatabase().rawQuery(str,null);
        if(res.moveToFirst()){
            do{
                init = res.getString(0);
            }while (res.moveToNext());
        }
        res.close();
        return init;
    }

    public void deleteOrderTableAfterSave(String pono) {
        getWritableDatabase().delete(Table_DispatchMaster,DM_PONO+"=?",new String[]{pono});
    }

}



