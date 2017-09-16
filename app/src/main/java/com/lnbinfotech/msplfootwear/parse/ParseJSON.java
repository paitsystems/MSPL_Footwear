package com.lnbinfotech.msplfootwear.parse;

import android.content.Context;

import com.lnbinfotech.msplfootwear.db.DBHandler;
import com.lnbinfotech.msplfootwear.model.CustomerDetailClass;

import org.json.JSONArray;

import java.util.ArrayList;

// Created by lnb on 8/11/2016.

public class ParseJSON {

    String json;
    Context context;

    public ParseJSON(String _json){
        this.json = _json;
    }

    public ParseJSON(String _json, Context _context){
        this.json = _json;
        this.context = _context;
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
                    new DBHandler(context).addCustomerDetail(custClass);
                    list.add(custClass);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }
}
