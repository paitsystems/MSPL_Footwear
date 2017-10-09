package com.lnbinfotech.msplfootwearex.parse;

import android.content.Context;

import com.lnbinfotech.msplfootwearex.db.DBHandler;
import com.lnbinfotech.msplfootwearex.log.WriteLog;
import com.lnbinfotech.msplfootwearex.model.AreaMasterClass;
import com.lnbinfotech.msplfootwearex.model.CityMasterClass;
import com.lnbinfotech.msplfootwearex.model.CustomerDetailClass;
import com.lnbinfotech.msplfootwearex.model.EmployeeMasterClass;
import com.lnbinfotech.msplfootwearex.model.HOMasterClass;
import com.lnbinfotech.msplfootwearex.model.ProductMasterClass;
import com.lnbinfotech.msplfootwearex.model.StockInfoMasterClass;

import org.json.JSONArray;

import java.util.ArrayList;

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
                    custClass.setGSTNo(jsonArray.getJSONObject(i).getString("GSTNo"));
                    custClass.setImagePath(jsonArray.getJSONObject(i).getString("ImagePath"));
                    db.addCustomerDetail(custClass);
                    list.add(custClass);
                }
                db.close();
            }
        }catch (Exception e){
            e.printStackTrace();
            writeLog("parseCustDetail_"+e.getMessage());
        }
        return list;
    }

    public void parseAreaMaster(){
        try{
            JSONArray jsonArray = new JSONArray(json);
            if (jsonArray.length() >= 1) {
                db.deleteTable(DBHandler.Table_AreaMaster);
                for (int i = 0; i < jsonArray.length(); i++) {
                    AreaMasterClass areaClass = new AreaMasterClass();
                    areaClass.setAuto(jsonArray.getJSONObject(i).getInt("auto"));
                    areaClass.setId(jsonArray.getJSONObject(i).getInt("id"));
                    areaClass.setArea(jsonArray.getJSONObject(i).getString("area"));
                    areaClass.setCityid(jsonArray.getJSONObject(i).getInt("cityid"));
                    db.addAreaMaster(areaClass);
                }
                db.close();
            }
        }catch (Exception e){
            e.printStackTrace();
            writeLog("parseAreaMaster_"+e.getMessage());
        }
    }

    public void parseCityMaster(){
        try{
            JSONArray jsonArray = new JSONArray(json);
            if (jsonArray.length() >= 1) {
                db.deleteTable(DBHandler.Table_CityMaster);
                for (int i = 0; i < jsonArray.length(); i++) {
                    CityMasterClass cityClass = new CityMasterClass();
                    cityClass.setAuto(jsonArray.getJSONObject(i).getInt("auto"));
                    cityClass.setId(jsonArray.getJSONObject(i).getInt("id"));
                    cityClass.setCity(jsonArray.getJSONObject(i).getString("city"));
                    cityClass.setStId(jsonArray.getJSONObject(i).getInt("stId"));
                    db.addCityMaster(cityClass);
                }
                db.close();
            }
        }catch (Exception e){
            e.printStackTrace();
            writeLog("parseCityMaster_"+e.getMessage());
        }
    }

    public void parseHOMaster(){
        try{
            JSONArray jsonArray = new JSONArray(json);
            if (jsonArray.length() >= 1) {
                db.deleteTable(DBHandler.Table_HOMaster);
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
            }
        }catch (Exception e){
            e.printStackTrace();
            writeLog("parseHOMaster_"+e.getMessage());
        }
    }

    public void parseEmployeeMaster(){
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
            }
        }catch (Exception e){
            e.printStackTrace();
            writeLog("parseEmployeeMaster_"+e.getMessage());
        }
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

    public void parseProductMaster(){
        try{
            JSONArray jsonArray = new JSONArray(json);
            if (jsonArray.length() >= 1) {
                db.deleteTable(DBHandler.Table_ProductMaster);
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
                    db.addProductMaster(prodClass);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            writeLog("parseProductMaster_"+e.getMessage());
        }
    }

    public void parseCustomerMaster(){
        try{
            JSONArray jsonArray = new JSONArray(json);
            if (jsonArray.length() >= 1) {
                db.deleteTable(DBHandler.Table_Customermaster);
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
                    db.addCustomerMaster(custClass);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            writeLog("parseCustomerMaster_"+e.getMessage());
        }
    }

    private void writeLog(String _data){
        new WriteLog().writeLog(context,"ParseJSON_"+_data);
    }

}
