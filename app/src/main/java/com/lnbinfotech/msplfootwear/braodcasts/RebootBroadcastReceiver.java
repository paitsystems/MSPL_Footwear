package com.lnbinfotech.msplfootwear.braodcasts;

//Created by lnb on 8/23/2017.

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.lnbinfotech.msplfootwear.services.AutoSyncService;
import com.lnbinfotech.msplfootwear.services.DataUpdateService;

public class RebootBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent!=null) {
            if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
                //new Constant(context).setRecurringAlarm();
                context.startService(new Intent(context, AutoSyncService.class));
            }
        }else{
            Log.d("Log", "Intent Null");
        }
    }
}
