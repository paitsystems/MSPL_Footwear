package com.lnbinfotech.msplfootwearex.parse;

import android.content.Context;
import android.content.SharedPreferences;

import com.lnbinfotech.msplfootwearex.FirstActivity;
import com.lnbinfotech.msplfootwearex.R;

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

    public String parseGetCountData(){
        String data = null;
       try{
            JSONArray jsonArray = new JSONArray(json);
            if (jsonArray.length() >= 1) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    String total = jsonArray.getJSONObject(i).getString("Total");
                    String complete = jsonArray.getJSONObject(i).getString("Complete");
                    String pending = jsonArray.getJSONObject(i).getString("Pending");
                    data = total + "^" + complete + "^" + pending;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return data;
    }

    public int parseUserData() {
        int result = 0;
        try {
            JSONArray jsonArray = new JSONArray(json);
            if (jsonArray.length() >= 1) {
                int auto = 0;
                String ClientID=null,ClientName=null,FTPUser=null,FTPPass=null,FTPFolder=null,CustomerName=null, mobile="0", FTPLocation = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    auto = jsonArray.getJSONObject(i).getInt("Auto");
                    ClientID = jsonArray.getJSONObject(i).getString("ClientID");
                    ClientName = jsonArray.getJSONObject(i).getString("ClientName");
                    mobile = jsonArray.getJSONObject(i).getString("Mobile");
                    FTPLocation = jsonArray.getJSONObject(i).getString("FTPLocation");
                    FTPUser = jsonArray.getJSONObject(i).getString("FTPUser");
                    FTPPass = jsonArray.getJSONObject(i).getString("FTPPass");
                    FTPFolder = jsonArray.getJSONObject(i).getString("FTPImgFolder");
                    CustomerName = jsonArray.getJSONObject(i).getString("CustomerName");
                }
                SharedPreferences.Editor editor = FirstActivity.pref.edit();
                editor.putInt(context.getString(R.string.pref_auto), auto);
                editor.putString(context.getString(R.string.pref_clientID), ClientID);
                editor.putString(context.getString(R.string.pref_ClientName), ClientName);
                editor.putString(context.getString(R.string.pref_mobno), mobile);
                if(FTPLocation!=null && !FTPLocation.equals("")) {
                    if(FTPLocation.length()>=6) {
                        FTPLocation = FTPLocation.substring(6, FTPLocation.length() - 1);
                    }else{
                        FTPLocation = "";
                    }
                }
                editor.putString(context.getString(R.string.pref_FTPLocation), FTPLocation);
                editor.putString(context.getString(R.string.pref_FTPUser), FTPUser);
                editor.putString(context.getString(R.string.pref_FTPPass), FTPPass);
                editor.putString(context.getString(R.string.pref_FTPImgFolder), FTPFolder);
                editor.putString(context.getString(R.string.pref_CustomerName), CustomerName);
                editor.apply();
                result = 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
