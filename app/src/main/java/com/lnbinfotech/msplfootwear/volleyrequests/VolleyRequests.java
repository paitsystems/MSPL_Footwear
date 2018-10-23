package com.lnbinfotech.msplfootwear.volleyrequests;

//Created by lnb on 9/15/2017.

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.lnbinfotech.msplfootwear.constant.AppSingleton;
import com.lnbinfotech.msplfootwear.constant.Constant;
import com.lnbinfotech.msplfootwear.db.DBHandler;
import com.lnbinfotech.msplfootwear.interfaces.ServerCallback;
import com.lnbinfotech.msplfootwear.interfaces.ServerCallbackList;
import com.lnbinfotech.msplfootwear.log.WriteLog;
import com.lnbinfotech.msplfootwear.model.CheckAvailStockClass;
import com.lnbinfotech.msplfootwear.model.CheckoutCustOrderClass;
import com.lnbinfotech.msplfootwear.model.CustomerDetailClass;
import com.lnbinfotech.msplfootwear.model.LedgerReportClass;
import com.lnbinfotech.msplfootwear.model.OurBankDetailsClass;
import com.lnbinfotech.msplfootwear.model.OuststandingReportClass;
import com.lnbinfotech.msplfootwear.model.StockInfoMasterClass;
import com.lnbinfotech.msplfootwear.model.TrackOrderDetailClass;
import com.lnbinfotech.msplfootwear.model.TrackOrderMasterClass;
import com.lnbinfotech.msplfootwear.model.UserClass;
import com.lnbinfotech.msplfootwear.parse.ParseJSON;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
        AppSingleton.getInstance(context).addToRequestQueue(request, "OTP");
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

    public void getCustomerDetail(String url, final ServerCallback callback) {
        StringRequest request = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Constant.showLog(response);
                        response = response.replace("\\", "");
                        response = response.replace("''", "");
                        response = response.substring(1, response.length() - 1);
                        ArrayList<CustomerDetailClass> list = new ParseJSON(response, context).parseCustDetail();
                        Constant.showLog(list.size() + "");
                        callback.onSuccess(response);
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

    public void saveCustomerDetail(String url, final ServerCallback callback) {
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Constant.showLog(response);
                response = response.replace("\\", "");
                response = response.replace("\"", "");
                if (response.equals("1")) {
                    callback.onSuccess(response);
                } else {
                    callback.onFailure("Error");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailure("saveCustomerDetail_Error_"+error.getMessage());
                Constant.showLog(error.getMessage());
            }
        });
        AppSingleton.getInstance(context).addToRequestQueue(request, "");

    }

    public void refreshAreaMaster(String url, final ServerCallback callback) {
        StringRequest request = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Constant.showLog(response);
                        response = response.replace("\\", "");
                        response = response.replace("''", "");
                        response = response.substring(1, response.length() - 1);
                        int ret = new ParseJSON(response, context).parseAreaMaster();
                        if (ret == 1) {
                            callback.onSuccess(response);
                        } else {
                            callback.onFailure("Error");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onFailure("refreshAreaMaster_" + error.getMessage());
                        Constant.showLog(error.getMessage());
                        writeLog("refreshAreaMaster_" + error.getMessage());
                    }
                }
        );
        AppSingleton.getInstance(context).addToRequestQueue(request, "AREA");
    }

    public void refreshCityMaster(String url, final ServerCallback callback) {
        StringRequest request = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Constant.showLog(response);
                        response = response.replace("\\", "");
                        response = response.replace("''", "");
                        response = response.substring(1, response.length() - 1);
                        int ret = new ParseJSON(response, context).parseCityMaster();
                        if (ret == 1) {
                            callback.onSuccess(response);
                        } else {
                            callback.onFailure("Error");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onFailure("refreshCityMaster_" + error.getMessage());
                        Constant.showLog(error.getMessage());
                        writeLog("refreshCityMaster_" + error.getMessage());
                    }
                }
        );
        AppSingleton.getInstance(context).addToRequestQueue(request, "CITY");
    }

    public void refreshHOMaster(String url, final ServerCallback callback, final int max) {
        StringRequest request = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Constant.showLog(response);
                        response = response.replace("\\", "");
                        response = response.replace("''", "");
                        response = response.substring(1, response.length() - 1);
                        int ret = new ParseJSON(response, context).parseHOMaster(max);
                        if (ret == 1) {
                            callback.onSuccess(response);
                        } else {
                            callback.onFailure("Error");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onFailure("refreshHOMaster_" + error.getMessage());
                        Constant.showLog(error.getMessage());
                        writeLog("refreshHOMaster_" + error.getMessage());
                    }
                }
        );
        AppSingleton.getInstance(context).addToRequestQueue(request, "CITY");
    }

    public void refreshEmployeeMaster(String url, final ServerCallback callback) {
        StringRequest request = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Constant.showLog(response);
                        response = response.replace("\\", "");
                        response = response.replace("''", "");
                        response = response.substring(1, response.length() - 1);
                        int ret = new ParseJSON(response, context).parseEmployeeMaster();
                        if (ret == 1) {
                            callback.onSuccess(response);
                        } else {
                            callback.onFailure("Error");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onFailure("refreshEmployeeMaster_" + error.getMessage());
                        Constant.showLog(error.getMessage());
                        writeLog("refreshEmployeeMaster_" + error.getMessage());
                    }
                }
        );
        AppSingleton.getInstance(context).addToRequestQueue(request, "CITY");
    }

    public void refreshStockInfo(String url, final ServerCallback callback) {
        StringRequest request = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Constant.showLog(response);
                        //response = response.replace("\\", "");
                        //response = response.replace("''", "");
                        //response = response.substring(1, response.length() - 1);
                        //new ParseJSON(response, context).parseStockInfo();
                        //AtomicInteger workInteger = new AtomicInteger(1);
                        //new readJSON(workInteger,response);
                        response = response.substring(1, response.length() - 1);
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onFailure("refreshStockInfo_" + error.getMessage());
                        Constant.showLog(error.getMessage());
                        writeLog("refreshStockInfo_" + error.getMessage());

                    }
                }
        );
        AppSingleton.getInstance(context).addToRequestQueue(request, "CITY");
    }

    public void refreshSizeNDesignMaster(String url, final ServerCallback callback) {
        StringRequest request = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        response = response.substring(1, response.length() - 1);
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onFailure("refreshSizeNDesignMaster_" + error.getMessage());
                        Constant.showLog(error.getMessage());
                        writeLog("refreshSizeNDesignMaster_" + error.getMessage());
                    }
                }
        );
        AppSingleton.getInstance(context).addToRequestQueue(request, "CITY");
    }

    public void refreshSizeNDesignMaster1(String url, final ServerCallback callback) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Constant.showLog(response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        AppSingleton.getInstance(context).addToRequestQueue(request, "CITY");
    }

    private class readJSON extends AsyncTask<Void, Void, String> {
        private final AtomicInteger workCounter1;
        String result;

        readJSON(AtomicInteger _workCounter, String _result) {
            workCounter1 = _workCounter;
            result = _result;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String retValue = "A";
            File sdFile = Constant.checkFolder(Constant.folder_name);
            FileWriter writer;
            try {
                String search = "\\\\", replace = "";
                File writeFile = new File(sdFile, writeFilename);
                writer = new FileWriter(writeFile);
                int size = result.length();
                if (size > 2) {
                    Log.d("Log", "Replacing");
                    int b = 50000;
                    for (int i = 0; i < size; i++) {
                        if (b >= size) {
                            b = size;
                        }
                        Constant.showLog("Size-" + size + " i-" + i + " b-" + b);
                        String q = result.substring(i, b);
                        String g = q.replaceAll(search, replace);
                        System.gc();
                        writer.append(g);
                        i = b - 1;
                        b = b + 50000;
                    }
                    retValue = "";
                }
                writer.flush();
                writer.close();
                return retValue;
            } catch (IOException | OutOfMemoryError e) {
                try {
                    writer = new FileWriter(new File(sdFile, "Log.txt"));
                    writer.append(e.getMessage());
                    writer.flush();
                    writer.close();
                } catch (Exception e1) {
                    e.printStackTrace();
                }
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            int tasksLeft = this.workCounter1.decrementAndGet();
            if (tasksLeft == 0 && s.equals("")) {
                AtomicInteger workInteger = new AtomicInteger(1);
                //new writeDB(workInteger).execute();
            } else if (tasksLeft == 0) {
                //showDia(2);
            }
        }
    }

    private class writeDB extends AsyncTask<Void, Void, String> {
        private final AtomicInteger workCounter2;
        File writeFile;

        writeDB(AtomicInteger _workCounter) {
            workCounter2 = _workCounter;
        }

        @Override
        protected String doInBackground(Void... voids) {
            File sdFile = Constant.checkFolder(Constant.folder_name);
            JsonFactory f = new JsonFactory();
            try {
                writeFile = new File(sdFile, writeFilename);
                JsonParser jp = f.createJsonParser(writeFile);
                int count = 0;
                DBHandler db = new DBHandler(context);
                db.deleteTable(DBHandler.Table_StockInfo);
                while (jp.nextToken() != org.codehaus.jackson.JsonToken.END_ARRAY) {
                    count++;
                    StockInfoMasterClass stockInfo = new StockInfoMasterClass();
                    while (jp.nextToken() != org.codehaus.jackson.JsonToken.END_OBJECT) {
                        String token = jp.getCurrentName();
                        if ("Company".equals(token)) {
                            jp.nextToken();
                            stockInfo.setCompany(jp.getText());
                        } else if ("ProductId".equals(token)) {
                            jp.nextToken();
                            stockInfo.setProductId(jp.getText());
                        } else if ("Color".equals(token)) {
                            jp.nextToken();
                            stockInfo.setColor(jp.getText());
                        } else if ("Size".equals(token)) {
                            jp.nextToken();
                            stockInfo.setSize(jp.getText());
                        } else if ("Rate".equals(token)) {
                            jp.nextToken();
                            stockInfo.setRate(jp.getText());
                        } else if ("LQty".equals(token)) {
                            jp.nextToken();
                            stockInfo.setLQty(Integer.parseInt(jp.getText()));
                        } else if ("PQty".equals(token)) {
                            jp.nextToken();
                            stockInfo.setPQty(Integer.parseInt(jp.getText()));
                        } else if ("PackUnpack".equals(token)) {
                            jp.nextToken();
                            stockInfo.setPackUnpack(jp.getText());
                        } else if ("PerPackQty".equals(token)) {
                            jp.nextToken();
                            stockInfo.setPerPackQty(Integer.parseInt(jp.getText()));
                        } else if ("SaleRate".equals(token)) {
                            jp.nextToken();
                            stockInfo.setSaleRate(jp.getText());
                        } else if ("Product_id".equals(token)) {
                            jp.nextToken();
                            stockInfo.setProduct_id(Integer.parseInt(jp.getText()));
                        }
                    }
                    db.addStockInfo(stockInfo);
                }
                Log.d("Log", "" + count);
                return "";
            } catch (Exception e) {
                try {
                    FileWriter writer = new FileWriter(new File(sdFile, "Log.txt"));
                    writer.append(e.getMessage());
                    writer.flush();
                    writer.close();
                } catch (Exception e1) {
                    e.printStackTrace();
                    return null;
                }
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            int tasksLeft = this.workCounter2.decrementAndGet();
            if (tasksLeft == 0 && s.equals("")) {
                if (writeFile.delete()) {
                    Log.d("Log", "Write Delete");
                }
                //showDia(3);
            } else if (tasksLeft == 0) {
                if (writeFile.delete()) {
                    Log.d("Log", "Write Delete");
                }
                //showDia(4);
            }
        }
    }

    public void refreshProductMaster(String url, final ServerCallback callback, final int from, final int to) {
        StringRequest request = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Constant.showLog(response);
                        response = response.replace("\\", "");
                        response = response.replace("''", "");
                        response = response.substring(1, response.length() - 1);
                        int ret = new ParseJSON(response, context).parseProductMaster(from, to);
                        if (ret == 1) {
                            callback.onSuccess(response);
                        } else {
                            callback.onFailure("Error");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onFailure("refreshProductMaster_" + error.getMessage());
                        Constant.showLog(error.getMessage());
                        writeLog("refreshProductMaster_" + error.getMessage());
                    }
                }
        );
        AppSingleton.getInstance(context).addToRequestQueue(request, "CITY");
    }

    public void refreshCustomerMaster(String url, final ServerCallback callback) {
        StringRequest request = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Constant.showLog(response);
                        response = response.replace("\\", "");
                        response = response.replace("''", "");
                        response = response.substring(1, response.length() - 1);
                        int ret = new ParseJSON(response, context).parseCustomerMaster();
                        if (ret == 1) {
                            callback.onSuccess(response);
                        } else {
                            callback.onFailure("Error");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onFailure("refreshCustomerMaster_" + error.getMessage());
                        Constant.showLog(error.getMessage());
                        writeLog("refreshCustomerMaster_" + error.getMessage());
                    }
                }
        );
        AppSingleton.getInstance(context).addToRequestQueue(request, "OTP");
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
                        if (ret == 1) {
                            callback.onSuccess(response);
                        } else {
                            callback.onFailure("Error");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onFailure("refreshCompanyMaster_" + error.getMessage());
                        Constant.showLog(error.getMessage());
                        writeLog("refreshCompanyMaster_" + error.getMessage());
                    }
                }
        );
        AppSingleton.getInstance(context).addToRequestQueue(request, "OTP");
    }

    public void refreshBankMaster(String url, final ServerCallback callback) {
        StringRequest request = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Constant.showLog(response);
                        response = response.replace("\\", "");
                        response = response.replace("''", "");
                        response = response.substring(1, response.length() - 1);
                        int ret = new ParseJSON(response, context).parseBankMaster();
                        if (ret == 1) {
                            callback.onSuccess(response);
                        } else {
                            callback.onFailure("Error");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onFailure("refreshBankMaster_" + error.getMessage());
                        Constant.showLog(error.getMessage());
                        writeLog("refreshBankMaster_" + error.getMessage());
                    }
                }
        );
        AppSingleton.getInstance(context).addToRequestQueue(request, "OTP");
    }

    public void refreshBankBranchMaster(String url, final ServerCallback callback) {
        StringRequest request = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Constant.showLog(response);
                        response = response.replace("\\", "");
                        response = response.replace("''", "");
                        response = response.substring(1, response.length() - 1);
                        int ret = new ParseJSON(response, context).parseBankBranchMaster();
                        if (ret == 1) {
                            callback.onSuccess(response);
                        } else {
                            callback.onFailure("Error");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onFailure("Error");
                        Constant.showLog(error.getMessage());
                        writeLog("refreshBankBranchMaster_" + error.getMessage());
                    }
                });
        AppSingleton.getInstance(context).addToRequestQueue(request, "OTP");
    }

    public void refreshDocumentMaster(String url, final ServerCallback callback) {
        StringRequest request = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Constant.showLog(response);
                        response = response.replace("\\", "");
                        response = response.replace("''", "");
                        response = response.substring(1, response.length() - 1);
                        int ret = new ParseJSON(response, context).parseDocumentMaster();
                        if (ret == 1) {
                            callback.onSuccess(response);
                        } else {
                            callback.onFailure("Error");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onFailure("Error");
                        Constant.showLog(error.getMessage());
                        writeLog("refreshDocumentMaster_" + error.getMessage());
                    }
                });
        AppSingleton.getInstance(context).addToRequestQueue(request, "OTP");
    }

    public void saveFeedbackDetail(String url, final ServerCallback callback) {
        StringRequest request = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Constant.showLog(response);
                        response = response.replace("\\", "");
                        response = response.replace("\"", "");
                        if (response.equals("1")) {
                            callback.onSuccess(response);
                        } else {
                            callback.onFailure("Error");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onFailure("Error");
                        Constant.showLog(error.getMessage());
                    }
                });
        AppSingleton.getInstance(context).addToRequestQueue(request, "");

    }

    public void loadTrackOrder(String url, final ServerCallbackList callback) {
        StringRequest request = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Constant.showLog(response);
                        response = response.replace("\\", "");
                        response = response.replace("''", "");
                        response = response.substring(1, response.length() - 1);
                        List<TrackOrderMasterClass> list = new ParseJSON(response, context).parseloadTrackOreder();
                        if (list.size() != 0) {
                            callback.onSuccess(list);
                        } else {
                            callback.onFailure("Error");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onFailure("Error");
                        Constant.showLog(error.getMessage());
                        writeLog("loadTrackOreder_" + error.getMessage());
                    }
                });
        AppSingleton.getInstance(context).addToRequestQueue(request, "OTP");
    }

    public void loadDetailOrder(String url, final ServerCallbackList callback) {
        StringRequest request = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Constant.showLog(response);
                        response = response.replace("\\", "");
                        response = response.replace("''", "");
                        response = response.substring(1, response.length() - 1);
                        int a = new ParseJSON(response, context).parseloadDetailOrderChanged();
                        if(a==1) {
                            callback.onSuccess(a);
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
                        writeLog("loadTrackOrder_" + error.getMessage());
                    }
                });
        AppSingleton.getInstance(context).addToRequestQueue(request, "OTP");
    }

    public void refreshGSTMaster(String url, final ServerCallback callback,final int max) {
        StringRequest request = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Constant.showLog(response);
                        response = response.replace("\\", "");
                        response = response.replace("''", "");
                        response = response.substring(1, response.length() - 1);
                        int ret = new ParseJSON(response, context).parseGSTMaster(max);
                        if (ret == 1) {
                            callback.onSuccess(response);
                        } else {
                            callback.onFailure("Error");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onFailure("refreshGSTMaster_VolleyError_"+error.getMessage());
                        Constant.showLog(error.getMessage());
                        writeLog("refreshBankMaster_" + error.getMessage());
                    }
                }
        );
        AppSingleton.getInstance(context).addToRequestQueue(request, "OTP");
    }

    public void loadCheckoutOrder(String url, final ServerCallbackList callback) {
        StringRequest request = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Constant.showLog(response);
                        //response = response.replace("\\", "");
                        //response = response.replace("\"", "");
                        //response = response.substring(1, response.length() - 1);
                        List<CheckoutCustOrderClass> list = new ParseJSON(response, context). parseloadCheckoutOrder();
                        if (list.size() != 0) {
                            callback.onSuccess(list);
                        } else {
                            callback.onFailure("Error");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onFailure("Error");
                        Constant.showLog(error.getMessage());
                        writeLog("loadCheckoutOrder_" + error.getMessage());
                    }
                });
        AppSingleton.getInstance(context).addToRequestQueue(request, "OTP");
    }

    public void loadCustOutstanding(String url, final ServerCallbackList callback) {
        StringRequest request = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Constant.showLog(response);
                        response = response.replace("\\", "");
                        response = response.replace("''", "");
                        response = response.substring(1, response.length() - 1);
                        int ret = new ParseJSON(response, context).parseCustOutstanding();
                        if (ret == 1) {
                            callback.onSuccess(response);
                        } else {
                            callback.onFailure("Error");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onFailure("loadCustOutstanding"+error.getMessage());
                        Constant.showLog(error.getMessage());
                        writeLog("loadCustOutstanding" + error.getMessage());
                    }
                }
        );
        AppSingleton.getInstance(context).addToRequestQueue(request, "OTP");
    }

    public void loadLedgerReport(String url, final ServerCallbackList callback) {
        StringRequest request = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Constant.showLog(response);
                        response = response.replace("\\", "");
                        response = response.replace("''", "");
                        response = response.substring(1, response.length() - 1);
                        List<LedgerReportClass> list = new ParseJSON(response, context).parseLedgerReport();
                        if (list.size() != 0) {
                            callback.onSuccess(list);
                        } else {
                            callback.onFailure("Error");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onFailure("Error");
                        Constant.showLog(error.getMessage());
                        writeLog("loadLedgerReport_" + error.getMessage());
                    }
                });
        AppSingleton.getInstance(context).addToRequestQueue(request, "OTP");
    }

    public void loadOuststndReport(String url, final ServerCallbackList callback) {
        StringRequest request = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Constant.showLog(response);
                        response = response.replace("\\", "");
                        response = response.replace("''", "");
                        response = response.substring(1, response.length() - 1);
                        List<OuststandingReportClass> list = new ParseJSON(response, context).parseOutStndReport();
                        if (list.size() != 0) {
                            callback.onSuccess(list);
                        } else {
                            callback.onFailure("Error");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onFailure("Error");
                        Constant.showLog(error.getMessage());
                        writeLog("loadLedgerReport_" + error.getMessage());
                    }
                });
        AppSingleton.getInstance(context).addToRequestQueue(request, "OTP");
    }

    public void loadBankDetails(String url, final ServerCallbackList callback) {
        StringRequest request = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Constant.showLog(response);
                        response = response.replace("\\", "");
                        response = response.replace("''", "");
                        response = response.substring(1, response.length() - 1);
                        List<OurBankDetailsClass> list = new ParseJSON(response, context).parseBankDetails();
                        if (list.size() != 0) {
                            callback.onSuccess(list);
                        } else {
                            callback.onFailure("Error");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onFailure("Error");
                        Constant.showLog(error.getMessage());
                        writeLog("loadLedgerReport_" + error.getMessage());
                    }
                });
        AppSingleton.getInstance(context).addToRequestQueue(request, "OTP");
    }

    public void getAvailStock(String url, final ServerCallbackList callback) {
        StringRequest request = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Constant.showLog(response);
                        //response = response.replace("\\", "");
                        //response = response.replace("''", "");
                        //response = response.substring(1, response.length() - 1);
                        List<CheckAvailStockClass> list = new ParseJSON(response, context).parseAvailQty();
                        if (list.size()!=0) {
                            callback.onSuccess(list);
                        } else {
                            callback.onFailure("Error");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onFailure("Error");
                        Constant.showLog(error.getMessage());
                        writeLog("getAvailStock_" + error.getMessage());
                    }
                });
        AppSingleton.getInstance(context).addToRequestQueue(request, "OTP");
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
        AppSingleton.getInstance(context).addToRequestQueue(request, "AREA");
    }

    public void getCustDiscLimit(String url, final ServerCallback callback) {
        StringRequest request = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Constant.showLog(response);
                        response = response.replace("\\", "");
                        response = response.replace("''", "");
                        response = response.substring(1, response.length() - 1);
                        double ret = new ParseJSON(response, context).parseCustDiscLimit();
                        callback.onSuccess(String.valueOf(ret));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onFailure("getCustDiscLimit_" + error.getMessage());
                        Constant.showLog(error.getMessage());
                        writeLog("getCustDiscLimit_"+error.getMessage());
                    }
                }
        );
        AppSingleton.getInstance(context).addToRequestQueue(request, "AREA");
    }

    public void getMaxAuto(String url, final ServerCallback callback) {
        StringRequest request = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Constant.showLog(response);
                        response = response.replace("\\", "");
                        response = response.replace("''", "");
                        response = response.substring(1, response.length() - 1);
                        int ret = new ParseJSON(response, context).parseGetMaxAuto();
                        if(ret != 0) {
                            callback.onSuccess(String.valueOf(ret));
                        }else{
                            callback.onFailure("Error");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onFailure("refreshAreaMaster_" + error.getMessage());
                        Constant.showLog(error.getMessage());
                        writeLog("refreshAreaMaster_"+error.getMessage());
                    }
                }
        );
        AppSingleton.getInstance(context).addToRequestQueue(request, "AREA");
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

    private void writeLog(String _data) {
        new WriteLog().writeLog(context, "VolleyRequest_" + _data);
    }

}
