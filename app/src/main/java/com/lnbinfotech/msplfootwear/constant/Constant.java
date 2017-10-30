package com.lnbinfotech.msplfootwear.constant;

//Created by lnb on 8/11/2017.

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.lnbinfotech.msplfootwear.R;
import com.lnbinfotech.msplfootwear.braodcasts.AutoUpdateBroadcastReceiver;
import com.lnbinfotech.msplfootwear.log.WriteLog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Constant {

    //TODO: Check VersionCode and Version Name

    public static String folder_name = "Ticket",image_folder = "Caputured_img",
            log_file_name = "Ticket",
            automailID = "automail@lnbinfotech.com",
            autoamilPass = "auto#456",
            mail_subject = "Log File",
            mail_body = "Find the Attached Log File",
            mailReceipient = "anup.p@lnbinfotech.com";
    //ftp_adress = "ftp.lnbinfotech.com"
    //ftp_username = "supportftp@lnbinfotech.com",
    //ftp_password = "support$456",
    //ftp_directory = "SMVisit_Indus",

    //TODO: Check Ip Address
     //public static final String ipaddress = "http://172.30.1.38/MSPL/service.svc";
    public static final String ipaddress = "http://license.lnbinfotech.com/MSPL/service.svc";
    //TODO: Check liveTestFlag 1-Live, 0-Test
    public static int liveTestFlag = 0;

    Activity activity;
    Context context;

    static ProgressDialog pd;

    public static void showLog(String log) {
        Log.d("Log", ""+log);
    }

    public Constant(Activity activity) {
        this.activity = activity;
        pd = new ProgressDialog(activity);
        pd.setCancelable(false);
        pd.setMessage("Please Wait");
    }

    public Constant(Context context) {
        this.context = context;
    }

    public void showPD() {
        if (pd.isShowing()) {
            pd.dismiss();
        } else {
            pd.show();
        }
    }

    public static File checkFolder(String foldername) {
        File extFolder = new File(android.os.Environment.getExternalStorageDirectory() + File.separator + foldername);
        if (!extFolder.exists()) {
            if (extFolder.mkdir()) {
                Constant.showLog("Directory Created");
            }
        }
        return extFolder;
    }

    public void doFinish() {
        activity.finish();
        activity.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }

    public void setRecurringAlarm() {
        Intent intent = new Intent(context, AutoUpdateBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        //alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,SystemClock.elapsedRealtime()+AlarmManager.INTERVAL_FIFTEEN_MINUTES,SystemClock.elapsedRealtime(),pendingIntent);
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 120000, 120000, pendingIntent);
        Constant.showLog("Alarm Set");
        Date date = new Date(SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_FIFTEEN_MINUTES);
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        String formatted = formatter.format(date);
        String s = "Alarm Set To " + formatted + "_";
        writeLog(s);
    }

    public static void deleteLogFile() {
        File sdFile = checkFolder(folder_name);
        File writeFile = new File(sdFile, log_file_name);
        if (writeFile.exists()) {
            if (writeFile.delete()) {
                showLog("Log File Delete");
            }
        }
    }

    public String getIMEINo(){
        TelephonyManager manager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        return manager.getDeviceId();
    }

    private void writeLog(String _data) {
        new WriteLog().writeLog(context, _data);
    }

}
