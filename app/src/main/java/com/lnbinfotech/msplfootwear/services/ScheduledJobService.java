package com.lnbinfotech.msplfootwear.services;

import android.content.Intent;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.lnbinfotech.msplfootwear.constant.Constant;
import com.lnbinfotech.msplfootwear.log.WriteLog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

//Created by Sneha on 22-06-2018.

public class ScheduledJobService extends JobService {

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Constant.showLog("onStartJob");
        int hour = Integer.parseInt(getTime());
        Constant.showLog("AutoSync_"+hour);
        if(hour<13||hour>20) {
            Intent service = new Intent(getApplicationContext(), DataUpdateService.class);
            getApplicationContext().startService(service);
        }
        writeLog("onStartJob_"+hour);
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
}
