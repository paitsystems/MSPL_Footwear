package com.pait.dispatch_app.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.pait.dispatch_app.connectivity.ConnectivityTest;
import com.pait.dispatch_app.constant.Constant;
import com.pait.dispatch_app.constant.Utitlity;
import com.pait.dispatch_app.log.WriteLog;

//Created by lnb on 8/23/2017.

public class AutoUpdateBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(ConnectivityTest.getNetStat(context)){
            //context.startService(new Intent(context, DataUpdateService.class));
            //writeLog(context,"AutoUpdateBroadcastReceiver_onReceive_Broadcast_Received");
            Constant.showLog("Broadcast Receiver");
            Utitlity.scheduledJob(context);
            writeLog(context,"onReceive_Job_Scheduled");

        }
    }

    private void writeLog(Context context,String _data){
        new WriteLog().writeLog(context,"AutoUpdateBroadcastReceiver_"+_data);
    }

}
