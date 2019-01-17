package com.lnbinfotech.msplfootwearex.services;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import com.lnbinfotech.msplfootwearex.FirstActivity;
import com.lnbinfotech.msplfootwearex.R;
import com.lnbinfotech.msplfootwearex.connectivity.ConnectivityTest;
import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.db.DBHandler;
import com.lnbinfotech.msplfootwearex.interfaces.RetrofitApiInterface;
import com.lnbinfotech.msplfootwearex.log.WriteLog;
import com.lnbinfotech.msplfootwearex.model.BankBranchMasterClass;
import com.lnbinfotech.msplfootwearex.model.CustomerDetailClass;
import com.lnbinfotech.msplfootwearex.model.ProductMasterClass;
import com.lnbinfotech.msplfootwearex.model.SizeDesignMastDetClass;
import com.lnbinfotech.msplfootwearex.model.SizeNDesignClass;
import com.lnbinfotech.msplfootwearex.post.RequestResponseClass;
import com.lnbinfotech.msplfootwearex.utility.RetrofitApiBuilder;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//Created by Anup on 03-07-2018.

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class ScheduledJobServicePL extends JobService {

    private DBHandler db;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Constant.showLog("onStartJob");
        int hour = Integer.parseInt(getTime());
        Constant.showLog("AutoSync_"+hour);
        /*if(hour<13||hour>20) {
            Intent service = new Intent(getApplicationContext(), DataUpdateService.class);
            getApplicationContext().startService(service);
        }*/
        //TODO : Set Time Limit
        //if(hour<13||hour>20) {
            if (ConnectivityTest.getNetStat(getApplicationContext())) {
                startSync();
                writeLog("onStartJob_" + hour + "_Online");
            } else {
                writeLog("onStartJob_" + hour + "_Offline");
            }
        //}

        return false;
    }

    private void startSync() {
        db = new DBHandler(getApplicationContext());
        RequestResponseClass reqResp = new RequestResponseClass(getApplicationContext());

        if (getDateDifference(getString(R.string.pref_autoArealine))) {
            reqResp.loadArealineMaster();
        } else {
            if (isSynced(getString(R.string.pref_autoArealine))) {
                String str = "select max(" + DBHandler.AL_Auto + ") from " + DBHandler.Table_AreaLineMaster;
                int max = db.getMaxAutoId(str);
                reqResp.loadArealineMaster(max);
            }
        }

        if (getDateDifference(getString(R.string.pref_autoArea))) {
            reqResp.loadAreaMaster();
        } else {
            if (isSynced(getString(R.string.pref_autoArea))) {
                String str = "select max(" + DBHandler.Area_Auto + ") from " + DBHandler.Table_AreaMaster;
                int max = db.getMaxAutoId(str);
                reqResp.loadAreaMaster(max);
            }
        }

        if (getDateDifference(getString(R.string.pref_autoBank))) {
            reqResp.loadBankMaster();
        } else {
            if (isSynced(getString(R.string.pref_autoBank))) {
                String str = "select max(" + DBHandler.Bank_Id + ") from " + DBHandler.Table_BankMaster;
                int max = db.getMaxAutoId(str);
                reqResp.loadBankMaster(max);
            }
        }

        /*if(getDateDifference(getString(R.string.pref_autoBankBranch))) {
            reqResp.getMaxAuto(3);
        }else{
            if(isSynced(getString(R.string.pref_autoBankBranch))) {
                String str = "select max(" + DBHandler.Branch_AutoId + ") from " + DBHandler.Table_BankBranchMaster;
                int max = db.getMaxAutoId(str);
                reqResp.loadBankBranchMaster(max);
            }
        }*/

        getBankBranchMasterV6();

        if (getDateDifference(getString(R.string.pref_autoCity))) {
            reqResp.loadCityMaster();
        } else {
            if (isSynced(getString(R.string.pref_autoCity))) {
                String str = "select max(" + DBHandler.City_Auto + ") from " + DBHandler.Table_CityMaster;
                int max = db.getMaxAutoId(str);
                reqResp.loadCityMaster(max);
            }
        }

        if (getDateDifference(getString(R.string.pref_autoCompany))) {
            reqResp.loadCompanyMaster();
        } else {
            if (isSynced(getString(R.string.pref_autoCompany))) {
                String str = "select max(" + DBHandler.Company_Id + ") from " + DBHandler.Table_CompanyMaster;
                int max = db.getMaxAutoId(str);
                reqResp.loadCompanyMaster(max);
            }
        }

        /*if (getDateDifference(getString(R.string.pref_autoCustomer))) {
            reqResp.getMaxAuto(2);
        } else {
            if (isSynced(getString(R.string.pref_autoCustomer))) {
                String str = "select max(" + DBHandler.CM_RetailCustID + ") from " + DBHandler.Table_Customermaster;
                int max = db.getMaxAutoId(str);
                reqResp.loadCustomerMaster(max);
            }
        }*/

        getCustomerMasterV6();

        if (getDateDifference(getString(R.string.pref_autoCurrency))) {
            reqResp.loadCurrencyMaster();
        } else {
            if (isSynced(getString(R.string.pref_autoCurrency))) {
                String str = "select max(" + DBHandler.Curr_Auto + ") from " + DBHandler.Table_CurrencyMaster;
                int max = db.getMaxAutoId(str);
                reqResp.loadCurrencyMaster(max);
            }
        }

        if (getDateDifference(getString(R.string.pref_autoDocument))) {
            reqResp.loadDocumentMaster();
        } else {
            if (isSynced(getString(R.string.pref_autoDocument))) {
                String str = "select max(" + DBHandler.Document_Id + ") from " + DBHandler.Table_DocumentMaster;
                int max = db.getMaxAutoId(str);
                reqResp.loadDocumentMaster(max);
            }
        }

        if (getDateDifference(getString(R.string.pref_autoEmployee))) {
            reqResp.loadEmployeeMaster();
        } else {
            if (isSynced(getString(R.string.pref_autoEmployee))) {
                String str = "select max(" + DBHandler.EMP_EmpId + ") from " + DBHandler.Table_Employee;
                int max = db.getMaxAutoId(str);
                reqResp.loadEmployeeMaster(max);
            }
        }

        if (getDateDifference(getString(R.string.pref_autoGST))) {
            reqResp.loadGSTMaster();
        } else {
            if (isSynced(getString(R.string.pref_autoGST))) {
                String str = "select max(" + DBHandler.GST_Auto + ") from " + DBHandler.Table_GSTMASTER;
                int max = db.getMaxAutoId(str);
                reqResp.loadGSTMaster(max);
            }
        }

        if (getDateDifference(getString(R.string.pref_autoHO))) {
            reqResp.loadHOMaster();
        } else {
            if (isSynced(getString(R.string.pref_autoHO))) {
                String str = "select max(" + DBHandler.HO_Auto + ") from " + DBHandler.Table_HOMaster;
                int max = db.getMaxAutoId(str);
                reqResp.loadHOMaster(max);
            }
        }

       /* if(getDateDifference(getString(R.string.pref_autoProduct))) {
            reqResp.getMaxAuto(1);
        }else{
            if(isSynced(getString(R.string.pref_autoProduct))) {
                String str = "select max(" + DBHandler.PM_ProductID + ") from " + DBHandler.Table_ProductMaster;
                int max = db.getMaxAutoId(str);
                reqResp.loadProductMaster(max);
            }
        }*/

        /*if(isSynced(getString(R.string.pref_autoProduct))) {
            getProductMasterV6();
        }*/

        getProductMasterV6();
    }

    private boolean isSynced(String prefName){
        boolean ret = false;
        try {
            FirstActivity.pref = getApplicationContext().getSharedPreferences(FirstActivity.PREF_NAME, Context.MODE_PRIVATE);
            String str = FirstActivity.pref.getString(prefName, "01/Jan/1900");
            String strArr[] = str.split("\\-");
            String savedDate = "";
            if (strArr.length > 1) {
                savedDate = strArr[0];
            }else{
                savedDate = str;
            }
            String currDate = getDateTime();
            Date sDate = new SimpleDateFormat("dd/MMM/yyyy",Locale.ENGLISH).parse(savedDate);
            Date cDate = new SimpleDateFormat("dd/MMM/yyyy",Locale.ENGLISH).parse(currDate);
            if(cDate.compareTo(sDate)!=0){
                ret = true;
                String str1 = prefName+"-"+savedDate+"-"+currDate+"-"+ret;
                Constant.showLog(str1);
                writeLog("isSynced_"+str1);
            }else{
                ret = false;
                String str1 = prefName+"-"+savedDate+"-"+currDate+"-"+ret;
                Constant.showLog(str1);
                writeLog("isSynced_"+str1);
            }
        }catch (Exception e){
            e.printStackTrace();
            writeLog("isSynced_"+e.getMessage());
            ret = false;
        }
        return ret;
    }

    private boolean getDateDifference(String prefName){
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        boolean ret = false;
        try {
            FirstActivity.pref = getApplicationContext().getSharedPreferences(FirstActivity.PREF_NAME, Context.MODE_PRIVATE);
            String str = FirstActivity.pref.getString(prefName, "01/Jan/1900");
            String strArr[] = str.split("\\-");
            String savedDate = "";
            if (strArr.length > 1) {
                savedDate = strArr[0];
            }else{
                savedDate = str;
            }
            String currDate = getDateTime();
            Date sDate = new SimpleDateFormat("dd/MMM/yyyy",Locale.ENGLISH).parse(savedDate);
            Date cDate = new SimpleDateFormat("dd/MMM/yyyy",Locale.ENGLISH).parse(currDate);

            long different = cDate.getTime() - sDate.getTime();
            long elapsedDays = different / daysInMilli;
            Constant.showLog("elapsedDays - "+elapsedDays);
            if(elapsedDays>15){
                ret = true;
                String str1 = prefName+"-"+savedDate+"-"+currDate+"-"+ret;
                Constant.showLog(str1);
                writeLog("ElapsedDays_"+elapsedDays+"_isSynced_"+str1);
            }else{
                ret = false;
                String str1 = prefName+"-"+savedDate+"-"+currDate+"-"+ret;
                Constant.showLog(str1);
                writeLog("ElapsedDays_"+elapsedDays+"_isSynced_"+str1);
            }
        }catch (Exception e){
            e.printStackTrace();
            writeLog("isSynced_"+e.getMessage());
            ret = false;
        }
        return ret;
    }

    private String getDateTime() {
        String str = "";
        try{
            str = new SimpleDateFormat("dd/MMM/yyyy", Locale.ENGLISH).format(new Date());
        }catch (Exception e){
            e.printStackTrace();
            writeLog("getDateTime_"+e.getMessage());
        }
        return str;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Constant.showLog("onStopJob");
        writeLog("onStopJob");
        return false;
    }

    private void writeLog(String data) {
        new WriteLog().writeLog(getBaseContext(), "ScheduledJobServicePL_" + data);
    }

    private String getTime() {
        String str = "";
        try{
            str = new SimpleDateFormat("HH", Locale.ENGLISH).format(new Date());
        }catch (Exception e){
            e.printStackTrace();
        }
        return str;
    }

    private String getTime1() {
        String str = "";
        try{
            str = new SimpleDateFormat("dd/MMM/yyyy HH:mm", Locale.ENGLISH).format(new Date());
        }catch (Exception e){
            e.printStackTrace();
        }
        return str;
    }

    private void getProductMasterV6() {
        try {
            final DBHandler db = new DBHandler(getApplicationContext());
            String url = 0 + "|" + 10000 + "|" + "E";
            writeLog("getProductMasterV6_"+url);
            final JSONObject jsonBody = new JSONObject();
            jsonBody.put("details", url);
            RequestBody body = RequestBody.create(okhttp3.MediaType.
                    parse("application/json; charset=utf-8"), (jsonBody).toString());
            Constant.showLog(jsonBody.toString());

            Call<List<ProductMasterClass>> call = new RetrofitApiBuilder().getApiBuilder().
                    create(RetrofitApiInterface.class).
                    getProductMasterV6(body);
            call.enqueue(new Callback<List<ProductMasterClass>>() {
                @Override
                public void onResponse(Call<List<ProductMasterClass>> call, Response<List<ProductMasterClass>> response) {
                    Constant.showLog("onResponse");
                    List<ProductMasterClass> list = response.body();
                    if (list != null) {
                        if (list.size()!=0) {
                            db.deleteTable(DBHandler.Table_ProductMaster);
                        }
                        db.addProductMaster(list);
                        Constant.showLog(list.size() + "_getProductMasterV6");
                        FirstActivity.pref = getSharedPreferences(FirstActivity.PREF_NAME,Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = FirstActivity.pref.edit();
                        String str = getDateTime()+"-"+"True"+"-"+getTime1();
                        Constant.showLog(getString(R.string.pref_autoProduct)+"-"+str);
                        editor.putString(getString(R.string.pref_autoProduct), getTime1());
                        editor.apply();
                        writeLog("getProductMasterV6_onResponse_" + list.size() + "_" + str);
                        getAllSizeDesignMastDetV6();
                    } else {
                        Constant.showLog("onResponse_list_null");
                        writeLog("getProductMasterV6_onResponse_list_null");
                    }
                }

                @Override
                public void onFailure(Call<List<ProductMasterClass>> call, Throwable t) {
                    Constant.showLog("onFailure");
                    if (!call.isCanceled()) {
                        call.cancel();
                    }
                    t.printStackTrace();
                    writeLog("getProductMasterV6_onFailure_" + t.getMessage());

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            writeLog("getProductMasterV6_" + e.getMessage());
        }
    }

    private void getAllSizeDesignMastDetV6() {
        try {
            final DBHandler db = new DBHandler(getApplicationContext());
            String url = 0 + "|" + 10000;
            writeLog("getAllSizeDesignMastDetV6_"+url);
            final JSONObject jsonBody = new JSONObject();
            jsonBody.put("details", url);
            RequestBody body = RequestBody.create(okhttp3.MediaType.
                    parse("application/json; charset=utf-8"), (jsonBody).toString());
            Constant.showLog(jsonBody.toString());

            Call<List<SizeNDesignClass>> call = new RetrofitApiBuilder().getApiBuilder().
                    create(RetrofitApiInterface.class).
                    getAllSizeDesignMastDetV6(body);
            call.enqueue(new Callback<List<SizeNDesignClass>>() {
                @Override
                public void onResponse(Call<List<SizeNDesignClass>> call, Response<List<SizeNDesignClass>> response) {
                    Constant.showLog("onResponse");
                    List<SizeNDesignClass> list = response.body();
                    if (list != null) {
                        if (list.size()!=0) {
                            db.deleteTable(DBHandler.Table_AllRequiredSizesDesigns);
                        }
                        db.addSizeNDesignMaster(list);
                        Constant.showLog(list.size() + "_getAllSizeDesignMastDetV6");
                        FirstActivity.pref = getSharedPreferences(FirstActivity.PREF_NAME,Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = FirstActivity.pref.edit();
                        String str = getDateTime()+"-"+"True"+"-"+getTime1();
                        Constant.showLog(getString(R.string.pref_autoSizeNDesign)+"-"+str);
                        editor.putString(getString(R.string.pref_autoSizeNDesign), getTime1());
                        editor.apply();
                        writeLog("getAllSizeDesignMastDetV6_onResponse_" + list.size() + "_" + str);
                        getSizeDesignMastDetV6();
                    } else {
                        Constant.showLog("onResponse_list_null");
                        writeLog("getAllSizeDesignMastDetV6_onResponse_list_null");
                    }
                }

                @Override
                public void onFailure(Call<List<SizeNDesignClass>> call, Throwable t) {
                    Constant.showLog("onFailure");
                    if (!call.isCanceled()) {
                        call.cancel();
                    }
                    t.printStackTrace();
                    writeLog("getAllSizeDesignMastDetV6_onFailure_" + t.getMessage());

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            writeLog("getAllSizeDesignMastDetV6_" + e.getMessage());
        }
    }

    private void getSizeDesignMastDetV6() {
        try {
            final DBHandler db = new DBHandler(getApplicationContext());
            String url = 0 + "|" + 10000;
            writeLog("getSizeDesignMastDetV6_"+url);
            final JSONObject jsonBody = new JSONObject();
            jsonBody.put("details", url);
            RequestBody body = RequestBody.create(okhttp3.MediaType.
                    parse("application/json; charset=utf-8"), (jsonBody).toString());
            Constant.showLog(jsonBody.toString());

            Call<List<SizeDesignMastDetClass>> call = new RetrofitApiBuilder().getApiBuilder().
                    create(RetrofitApiInterface.class).
                    getSizeDesignMastDetV6(body);
            call.enqueue(new Callback<List<SizeDesignMastDetClass>>() {
                @Override
                public void onResponse(Call<List<SizeDesignMastDetClass>> call, Response<List<SizeDesignMastDetClass>> response) {
                    Constant.showLog("onResponse");
                    List<SizeDesignMastDetClass> list = response.body();
                    if (list != null) {
                        if (list.size()!=0) {
                            db.deleteTable(DBHandler.Table_SizeDesignMastDet);
                        }
                        db.addSizeDesignMastDet(list);
                        Constant.showLog(list.size() + "_getSizeDesignMastDetV6");
                        FirstActivity.pref = getSharedPreferences(FirstActivity.PREF_NAME,Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = FirstActivity.pref.edit();
                        String str = getDateTime()+"-"+"True"+"-"+getTime1();
                        Constant.showLog(getString(R.string.pref_autoSizeDetail)+"-"+str);
                        editor.putString(getString(R.string.pref_autoSizeDetail), getTime1());
                        editor.apply();
                        writeLog("getSizeDesignMastDetV6_onResponse_" + list.size() + "_" + str);
                    } else {
                        Constant.showLog("onResponse_list_null");
                        writeLog("getSizeDesignMastDetV6_onResponse_list_null");
                    }
                }

                @Override
                public void onFailure(Call<List<SizeDesignMastDetClass>> call, Throwable t) {
                    Constant.showLog("onFailure");
                    if (!call.isCanceled()) {
                        call.cancel();
                    }
                    t.printStackTrace();
                    writeLog("getSizeDesignMastDetV6_onFailure_" + t.getMessage());

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            writeLog("getSizeDesignMastDetV6_" + e.getMessage());
        }
    }

    private void getBankBranchMasterV6() {
        try {
            final DBHandler db = new DBHandler(getApplicationContext());
            String url = 0 + "|" + 10000 + "|E";
            writeLog("getBankBranchMasterV6_"+url);
            final JSONObject jsonBody = new JSONObject();
            jsonBody.put("details", url);
            RequestBody body = RequestBody.create(okhttp3.MediaType.
                    parse("application/json; charset=utf-8"), (jsonBody).toString());
            Constant.showLog(jsonBody.toString());

            Call<List<BankBranchMasterClass>> call = new RetrofitApiBuilder().getApiBuilder().
                    create(RetrofitApiInterface.class).
                    getBankBrnachMasterV6(body);
            call.enqueue(new Callback<List<BankBranchMasterClass>>() {
                @Override
                public void onResponse(Call<List<BankBranchMasterClass>> call, Response<List<BankBranchMasterClass>> response) {
                    Constant.showLog("onResponse");
                    List<BankBranchMasterClass> list = response.body();
                    if (list != null) {
                        if (list.size()!=0) {
                            db.deleteTable(DBHandler.Table_BankBranchMaster);
                        }
                        db.addBankBranchMaster(list);
                        Constant.showLog(list.size() + "_getBankBranchMasterV6");
                        FirstActivity.pref = getSharedPreferences(FirstActivity.PREF_NAME,Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = FirstActivity.pref.edit();
                        String str = getDateTime()+"-"+"True"+"-"+getTime1();
                        Constant.showLog(getString(R.string.pref_autoBankBranch)+"-"+str);
                        editor.putString(getString(R.string.pref_autoBankBranch), getTime1());
                        editor.apply();
                        writeLog("getBankBranchMasterV6_onResponse_" + list.size() + "_" + str);
                    } else {
                        Constant.showLog("onResponse_list_null");
                        writeLog("getBankBranchMasterV6_onResponse_list_null");
                    }
                }

                @Override
                public void onFailure(Call<List<BankBranchMasterClass>> call, Throwable t) {
                    Constant.showLog("onFailure");
                    if (!call.isCanceled()) {
                        call.cancel();
                    }
                    t.printStackTrace();
                    writeLog("getBankBranchMasterV6_onFailure_" + t.getMessage());

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            writeLog("getBankBranchMasterV6_" + e.getMessage());
        }
    }

    private void getCustomerMasterV6() {
        try {
            final DBHandler db = new DBHandler(getApplicationContext());
            String url = 0 + "|" + 10000 + "|E";
            writeLog("getCustomerMasterV6_"+url);
            final JSONObject jsonBody = new JSONObject();
            jsonBody.put("details", url);
            RequestBody body = RequestBody.create(okhttp3.MediaType.
                    parse("application/json; charset=utf-8"), (jsonBody).toString());
            Constant.showLog(jsonBody.toString());

            Call<List<CustomerDetailClass>> call = new RetrofitApiBuilder().getApiBuilder().
                    create(RetrofitApiInterface.class).
                    getCustomerMasterV6(body);
            call.enqueue(new Callback<List<CustomerDetailClass>>() {
                @Override
                public void onResponse(Call<List<CustomerDetailClass>> call, Response<List<CustomerDetailClass>> response) {
                    Constant.showLog("onResponse");
                    List<CustomerDetailClass> list = response.body();
                    if (list != null) {
                        if (list.size()!=0) {
                            db.deleteTable(DBHandler.Table_Customermaster);
                        }
                        db.addCustomerDetail(new ArrayList<>(list));
                        Constant.showLog(list.size() + "_getCustomerMasterV6");
                        FirstActivity.pref = getSharedPreferences(FirstActivity.PREF_NAME,Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = FirstActivity.pref.edit();
                        String str = getDateTime()+"-"+"True"+"-"+getTime1();
                        Constant.showLog(getString(R.string.pref_autoBankBranch)+"-"+str);
                        editor.putString(getString(R.string.pref_autoBankBranch), getTime1());
                        editor.apply();
                        writeLog("getCustomerMasterV6_onResponse_" + list.size() + "_" + str);
                    } else {
                        Constant.showLog("onResponse_list_null");
                        writeLog("getCustomerMasterV6_onResponse_list_null");
                    }
                }

                @Override
                public void onFailure(Call<List<CustomerDetailClass>> call, Throwable t) {
                    Constant.showLog("onFailure");
                    if (!call.isCanceled()) {
                        call.cancel();
                    }
                    t.printStackTrace();
                    writeLog("getCustomerMasterV6_onFailure_" + t.getMessage());

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            writeLog("getCustomerMasterV6_" + e.getMessage());
        }
    }

}

