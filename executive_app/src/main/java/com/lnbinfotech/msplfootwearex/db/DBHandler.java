package com.lnbinfotech.msplfootwearex.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lnbinfotech.msplfootwearex.AddToCartActivity;
import com.lnbinfotech.msplfootwearex.OptionsActivity;
import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.interfaces.DatabaseUpdateInterface;
import com.lnbinfotech.msplfootwearex.model.AreaMasterClass;
import com.lnbinfotech.msplfootwearex.model.ArealineMasterClass;
import com.lnbinfotech.msplfootwearex.model.BankBranchMasterClass;
import com.lnbinfotech.msplfootwearex.model.BankMasterClass;
import com.lnbinfotech.msplfootwearex.model.CheckoutCustOrderClass;
import com.lnbinfotech.msplfootwearex.model.CityMasterClass;
import com.lnbinfotech.msplfootwearex.model.CompanyMasterClass;
import com.lnbinfotech.msplfootwearex.model.CurrencyMasterClass;
import com.lnbinfotech.msplfootwearex.model.CustomerDetailClass;
import com.lnbinfotech.msplfootwearex.model.CustomerOrderClass;
import com.lnbinfotech.msplfootwearex.model.DocumentMasterClass;
import com.lnbinfotech.msplfootwearex.model.EmployeeMasterClass;
import com.lnbinfotech.msplfootwearex.model.GSTMasterClass;
import com.lnbinfotech.msplfootwearex.model.HOMasterClass;
import com.lnbinfotech.msplfootwearex.model.ProductMasterClass;
import com.lnbinfotech.msplfootwearex.model.SizeDesignMastDetClass;
import com.lnbinfotech.msplfootwearex.model.SizeNDesignClass;
import com.lnbinfotech.msplfootwearex.model.StockInfoMasterClass;
import com.lnbinfotech.msplfootwearex.model.TrackOrderDetailChangedClass;
import com.lnbinfotech.msplfootwearex.model.UserClass;

import java.util.ArrayList;
import java.util.List;

// Created by lnb on 8/11/2016.

public class DBHandler extends SQLiteOpenHelper {

    //private DatabaseUpdateInterface dbInterface;

    public static final String Database_Name = "SmartGST.db";
    //TODO: Change Version
    public static final int Database_Version = 5;

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

    public static final String Table_AreaLineMaster = "AreaLineMaster";
    public static final String AL_Auto = "Auto";
    public static final String AL_AreaId = "AreaId";
    public static final String AL_Area = "Area";
    public static final String AL_CustCount = "CustCount";

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

    public static final String Table_CurrencyMaster = "CurrencyMasterClass";
    public static final String Curr_Auto = "Auto";
    public static final String Curr_Currency = "Currency";
    public static final String Curr_Status = "Status";


