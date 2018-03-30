package com.lnbinfotech.msplfootwear.services;

//Created by ANUP on 3/26/2018.

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.lnbinfotech.msplfootwear.FirstActivity;
import com.lnbinfotech.msplfootwear.R;
import com.lnbinfotech.msplfootwear.constant.Constant;
import com.lnbinfotech.msplfootwear.log.WriteLog;
import com.lnbinfotech.msplfootwear.post.RequestResponseClass;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DataUpdateService extends IntentService {

    public DataUpdateService() {
        super(DataUpdateService.class.getName());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        RequestResponseClass reqResp = new RequestResponseClass(getApplicationContext());
        if(isSynced(getString(R.string.pref_autoArea))) {
            reqResp.loadAreaMaster();
        }
        if(isSynced(getString(R.string.pref_autoBank))) {
            reqResp.loadBankMaster();
        }
        if(isSynced(getString(R.string.pref_autoBankBranch))) {
            reqResp.loadBankBranchMaster();
        }
        if(isSynced(getString(R.string.pref_autoCity))) {
            reqResp.loadCityMaster();
        }
        if(isSynced(getString(R.string.pref_autoCompany))) {
            reqResp.loadCompanyMaster();
        }
        if(isSynced(getString(R.string.pref_autoCustomer))) {
            reqResp.loadCustomerMaster();
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
            reqResp.loadProductMaster();
        }
        /*if(isSynced(getString(R.string.pref_autoSizeNDesign))) {
            reqResp.loadSizeNDesignMaster();
        }
        if(isSynced(getString(R.string.pref_autoSizeDetail))) {
            reqResp.loadSDMD();
        }*/
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
            Date sDate = new SimpleDateFormat("dd/MMM/yyyy", Locale.ENGLISH).parse(savedDate);
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
