package com.lnbinfotech.msplfootwear.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lnbinfotech.msplfootwear.constant.Constant;
import com.lnbinfotech.msplfootwear.model.CustomerDetailClass;
import com.lnbinfotech.msplfootwear.model.UserGetterSetterClass;

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
    public static final String CM_Email = "Email";
    public static final String CM_PANNo = "PANNo";
    public static final String CM_GSTNo = "GSTNo";
    public static final String CM_ImagePath = "ImagePath";
    public static final String CM_PIN = "PIN";

    public DBHandler(Context context) {
        super(context, Database_Name, null, Database_Version);
    }

    String create_cust_master = "create table if not exists "+Table_Customermaster+"("+
            CM_RetailCustID+" int,"+CM_Name+" text,"+CM_Address+" text,"+CM_MobileNo+" text,"+
            CM_Email+" text,"+CM_PANNo+" text,"+CM_GSTNo+" text,"+CM_ImagePath+" text,"+CM_PIN+" text)";

    @Override
    public void onCreate(SQLiteDatabase db) {
        Constant.showLog(create_cust_master);
        db.execSQL(create_cust_master);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void addCustomerDetail(CustomerDetailClass cust) {
        ContentValues cv = new ContentValues();
        cv.put(CM_RetailCustID,cust.getCustID());
        cv.put(CM_Name,cust.getName());
        cv.put(CM_Address,cust.getAddress());
        cv.put(CM_MobileNo,cust.getMobile());
        cv.put(CM_Email,cust.getEmail());
        cv.put(CM_PANNo,cust.getPANno());
        cv.put(CM_GSTNo,cust.getGSTNo());
        cv.put(CM_ImagePath,cust.getImagePath());
        cv.put(CM_PIN,"-1");
        getWritableDatabase().insert(Table_Customermaster,null,cv);
    }

    public ArrayList<CustomerDetailClass> getCustomerDetail(){
        ArrayList<CustomerDetailClass> list = new ArrayList<>();
        String str = "select * from "+Table_Customermaster;
        Cursor res = getWritableDatabase().rawQuery(str,null);
        if(res.moveToFirst()){
            do{
                CustomerDetailClass custClass = new CustomerDetailClass();
                custClass.setCustID(res.getInt(res.getColumnIndex(CM_RetailCustID)));
                custClass.setName(res.getString(res.getColumnIndex(CM_Name)));
                custClass.setAddress(res.getString(res.getColumnIndex(CM_Address)));
                custClass.setMobile(res.getString(res.getColumnIndex(CM_MobileNo)));
                custClass.setEmail(res.getString(res.getColumnIndex(CM_Email)));
                custClass.setPANno(res.getString(res.getColumnIndex(CM_PANNo)));
                custClass.setGSTNo(res.getString(res.getColumnIndex(CM_GSTNo)));
                custClass.setImagePath(res.getString(res.getColumnIndex(CM_ImagePath)));
                list.add(custClass);
            }while (res.moveToNext());
        }
        res.close();
        return list;
    }

    public String getCustPIN(String custid){
        String pin = "-1";
        String str = "select "+CM_PIN+" from "+Table_Customermaster+" where "+CM_RetailCustID+"="+custid;
        Cursor res = getWritableDatabase().rawQuery(str,null);
        if(res.moveToFirst()){
            do{
                pin = res.getString(res.getColumnIndex(CM_PIN));
            }while (res.moveToNext());
        }
        res.close();
        return pin;
    }

    public List<String> checkPINUnsetID(){
        List<String> list = new ArrayList<>();
        String str = "select "+CM_PIN+" from "+Table_Customermaster+" where "+CM_PIN+"=-1";
        Cursor res = getWritableDatabase().rawQuery(str,null);
        if(res.moveToFirst()){
            do{
                list.add(res.getString(res.getColumnIndex(CM_PIN)));
            }while (res.moveToNext());
        }
        res.close();
        return list;
    }


    public void updatePIN(String custid, String pin){
        ContentValues cv = new ContentValues();
        cv.put(CM_PIN,pin);
        getWritableDatabase().update(Table_Customermaster,cv,CM_RetailCustID+"=?",new String[]{custid});
    }
}


