package com.lnbinfotech.msplfootwearex.services;

//Created by Anup on 03-07-2018.

import android.content.Context;
import android.content.Intent;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.lnbinfotech.msplfootwearex.FirstActivity;
import com.lnbinfotech.msplfootwearex.R;
import com.lnbinfotech.msplfootwearex.connectivity.ConnectivityTest;
import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.db.DBHandler;
import com.lnbinfotech.msplfootwearex.log.WriteLog;
import com.lnbinfotech.msplfootwearex.post.RequestResponseClass;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ScheduledJobService extends JobService {

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
        if(ConnectivityTest.getNetStat(getApplicationContext())) {
            startSync();
            writeLog("onStartJob_"+hour+"_Online");
        }else {
            writeLog("onStartJob_"+hour+"_Offline");
        }
        //writeLog("onStartJob_"+hour);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Constant.showLog("onStopJob");
        writeLog("onStopJob");
        return false;
    }

    private void writeLog(String data) {
        new WriteLog().writeLog(getBaseContext(), "ScheduledJobService_" + data);
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

    private void startSync(){
        db = new DBHandler(getApplicationContext());
        RequestResponseClass reqResp = new RequestResponseClass(getApplicationContext());

        if(getDateDifference(getString(R.string.pref_autoArealine))) {
            reqResp.loadArealineMaster();
        }else{
            if(isSynced(getString(R.string.pref_autoArealine))) {
                String str = "select max(" + DBHandler.AL_Auto + ") from " + DBHandler.Table_AreaLineMaster;
                int max = db.getMaxAutoId(str);
                reqResp.loadArealineMaster(max);
            }
        }

        if(getDateDifference(getString(R.string.pref_autoArea))) {
            reqResp.loadAreaMaster();
        }else{
            if(isSynced(getString(R.string.pref_autoArea))) {
                String str = "select max(" + DBHandler.Area_Auto + ") from " + DBHandler.Table_AreaMaster;
                int max = db.getMaxAutoId(str);
                reqResp.loadAreaMaster(max);
            }
        }

        if(getDateDifference(getString(R.string.pref_autoBank))) {
            reqResp.loadBankMaster();
        }else{
            if(isSynced(getString(R.string.pref_autoBank))) {
                String str = "select max(" + DBHandler.Bank_Id + ") from " + DBHandler.Table_BankMaster;
                int max = db.getMaxAutoId(str);
                reqResp.loadBankMaster(max);
            }
        }

        if(getDateDifference(getString(R.string.pref_autoBankBranch))) {
            reqResp.getMaxAuto(3);
        }else{
            if(isSynced(getString(R.string.pref_autoBankBranch))) {
                String str = "select max(" + DBHandler.Branch_AutoId + ") from " + DBHandler.Table_BankBranchMaster;
                int max = db.getMaxAutoId(str);
                reqResp.loadBankBranchMaster(max);
            }
        }

        if(getDateDifference(getString(R.string.pref_autoCity))) {
            reqResp.loadCityMaster();
        }else{
            if(isSynced(getString(R.string.pref_autoCity))) {
                String str = "select max(" + DBHandler.City_Auto + ") from " + DBHandler.Table_CityMaster;
                int max = db.getMaxAutoId(str);
                reqResp.loadCityMaster(max);
            }
        }

        if(getDateDifference(getString(R.string.pref_autoCompany))) {
            reqResp.loadCompanyMaster();
        }else{
            if(isSynced(getString(R.string.pref_autoCompany))) {
                String str = "select max(" + DBHandler.Company_Id + ") from " + DBHandler.Table_CompanyMaster;
                int max = db.getMaxAutoId(str);
                reqResp.loadCompanyMaster(max);
            }
        }

        if(getDateDifference(getString(R.string.pref_autoCustomer))) {
            reqResp.getMaxAuto(2);
        }else{
            if(isSynced(getString(R.string.pref_autoCustomer))) {
                String str = "select max(" + DBHandler.CM_RetailCustID + ") from " + DBHandler.Table_Customermaster;
                int max = db.getMaxAutoId(str);
                reqResp.loadCustomerMaster(max);
            }
        }

        if(getDateDifference(getString(R.string.pref_autoCurrency))) {
            reqResp.loadCurrencyMaster();
        }else{
            if(isSynced(getString(R.string.pref_autoCurrency))) {
                String str = "select max(" + DBHandler.Curr_Auto + ") from " + DBHandler.Table_CurrencyMaster;
                int max = db.getMaxAutoId(str);
                reqResp.loadCurrencyMaster(max);
            }
        }

        if(getDateDifference(getString(R.string.pref_autoDocument))) {
            reqResp.loadDocumentMaster();
        }else{
            if(isSynced(getString(R.string.pref_autoDocument))) {
                String str = "select max(" + DBHandler.Document_Id + ") from " + DBHandler.Table_DocumentMaster;
                int max = db.getMaxAutoId(str);
                reqResp.loadDocumentMaster(max);
            }
        }

        if(getDateDifference(getString(R.string.pref_autoEmployee))) {
            reqResp.loadEmployeeMaster();
        }else{
            if(isSynced(getString(R.string.pref_autoEmployee))) {
                String str = "select max(" + DBHandler.EMP_EmpId + ") from " + DBHandler.Table_Employee;
                int max = db.getMaxAutoId(str);
                reqResp.loadEmployeeMaster(max);
            }
        }

        if(getDateDifference(getString(R.string.pref_autoGST))) {
            reqResp.loadGSTMaster();
        }else{
            if(isSynced(getString(R.string.pref_autoGST))) {
                String str = "select max(" + DBHandler.GST_Auto + ") from " + DBHandler.Table_GSTMASTER;
                int max = db.getMaxAutoId(str);
                reqResp.loadGSTMaster(max);
            }
        }

        if(getDateDifference(getString(R.string.pref_autoHO))) {
            reqResp.loadHOMaster();
        }else{
            if(isSynced(getString(R.string.pref_autoHO))) {
                String str = "select max(" + DBHandler.HO_Auto + ") from " + DBHandler.Table_HOMaster;
                int max = db.getMaxAutoId(str);
                reqResp.loadHOMaster(max);
            }
        }

        if(getDateDifference(getString(R.string.pref_autoProduct))) {
            reqResp.getMaxAuto(1);
        }else{
            if(isSynced(getString(R.string.pref_autoProduct))) {
                String str = "select max(" + DBHandler.PM_ProductID + ") from " + DBHandler.Table_ProductMaster;
                int max = db.getMaxAutoId(str);
                reqResp.loadProductMaster(max);
            }
        }
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

}
