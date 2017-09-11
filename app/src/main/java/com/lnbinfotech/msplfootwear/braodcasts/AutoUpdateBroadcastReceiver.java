package com.lnbinfotech.msplfootwear.braodcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.lnbinfotech.msplfootwear.connectivity.ConnectivityTest;
import com.lnbinfotech.msplfootwear.constant.Constant;
import com.lnbinfotech.msplfootwear.log.WriteLog;
import com.lnbinfotech.msplfootwear.services.CheckNewTicketService;

//Created by lnb on 8/23/2017.

public class AutoUpdateBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(ConnectivityTest.getNetStat(context)){
            context.startService(new Intent(context, CheckNewTicketService.class));
            Constant.showLog("Broadcast Receiver");
            writeLog(context,"AutoUpdateBroadcastReceiver_onReceive_Broadcast_Received");
        }
    }

    private void writeLog(Context context,String _data){
        new WriteLog().writeLog(context,_data);
    }

}
