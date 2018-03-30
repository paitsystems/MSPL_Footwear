package com.lnbinfotech.msplfootwear.services;

//Created by lnb on 8/23/2017.

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.lnbinfotech.msplfootwear.constant.Constant;
import com.lnbinfotech.msplfootwear.log.WriteLog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class AutoSyncService extends Service {

    private Timer timer;
    private Handler handler = new Handler();
    private long period = 60*60*1000;

    public AutoSyncService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if(timer==null) {
            timer = new Timer();
        }
        timer.scheduleAtFixedRate(new UpdateData(), 0, period);
    }

    private class UpdateData extends TimerTask {

        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    int hour = Integer.parseInt(getTime());
                    Constant.showLog("AutoSync_"+hour);
                    if(hour<13||hour>19) {
                        getApplicationContext().startService(new Intent(getApplicationContext(), DataUpdateService.class));
                        writeLog("AutoSync_Started_"+hour);
                    }else{
                        writeLog("AutoSync_NOT_Started_"+hour);
                    }
                }
            });

        }
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

    private void writeLog(String _data){
        new WriteLog().writeLog(getApplicationContext(),"AutoSyncService_"+_data);
    }

}
