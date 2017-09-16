package com.lnbinfotech.msplfootwear.services;

//Created by lnb on 8/23/2017.

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lnbinfotech.msplfootwear.FirstActivity;
import com.lnbinfotech.msplfootwear.R;
import com.lnbinfotech.msplfootwear.constant.Constant;
import com.lnbinfotech.msplfootwear.log.WriteLog;
import com.lnbinfotech.msplfootwear.parse.ParseJSON;

public class CheckNewTicketService extends IntentService{

    public CheckNewTicketService() {
        super(CheckNewTicketService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    private void showNotification(){
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent intent = new Intent(getApplicationContext(),FirstActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,intent,0);
        Notification notification = new Notification.Builder(this)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("New Ticket Generated")
                .setSmallIcon(R.drawable.lnb_logo)
                .setSound(uri)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0,notification);

    }

    private void writeLog(Context context, String _data){
        new WriteLog().writeLog(context,_data);
    }

}