    public DBHandler(Context context) {
        super(context, Database_Name, null, Database_Version);
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

    private String create_arealine_master = "create table if not exists " + Table_AreaLineMaster + "(" +
            AL_Auto + " int," + AL_AreaId + " int," + AL_Area + " text," + AL_CustCount + " int)";

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

    private String create_currency_master = "create table if not exists " + Table_CurrencyMaster + "(" +
            Curr_Auto + " int," + Curr_Currency + " int," + Curr_Status + " text)";

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
        Constant.showLog(create_arealine_master);
        db.execSQL(create_arealine_master);
        Constant.showLog(create_area_master);
        db.execSQL(create_area_master);
        Constant.showLog(create_city_master);
        db.execSQL(create_city_master);
        Constant.showLog(create_company_master);
        db.execSQL(create_company_master);
        Constant.showLog(create_bank_master);
        db.execSQL(create_bank_master);
        Constant.showLog(create_bank_branch_master);
        db.execSQL(create_bank_branch_master);
        Constant.showLog(create_document_master);
        db.execSQL(create_document_master);
        Constant.showLog(create_user_master);
        db.execSQL(create_user_master);
        Constant.showLog(create_arsd_master);
        db.execSQL(create_arsd_master);
        Constant.showLog(create_custorder_table);
        db.execSQL(create_custorder_table);
        Constant.showLog(create_gstmaster_table);
        db.execSQL(create_gstmaster_table);
        Constant.showLog(create_trackcustomerorder_table);
        db.execSQL(create_trackcustomerorder_table);
        Constant.showLog(create_sizedesignmastdet_table);
        db.execSQL(create_sizedesignmastdet_table);
        Constant.showLog(create_currency_master);
        db.execSQL(create_currency_master);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL(create_arealine_master);
        }
        if(oldVersion<3){
            db.execSQL(create_currency_master);
        }
        if(oldVersion<4){
            String str = "alter table "+Table_Usermaster+" add "+UM_PINText+" text";
            db.execSQL(str);
            str = "update "+Table_Usermaster+" set "+UM_PINText+"="+UM_PIN;
            db.execSQL(str);
        }
        if(oldVersion<5){
            String str = "alter table "+Table_CompanyMaster+" add "+Company_Company_Add + " text";
            db.execSQL(str);
            str = "alter table "+Table_CompanyMaster+" add "+Company_Company_Phno + " text";
            db.execSQL(str);
            str = "alter table "+Table_CompanyMaster+" add "+Company_Company_Email + " text";
            db.execSQL(str);
            str = "alter table "+Table_CompanyMaster+" add "+Company_MobileNo + " text";
            db.execSQL(str);
            str = "alter table "+Table_CompanyMaster+" add "+Company_Company_Phone2 + " text";
            db.execSQL(str);
            str = "alter table "+Table_CompanyMaster+" add "+Company_Mobileno2 + " text";
            db.execSQL(str);
        }
        //dbInterface.dbUpdated();
    }

    public void addCustomerDetail(ArrayList<CustomerDetailClass> custList) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        ContentValues cv = new ContentValues();
        int count = 1;
        for (CustomerDetailClass cust : custList) {
            cv.put(CM_RetailCustID, cust.getCustID());
            cv.put(CM_Name, cust.getName());
            cv.put(CM_Address, cust.getAddress());
            cv.put(CM_MobileNo, cust.getMobile());
            cv.put(CM_Status, cust.getStatus());
            cv.put(CM_Email, cust.getEmail());
            cv.put(CM_PANNo, cust.getPANno());
            cv.put(CM_PartyName, cust.getPartyName());
            cv.put(CM_GSTNo, cust.getGSTNo());
            cv.put(CM_ImagePath, cust.getImagePath());
            cv.put(CM_PIN, "-1");
            cv.put(CM_Discount, cust.getDiscount());
            cv.put(CM_BranchId,cust.getBranchId());
            cv.put(CM_District,cust.getDistrict());
            cv.put(CM_Taluka,cust.getTaluka());
            cv.put(CM_CityId,cust.getCityId());
            cv.put(CM_AreaId,cust.getAreaId());
            cv.put(CM_HOCode,cust.getHOCode());
            cv.put(CM_AadhaarNo,cust.getAadharNo());
            db.insert(Table_Customermaster, null, cv);
            count++;
        }
        Constant.showLog("Customer Count " + count);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public void addAreaMaster(List<AreaMasterClass> areaClassList) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        ContentValues cv = new ContentValues();
        for (AreaMasterClass areaClass : areaClassList) {
            cv.put(Area_Auto, areaClass.getAuto());
            cv.put(Area_Id, areaClass.getId());
            cv.put(Area_Area, areaClass.getArea());
            cv.put(Area_Cityid, areaClass.getCityid());
            db.insert(Table_AreaMaster, null, cv);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public void addAreaLineMaster(List<ArealineMasterClass> areaClassList) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        ContentValues cv = new ContentValues();
        for (ArealineMasterClass areaClass : areaClassList) {
            cv.put(AL_Auto, areaClass.getAuto());
            cv.put(AL_AreaId, areaClass.getAreaid());
            cv.put(AL_Area, areaClass.getArea());
            cv.put(AL_CustCount, areaClass.getCustcount());
            db.insert(Table_AreaLineMaster, null, cv);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public void addCityMaster(List<CityMasterClass> cityClassList) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        ContentValues cv = new ContentValues();
        for (CityMasterClass cityClass : cityClassList) {
            cv.put(City_Auto, cityClass.getAuto());
            cv.put(City_Id, cityClass.getId());
            cv.put(City_City, cityClass.getCity());
            cv.put(City_Stateid, cityClass.getStId());
            db.insert(Table_CityMaster, null, cv);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
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

    public void addSizeNDesignMaster(SizeNDesignClass sizeNDesignClass) {
        ContentValues cv = new ContentValues();
        cv.put(ARSD_Productid, sizeNDesignClass.getProductid());
        cv.put(ARSD_Cat1, sizeNDesignClass.getCat1());
        cv.put(ARSD_Cat2, sizeNDesignClass.getCat2());
        cv.put(ARSD_Cat3, sizeNDesignClass.getCat3());
        cv.put(ARSD_Cat4, sizeNDesignClass.getCat4());
        cv.put(ARSD_Cat5, sizeNDesignClass.getCat5());
        cv.put(ARSD_Cat6, sizeNDesignClass.getCat6());
        cv.put(ARSD_Final_prod, sizeNDesignClass.getFinal_prod());
        cv.put(ARSD_Uom, sizeNDesignClass.getUom());
        cv.put(ARSD_Vat, sizeNDesignClass.getVat());
        cv.put(ARSD_DesignNo, sizeNDesignClass.getDesignNo());
        cv.put(ARSD_Colour, sizeNDesignClass.getColour());
        cv.put(ARSD_SizeGroup, sizeNDesignClass.getSizeGroup());
        cv.put(ARSD_typ, sizeNDesignClass.getTyp());
        cv.put(ARSD_SizeFrom, sizeNDesignClass.getSizeFrom());
        cv.put(ARSD_SizeTo, sizeNDesignClass.getSizeTo());
        cv.put(ARSD_ActualInw, sizeNDesignClass.getActualInw());
        cv.put(ARSD_GSTGroup, sizeNDesignClass.getGSTGroup());
        cv.put(ARSD_InOutType, sizeNDesignClass.getInOutType());
        cv.put(ARSD_Total, sizeNDesignClass.getTotal());
        cv.put(ARSD_HashCode, sizeNDesignClass.getHashCode());
        cv.put(ARSD_ImageName, sizeNDesignClass.getImageName());
        getWritableDatabase().insert(Table_AllRequiredSizesDesigns, null, cv);
    }

    public void addSizeNDesignMaster(List<SizeNDesignClass> sizeNDesignList) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        ContentValues cv = new ContentValues();
        for (SizeNDesignClass sizeNDesignClass : sizeNDesignList) {
            cv.put(ARSD_Productid, sizeNDesignClass.getProductid());
            cv.put(ARSD_Cat1, sizeNDesignClass.getCat1());
            cv.put(ARSD_Cat2, sizeNDesignClass.getCat2());
            cv.put(ARSD_Cat3, sizeNDesignClass.getCat3());
            cv.put(ARSD_Cat4, sizeNDesignClass.getCat4());
            cv.put(ARSD_Cat5, sizeNDesignClass.getCat5());
            cv.put(ARSD_Cat6, sizeNDesignClass.getCat6());
            cv.put(ARSD_Final_prod, sizeNDesignClass.getFinal_prod());
            cv.put(ARSD_Uom, sizeNDesignClass.getUom());
            cv.put(ARSD_Vat, sizeNDesignClass.getVat());
            cv.put(ARSD_DesignNo, sizeNDesignClass.getDesignNo());
            cv.put(ARSD_Colour, sizeNDesignClass.getColour());
            cv.put(ARSD_SizeGroup, sizeNDesignClass.getSizeGroup());
            cv.put(ARSD_typ, sizeNDesignClass.getTyp());
            cv.put(ARSD_SizeFrom, sizeNDesignClass.getSizeFrom());
            cv.put(ARSD_SizeTo, sizeNDesignClass.getSizeTo());
            cv.put(ARSD_ActualInw, sizeNDesignClass.getActualInw());
            cv.put(ARSD_GSTGroup, sizeNDesignClass.getGSTGroup());
            cv.put(ARSD_InOutType, sizeNDesignClass.getInOutType());
            cv.put(ARSD_Total, sizeNDesignClass.getTotal());
            cv.put(ARSD_HashCode, sizeNDesignClass.getHashCode());
            cv.put(ARSD_ImageName, sizeNDesignClass.getImageName());
            db.insert(Table_AllRequiredSizesDesigns, null, cv);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public void addSizeDesignMastDet(List<SizeDesignMastDetClass> sizeNDesignList) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        ContentValues cv = new ContentValues();
        for (SizeDesignMastDetClass sizeNDesignClass : sizeNDesignList) {
            cv.put(SDMD_Auto,sizeNDesignClass.getAuto());
            cv.put(SDMD_ProductId,sizeNDesignClass.getProductid());
            cv.put(SDMD_DesignNo,sizeNDesignClass.getDesignNo());
            cv.put(SDMD_SizeGroupFrom,sizeNDesignClass.getSizeGroupFrom());
            cv.put(SDMD_SizeGroupTo,sizeNDesignClass.getSizeGroupTo());
            cv.put(SDMD_Total,sizeNDesignClass.getTotal());
            cv.put(SDMD_SizeGroup,sizeNDesignClass.getSizeGroup());
            cv.put(SDMD_Colour,sizeNDesignClass.getColour());
            cv.put(SDMD_Size,sizeNDesignClass.getSize());
            cv.put(SDMD_Qty,sizeNDesignClass.getQty());
            db.insert(Table_SizeDesignMastDet, null, cv);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public void addProductMaster(List<ProductMasterClass> prodList) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        ContentValues cv = new ContentValues();
        for (ProductMasterClass prodClass : prodList) {
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
            cv.put(PM_Cat10, prodClass.getCat10());
            cv.put(PM_HKHO, prodClass.getHKHO());
            cv.put(PM_HKRD, prodClass.getHKRD());
            cv.put(PM_HANR, prodClass.getHANR());
            cv.put(PM_MarkUp,prodClass.getMarkUp());
            cv.put(PM_MarkDown,prodClass.getMarkDown());
            db.insert(Table_ProductMaster, null, cv);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public void addCustomerMaster(List<CustomerDetailClass> custList) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        ContentValues cv = new ContentValues();
        for (CustomerDetailClass cust : custList) {
            cv.put(CM_RetailCustID, cust.getCustID());
            cv.put(CM_Name, cust.getName());
            cv.put(CM_Address, cust.getAddress());
            cv.put(CM_MobileNo, cust.getMobile());
            db.insert(Table_Customermaster, null, cv);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
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

    public void addBankBranchMaster(List<BankBranchMasterClass> bankbranchList) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        ContentValues cv = new ContentValues();
        for (BankBranchMasterClass branchClass : bankbranchList) {
            cv.put(Branch_AutoId, branchClass.getAutoid());
            cv.put(Branch_Id, branchClass.getId());
            cv.put(Branch_Branch, branchClass.getBranch());
            cv.put(Branch_CustId, branchClass.getCustid());
            cv.put(Branch_AccountNo, branchClass.getAccountNo());
            cv.put(Branch_CBankId, branchClass.getcBankid());
            cv.put(Branch_CBranch, branchClass.getcBranch());
            db.insert(Table_BankBranchMaster, null, cv);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public void addNewCustomer() {
        ContentValues cv = new ContentValues();
        cv.put(CM_RetailCustID, OptionsActivity.new_cus.getCust_id());
        cv.put(CM_Name, OptionsActivity.new_cus.getCust_name());
        cv.put(CM_Address, OptionsActivity.new_cus.getAddress());
        cv.put(CM_MobileNo, OptionsActivity.new_cus.getMobile_no());
        cv.put(CM_Status, OptionsActivity.new_cus.getStatus());
        cv.put(CM_BranchId, OptionsActivity.new_cus.getBranchId());
        cv.put(CM_Email, OptionsActivity.new_cus.getEmail_id());
        cv.put(CM_District, OptionsActivity.new_cus.getDistrict());
        cv.put(CM_Taluka, OptionsActivity.new_cus.getTaluka());
        cv.put(CM_CityId, OptionsActivity.new_cus.getCityId());
        cv.put(CM_AreaId, OptionsActivity.new_cus.getAreaId());
        // cv.put(CM_PANNo, OptionsActivity.new_cus.getPANno());
        // cv.put(CM_ImagePath, OptionsActivity.new_cus.getImagePath());
        cv.put(CM_HOCode, OptionsActivity.new_cus.getHOCode());
        //  cv.put(CM_GSTNo, OptionsActivity.new_cus.getGSTNo());
        cv.put(CM_IMEINo, OptionsActivity.new_cus.getIMEINo());
        cv.put(CM_isRegistered, OptionsActivity.new_cus.getIsReg());
        // cv.put(CM_AadhaarNo, OptionsActivity.new_cus.getAadharNo());
        cv.put(CM_PIN, OptionsActivity.new_cus.getPin());
        getWritableDatabase().insert(Table_Customermaster, null, cv);
    }

    public void addDocumentMaster(DocumentMasterClass documentClass) {
        ContentValues cv = new ContentValues();
        cv.put(Document_Id, documentClass.getId());
        cv.put(Document_DocName, documentClass.getDocName());
        cv.put(Document_ForWhom, documentClass.getForWhom());
        cv.put(Document_Compulsary, documentClass.getCompulsary());
        getWritableDatabase().insert(Table_DocumentMaster, null, cv);
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
        cv.put(UM_BranchId, user.getBranchId());
        cv.put(UM_HOCode, user.getHOCode());
        cv.put(UM_IMEINo, user.getIMEINo());
        cv.put(UM_isRegistered, user.getIsRegistered());
        cv.put(UM_AadhaarNo, user.getAadharNo());
        cv.put(UM_PIN, "-1");
        cv.put(UM_PINText, "-1");
        getWritableDatabase().insert(Table_Usermaster, null, cv);
    }

    public void addGSTMaster(GSTMasterClass gst) {
        ContentValues cv = new ContentValues();
        cv.put(GST_Auto, gst.getAuto());
        cv.put(GST_GroupNm, gst.getGroupName());
        cv.put(GST_Status, gst.getStatus());
        cv.put(GST_GSTPer, gst.getGstPer());
        cv.put(GST_CGSTPer, gst.getCgstPer());
        cv.put(GST_SGSTPer, gst.getSgstPer());
        cv.put(GST_CESSPer, gst.getCessPer());
        cv.put(GST_CGSTSHARE, gst.getCgstShare());
        cv.put(GST_SGSTSHARE, gst.getSgstShare());
        getWritableDatabase().insert(Table_GSTMASTER, null, cv);
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

    public List<String> checkPINUnsetID() {
        List<String> list = new ArrayList<>();
        //String str = "select " + UM_PIN + " from " + Table_Usermaster + " where " + UM_PIN + "=-1";
        String str = "select " + UM_PINText + " from " + Table_Usermaster + " where " + UM_PINText + "=-1";
        Cursor res = getWritableDatabase().rawQuery(str, null);
        if (res.moveToFirst()) {
            do {
                //list.add(res.getString(res.getColumnIndex(UM_PIN)));
                list.add(res.getString(res.getColumnIndex(UM_PINText)));
            } while (res.moveToNext());
        }
        res.close();
        return list;
    }

    public void updatePIN(String custid, String pin) {
        ContentValues cv = new ContentValues();
        //cv.put(UM_PIN, pin);
        cv.put(UM_PINText, pin);
        getWritableDatabase().update(Table_Usermaster, cv, UM_RetailCustID + "=?", new String[]{custid});
    }

    public Cursor getAreaName(int cityid) {
        String str = "select " + Area_Area + " from " + Table_AreaMaster + " where " + Area_Cityid + " = " + cityid + " order by " + Area_Area;
        Constant.showLog(str);
        return getWritableDatabase().rawQuery(str, null);
    }

    public Cursor getDistinctAreaLine() {
        String str = "select distinct " + AL_Area + " from " + Table_AreaLineMaster + " order by " + AL_Area;
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
        String str = "select distinct " + Bank_Name + " from " + Table_BankMaster + " where "+Bank_CustType+"='C' order by "+Bank_Name;
        Constant.showLog(str);
        return getWritableDatabase().rawQuery(str, null);
    }

    public Cursor getBranchName() {
        // String str = "select "+Branch_CBranch+" from "+Table_BankBranchMaster+" where "+Branch_CBranch+"  <> ''";
        String str = "select distinct " + Branch_CBranch + " from " + Table_BankBranchMaster + " order by "+Branch_CBranch;
        Constant.showLog(str);
        return getWritableDatabase().rawQuery(str, null);
    }

    public Cursor getOffBankName() {
        String str = "select distinct " + Bank_Name + " from " + Table_BankMaster + " order by "+Bank_Name;
        Constant.showLog(str);
        return getWritableDatabase().rawQuery(str, null);
    }

    public Cursor getOffBranchName() {
        // String str = "select "+Branch_CBranch+" from "+Table_BankBranchMaster+" where "+Branch_CBranch+"  <> ''";
        String str = "select distinct " + Branch_CBranch + " from " + Table_BankBranchMaster + " order by "+Branch_CBranch;
        Constant.showLog(str);
        return getWritableDatabase().rawQuery(str, null);
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

    public Cursor getSubCategory(String catName) {
        String str = "select distinct " + PM_Cat2 + " from " + Table_ProductMaster + " where " + PM_Cat9 + "='" + catName + "' order by " + PM_Cat2;
        return getWritableDatabase().rawQuery(str, null);
    }

    public Cursor getIdOfDocType(String docName) {
        String str = "select " + Document_Id + " from " + Table_DocumentMaster + " where " + Document_DocName + " = '" + docName + "'";
        return getWritableDatabase().rawQuery(str, null);
    }

    public Cursor getUserDetails() {
        String str = "select * from " + Table_Usermaster;
        return getWritableDatabase().rawQuery(str, null);
    }

    public Cursor getDocName() {
        String str = "select " + Document_DocName + " from " + Table_DocumentMaster;
        return getWritableDatabase().rawQuery(str, null);
    }

    public void deleteTable(String tableName) {
        getWritableDatabase().execSQL("delete from " + tableName);
    }

    public Cursor getFinalProduct(String cat2, String cat9) {
        String str = "";
        if(cat2.equals("0")&&cat9.equals("0")){
            str = "select distinct " + PM_Finalprod + "," + PM_ProductID+ "," + PM_Cat2+ "," + PM_Cat9 + " from " + Table_ProductMaster + " where " + PM_Cat9 + " not like 'Item' order by "+PM_Finalprod;
        }else {
            str = "select distinct " + PM_Finalprod + "," + PM_ProductID+ "," + PM_Cat2+ "," + PM_Cat9 + " from " + Table_ProductMaster + " where " + PM_Cat2 + "='" + cat2 + "' and " + PM_Cat9 + "='" + cat9 + "' order by "+PM_Finalprod;
        }
        return getWritableDatabase().rawQuery(str, null);
    }

    public Cursor getCustNameAreawise(int id) {
        String str = "select " + CM_PartyName + " from " + Table_Customermaster + " where " + CM_AreaId + " = " + id + " order by " + CM_PartyName;
        Constant.showLog(str);
        return getWritableDatabase().rawQuery(str, null);
    }

    public int getCustNameId(String name) {
        int id = 0;
        String str = "select " + CM_RetailCustID + " from " + Table_Customermaster + " where " + CM_PartyName + " = '" + name + "'";
        Constant.showLog(str);
        Cursor res = getWritableDatabase().rawQuery(str, null);
        if (res.moveToFirst()) {
            id = res.getInt(0);
        }
        res.close();
        return id;
    }

    public int getCartCount() {
        int a = 0;
        String str = "select count(" + CO_Auto + ") from " + Table_CustomerOrder;
        Constant.showLog(str);
        Cursor res = getWritableDatabase().rawQuery(str, null);
        if (res.moveToFirst()) {
            a = res.getInt(0);
        }
        res.close();
        return a;
    }

    public Cursor getDetailOfCustomer(int id) {
        // String str = "select "+UM_BranchId+","+UM_District+","+UM_Taluka+","+UM_CityId+","+AreaId+","+AreaId+","+HOCode+","+IMEINo+","+isRegistered","+from UserMaster+" where "+CustId+" = "+UM_RetailCustID;
        String str = "select * from " + Table_Usermaster + " where " + UM_RetailCustID + " = '" + id + "'";
        return getWritableDatabase().rawQuery(str, null);
    }

    public Cursor getCustName(int hocode) {
        //String str = "select " + CM_PartyName + " from " + Table_Customermaster + " where " + CM_Status + "='A' order by " + CM_PartyName;
        String str = "select " + CM_PartyName + " from " + Table_Customermaster + " where " + CM_Status + "='A' and "+CM_HOCode+" = "+hocode+" order by " + CM_PartyName;
        Constant.showLog(str);
        return getWritableDatabase().rawQuery(str, null);
    }

    public Cursor getExpListData(String areaIds) {
        String str = "select " + Table_AreaMaster + "." + Area_Id + "," + Table_AreaMaster + "." + Area_Area + "," + Table_Customermaster + "." + CM_RetailCustID + "," + Table_Customermaster + "." + CM_PartyName + "," + Table_Customermaster + "." + CM_AreaId +
                " from " + Table_AreaMaster + "," + Table_Customermaster +
                " where " + Table_AreaMaster + "." + Area_Id + "=" + Table_Customermaster + "." + CM_AreaId +
                " and " + Table_AreaMaster + "." + Area_Id + " in("+areaIds+") group by " + Table_AreaMaster + "." + Area_Id + "," + Table_Customermaster + "." + CM_Name +
                " order by " + Table_AreaMaster + "." + Area_Area +","+Table_Customermaster + "." + CM_PartyName;
        Constant.showLog(str);
        return getWritableDatabase().rawQuery(str, null);
    }

    public Cursor getExpListData() {
        String str = "select " + Table_AreaMaster + "." + Area_Id + "," + Table_AreaMaster + "." + Area_Area + "," + Table_Customermaster + "." + CM_RetailCustID + "," + Table_Customermaster + "." + CM_PartyName + "," + Table_Customermaster + "." + CM_AreaId +
                " from " + Table_AreaMaster + "," + Table_Customermaster +
                " where " + Table_AreaMaster + "." + Area_Id + "=" + Table_Customermaster + "." + CM_AreaId +
                " group by " + Table_AreaMaster + "." + Area_Id + "," + Table_Customermaster + "." + CM_Name +
                " order by " + Table_AreaMaster + "." + Area_Area +","+Table_Customermaster + "." + CM_PartyName;
        Constant.showLog(str);
        return getWritableDatabase().rawQuery(str, null);
    }

    public Cursor getDistinctSizeGroup(String packUnpackType) {
        String str = "select distinct " + ARSD_SizeGroup + " from " + Table_AllRequiredSizesDesigns +
                " where " + ARSD_Productid + "=" + AddToCartActivity.selProdId + " and " + ARSD_typ + "='" + packUnpackType + "' and "
                + ARSD_InOutType + "='I' order by cast(" + ARSD_SizeGroup+" as int)";
        Constant.showLog("getDistinctSizeGroup :- " + str);
        return getWritableDatabase().rawQuery(str, null);
    }

    /*public Cursor getDistinctSizes(String packUnpackType, String sizegroup){
        String str = "select Distinct "+ARSD_Total+",(select "+ARSD_Total+" from "+Table_AllRequiredSizesDesigns
                +" where "+ARSD_Productid+"="+AddToCartActivity.selProdId
                +" and "+ARSD_typ+"='"+packUnpackType+"' and InOutType='I') as CompanyPack from "
                + Table_AllRequiredSizesDesigns +" where "+ ARSD_Productid+"="+AddToCartActivity.selProdId
                +" and "+ARSD_typ+"='"+packUnpackType+"' and InOutType='O' and "+ARSD_SizeGroup+" like '"+sizegroup
                +"' order by "+ARSD_Total;
        Constant.showLog("getDistinctSizes :- "+str);
        return getWritableDatabase().rawQuery(str,null);
    }*/

    public Cursor getDistinctSizes(String packUnpackType, String sizegroup) {
        String str = "select Distinct " + ARSD_Total + " from " + Table_AllRequiredSizesDesigns
                + " where " + ARSD_Productid + "=" + AddToCartActivity.selProdId
                + " and " + ARSD_typ + "='" + packUnpackType + "' and InOutType='O' and " + ARSD_SizeGroup + " like '" + sizegroup + "'"
                + " union all "
                + " select Distinct " + ARSD_Total + " from " + Table_AllRequiredSizesDesigns
                + " where " + ARSD_Productid + "=" + AddToCartActivity.selProdId
                + " and " + ARSD_typ + "='" + packUnpackType + "' and InOutType='I' and " + ARSD_SizeGroup + " like '" + sizegroup + "'"
                + " order by " + ARSD_Total;
        Constant.showLog("getDistinctSizes :- " + str);
        return getWritableDatabase().rawQuery(str, null);
    }

    public Cursor getDistinctColour(String sizegroup) {
        String str = "select Distinct " + ARSD_Colour + "," + ARSD_HashCode + " from " + Table_AllRequiredSizesDesigns +
                " where " + ARSD_Productid + "=" + AddToCartActivity.selProdId + " and " + ARSD_InOutType + "='I' and " +
                ARSD_SizeGroup + " like '" + sizegroup + "' order by " + ARSD_Colour;
        Constant.showLog("getDistinctColour :- " + str);
        return getWritableDatabase().rawQuery(str, null);
    }

    public int getCustMax() {
        int a = 0;
        String str = "select max(" + CM_RetailCustID + ") from " + Table_Customermaster;
        Cursor res = getWritableDatabase().rawQuery(str, null);
        if (res.moveToFirst()) {
            do {
                a = res.getInt(0);
            } while (res.moveToNext());
        }
        res.close();
        return a;
    }

    public int getMaxProdId() {
        int a = 0;
        String str = "select max(" + PM_ProductID + ") from " + Table_ProductMaster;
        Cursor res = getWritableDatabase().rawQuery(str, null);
        if (res.moveToFirst()) {
            a = res.getInt(0);
        }
        res.close();
        return a;
    }

    public int getMaxAuto() {
        int a = 0;
        String str = "select max(" + SDMD_Auto + ") from " + Table_SizeDesignMastDet;
        Cursor res = getWritableDatabase().rawQuery(str, null);
        if (res.moveToFirst()) {
            a = res.getInt(0);
        }
        res.close();
        return a;
    }

    public int getDispatchCenter(String colName) {
        String str = "select " + colName + " from " + Table_ProductMaster + " where " + PM_ProductID + "=" + AddToCartActivity.selProdId;
        Constant.showLog("Dispatch Center :- " + str);
        Cursor res = getWritableDatabase().rawQuery(str, null);
        int a = 0;
        if (res.moveToFirst()) {
            a = res.getInt(0);
        }
        res.close();
        return a;
    }

    public Cursor getDispatchCenter(int hocode) {
        String str = "select " + Company_Id+","+Company_Initial + " from " + Table_CompanyMaster + " where " + Company_HOCode + "=" + hocode +" and "+Company_Name+" not like '%damage%' order By "+Company_Name;
        Constant.showLog("getDispatch Center :- " + str);
        return getWritableDatabase().rawQuery(str, null);
    }

    public Cursor getDispatchCenterWiseTotal() {
        String str = "select "+CO_BranchId+",Sum("+CO_LooseQty+") "+CO_LooseQty+" from "+Table_CustomerOrder+" group by "+CO_BranchId;
        Constant.showLog("DispatchwiseTotal Center :- " + str);
        return getWritableDatabase().rawQuery(str, null);
    }

    public String getRequiredSize(String sizegroup, String color) {
        String str = "select " + ARSD_DesignNo + "," + ARSD_Total + " from " + Table_AllRequiredSizesDesigns +
                " where " + ARSD_Productid + "=" + AddToCartActivity.selProdId + " and " + ARSD_SizeGroup +
                " like '" + sizegroup + "' and " + ARSD_InOutType + "='I' and " + ARSD_Colour + "='" + color + "'";
        Constant.showLog("RequiredSizeNTotal :- " + str);
        Cursor res = getWritableDatabase().rawQuery(str, null);
        String output = "";
        if (res.moveToFirst()) {
            output = res.getString(0) + "^" + res.getString(1);
        }
        res.close();
        return output;
    }

    public Cursor getGSTDetails(String gstGroupName) {
        String str = "select * from " + Table_GSTMASTER + " where " + GST_GroupNm + "='" + gstGroupName + "' and " + GST_Status + "='A'";
        Constant.showLog("getGSTDetails :- " + str);
        return getWritableDatabase().rawQuery(str, null);
    }

    public int getCustOrderMax() {
        int a = 0;
        String str = "select max(" + CO_Auto + ") from " + Table_CustomerOrder;
        Cursor res = getWritableDatabase().rawQuery(str, null);
        if (res.moveToFirst()) {
            a = res.getInt(0);
        }
        res.close();
        a++;
        return a;
    }

    public void addCustomerOrder(CustomerOrderClass custOrder) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        ContentValues cv = new ContentValues();
        cv.put(CO_Auto, custOrder.getAuto());
        cv.put(CO_BranchId, custOrder.getBranchId());
        cv.put(CO_Productid, custOrder.getProductid());
        cv.put(CO_SizeGroup, custOrder.getSizeGroup());
        cv.put(CO_RequiredSize, custOrder.getRequiredSize());
        cv.put(CO_PerPackQty, custOrder.getPerPackQty());
        cv.put(CO_Color, custOrder.getColor());
        cv.put(CO_HashCode, custOrder.getHashCode());
        cv.put(CO_Rate, custOrder.getRate());
        cv.put(CO_MRP, custOrder.getMrp());
        cv.put(CO_Qty, custOrder.getQty());
        cv.put(CO_LooseQty, custOrder.getLooseQty());
        cv.put(CO_ActLooseQty, custOrder.getActLooseQty());
        cv.put(CO_Amount, custOrder.getAmount());
        cv.put(CO_LoosePackTyp, custOrder.getLoosePackTyp());
        cv.put(CO_PendingLooseQty, custOrder.getPendingLooseQty());
        cv.put(CO_TotalAmt, custOrder.getTotalamt());
        cv.put(CO_GSTPer, custOrder.getGstper());
        cv.put(CO_CGSTAmt, custOrder.getCgstamt());
        cv.put(CO_SGSTAmt, custOrder.getSgstamt());
        cv.put(CO_IGSTAmt, custOrder.getIgstamt());
        cv.put(CO_CGSTPer, custOrder.getCgstper());
        cv.put(CO_SGSTPer, custOrder.getSgstper());
        cv.put(CO_CESSPer, custOrder.getCessper());
        cv.put(CO_CESSAmt, custOrder.getCessamt());
        cv.put(CO_DiscPer, custOrder.getDiscPer());
        cv.put(CO_DiscAmt, custOrder.getDiscamnt());
        cv.put(CO_GSTAmt, custOrder.getGstAmt());
        cv.put(CO_NetAmt, custOrder.getNetamnt());
        cv.put(CO_AmtAfterDisc, custOrder.getAmtAfterDisc());
        cv.put(CO_OrderType, custOrder.getOrderType());
        cv.put(CO_Prodid, custOrder.getProdId());
        db.insert(Table_CustomerOrder, null, cv);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public Cursor getViewOrderData(int isAllBranch, String branchIds) {
        String str;
        if(isAllBranch==1) {
            str = "select * from " + Table_CustomerOrder + " order by " + CO_Productid + ",cast(" + CO_SizeGroup+" as int)";
        }else{
            str = "select * from " + Table_CustomerOrder + " where "+CO_BranchId+" in("+branchIds+") order by " + CO_Productid + ",cast(" + CO_SizeGroup+" as int)";
        }
        Constant.showLog(str);
        return getWritableDatabase().rawQuery(str, null);
    }

    public float getCustDiscount(int custid) {
        float a = 0;
        String str = "select " + CM_Discount + " from " + Table_Customermaster + " where " + CM_RetailCustID + "=" + custid;
        Cursor res = getWritableDatabase().rawQuery(str, null);
        if (res.moveToFirst()) {
            do {
                a = res.getInt(0);
            } while (res.moveToNext());
        }
        res.close();
        return a;
    }

    public Cursor getCustOrderTotals() {
        String str = "select count(" + CO_Auto + ") as " + CO_Auto + ",sum(" + CO_LooseQty + ") as " + CO_LooseQty + ", sum(" + CO_Amount +
                ") as " + CO_Amount + ", sum(" + CO_NetAmt + ") as " + CO_NetAmt + ", sum(" + CO_AmtAfterDisc + ") as " + CO_AmtAfterDisc + "," +
                "SUM(" + CO_GSTAmt + ") as " + CO_GSTAmt + ", sum(" + CO_DiscAmt + ") as " + CO_DiscAmt + " from " + Table_CustomerOrder;
        Constant.showLog("getCustOrderTotals :- " + str);
        return getWritableDatabase().rawQuery(str, null);
    }

    public Cursor getCustOrderTotalsAtViewOrder(int allBranch,String branchIds) {
        String str;
        if(allBranch==1) {
            str = "select count(" + CO_Auto + ") as " + CO_Auto + ",sum(" + CO_LooseQty + ") as " + CO_LooseQty + ", sum(" + CO_Amount +
                    ") as " + CO_Amount + ", sum(" + CO_NetAmt + ") as " + CO_NetAmt + ", sum(" + CO_AmtAfterDisc + ") as " + CO_AmtAfterDisc + "," +
                    "SUM(" + CO_GSTAmt + ") as " + CO_GSTAmt + ", sum(" + CO_DiscAmt + ") as " + CO_DiscAmt + " from " + Table_CustomerOrder;
        }else{
            str = "select count(" + CO_Auto + ") as " + CO_Auto + ",sum(" + CO_LooseQty + ") as " + CO_LooseQty + ", sum(" + CO_Amount +
                    ") as " + CO_Amount + ", sum(" + CO_NetAmt + ") as " + CO_NetAmt + ", sum(" + CO_AmtAfterDisc + ") as " + CO_AmtAfterDisc + "," +
                    "SUM(" + CO_GSTAmt + ") as " + CO_GSTAmt + ", sum(" + CO_DiscAmt + ") as " + CO_DiscAmt + " from " + Table_CustomerOrder
                    + " where "+CO_BranchId+" in("+branchIds+")";
        }
        Constant.showLog("getCustOrderTotals :- " + str);
        return getWritableDatabase().rawQuery(str, null);
    }

    public void deleteOrderTable(int auto) {
        getWritableDatabase().execSQL("delete from " + Table_CustomerOrder + " where " + CO_Auto + "=" + auto);
    }

    public void deleteOrderTableAfterSave(int branchid, String gstPer) {
        getWritableDatabase().execSQL("delete from " + Table_CustomerOrder + " where " + CO_BranchId + "=" + branchid+" and "+CO_GSTPer+"='"+gstPer+"'");
    }

    public Cursor getProdName(String prodids) {
        String str = "select " + PM_ProductID + "," + PM_Finalprod + " from " + Table_ProductMaster + " where " + PM_ProductID + " in (" + prodids + ")";
        return getWritableDatabase().rawQuery(str, null);
    }

    public Cursor getProductDetails(String packUnpackType) {
        String str = "select " + PM_ProductID + "," + PM_SRate + "," + PM_MRPRate + "," + PM_HSNCode + "," + PM_GSTGroup + "," + PM_Finalprod + "," + PM_ProdId +
                " from " + Table_ProductMaster + " where " + PM_ProductID + "=" + AddToCartActivity.selProdId;
        Constant.showLog("getProductDetails :- " + str);
        return getWritableDatabase().rawQuery(str, null);
    }

    public Cursor getProductDetails() {
        String str = "select " + Table_ProductMaster +"." + PM_ProductID + "," + Table_ProductMaster +"." + PM_SRate + "," +
                Table_ProductMaster + "."+PM_MRPRate + "," + Table_ProductMaster +"."+ PM_HSNCode + "," + Table_ProductMaster +"." +
                PM_GSTGroup +"," + Table_GSTMASTER + "." + GST_GSTPer + "," + Table_ProductMaster + "." + PM_Finalprod +"," + Table_ProductMaster +
                "." + PM_ProdId +"," + Table_ProductMaster + "." + PM_MarkUp+ "," + Table_ProductMaster + "." + PM_MarkDown +
                " from " + Table_ProductMaster + "," + Table_GSTMASTER + " where " + Table_ProductMaster +"." + PM_GSTGroup + "="
                + Table_GSTMASTER + "." + GST_GroupNm + " and "+ Table_ProductMaster + "." + PM_ProductID + "=" + AddToCartActivity.selProdId;
        Constant.showLog("getProductDetails :- " + str);
        return getWritableDatabase().rawQuery(str, null);
    }

    /*public Cursor getDistinctSizes(String packUnpackType, String sizegroup){
        String str = "select Distinct "+ARSD_Total+",(select "+ARSD_Total+" from "+Table_AllRequiredSizesDesigns
                +" where "+ARSD_Productid+"="+AddToCartActivity.selProdId
                +" and "+ARSD_typ+"='"+packUnpackType+"' and InOutType='I') as CompanyPack from "
                + Table_AllRequiredSizesDesigns +" where "+ ARSD_Productid+"="+AddToCartActivity.selProdId
                +" and "+ARSD_typ+"='"+packUnpackType+"' and InOutType='O' and "+ARSD_SizeGroup+" like '"+sizegroup
                +"' order by "+ARSD_Total;
        Constant.showLog("getDistinctSizes :- "+str);
        return getWritableDatabase().rawQuery(str,null);
    }*/

    public Cursor getSavedUnpackOrder() {
        String str = "select " + CO_SizeGroup + "," + CO_Qty + "," + CO_Color + "," + CO_HashCode + " from " + Table_CustomerOrder + " where " + CO_OrderType + "='U' and " + CO_Productid + "=" + AddToCartActivity.selProdId;
        return getWritableDatabase().rawQuery(str, null);
    }

    public void deleteOrderTableUnpack() {
        getWritableDatabase().execSQL("delete from " + Table_CustomerOrder + " where " + CO_OrderType + "='U' and " + CO_Productid + "=" + AddToCartActivity.selProdId);
    }

    public Cursor getDataToCheck() {
        String str = "select " + CO_BranchId + "," + CO_Productid + "," + CO_SizeGroup + "," + CO_Color + "," + CO_HashCode + "," + CO_MRP + "," + CO_LooseQty +"," + CO_OrderType + " from " + Table_CustomerOrder + " order by " + CO_Productid + "," + CO_SizeGroup;
        Constant.showLog(str);
        return getWritableDatabase().rawQuery(str, null);
    }

    public Cursor getDistinctBrachIdFromCustOrder() {
        String str = "select distinct " + CO_BranchId + " from " + Table_CustomerOrder;
        Constant.showLog("getDistinctBrachIdFromCustOrder :- "+str);
        return getWritableDatabase().rawQuery(str, null);
    }

    public Cursor getCustOrderDetail(int branchid, String gstPer) {
        String str = "select * from " + Table_CustomerOrder + " where " + CO_BranchId + "=" + branchid+" and "+CO_GSTPer+"='"+gstPer+"' order by "+CO_Productid+","+CO_Color+",cast("+CO_SizeGroup+" as int)";
        Constant.showLog("getCustOrderDetail :- "+str);
        return getWritableDatabase().rawQuery(str, null);
    }

    public Cursor getDistinctGSTPerFromCustOrder(int branchid) {
        String str = "select distinct " + CO_GSTPer + " from " + Table_CustomerOrder +" where "+CO_BranchId+"="+branchid;//+" limit 1";
        Constant.showLog("getDistinctGSTPerFromCustOrder :- "+str);
        return getWritableDatabase().rawQuery(str, null);
    }

    public int getCustOrderDetail() {
        int count = 0;
        String str = "select count("+CO_Auto+") from " + Table_CustomerOrder;
        Cursor res = getWritableDatabase().rawQuery(str, null);
        if(res.moveToFirst()){
            count = res.getInt(0);
        }
        res.close();
        return  count;
    }

    public void updateAvailQty(List<CheckoutCustOrderClass> custOrderList) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        ContentValues cv = new ContentValues();
        for (CheckoutCustOrderClass custOrder : custOrderList) {
            cv.put(CO_AvailQty, custOrder.getAvailableQty());
            db.update(Table_CustomerOrder, cv,
                    CO_BranchId + "=? and " + CO_Productid + "=? and " + CO_SizeGroup + "=? and " + CO_Color + "=? and " + CO_MRP + "=? and " + CO_LooseQty + "=?",
                    new String[]{custOrder.getBranchId(), custOrder.getProductId(), custOrder.getSizeGroup(), custOrder.getColor(), custOrder.getRate(), custOrder.getEnterQty()});
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public Cursor getCustOrderMaster(int branchid) {
        String str = "select sum(" + CO_Qty + ") as " + CO_Qty + ", Sum(" + CO_LooseQty + ") as " + CO_LooseQty +
                ",sum(" + CO_Amount + ") as " + CO_Amount + ",sum(" + CO_GSTAmt + ") as " + CO_GSTAmt + ",Sum(" +
                CO_TotalAmt + ") as " + CO_TotalAmt + ",sum(" + CO_NetAmt + ")as " + CO_NetAmt + ",sum(" + CO_AmtAfterDisc +
                ") as " + CO_AmtAfterDisc + " from " + Table_CustomerOrder + " where " + CO_BranchId + "=" + branchid;
        return getWritableDatabase().rawQuery(str, null);
    }

    public Cursor getProductWiseOrderList(int prodId) {
        /*String str = "select "+CO_Productid+","+CO_SizeGroup+","+CO_Color+","+CO_HashCode+","+CO_Qty
                    +","+CO_LooseQty+","+CO_OrderType+" from "+Table_CustomerOrder+" where "
                    +CO_Productid+"="+AddToCartActivity.selProdId+" order by "+CO_SizeGroup;*/

        String str = "select " + CO_Productid + "," + CO_SizeGroup + "," + CO_Color + "," + CO_HashCode + ",sum(" + CO_Qty
                + ")" + CO_Qty + ",sum(" + CO_LooseQty + ")" + CO_LooseQty + "," + CO_OrderType + " from " + Table_CustomerOrder + " where "
                + CO_Productid + "=" + prodId + " group by " + CO_SizeGroup + "," + CO_Color + " order by cast(" + CO_SizeGroup+" as int)";

        Constant.showLog(str);
        return getWritableDatabase().rawQuery(str, null);
    }

    public int getCustid(String name) {
        int a = 0;
        String str = "select " + CM_RetailCustID + " From " + Table_Customermaster + " where " + CM_PartyName + " = '" + name + "'";
        Cursor res = getWritableDatabase().rawQuery(str, null);
        Constant.showLog(str);
        if (res.moveToFirst()) {
            a = res.getInt(0);
        }
        res.close();
        return a;
    }

    public Cursor getWarehouseData(){
        String str = "select "+CO_BranchId+","+Company_Initial+",Sum("+CO_LooseQty+") LooseQty, Sum("+CO_NetAmt+") NetAmt from "+Table_CustomerOrder+","+Table_CompanyMaster+
                " where "+Table_CustomerOrder+"."+CO_BranchId+" = "+Table_CompanyMaster+"."+Company_Id+" group by "+CO_BranchId;
        Constant.showLog(str);
        return getWritableDatabase().rawQuery(str,null);
    }

    public String getDesignNo(String sizegroup, int total) {
        String str = "select " + ARSD_DesignNo + " from " + Table_AllRequiredSizesDesigns +
                " where " + ARSD_Productid + "=" + AddToCartActivity.selProdId + " and " + ARSD_SizeGroup +
                " like '" + sizegroup + "' and " + ARSD_InOutType + "='O' and " + ARSD_Total + "=" + total;
        Constant.showLog("getDesignNo :- " + str);
        Cursor res = getWritableDatabase().rawQuery(str, null);
        String output = "";
        if (res.moveToFirst()) {
            output = res.getString(0);
        }
        res.close();
        return output;
    }

    public List<String> getSizeDetails(String sizegroup, String designNo, int total) {
        String str = "select " + SDMD_Size+ ","+ SDMD_Qty + " from " + Table_SizeDesignMastDet +
                " where " + SDMD_ProductId + "=" + AddToCartActivity.selProdId + " and " + SDMD_SizeGroup +
                " like '" + sizegroup + "' and " + SDMD_DesignNo + "='"+designNo+"' and " + SDMD_Total + "=" + total;
        Constant.showLog("getSizeDetails :- " + str);
        Cursor res = getWritableDatabase().rawQuery(str, null);
        List<String> list = new ArrayList<>();
        if (res.moveToFirst()) {
            do {
                String output = res.getInt(0) + "^" + res.getInt(1);
                list.add(output);
            }while (res.moveToNext());
        }
        res.close();
        return list;
    }

    public String getDistinctColour(String sizegroup, String hashCode) {
        String str = "select Distinct " + ARSD_Colour + "," + ARSD_HashCode + " from " + Table_AllRequiredSizesDesigns +
                " where " + ARSD_Productid + "=" + AddToCartActivity.selProdId + " and " + ARSD_InOutType + "='I' and " +
                ARSD_SizeGroup + " like '" + sizegroup + "' and "+ARSD_HashCode+"='"+hashCode+"' order by " + ARSD_Colour;
        Constant.showLog("getDistinctColourConversion :- " + str);
        String colourHashcode = "";
        Cursor res1 = getWritableDatabase().rawQuery(str, null);
        if (res1.moveToFirst()) {
            colourHashcode = res1.getString(res1.getColumnIndex(DBHandler.ARSD_Colour))
                    +"-"+
                    res1.getString(res1.getColumnIndex(DBHandler.ARSD_HashCode));
        }
        res1.close();
        return colourHashcode;
    }

    public Cursor getAreaIdAreaLineWise(String areaLine){
        String str = "select "+AL_AreaId+" from "+Table_AreaLineMaster+" where "+AL_Area+"='"+areaLine+"' order by "+AL_AreaId;
        Constant.showLog(str);
        return getWritableDatabase().rawQuery(str,null);
    }

    public void addCurrencyMaster(List<CurrencyMasterClass> currClassList) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        ContentValues cv = new ContentValues();
        for (CurrencyMasterClass currClass : currClassList) {
            cv.put(Curr_Auto, currClass.getAuto());
            cv.put(Curr_Currency, currClass.getCurrency());
            cv.put(Curr_Status, currClass.getStatus());
            db.insert(Table_CurrencyMaster, null, cv);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    /*public void initInterface(DatabaseUpdateInterface _dbInterface){
        this.dbInterface = _dbInterface;
    }*/

    public Cursor getCurrenyMaster() {
        String str = "select " + Curr_Currency + " from " + Table_CurrencyMaster + " order by " + Curr_Currency + " desc";
        Constant.showLog(str);
        return getWritableDatabase().rawQuery(str, null);
    }

    public Cursor getImageSubCategory(String catName) {
        String str = "select "+Table_ProductMaster+"."+PM_ProductID+","+Table_ProductMaster+"."+PM_Cat2+","+Table_AllRequiredSizesDesigns+"."+
                ARSD_ImageName+" from "+Table_ProductMaster+","+ Table_AllRequiredSizesDesigns+" where "+
                Table_ProductMaster+"."+PM_Cat9+"='"+catName+"' and "+Table_AllRequiredSizesDesigns+"."+ARSD_Productid+"="+
                Table_ProductMaster+"."+PM_ProductID+" group by "+Table_ProductMaster+"."+PM_Cat2+" order by "+
                Table_ProductMaster+"."+PM_Cat2;
        //Constant.showLog("getImageSubCategory :- "+str);
        return getWritableDatabase().rawQuery(str, null);
    }

    public Cursor getImageSubCategory1(String catName, String subCatName) {
        String str = "select "+Table_ProductMaster+"."+PM_ProductID+","+Table_ProductMaster+"."+PM_Cat2+","
                +Table_ProductMaster+"."+PM_Finalprod+","+Table_ProductMaster+"."+PM_ProdId+","+Table_AllRequiredSizesDesigns+"."+
                ARSD_ImageName+" from "+Table_ProductMaster+","+ Table_AllRequiredSizesDesigns+" where "+
                Table_ProductMaster+"."+PM_Cat9+"='"+catName+"' and "+Table_ProductMaster+"."+PM_Cat2+"='"+subCatName+"' and "
                +Table_AllRequiredSizesDesigns+"."+ARSD_Productid+"="+
                Table_ProductMaster+"."+PM_ProductID+" group by "+Table_ProductMaster+"."+PM_Finalprod+" order by "+
                Table_ProductMaster+"."+PM_Cat2;
        //Constant.showLog("getImageSubCategory 1 :- "+str);
        return getWritableDatabase().rawQuery(str, null);
    }

    public Cursor getContactUsData(int hocode){
        String str = "";
        if(hocode==1){
            str = "select * from "+Table_CompanyMaster+" where "+Company_Initial+"='HKHO'";
        }else if(hocode==12){
            str = "select * from "+Table_CompanyMaster+" where "+Company_Initial+"='HKRD'";
        }else if(hocode==13){
            str = "select * from "+Table_CompanyMaster+" where "+Company_Initial+"='HANR'";
        }
        return getWritableDatabase().rawQuery(str,null);
    }

    public Cursor getCustInfo(int custId) {
        String str = "select "+CM_Name+","+CM_Address+","+CM_ImagePath+","+CM_MobileNo+" from " + Table_Customermaster + " where " + CM_RetailCustID + "="+custId;
        return getWritableDatabase().rawQuery(str, null);
    }

    public Cursor getTrackOrderDetailData() {
        String str;
        str = "select * from " + Table_TrackCustomerOrder;
        Constant.showLog(str);
        return getWritableDatabase().rawQuery(str, null);
    }

    public void addTrackOrderDetailChanged(List<TrackOrderDetailChangedClass> trackOrderList) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        ContentValues cv = new ContentValues();
        for (TrackOrderDetailChangedClass trackClass : trackOrderList) {
            cv.put(TCO_Auto,trackClass.getAuto());
            cv.put(TCO_Productid,trackClass.getProductid());
            cv.put(TCO_SizeGroup,trackClass.getSize_group());
            cv.put(TCO_Color,trackClass.getColor());
            cv.put(TCO_HashCode,trackClass.getHashcode());
            cv.put(TCO_MRP,trackClass.getMrp());
            cv.put(TCO_Rate,trackClass.getRate());
            cv.put(TCO_OrderQty,trackClass.getOrderqty());
            cv.put(TCO_LoosePackTyp,trackClass.getLoosePackTyp());
            cv.put(TCO_CreditApp,trackClass.getCreditapp());
            cv.put(TCO_AllotedToPck,trackClass.getAllowtopck());
            cv.put(TCO_TaxInvMade,trackClass.getTaxinvmade());
            cv.put(TCO_InvNo,trackClass.getInvno());
            cv.put(TCO_InvDate,trackClass.getInvdate());
            cv.put(TCO_InvAmt,trackClass.getInvamnt());
            cv.put(TCO_Transporter,trackClass.getTransporter());
            cv.put(TCO_TransporterNo,trackClass.getTransNo());
            cv.put(TCO_Prodid,trackClass.getProdid());
            cv.put(TCO_InvQty,trackClass.getInvqty());
            cv.put(TCO_CancelQty,trackClass.getCanqty());
            cv.put(TCO_Status,trackClass.getStatus());
            db.insert(Table_TrackCustomerOrder, null, cv);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public Cursor getCustOrderTotalsAtTrackOrder(int allBranch,String branchIds) {
        String str;
        if(allBranch==1) {
            str = "select count(" + TCO_Auto + ") as " + TCO_Auto + ",sum(" + TCO_OrderQty + ") as " + TCO_OrderQty + ", sum(" + TCO_InvQty +
                    ") as " + TCO_InvQty + ", sum(" + TCO_CancelQty + ") as " + TCO_CancelQty + " from " + Table_TrackCustomerOrder;
        }else{
            str = "select count(" + CO_Auto + ") as " + CO_Auto + ",sum(" + CO_LooseQty + ") as " + CO_LooseQty + ", sum(" + CO_Amount +
                    ") as " + CO_Amount + ", sum(" + CO_NetAmt + ") as " + CO_NetAmt + ", sum(" + CO_AmtAfterDisc + ") as " + CO_AmtAfterDisc + "," +
                    "SUM(" + CO_GSTAmt + ") as " + CO_GSTAmt + ", sum(" + CO_DiscAmt + ") as " + CO_DiscAmt + " from " + Table_TrackCustomerOrder
                    + " where "+CO_BranchId+" in("+branchIds+")";
        }
        Constant.showLog("getCustOrderTotals :- " + str);
        return getWritableDatabase().rawQuery(str, null);
    }

    public void deleteTableFromToRange(String tableName, int from, int to) {
        Constant.showLog("Delete From "+tableName+"-"+from+"-"+to);
        getWritableDatabase().execSQL("delete from " + tableName + "  where "+PM_ProdId +" between "+from+" and "+to);
    }

    public void deleteTableFromToRangeSDMD(String tableName, int from, int to) {
        Constant.showLog("Delete From "+tableName+"-"+from+"-"+to);
        getWritableDatabase().execSQL("delete from " + tableName + "  where "+SDMD_ProductId +" between "+from+" and "+to);
    }

}


