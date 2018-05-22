package com.lnbinfotech.msplfootwearex.services;

//Created by ANUP on 3/9/2018.

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.lnbinfotech.msplfootwearex.FirstActivity;
import com.lnbinfotech.msplfootwearex.R;
import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.db.DBHandler;
import com.lnbinfotech.msplfootwearex.log.WriteLog;
import com.lnbinfotech.msplfootwearex.post.RequestResponseClass;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DataUpdateService extends IntentService {

    private DBHandler db;

    public DataUpdateService() {
        super(DataUpdateService.class.getName());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        db = new DBHandler(getApplicationContext());
        RequestResponseClass reqResp = new RequestResponseClass(getApplicationContext());
        /*if(isSynced(getString(R.string.pref_autoArealine))) {
            reqResp.loadArealineMaster();
        }
        if(isSynced(getString(R.string.pref_autoArea))) {
            reqResp.loadAreaMaster();
        }
        if(isSynced(getString(R.string.pref_autoBank))) {
            reqResp.loadBankMaster();
        }
        if(isSynced(getString(R.string.pref_autoBankBranch))) {
            //reqResp.loadBankBranchMaster();
            reqResp.getMaxAuto(3);
        }
        if(isSynced(getString(R.string.pref_autoCity))) {
            reqResp.loadCityMaster();
        }
        if(isSynced(getString(R.string.pref_autoCompany))) {
            reqResp.loadCompanyMaster();
        }
        if(isSynced(getString(R.string.pref_autoCustomer))) {
            //reqResp.loadCustomerMaster();
            reqResp.getMaxAuto(2);
        }
        if(isSynced(getString(R.string.pref_autoCurrency))) {
            reqResp.loadCurrencyMaster();
        }
        if(isSynced(getString(R.string.pref_autoDocument))) {
            reqResp.loadDocumentMaster();
        }
        if(isSynced(getString(R.string.pref_autoEmployee))) {
            reqResp.loadEmployeeMaster();
        }
        if(isSynced(getString(R.string.pref_autoGST))) {
            reqResp.loadGSTMaster();
        }
        if(isSynced(getString(R.string.pref_autoHO))) {
            reqResp.loadHOMaster();
        }
        if(isSynced(getString(R.string.pref_autoProduct))) {
            //reqResp.loadProductMaster();
            //reqResp.loadProductMaster(0, 100);
            reqResp.getMaxAuto(1);
        }*/
        /*if(isSynced(getString(R.string.pref_autoSizeNDesign))) {
            reqResp.loadSizeNDesignMaster();
        }
        if(isSynced(getString(R.string.pref_autoSizeDetail))) {
            reqResp.loadSDMD();
        }*/

        if(getDateDifference(getString(R.string.pref_autoArealine))) {
            reqResp.loadArealineMaster();
        }else{
            String str = "select max("+DBHandler.AL_Auto+") from "+DBHandler.Table_AreaLineMaster;
            int max = db.getMaxAutoId(str);
            reqResp.loadArealineMaster(max);
        }

        if(getDateDifference(getString(R.string.pref_autoArea))) {
            reqResp.loadAreaMaster();
        }else{
            String str = "select max("+DBHandler.Area_Auto+") from "+DBHandler.Table_AreaMaster;
            int max = db.getMaxAutoId(str);
            reqResp.loadAreaMaster(max);
        }

        if(getDateDifference(getString(R.string.pref_autoBank))) {
            reqResp.loadBankMaster();
        }else{
            String str = "select max("+DBHandler.Bank_Id+") from "+DBHandler.Table_BankMaster;
            int max = db.getMaxAutoId(str);
            reqResp.loadBankMaster(max);
        }

        if(getDateDifference(getString(R.string.pref_autoBankBranch))) {
            reqResp.getMaxAuto(3);
        }else{
            String str = "select max("+DBHandler.Branch_AutoId+") from "+DBHandler.Table_BankBranchMaster;
            int max = db.getMaxAutoId(str);
            reqResp.loadBankBranchMaster(max);
        }

        if(getDateDifference(getString(R.string.pref_autoCity))) {
            reqResp.loadCityMaster();
        }else{
            String str = "select max("+DBHandler.City_Auto+") from "+DBHandler.Table_CityMaster;
            int max = db.getMaxAutoId(str);
            reqResp.loadCityMaster(max);
        }

        if(getDateDifference(getString(R.string.pref_autoCompany))) {
            reqResp.loadCompanyMaster();
        }else{
            String str = "select max("+DBHandler.Company_Id+") from "+DBHandler.Table_CompanyMaster;
            int max = db.getMaxAutoId(str);
            reqResp.loadCompanyMaster(max);
        }

        if(getDateDifference(getString(R.string.pref_autoCustomer))) {
            reqResp.getMaxAuto(2);
        }else{
            String str = "select max("+DBHandler.CM_RetailCustID+") from "+DBHandler.Table_Customermaster;
            int max = db.getMaxAutoId(str);
            reqResp.loadCustomerMaster(max);
        }

        if(getDateDifference(getString(R.string.pref_autoCurrency))) {
            reqResp.loadCurrencyMaster();
        }else{
            String str = "select max("+DBHandler.Curr_Auto+") from "+DBHandler.Table_CurrencyMaster;
            int max = db.getMaxAutoId(str);
            reqResp.loadCurrencyMaster(max);
        }

        if(getDateDifference(getString(R.string.pref_autoDocument))) {
            reqResp.loadDocumentMaster();
        }else{
            String str = "select max("+DBHandler.Document_Id+") from "+DBHandler.Table_DocumentMaster;
            int max = db.getMaxAutoId(str);
            reqResp.loadDocumentMaster(max);
        }

        if(getDateDifference(getString(R.string.pref_autoEmployee))) {
            reqResp.loadEmployeeMaster();
        }else{
            String str = "select max("+DBHandler.EMP_EmpId+") from "+DBHandler.Table_Employee;
            int max = db.getMaxAutoId(str);
            reqResp.loadEmployeeMaster(max);
        }

        if(getDateDifference(getString(R.string.pref_autoGST))) {
            reqResp.loadGSTMaster();
        }else{
            String str = "select max("+DBHandler.GST_Auto+") from "+DBHandler.Table_GSTMASTER;
            int max = db.getMaxAutoId(str);
            reqResp.loadGSTMaster(max);
        }

        if(getDateDifference(getString(R.string.pref_autoHO))) {
            reqResp.loadHOMaster();
        }else{
            String str = "select max("+DBHandler.HO_Auto+") from "+DBHandler.Table_HOMaster;
            int max = db.getMaxAutoId(str);
            reqResp.loadHOMaster(max);
        }

        if(getDateDifference(getString(R.string.pref_autoProduct))) {
            reqResp.getMaxAuto(1);
        }else{
            String str = "select max("+DBHandler.PM_ProductID+") from "+DBHandler.Table_ProductMaster;
            int max = db.getMaxAutoId(str);
            reqResp.loadProductMaster(max);
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

    private void writeLog(String _data){
        new WriteLog().writeLog(getApplicationContext(),"DataUpdateService_"+_data);
    }

}
