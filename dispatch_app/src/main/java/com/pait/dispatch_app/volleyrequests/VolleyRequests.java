package com.pait.dispatch_app.volleyrequests;

//Created by lnb on 9/15/2017.

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.pait.dispatch_app.constant.AppSingleton;
import com.pait.dispatch_app.constant.Constant;
import com.pait.dispatch_app.interfaces.ServerCallback;
import com.pait.dispatch_app.log.WriteLog;
import com.pait.dispatch_app.parse.ParseJSON;
import com.pait.dispatch_app.parse.UserClass;

import org.json.JSONArray;

import java.util.ArrayList;

public class VolleyRequests {

    private Context context;
    private String writeFilename = "Write.txt";

    public VolleyRequests(Context _context) {
        this.context = _context;
    }

    public void getOTPCode(String url, final ServerCallback callback) {
        StringRequest request = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Constant.showLog(response);
                        response = response.replace("\\", "");
                        response = response.replace("\"", "");
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onFailure("getOTPCode_VolleyError_"+error.getMessage());
                        Constant.showLog(error.getMessage());
                        writeLog("getOTPCode_" + error.getMessage());
                    }
                }
        );
        AppSingleton.getInstance(context).addToRequestQueue1(request, "OTP");
    }

    public void getUserDetail(String url, final ServerCallback callback) {
        StringRequest request = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Constant.showLog(response);
                        response = response.replace("\\", "");
                        response = response.replace("''", "");
                        response = response.substring(1, response.length() - 1);
                        ArrayList<UserClass> list = new ParseJSON(response, context).parseUserDetail();
                        if(list!=null){
                            if(list.size()!=0){
                                Constant.showLog(list.size() + "");
                                callback.onSuccess(response);
                            }else{
                                callback.onFailure("Error");
                            }
                        }else{
                            callback.onFailure("Error");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onFailure("Error");
                        Constant.showLog(error.getMessage());
                        writeLog("getCustomerDetail_" + error.getMessage());
                    }
                }
        );
        AppSingleton.getInstance(context).addToRequestQueue(request, "OTP");
    }

    public void updateIMEINo(String url, final ServerCallback callback) {
        StringRequest request = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Constant.showLog(response);
                        response = response.replace("\\", "");
                        response = response.replace("\"", "");
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onFailure("updateIMEINo_VolleyError_"+error.getMessage());
                        Constant.showLog(error.getMessage());
                        writeLog("updateIMEINo_" + error.getMessage());
                    }
                }
        );
        AppSingleton.getInstance(context).addToRequestQueue(request, "IMEINo");
    }

    public void getActiveStatus(String url, final ServerCallback callback) {
        StringRequest request = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Constant.showLog(response);
                        response = response.replace("\\", "");
                        response = response.replace("''", "");
                        response = response.substring(1, response.length() - 1);
                        String status = "C";
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            if (jsonArray.length() >= 1) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    status = jsonArray.getJSONObject(i).getString("status");
                                }
                                callback.onSuccess(status);
                            }else{
                                callback.onFailure("Error");
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            callback.onFailure("Error");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onFailure("getActiveStatus_" + error.getMessage());
                        Constant.showLog(error.getMessage());
                        writeLog("getActiveStatus_"+error.getMessage());
                    }
                }
        );
        AppSingleton.getInstance(context).addToRequestQueue1(request, "AREA");
    }

    public void refreshEmployeeMaster(String url, final ServerCallback callback,final int max) {
        StringRequest request = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Constant.showLog(response);
                        response = response.replace("\\", "");
                        response = response.replace("''", "");
                        response = response.substring(1, response.length() - 1);
                        int ret = new ParseJSON(response, context).parseEmployeeMaster(max);
                        if(ret == 1) {
                            callback.onSuccess(response);
                        }else{
                            callback.onFailure("Error");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onFailure("refreshEmployeeMaster_" + error.getMessage());
                        Constant.showLog(error.getMessage());
                        writeLog("refreshEmployeeMaster_"+error.getMessage());
                    }
                }
        );
        AppSingleton.getInstance(context).addToRequestQueue(request, "CITY");
    }

    public void refreshCompanyMaster(String url, final ServerCallback callback,final int max) {
        StringRequest request = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Constant.showLog(response);
                        response = response.replace("\\\\r\\\\n", "");
                        response = response.replace("\\", "");
                        response = response.replace("''", "");
                        response = response.substring(1, response.length() - 1);
                        int ret = new ParseJSON(response, context).parseCompanyMaster(max);
                        if(ret == 1) {
                            callback.onSuccess(response);
                        }else{
                            callback.onFailure("Error");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onFailure("refreshCompanyMaster_" + error.getMessage());
                        Constant.showLog(error.getMessage());
                        writeLog("refreshCompanyMaster_"+error.getMessage());
                    }
                }
        );
        AppSingleton.getInstance(context).addToRequestQueue(request, "OTP");
    }

    private void writeLog(String _data) {
        new WriteLog().writeLog(context, "VolleyRequest_" + _data);
    }

}
