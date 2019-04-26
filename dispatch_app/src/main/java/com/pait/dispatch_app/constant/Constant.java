package com.pait.dispatch_app.constant;

//Created by lnb on 8/11/2017.

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.pait.dispatch_app.R;
import com.pait.dispatch_app.log.WriteLog;

import java.io.File;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Constant {

    //TODO: Check VersionCode and Version Name

    public static String folder_name = "MSPL FootwearDA",
            gstFolderName = "Report",
            zipFolderName = "Zip",
            unzipFolderName = "UnZip",
            image_folder = "Images",
            captured_images_folder = "MSPL_CapturedImages",
            log_file_name = "MSPL_Footwear_DA",
            automailID = "automail@lnbinfotech.com",
            autoamilPass = "auto#456",
            mail_subject = "Log File",
            mail_body = "Find the Attached Log File",
            mailReceipient = "anup.p@paitsystems.com",
            ftp_adress = "ftp.lnbinfotech.com",
            ftp_username = "supportftp@lnbinfotech.com",
            ftp_password = "support$456",
            //ftp_directory = "Receipt Details";
            ftp_directory = "Test",
            zip_file = "SmartGST.zip";

    //TODO: Check Ip Address

    // PORT : 24085 - GRAVITY_PC
    // PORT : 24086 - SERVER

    //public static String ipaddress = "http://172.30.1.209/MSPLD1/service.svc";
    //public static String ipaddress = "http://license.lnbinfotech.com/MSPLD1/service.svc";
    public static String ipaddress = "http://43.239.147.103:24085/MSPLD1/service.svc";

    //TODO: Check Image Url
    public static String imgUrl = "http://43.239.147.103:24085/IMAGES/";

    //TODO: Check CustImage Url
    public static String custimgUrl = "http://43.239.147.103:24085/custImage/";

    private static int connectionFlag = 0;

    //TODO: Check liveTestFlag 1-ScreenShotDisable, 0-ScreenShotEnable
    public static int liveTestFlag = 1;

    //TODO: Check liveTestFlag 1-HideLog, 0-ShowLog
    private static int showLogFlag = 1;

    public static int TIMEOUT_CON = 10*1000;
    public static int TIMEOUT_SO = 2*60*1000;

    private Activity activity;
    private Context context;

    private static ProgressDialog pd;

    public static void showLog(String log) {
        if(showLogFlag==0) {
            Log.d("Log", "" + log);
        }
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

    public Constant() {
        //TODO: Change Image Ip Also
        if (connectionFlag == 0) {
            ipaddress = "http://103.109.13.200:24086/MSPLD1/service.svc";
            imgUrl = "http://103.109.13.200:24086/IMAGES/";
            custimgUrl = "http://103.109.13.200:24086/custImage/";
            connectionFlag = 1;
        } else if (connectionFlag == 1) {
            ipaddress = "http://43.239.147.103:24085/MSPLD1/service.svc";
            imgUrl = "http://43.239.147.103:24085/IMAGES/";
            custimgUrl = "http://43.239.147.103:24085/custImage/";
            connectionFlag = 0;
        }
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

    public static void deleteLogFile() {
        File sdFile = checkFolder(folder_name);
        File writeFile = new File(sdFile, log_file_name);
        if (writeFile.exists()) {
            if (writeFile.delete()) {
                showLog("Log File Delete");
            }
        }
    }

    public String getIMEINo() {
        String myAndroidDeviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (manager.getDeviceId() != null) {
            myAndroidDeviceId = manager.getDeviceId();
        } else {
            myAndroidDeviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return myAndroidDeviceId;
    }

    public String getIMEINo1(){
        String imeino="";
        TelephonyManager telephony = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            Class<?> telephonyClass = Class.forName(telephony.getClass().getName());
            Class<?>[] parameter = new Class[1];
            parameter[0] = int.class;
            Method getFirstMethod = telephonyClass.getMethod("getDeviceId", parameter);
            Log.d("Log", getFirstMethod.toString());
            Object[] obParameter = new Object[1];
            obParameter[0] = 0;
            //TelephonyManager manager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            String first = (String) getFirstMethod.invoke(telephony, obParameter);
            Log.d("Log", "FIRST :" + first);
            obParameter[0] = 1;
            String second = (String) getFirstMethod.invoke(telephony, obParameter);
            Log.d("Log", "SECOND :" + second);
            imeino = first+"^"+second;
        } catch (Exception e) {
            e.printStackTrace();
            writeLog("getIMEINo1_"+e.getMessage());
        }
        return imeino;
    }

    public String getDate() {
        String str = "";
        try {
            str = new SimpleDateFormat("dd/MMM/yyyy", Locale.ENGLISH).format(new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    private void writeLog(String _data) {
        new WriteLog().writeLog(context, _data);
    }

}
