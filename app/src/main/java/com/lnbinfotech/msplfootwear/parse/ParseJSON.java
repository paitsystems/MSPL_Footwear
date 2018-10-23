package com.lnbinfotech.msplfootwear.parse;

import android.content.Context;
import android.content.SharedPreferences;

import com.lnbinfotech.msplfootwear.DisplayCustOutstandingActivity;
import com.lnbinfotech.msplfootwear.FirstActivity;
import com.lnbinfotech.msplfootwear.R;
import com.lnbinfotech.msplfootwear.constant.Constant;
import com.lnbinfotech.msplfootwear.db.DBHandler;
import com.lnbinfotech.msplfootwear.log.WriteLog;
import com.lnbinfotech.msplfootwear.model.AreaMasterClass;
import com.lnbinfotech.msplfootwear.model.BankBranchMasterClass;
import com.lnbinfotech.msplfootwear.model.BankMasterClass;
import com.lnbinfotech.msplfootwear.model.CheckAvailStockClass;
import com.lnbinfotech.msplfootwear.model.CheckoutCustOrderClass;
import com.lnbinfotech.msplfootwear.model.CityMasterClass;
import com.lnbinfotech.msplfootwear.model.CompanyMasterClass;
import com.lnbinfotech.msplfootwear.model.CustOutstandingClass;
import com.lnbinfotech.msplfootwear.model.CustomerDetailClass;
import com.lnbinfotech.msplfootwear.model.DocumentMasterClass;
import com.lnbinfotech.msplfootwear.model.EmployeeMasterClass;
import com.lnbinfotech.msplfootwear.model.GSTMasterClass;
import com.lnbinfotech.msplfootwear.model.HOMasterClass;
import com.lnbinfotech.msplfootwear.model.LedgerReportClass;
import com.lnbinfotech.msplfootwear.model.OurBankDetailsClass;
import com.lnbinfotech.msplfootwear.model.OuststandingReportClass;
import com.lnbinfotech.msplfootwear.model.ProductMasterClass;
import com.lnbinfotech.msplfootwear.model.StockInfoMasterClass;
import com.lnbinfotech.msplfootwear.model.TrackOrderDetailChangedClass;
import com.lnbinfotech.msplfootwear.model.TrackOrderDetailClass;
import com.lnbinfotech.msplfootwear.model.TrackOrderMasterClass;
import com.lnbinfotech.msplfootwear.model.UserClass;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// Created by lnb on 8/11/2016.

public class ParseJSON {

    private String json;
    private Context context;
    private DBHandler db;

    public ParseJSON(String _json){
        this.json = _json;
    }

    public ParseJSON(String _json, Context _context){
        this.json = _json;
        this.context = _context;
        db = new DBHandler(context);
    }

    public ArrayList<UserClass> parseUserDetail(){
        ArrayList<UserClass> list = new ArrayList<>();
        try{
            JSONArray jsonArray = new JSONArray(json);
            if (jsonArray.length() >= 1) {
                db.deleteTable(DBHandler.Table_Usermaster);
                for (int i = 0; i < jsonArray.length(); i++) {
                    UserClass userClass = new UserClass();
                    userClass.setCustID(jsonArray.getJSONObject(i).getInt("retailCustID"));
                    userClass.setName(jsonArray.getJSONObject(i).getString("name"));
                    userClass.setAddress(jsonArray.getJSONObject(i).getString("address"));
                    userClass.setMobile(jsonArray.getJSONObject(i).getString("mobile"));
                    userClass.setEmail(jsonArray.getJSONObject(i).getString("email"));
                    userClass.setBranchId(jsonArray.getJSONObject(i).getInt("branchId"));
                    userClass.setPANno(jsonArray.getJSONObject(i).getString("Panno"));
                    userClass.setPartyName(jsonArray.getJSONObject(i).getString("PartyName"));
                    userClass.setGSTNo(jsonArray.getJSONObject(i).getString("GSTNo"));
                    userClass.setImagePath(jsonArray.getJSONObject(i).getString("ImagePath"));
                    userClass.setStatus(jsonArray.getJSONObject(i).getString("Status"));
                    userClass.setDistrict(jsonArray.getJSONObject(i).getString("District"));
                    userClass.setTaluka(jsonArray.getJSONObject(i).getString("Taluka"));
                    userClass.setCityId(jsonArray.getJSONObject(i).getInt("CityId"));
                    userClass.setAreaId(jsonArray.getJSONObject(i).getInt("AreaId"));
                    userClass.setHOCode(jsonArray.getJSONObject(i).getInt("HoCode"));
                    userClass.setIMEINo(jsonArray.getJSONObject(i).getString("IMEINo"));
                    userClass.setIsRegistered(jsonArray.getJSONObject(i).getString("isRegistered"));
                    userClass.setAadharNo(jsonArray.getJSONObject(i).getString("AadharNo"));
                    userClass.setPIN("-1");
                    userClass.setPintext("-1");

                    SharedPreferences.Editor editor = FirstActivity.pref.edit();
                    editor.putInt(context.getString(R.string.pref_branchid),userClass.getBranchId());
                    editor.putInt(context.getString(R.string.pref_retailCustId),userClass.getCustID());
                    editor.putInt(context.getString(R.string.pref_cityid),userClass.getCityId());
                    editor.apply();

                    db.addUserDetail(userClass);
                    list.add(userClass);
                }
                db.close();
            }
        }catch (Exception e){
            e.printStackTrace();
            writeLog("parseCustDetail_"+e.getMessage());
        }
        return list;
    }

