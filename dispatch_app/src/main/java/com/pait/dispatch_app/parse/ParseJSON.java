package com.pait.dispatch_app.parse;

import android.content.Context;
import android.content.SharedPreferences;

import com.pait.dispatch_app.FirstActivity;
import com.pait.dispatch_app.R;
import com.pait.dispatch_app.db.DBHandler;
import com.pait.dispatch_app.log.WriteLog;
import com.pait.dispatch_app.model.CompanyMasterClass;
import com.pait.dispatch_app.model.EmployeeMasterClass;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

// Created by lnb on 8/11/2016.

public class ParseJSON {

    private String json;
    private Context context;
    private DBHandler db;

    public ParseJSON(String _json) {
        this.json = _json;
    }

    public ParseJSON(String _json, Context _context) {
        this.json = _json;
        this.context = _context;
        db = new DBHandler(context);
    }

    public ArrayList<UserClass> parseUserDetail() {
        ArrayList<UserClass> list = new ArrayList<>();
        try {
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
                    editor.putInt(context.getString(R.string.pref_branchid), userClass.getBranchId());
                    editor.putInt(context.getString(R.string.pref_retailCustId), userClass.getCustID());
                    editor.putInt(context.getString(R.string.pref_cityid), userClass.getCityId());
                    editor.apply();

                    db.addUserDetail(userClass);
                    list.add(userClass);
                }
                db.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            writeLog("parseCustDetail_" + e.getMessage());
        }
        return list;
    }

    public String parseVersion() {
        String data = null;
        try {
            JSONArray jsonArray = new JSONArray(json);
            if (jsonArray.length() >= 1) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    data = jsonArray.getJSONObject(i).getString("mob_version");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            writeLog("ParseJSON_parseVersion_" + e.getMessage());
        }
        return data;
    }

    public int parseEmployeeMaster(int max) {
        int ret = 1;
        try {
            JSONArray jsonArray = new JSONArray(json);
            if (jsonArray.length() >= 1) {
                if (max == 0) {
                    db.deleteTable(DBHandler.Table_Employee);
                }
                List<EmployeeMasterClass> list = new ArrayList<>();
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
                    list.add(empClass);
                }
                db.addEmployeeMaster(list);
                db.close();
                ret = 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            writeLog("parseEmployeeMaster_" + e.getMessage());
            ret = 0;
        }
        return ret;
    }

    public int parseCompanyMaster(int max) {
        int ret = 1;
        try {
            JSONArray jsonArray = new JSONArray(json);
            if (jsonArray.length() >= 1) {
                if (max == 0) {
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
        } catch (Exception e) {
            e.printStackTrace();
            writeLog("parseCompanyMaster_" + e.getMessage());
            ret = 0;
        }
        return ret;
    }

    private void writeLog(String _data) {
        new WriteLog().writeLog(context, "ParseJSON_" + _data);
    }

}
