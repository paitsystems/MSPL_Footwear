package com.lnbinfotech.msplfootwearex.braodcasts;

//Created by lnb on 8/23/2017.

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.lnbinfotech.msplfootwearex.constant.Utility;
import com.lnbinfotech.msplfootwearex.log.WriteLog;

public class RebootBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent!=null) {
            if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
                //new Constant(context).setRecurringAlarm();
                //context.startService(new Intent(context, AutoSyncService.class));
                Utility.scheduledJob(context);
                new WriteLog().writeLog(context,"RebootBroadcastReceiver_onReceive_Job_Scheduled");
            }
        }else{
            Log.d("Log", "Intent Null");
        }
    }
}