    public ArrayList<CustomerDetailClass> parseCustDetail(){
        ArrayList<CustomerDetailClass> list = new ArrayList<>();
        try{
            JSONArray jsonArray = new JSONArray(json);
            if (jsonArray.length() >= 1) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    CustomerDetailClass custClass = new CustomerDetailClass();
                    custClass.setCustID(jsonArray.getJSONObject(i).getInt("retailCustID"));
                    custClass.setName(jsonArray.getJSONObject(i).getString("name"));
                    custClass.setAddress(jsonArray.getJSONObject(i).getString("address"));
                    custClass.setMobile(jsonArray.getJSONObject(i).getString("mobile"));
                    custClass.setEmail(jsonArray.getJSONObject(i).getString("email"));
                    custClass.setPANno(jsonArray.getJSONObject(i).getString("Panno"));
                    custClass.setPartyName(jsonArray.getJSONObject(i).getString("PartyName"));
                    custClass.setGSTNo(jsonArray.getJSONObject(i).getString("GSTNo"));
                    custClass.setImagePath(jsonArray.getJSONObject(i).getString("ImagePath"));
                    custClass.setDiscount(Float.parseFloat(jsonArray.getJSONObject(i).getString("Discount")));
                    //db.addCustomerDetail(custClass);
                    list.add(custClass);
                }
                Constant.showLog(""+list.size());
                db.addCustomerDetail(list);
                db.close();
            }
        }catch (Exception e){
            e.printStackTrace();
            writeLog("parseCustDetail_"+e.getMessage());
        }
        return list;
    }

    public int parseAreaMaster(){
        int ret = 0;
        List<AreaMasterClass> areaList = new ArrayList<>();
        try{
            JSONArray jsonArray = new JSONArray(json);
            if (jsonArray.length() >= 1) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    AreaMasterClass areaClass = new AreaMasterClass();
                    areaClass.setAuto(jsonArray.getJSONObject(i).getInt("auto"));
                    areaClass.setId(jsonArray.getJSONObject(i).getInt("id"));
                    areaClass.setArea(jsonArray.getJSONObject(i).getString("area"));
                    areaClass.setCityid(jsonArray.getJSONObject(i).getInt("cityid"));
                    areaList.add(areaClass);
                }
                db.deleteTable(DBHandler.Table_AreaMaster);
                db.addAreaMaster(areaList);
                db.close();
                ret = 1;
            }
        }catch (Exception e){
            e.printStackTrace();
            writeLog("parseAreaMaster_"+e.getMessage());
        }
        return ret;
    }

    public int parseCityMaster(){
        int ret = 0;
        List<CityMasterClass> cityList = new ArrayList<>();
        try{
            JSONArray jsonArray = new JSONArray(json);
            if (jsonArray.length() >= 1) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    CityMasterClass cityClass = new CityMasterClass();
                    cityClass.setAuto(jsonArray.getJSONObject(i).getInt("auto"));
                    cityClass.setId(jsonArray.getJSONObject(i).getInt("id"));
                    cityClass.setCity(jsonArray.getJSONObject(i).getString("city"));
                    cityClass.setStId(jsonArray.getJSONObject(i).getInt("stId"));
                    cityList.add(cityClass);
                }
                db.deleteTable(DBHandler.Table_CityMaster);
                db.addCityMaster(cityList);
                db.close();
                ret = 1;
            }
        }catch (Exception e){
            e.printStackTrace();
            writeLog("parseCityMaster_"+e.getMessage());
        }
        return ret;
    }

    public int parseHOMaster(int max){
        int ret = 0;
        try{
            JSONArray jsonArray = new JSONArray(json);
            if (jsonArray.length() >= 1) {
                if(max==0) {
                    db.deleteTable(DBHandler.Table_HOMaster);
                }
                for (int i = 0; i < jsonArray.length(); i++) {
                    HOMasterClass hoClass = new HOMasterClass();
                    hoClass.setAuto(jsonArray.getJSONObject(i).getInt("auto"));
                    hoClass.setId(jsonArray.getJSONObject(i).getInt("id"));
                    hoClass.setName(jsonArray.getJSONObject(i).getString("Name"));
                    hoClass.setCity(jsonArray.getJSONObject(i).getInt("City"));
                    hoClass.setState(jsonArray.getJSONObject(i).getInt("State"));
                    hoClass.setIni(jsonArray.getJSONObject(i).getString("ini"));
                    db.addHOMaster(hoClass);
                }
                db.close();
                ret = 1;
            }
        }catch (Exception e){
            e.printStackTrace();
            writeLog("parseHOMaster_"+e.getMessage());
        }
        return ret;
    }

    public int parseEmployeeMaster(){
        int ret = 0;
        try{
            JSONArray jsonArray = new JSONArray(json);
            if (jsonArray.length() >= 1) {
                db.deleteTable(DBHandler.Table_Employee);
                for (int i = 0; i < jsonArray.length(); i++) {
                    EmployeeMasterClass empClass = new EmployeeMasterClass();
                    empClass.setEmp_Id(jsonArray.getJSONObject(i).getInt("Emp_Id"));
                    empClass.setName(jsonArray.getJSONObject(i).getString("Emp_Name"));
                    empClass.setMobno(jsonArray.getJSONObject(i).getString("Emp_mobno"));
                    empClass.setAdd(jsonArray.getJSONObject(i).getString("Emp_Add"));
                    empClass.setDesig_Id(jsonArray.getJSONObject(i).getInt("Desig_Id"));
                    empClass.setBranch_Id(jsonArray.getJSONObject(i).getInt("Branch_Id"));
                    empClass.setStatus(jsonArray.getJSONObject(i).getString("Emp_Status"));
                    empClass.setDesigName(jsonArray.getJSONObject(i).getString("Desig_Name"));
                    empClass.setCompanyName(jsonArray.getJSONObject(i).getString("Company_Name"));
                    empClass.setCompanyInit(jsonArray.getJSONObject(i).getString("Company_Initial"));
                    empClass.setHoCode(jsonArray.getJSONObject(i).getInt("HoCode"));
                    empClass.setPIN(jsonArray.getJSONObject(i).getInt("PIN"));
                    db.addEmployeeMaster(empClass);
                }
                db.close();
                ret = 1;
            }
        }catch (Exception e){
            e.printStackTrace();
            writeLog("parseEmployeeMaster_"+e.getMessage());
        }
        return ret;
    }

    public void parseStockInfo(){
        try{
            JSONArray jsonArray = new JSONArray(json);
            if (jsonArray.length() >= 1) {
                db.deleteTable(DBHandler.Table_StockInfo);
                for (int i = 0; i < jsonArray.length(); i++) {
                    StockInfoMasterClass stockInfo = new StockInfoMasterClass();
                    stockInfo.setCompany(jsonArray.getJSONObject(i).getString("Company"));
                    stockInfo.setProductId(jsonArray.getJSONObject(i).getString("ProductId"));
                    stockInfo.setColor(jsonArray.getJSONObject(i).getString("Color"));
                    stockInfo.setSize(jsonArray.getJSONObject(i).getString("Size"));
                    stockInfo.setRate(jsonArray.getJSONObject(i).getString("Rate"));
                    stockInfo.setLQty(jsonArray.getJSONObject(i).getInt("LQty"));
                    stockInfo.setPQty(jsonArray.getJSONObject(i).getInt("PQty"));
                    stockInfo.setPackUnpack(jsonArray.getJSONObject(i).getString("PackUnpack"));
                    stockInfo.setPerPackQty(jsonArray.getJSONObject(i).getInt("PerPackQty"));
                    stockInfo.setSaleRate(jsonArray.getJSONObject(i).getString("SaleRate"));
                    stockInfo.setProduct_id(jsonArray.getJSONObject(i).getInt("Product_id"));
                    db.addStockInfo(stockInfo);
                }
                db.close();
            }
        }catch (Exception e){
            e.printStackTrace();
            writeLog("parseStockInfo_"+e.getMessage());
        }
    }

    public int parseProductMaster(int from, int to){
        int ret = 0;
        try{
            JSONArray jsonArray = new JSONArray(json);
            if (jsonArray.length() >= 1) {
                List<ProductMasterClass> prodList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    ProductMasterClass prodClass = new ProductMasterClass();
                    prodClass.setProduct_id(jsonArray.getJSONObject(i).getInt("Product_id"));
                    prodClass.setCat1(jsonArray.getJSONObject(i).getString("Cat1"));
                    prodClass.setCat2(jsonArray.getJSONObject(i).getString("Cat2"));
                    prodClass.setCat3(jsonArray.getJSONObject(i).getString("Cat3"));
                    prodClass.setCat4(jsonArray.getJSONObject(i).getString("Cat4"));
                    prodClass.setCat5(jsonArray.getJSONObject(i).getString("Cat5"));
                    prodClass.setCat6(jsonArray.getJSONObject(i).getString("Cat6"));
                    prodClass.setFinal_prod(jsonArray.getJSONObject(i).getString("Final_prod"));
                    prodClass.setUom(jsonArray.getJSONObject(i).getString("Uom"));
                    prodClass.setSrate(jsonArray.getJSONObject(i).getString("Srate"));
                    prodClass.setPrate(jsonArray.getJSONObject(i).getString("Prate"));
                    prodClass.setBranchid(jsonArray.getJSONObject(i).getInt("Branchid"));
                    prodClass.setStatus(jsonArray.getJSONObject(i).getString("Status"));
                    prodClass.setNoOfPices(jsonArray.getJSONObject(i).getInt("NoOfPices"));
                    prodClass.setCompany_Id(jsonArray.getJSONObject(i).getInt("Company_Id"));
                    prodClass.setMRPRate(jsonArray.getJSONObject(i).getString("MRPRate"));
                    prodClass.setProductId(jsonArray.getJSONObject(i).getString("ProductId"));
                    prodClass.setCat7(jsonArray.getJSONObject(i).getString("Cat7"));
                    prodClass.setCat8(jsonArray.getJSONObject(i).getString("Cat8"));
                    prodClass.setMinStkQty(jsonArray.getJSONObject(i).getString("MinStkQty"));
                    prodClass.setMaxStkQty(jsonArray.getJSONObject(i).getString("MaxStkQty"));
                    prodClass.setGSTGroup(jsonArray.getJSONObject(i).getString("GSTGroup"));
                    prodClass.setHSNCode(jsonArray.getJSONObject(i).getString("HSNCode"));
                    prodClass.setCat9(jsonArray.getJSONObject(i).getString("Cat9"));
                    prodClass.setCat10(jsonArray.getJSONObject(i).getString("Cat10"));
                    prodClass.setHKHO(jsonArray.getJSONObject(i).getInt("HKHO"));
                    prodClass.setHKRD(jsonArray.getJSONObject(i).getInt("HKRD"));
                    prodClass.setHANR(jsonArray.getJSONObject(i).getInt("HANR"));
                    prodClass.setMarkUp(jsonArray.getJSONObject(i).getString("Sales_MarkUp"));
                    prodClass.setMarkDown(jsonArray.getJSONObject(i).getString("Sales_MarkDown"));
                    //db.addProductMaster(prodClass);
                    prodList.add(prodClass);
                }
                db.deleteProductTableFromToRange(DBHandler.Table_ProductMaster,from,to);
                db.addProductMaster(prodList);
                db.close();
                ret = 1;
            }
        }catch (Exception e){
            e.printStackTrace();
            writeLog("parseProductMaster_"+e.getMessage());
        }
        return ret;
    }

    public int parseCustomerMaster(){
        int ret = 0;
        List<CustomerDetailClass> CustomerDetailList = new ArrayList<>();
        try{
            JSONArray jsonArray = new JSONArray(json);
            if (jsonArray.length() >= 1) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    CustomerDetailClass custClass = new CustomerDetailClass();
                    custClass.setCustID(jsonArray.getJSONObject(i).getInt("retailCustID"));
                    custClass.setName(jsonArray.getJSONObject(i).getString("name"));
                    custClass.setAddress(jsonArray.getJSONObject(i).getString("address"));
                    custClass.setMobile(jsonArray.getJSONObject(i).getString("mobile"));
                    custClass.setStatus(jsonArray.getJSONObject(i).getString("status"));
                    custClass.setBranchId(jsonArray.getJSONObject(i).getInt("branchId"));
                    custClass.setEmail(jsonArray.getJSONObject(i).getString("email"));
                    custClass.setDistrict(jsonArray.getJSONObject(i).getString("District"));
                    custClass.setTaluka(jsonArray.getJSONObject(i).getString("Taluka"));
                    custClass.setCityId(jsonArray.getJSONObject(i).getInt("cityId"));
                    custClass.setAreaId(jsonArray.getJSONObject(i).getInt("areaId"));
                    custClass.setPANno(jsonArray.getJSONObject(i).getString("Panno"));
                    custClass.setImagePath(jsonArray.getJSONObject(i).getString("ImagePath"));
                    custClass.setHOCode(jsonArray.getJSONObject(i).getInt("HoCode"));
                    custClass.setGSTNo(jsonArray.getJSONObject(i).getString("GSTNo"));
                    custClass.setIMEINo(jsonArray.getJSONObject(i).getString("IMEINo"));
                    custClass.setIsRegistered(jsonArray.getJSONObject(i).getString("isRegistered"));
                    custClass.setAadharNo(jsonArray.getJSONObject(i).getString("AadharNo"));
                    custClass.setPIN(jsonArray.getJSONObject(i).getString("PIN"));
                    CustomerDetailList.add(custClass);
                }
                db.deleteTable(DBHandler.Table_Customermaster);
                db.addCustomerMaster(CustomerDetailList);
                db.close();
                ret = 1;
            }
        }catch (Exception e){
            e.printStackTrace();
            writeLog("parseCustomerMaster_"+e.getMessage());
        }
        return ret;
    }

    public int parseCompanyMaster(int max){
        int ret = 0;
        try{
            JSONArray jsonArray = new JSONArray(json);
            if (jsonArray.length() >= 1) {
                if(max==0) {
                    db.deleteTable(DBHandler.Table_CompanyMaster);
                }
                for (int i = 0; i < jsonArray.length(); i++) {
                    CompanyMasterClass companyClass = new CompanyMasterClass();
                    companyClass.setCompanyId(jsonArray.getJSONObject(i).getString("Company_id"));
                    companyClass.setCompanyName(jsonArray.getJSONObject(i).getString("Company_Name"));
                    companyClass.setCompanyInitial(jsonArray.getJSONObject(i).getString("Company_Initial"));
                    companyClass.setCompanyPan(jsonArray.getJSONObject(i).getString("Company_Pan"));
                    companyClass.setDisplayCmp(jsonArray.getJSONObject(i).getString("DisplayCmp"));
                    companyClass.setGSTNo(jsonArray.getJSONObject(i).getString("GSTNo"));
                    companyClass.setHOCode(jsonArray.getJSONObject(i).getString("HOCode"));
                    companyClass.setCompany_Add(jsonArray.getJSONObject(i).getString("Company_Add"));
                    companyClass.setCompany_Phno(jsonArray.getJSONObject(i).getString("Company_Phno"));
                    companyClass.setCompany_Email(jsonArray.getJSONObject(i).getString("Company_Email"));
                    companyClass.setMobileNo(jsonArray.getJSONObject(i).getString("MobileNo"));
                    companyClass.setCompany_Phone2(jsonArray.getJSONObject(i).getString("company_phone2"));
                    companyClass.setMobileno2(jsonArray.getJSONObject(i).getString("Mobileno2"));
                    db.addCompanyMaster(companyClass);
                }
                db.close();
                ret = 1;
            }
        }catch (Exception e){
            e.printStackTrace();
            writeLog("parseCompanyMaster_"+e.getMessage());
        }
        return ret;
    }

    public int parseBankMaster(){
        int ret = 0;
        try{
            JSONArray jsonArray = new JSONArray(json);
            if (jsonArray.length() >= 1) {
                db.deleteTable(DBHandler.Table_BankMaster);
                for (int i = 0; i < jsonArray.length(); i++) {
                    BankMasterClass bankClass = new BankMasterClass();
                    bankClass.setBankId(jsonArray.getJSONObject(i).getString("bankId"));
                    bankClass.setBranchId(jsonArray.getJSONObject(i).getString("branchid"));
                    bankClass.setBankName(jsonArray.getJSONObject(i).getString("bankName"));
                    bankClass.setAccountNo(jsonArray.getJSONObject(i).getString("accountNo"));
                    bankClass.setStatus(jsonArray.getJSONObject(i).getString("status"));
                    bankClass.setIFSC(jsonArray.getJSONObject(i).getString("IFSC"));
                    bankClass.setMICR(jsonArray.getJSONObject(i).getString("MICR"));
                    bankClass.setCustType(jsonArray.getJSONObject(i).getString("CustType"));
                    bankClass.setHoCode(jsonArray.getJSONObject(i).getString("HoCode"));
                    db.addBankMaster(bankClass);
                }
                db.close();
                ret = 1;
            }
        }catch (Exception e){
            e.printStackTrace();
            writeLog("parseBankMaster_"+e.getMessage());
        }
        return ret;
    }

    public int parseBankBranchMaster(){
        int ret = 0;
        List<BankBranchMasterClass> bankBranchList = new ArrayList<>();
        try{
            JSONArray jsonArray = new JSONArray(json);
            if (jsonArray.length() >= 1) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    BankBranchMasterClass branchClass = new BankBranchMasterClass();
                    branchClass.setAutoid(jsonArray.getJSONObject(i).getString("Autoid"));
                    branchClass.setId(jsonArray.getJSONObject(i).getString("id"));
                    branchClass.setBranch(jsonArray.getJSONObject(i).getString("Branch"));
                    branchClass.setCustid(jsonArray.getJSONObject(i).getString("Custid"));
                    branchClass.setAccountNo(jsonArray.getJSONObject(i).getString("AccountNo"));
                    branchClass.setcBankid(jsonArray.getJSONObject(i).getString("Custid"));
                    branchClass.setcBranch(jsonArray.getJSONObject(i).getString("CBranch"));
                    //db.addBankBranchMaster(branchClass);
                    bankBranchList.add(branchClass);
                }
                db.deleteTable(DBHandler.Table_BankBranchMaster);
                db.addBankBranchMaster(bankBranchList);
                db.close();
                ret = 1;
            }
        }catch (Exception e){
            e.printStackTrace();
            writeLog("parseBankBranchMaster_" + e.getMessage());
        }
        return ret;
    }

    public int parseDocumentMaster(){
        int ret = 0;
        try{
            JSONArray jsonArray = new JSONArray(json);
            db.deleteTable(DBHandler.Table_DocumentMaster);
            if (jsonArray.length() >= 1) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    DocumentMasterClass documentClass = new DocumentMasterClass();
                    documentClass.setId(jsonArray.getJSONObject(i).getString("Id"));
                    documentClass.setDocName(jsonArray.getJSONObject(i).getString("DocName"));
                    documentClass.setForWhom(jsonArray.getJSONObject(i).getString("ForWhom"));
                    documentClass.setCompulsary(jsonArray.getJSONObject(i).getString("Compulsary"));
                    db.addDocumentMaster(documentClass);
                }
                db.close();
                ret = 1;
            }
        }catch (Exception e){
            e.printStackTrace();
            writeLog("parseDocumentMaster_" + e.getMessage());
        }
        return ret;
    }

    public List<TrackOrderMasterClass> parseloadTrackOreder(){

        List<TrackOrderMasterClass> list = new ArrayList<>();
        try{
            JSONArray jsonArray = new JSONArray(json);
            if (jsonArray.length() >= 1) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    TrackOrderMasterClass trackOrderClass = new TrackOrderMasterClass();
                    trackOrderClass.setAuto(jsonArray.getJSONObject(i).getString("auto"));
                    trackOrderClass.setBranchid(jsonArray.getJSONObject(i).getString("branchid"));
                    trackOrderClass.setId(jsonArray.getJSONObject(i).getString("id"));
                    trackOrderClass.setPODate(jsonArray.getJSONObject(i).getString("PODate"));
                    trackOrderClass.setPono(jsonArray.getJSONObject(i).getString("PONO"));
                    trackOrderClass.setCustid(jsonArray.getJSONObject(i).getString("CUstid"));
                    trackOrderClass.setSalesExe(jsonArray.getJSONObject(i).getString("SalesExe"));
                    trackOrderClass.setLooseQty(jsonArray.getJSONObject(i).getString("LooseQty"));
                    trackOrderClass.setNetAmt(jsonArray.getJSONObject(i).getString("NetAmt"));
                    trackOrderClass.setApprove(jsonArray.getJSONObject(i).getString("Approve"));
                    list.add(trackOrderClass);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            writeLog("parseloadTrackOreder_" + e.getMessage());
        }
        return list;
    }

    public HashMap<Integer,List<TrackOrderDetailClass>> parseloadDetailOrder(){
        HashMap<Integer,List<TrackOrderDetailClass>> map = new HashMap<>();
        try{
            JSONArray jsonArray = new JSONArray(json);
            if (jsonArray.length() >= 1) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    List<TrackOrderDetailClass> list;
                    TrackOrderDetailClass orderClass = new TrackOrderDetailClass();
                    orderClass.setAuto(jsonArray.getJSONObject(i).getInt("auto"));
                    int prodid = jsonArray.getJSONObject(i).getInt("productid");
                    orderClass.setProductid(prodid);
                    //orderClass.setFinal_prod(jsonArray.getJSONObject(i).getString("Final_prod"));
                    orderClass.setSize_group(jsonArray.getJSONObject(i).getString("SizeGroup"));
                    orderClass.setColor(jsonArray.getJSONObject(i).getString("Color"));
                    orderClass.setMrp(jsonArray.getJSONObject(i).getString("MRP"));
                    orderClass.setActLooseQty(jsonArray.getJSONObject(i).getString("ActLooseQty"));
                    orderClass.setLoosePackTyp(jsonArray.getJSONObject(i).getString("LoosePackTyp"));
                    if(map.isEmpty()) {
                        list = new ArrayList<>();
                        list.add(orderClass);
                        map.put(prodid,list);
                    }else if(map.containsKey(prodid)){
                        list = map.get(prodid);
                        list.add(orderClass);
                        map.put(prodid,list);
                    }else{
                        list = new ArrayList<>();
                        list.add(orderClass);
                        map.put(prodid,list);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            writeLog("parseloadDetailOrder_" + e.getMessage());
        }
        return map;
    }

    public int parseloadDetailOrderChanged(){
        int a = 0;
        try{
            JSONArray jsonArray = new JSONArray(json);
            List<TrackOrderDetailChangedClass> list = new ArrayList<>();
            if (jsonArray.length() >= 1) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    TrackOrderDetailChangedClass orderClass = new TrackOrderDetailChangedClass();
                    orderClass.setAuto(jsonArray.getJSONObject(i).getInt("auto"));
                    orderClass.setProductid(jsonArray.getJSONObject(i).getInt("productid"));
                    orderClass.setSize_group(jsonArray.getJSONObject(i).getString("SizeGroup"));
                    orderClass.setColor(jsonArray.getJSONObject(i).getString("Color"));
                    orderClass.setHashcode(jsonArray.getJSONObject(i).getString("HashCode"));
                    orderClass.setMrp(jsonArray.getJSONObject(i).getString("MRP"));
                    orderClass.setRate(jsonArray.getJSONObject(i).getString("Rate"));
                    orderClass.setOrderqty(jsonArray.getJSONObject(i).getInt("OrderQty"));
                    orderClass.setLoosePackTyp(jsonArray.getJSONObject(i).getString("LoosePackTyp"));
                    orderClass.setCreditapp(jsonArray.getJSONObject(i).getString("CreditApp"));
                    orderClass.setAllowtopck(jsonArray.getJSONObject(i).getString("AllotedtoPck"));
                    orderClass.setTaxinvmade(jsonArray.getJSONObject(i).getString("TaxInvMad"));
                    orderClass.setInvno(jsonArray.getJSONObject(i).getString("InvNo"));
                    orderClass.setInvdate(jsonArray.getJSONObject(i).getString("InvDate"));
                    orderClass.setInvamnt(jsonArray.getJSONObject(i).getString("InvAmt"));
                    orderClass.setTransporter(jsonArray.getJSONObject(i).getString("Transporter"));
                    orderClass.setTransNo(jsonArray.getJSONObject(i).getString("TransporterNo"));
                    orderClass.setProdid(jsonArray.getJSONObject(i).getString("prodid"));
                    orderClass.setInvqty(jsonArray.getJSONObject(i).getInt("Invo"));
                    orderClass.setCanqty(jsonArray.getJSONObject(i).getInt("Can"));
                    orderClass.setStatus(jsonArray.getJSONObject(i).getString("STATUS"));
                    list.add(orderClass);
                }
                db.deleteTable(DBHandler.Table_TrackCustomerOrder);
                db.addTrackOrderDetailChanged(list);
                a = 1;
            }
        }catch (Exception e){
            e.printStackTrace();
            writeLog("parseloadDetailOrderChanged_" + e.getMessage());
            a = 0;
        }
        return a;
    }

    public int parseGSTMaster(int max){
        int ret = 0;
        try{
            JSONArray jsonArray = new JSONArray(json);
            if (jsonArray.length() >= 1) {
                if(max==0) {
                    db.deleteTable(DBHandler.Table_GSTMASTER);
                }
                for (int i = 0; i < jsonArray.length(); i++) {
                    GSTMasterClass gstClass = new GSTMasterClass();
                    gstClass.setAuto(jsonArray.getJSONObject(i).getInt("Auto"));
                    gstClass.setGroupName(jsonArray.getJSONObject(i).getString("GroupNm"));
                    gstClass.setStatus(jsonArray.getJSONObject(i).getString("Status"));

                    gstClass.setGstPer(jsonArray.getJSONObject(i).getString("GSTPer"));
                    gstClass.setCgstPer(jsonArray.getJSONObject(i).getString("CGSTPer"));
                    gstClass.setSgstPer(jsonArray.getJSONObject(i).getString("SGSTPer"));
                    gstClass.setCessPer(jsonArray.getJSONObject(i).getString("CESSPer"));
                    gstClass.setCgstShare(jsonArray.getJSONObject(i).getString("CGSTSHARE"));
                    gstClass.setSgstShare(jsonArray.getJSONObject(i).getString("SGSTSHARE"));

                    db.addGSTMaster(gstClass);
                }
                db.close();
                ret = 1;
            }
        }catch (Exception e){
            e.printStackTrace();
            writeLog("parseGSTMaster_"+e.getMessage());
        }
        return ret;
    }

    public List<CheckoutCustOrderClass> parseloadCheckoutOrder(){
        List<CheckoutCustOrderClass> list = new ArrayList<>();
        try{
            JSONArray jsonArray = new JSONArray(new JSONObject(json).get("CheckStockResult").toString());
            if (jsonArray.length() >= 1) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    CheckoutCustOrderClass checkout = new CheckoutCustOrderClass();
                    checkout.setBranchId(jsonArray.getJSONObject(i).getString("Branch_id"));
                    checkout.setProductId(jsonArray.getJSONObject(i).getString("Product_id"));
                    checkout.setColor(jsonArray.getJSONObject(i).getString("Color"));
                    checkout.setSizeGroup(jsonArray.getJSONObject(i).getString("SizeGroup"));
                    checkout.setAvailableQty(jsonArray.getJSONObject(i).getString("AvailQty"));
                    checkout.setRate(jsonArray.getJSONObject(i).getString("Rate"));
                    checkout.setHashCode(jsonArray.getJSONObject(i).getString("HashCode"));
                    checkout.setEnterQty(jsonArray.getJSONObject(i).getString("EnterQty"));
                    list.add(checkout);
                }
                db.updateAvailQty(list);
            }
        }catch (Exception e){
            e.printStackTrace();
            writeLog("parseloadCheckoutOrder_" + e.getMessage());
        }
        return list;
    }

    public int parseCustOutstanding(){
        int ret = 0;
        try{
            //retailCustId, name,ledgerBal,Creditlimit,Creditdays,PartName,PostDatedCheque,BalForPayment,OverDueAmnt,OverDueDays,OverLimit
            JSONArray jsonArray = new JSONArray(json);
            if (jsonArray.length() >= 1) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    DisplayCustOutstandingActivity.outClass = new CustOutstandingClass();
                    DisplayCustOutstandingActivity.outClass.setRetailCustId(jsonArray.getJSONObject(i).getString("retailCustID"));
                    DisplayCustOutstandingActivity.outClass.setName(jsonArray.getJSONObject(i).getString("name"));
                    DisplayCustOutstandingActivity.outClass.setLedgerBal(jsonArray.getJSONObject(i).getString("ledgerBal"));
                    DisplayCustOutstandingActivity.outClass.setCreditlimit(jsonArray.getJSONObject(i).getString("Creditlimit"));
                    DisplayCustOutstandingActivity.outClass.setCreditdays(jsonArray.getJSONObject(i).getString("Creditdays"));
                    DisplayCustOutstandingActivity.outClass.setPartName(jsonArray.getJSONObject(i).getString("PartyName"));
                    DisplayCustOutstandingActivity.outClass.setCurrOutstnd(jsonArray.getJSONObject(i).getString("CurrentOutstnd"));
                    DisplayCustOutstandingActivity.outClass.setPostDatedCheque(jsonArray.getJSONObject(i).getString("PostDatedCheque"));
                    DisplayCustOutstandingActivity.outClass.setBalForPayment(jsonArray.getJSONObject(i).getString("BalForPayment"));
                    DisplayCustOutstandingActivity.outClass.setOverDueAmnt(jsonArray.getJSONObject(i).getString("OverDueAmnt"));
                    DisplayCustOutstandingActivity.outClass.setOverDueDays(jsonArray.getJSONObject(i).getString("OverDueDays"));
                    DisplayCustOutstandingActivity.outClass.setOverLimit(jsonArray.getJSONObject(i).getString("OverLimit"));
                }
                ret = 1;
            }
        }catch (Exception e){
            e.printStackTrace();
            writeLog("parseCustOutstanding"+e.getMessage());
        }
        return ret;
    }

    public List<LedgerReportClass> parseLedgerReport(){

        List<LedgerReportClass> list = new ArrayList<>();
        try{
            JSONArray jsonArray = new JSONArray(json);
            if (jsonArray.length() >= 1) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    LedgerReportClass ledgerClass = new LedgerReportClass();
                    ledgerClass.setDate(jsonArray.getJSONObject(i).getString("Date"));
                    ledgerClass.setAgainst(jsonArray.getJSONObject(i).getString("Against"));
                    ledgerClass.setOpnbal(jsonArray.getJSONObject(i).getDouble("openingBalance"));
                    ledgerClass.setDebit(jsonArray.getJSONObject(i).getDouble("Debit"));
                    ledgerClass.setCredit(jsonArray.getJSONObject(i).getDouble("Credit"));
                    ledgerClass.setClsbal(jsonArray.getJSONObject(i).getDouble("ClosingBal"));
                    ledgerClass.setTransno(jsonArray.getJSONObject(i).getString("TransNo"));
                    list.add(ledgerClass);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            writeLog("parseLedgerReport_" + e.getMessage());
        }
        return list;
    }

    public List<OuststandingReportClass> parseOutStndReport(){

        List<OuststandingReportClass> list = new ArrayList<>();
        try{
            JSONArray jsonArray = new JSONArray(json);
            if (jsonArray.length() >= 1) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    OuststandingReportClass outClass = new OuststandingReportClass();
                    outClass.setDate(jsonArray.getJSONObject(i).getString("Date"));
                    outClass.setType(jsonArray.getJSONObject(i).getString("Type"));
                    outClass.setDcno(jsonArray.getJSONObject(i).getString("dcNo"));
                    outClass.setTotal(jsonArray.getJSONObject(i).getDouble("Total"));

                    list.add(outClass);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            writeLog("parseOutStndReport_" + e.getMessage());
        }
        return list;
    }

    public List<OurBankDetailsClass> parseBankDetails(){

        List<OurBankDetailsClass> list = new ArrayList<>();
        try{
            JSONArray jsonArray = new JSONArray(json);
            if (jsonArray.length() >= 1) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    OurBankDetailsClass bankClass = new OurBankDetailsClass();
                    bankClass.setBankName(jsonArray.getJSONObject(i).getString("BankName"));
                    bankClass.setAccNo(jsonArray.getJSONObject(i).getString("AccountNo"));
                    bankClass.setBankId(jsonArray.getJSONObject(i).getInt("BankId"));
                    bankClass.setBranchId(jsonArray.getJSONObject(i).getInt("BranchId"));
                    bankClass.setIFSC(jsonArray.getJSONObject(i).getString("IFSC"));
                    bankClass.setMICR(jsonArray.getJSONObject(i).getString("MICR"));
                    bankClass.setHOCode(jsonArray.getJSONObject(i).getInt("HoCode"));
                    list.add(bankClass);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            writeLog("parseBankDetails" + e.getMessage());
        }
        return list;
    }

    public List<CheckAvailStockClass> parseAvailQty(){
        List<CheckAvailStockClass> list = new ArrayList<>();
        try{
            JSONArray jsonArray = new JSONArray(new JSONObject(json).get("CheckLooseStockResult").toString());
            if (jsonArray.length() >= 1) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    CheckAvailStockClass checkStock = new CheckAvailStockClass();
                    checkStock.setProductid(jsonArray.getJSONObject(i).getInt("Product_id"));
                    checkStock.setColor(jsonArray.getJSONObject(i).getString("Color"));
                    checkStock.setSizegroup(jsonArray.getJSONObject(i).getString("SizeGroup"));
                    checkStock.setAvailQty(jsonArray.getJSONObject(i).getInt("AvailQty"));
                    checkStock.setRate(jsonArray.getJSONObject(i).getString("Rate"));
                    checkStock.setBranchid(jsonArray.getJSONObject(i).getInt("BranchId"));
                    checkStock.setAlias(jsonArray.getJSONObject(i).getString("Alias"));
                    checkStock.setStat(jsonArray.getJSONObject(i).getString("Stat"));
                    checkStock.setHashcode(jsonArray.getJSONObject(i).getString("HashCode"));
                    list.add(checkStock);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            writeLog("parseAvailQty_" + e.getMessage());
        }
        return list;
    }

    public double parseCustDiscLimit(){
        double ret = 0;
        try{
            JSONArray jsonArray = new JSONArray(json);
            if (jsonArray.length() >= 1) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    ret = jsonArray.getJSONObject(i).getDouble("Limit");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            writeLog("parseCustDiscLimit_"+e.getMessage());
        }
        return ret;
    }

    public String parseVersion() {
        String data = null;
        try {
            JSONArray jsonArray = new JSONArray(json);
            if (jsonArray.length() >= 1) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    data = jsonArray.getJSONObject(i).getString("cmob_version");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            writeLog("ParseJSON_parseVersion_"+e.getMessage());
        }
        return data;
    }

    public int parseGetMaxAuto(){
        int ret = 0;
        try{
            JSONArray jsonArray = new JSONArray(json);
            if (jsonArray.length() >= 1) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    ret = jsonArray.getJSONObject(i).getInt("maxAuto");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            writeLog("parseAreaMaster_"+e.getMessage());
        }
        return ret;
    }

    private void writeLog(String _data){
        new WriteLog().writeLog(context,"ParseJSON_"+_data);
    }

}
