package com.lnbinfotech.msplfootwearex.braodcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.lnbinfotech.msplfootwearex.connectivity.ConnectivityTest;
import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.constant.Utility;
import com.lnbinfotech.msplfootwearex.log.WriteLog;

//Created by lnb on 8/23/2017.

public class AutoUpdateBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(ConnectivityTest.getNetStat(context)){
            //context.startService(new Intent(context, DataUpdateService.class));
            //writeLog(context,"AutoUpdateBroadcastReceiver_onReceive_Broadcast_Received");
            Constant.showLog("Broadcast Receiver");
            Utility.scheduledJob(context);
            writeLog(context,"onReceive_Job_Scheduled");
        }
    }

    private void writeLog(Context context,String _data){
        new WriteLog().writeLog(context,_data);
    }

}
