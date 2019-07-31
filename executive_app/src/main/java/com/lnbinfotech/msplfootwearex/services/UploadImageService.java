package com.lnbinfotech.msplfootwearex.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import com.lnbinfotech.msplfootwearex.R;
import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.log.WriteLog;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;

// Created by SNEHA on 12-10-2017.

public class UploadImageService extends IntentService {

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground(){
        String NOTIFICATION_CHANNEL_ID = "com.example.simpleapp";
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }

    public UploadImageService() {
        super(UploadImageService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            Constant.showLog("Service started..");
            FTPClient client = new FTPClient();
            try {
                client.connect(Constant.ftp_adress1, 21);
            } catch (Exception e) {
                e.printStackTrace();
                writeLog("UploadImageService_doInBackground_"+e.getMessage());
                client.connect(Constant.ftp_adress2, 21);
            }
            client.login(Constant.ftp_username, Constant.ftp_password);
            client.setFileType(FTP.BINARY_FILE_TYPE);
            client.enterLocalPassiveMode();
            File f;
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                f = new File(Environment.getExternalStorageDirectory() + File.separator +
                        Constant.folder_name + File.separator + Constant.image_folder);
            } else {
                f = getApplicationContext().getFilesDir();
            }
            if (f != null) {
                if (f.exists()) {
                    Constant.showLog(f.getPath());
                    for (File file : f.listFiles()) {
                        if (file != null && !file.isDirectory()) {
                            FileInputStream ifile = new FileInputStream(file);
                            String str = file.getName();
                            String arr[] = str.split("_");
                            try{
                                String imgType = arr[0];
                                if(imgType.equals("C")){
                                    client.changeToParentDirectory();
                                    Constant.showLog(client.printWorkingDirectory());
                                    client.cwd(Constant.dir_Customer_Master);
                                    Constant.showLog(client.printWorkingDirectory());
                                    if (client.storeFile(file.getName(), ifile)) {
                                        if(!client.printWorkingDirectory().equals(Constant.dir_Customer_Master)){
                                            client.changeToParentDirectory();
                                            Constant.showLog(client.printWorkingDirectory());
                                        }
                                        file.delete();
                                        Constant.showLog("Customer Image deleted.."+file.getName());
                                    } else {
                                        writeLog("onHandleIntent_Error_While_Storing_Customer_File");
                                    }
                                } else if(imgType.equals("F")) {
                                    client.changeToParentDirectory();
                                    Constant.showLog(client.printWorkingDirectory());
                                    String month = arr[5];
                                    String day = arr[4];
                                    String hoCode = "HKHO";
                                    client.cwd(Constant.dir_Feed_Back);
                                    Constant.showLog(client.printWorkingDirectory());
                                    if(arr[3].equals("01") || arr[3].equals("1")){
                                        hoCode = "HKHO";
                                    } else if(arr[3].equals("12")){
                                        hoCode = "HKRD";
                                    }else if(arr[3].equals("13")){
                                        hoCode = "HANR";
                                    }
                                    Constant.showLog("F/"+hoCode + "/" + month);
                                    client.cwd(hoCode + "/" + month);
                                    Constant.showLog(client.printWorkingDirectory());
                                    if (client.storeFile(file.getName(), ifile)) {
                                        if(!client.printWorkingDirectory().equals(Constant.dir_Feed_Back)){
                                            client.changeToParentDirectory();
                                            Constant.showLog(client.printWorkingDirectory());
                                            if(!client.printWorkingDirectory().equals(Constant.dir_Feed_Back)){
                                                client.changeToParentDirectory();
                                                Constant.showLog(client.printWorkingDirectory());
                                                if(!client.printWorkingDirectory().equals(Constant.dir_Feed_Back)){
                                                    client.changeToParentDirectory();
                                                    Constant.showLog(client.printWorkingDirectory());
                                                }
                                            }
                                        }
                                        file.delete();
                                        Constant.showLog("Feedback Image deleted.."+file.getName());
                                    } else {
                                        writeLog("onHandleIntent_Error_While_Storing_Feedback_File");
                                    }
                                } else if(imgType.equals("Q")) {
                                    client.changeToParentDirectory();
                                    Constant.showLog(client.printWorkingDirectory());
                                    String month = arr[7];
                                    String day = arr[6];
                                    String hoCode = "HKHO";
                                    client.cwd(Constant.dir_Cheque_Details);
                                    Constant.showLog(client.printWorkingDirectory());
                                    if(arr[5].equals("1") || arr[5].equals("01")){
                                        hoCode = "HKHO";
                                    } else if(arr[5].equals("12")){
                                        hoCode = "HKRD";
                                    }else if(arr[5].equals("13")){
                                        hoCode = "HANR";
                                    }
                                    Constant.showLog("Q/"+hoCode + "/" + month + "/" + day);
                                    client.changeWorkingDirectory(hoCode + "/" + month + "/" + day);
                                    Constant.showLog(client.printWorkingDirectory());
                                    if (client.storeFile(file.getName(), ifile)) {
                                        if(!client.printWorkingDirectory().equals(Constant.dir_Cheque_Details)){
                                            client.changeToParentDirectory();
                                            Constant.showLog(client.printWorkingDirectory());
                                            if(!client.printWorkingDirectory().equals(Constant.dir_Cheque_Details)){
                                                client.changeToParentDirectory();
                                                Constant.showLog(client.printWorkingDirectory());
                                                if(!client.printWorkingDirectory().equals(Constant.dir_Cheque_Details)){
                                                    client.changeToParentDirectory();
                                                    Constant.showLog(client.printWorkingDirectory());
                                                }
                                            }
                                        }
                                        file.delete();
                                        Constant.showLog("Cheque Image deleted.."+file.getName());
                                    } else {
                                        writeLog("onHandleIntent_Error_While_Storing_Cheque_File");
                                    }
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                                writeLog("onHandleIntent_"+e.getMessage());
                            }
                        }
                    }
                }
            }
            client.disconnect();
            Constant.showLog("disconnected..");
        } catch (Exception e) {
            e.printStackTrace();
            writeLog("onHandleIntent_"+e.getMessage());
        }
    }

    private void writeLog(String _data) {
        new WriteLog().writeLog(getApplicationContext(), "UploadImageService_" + _data);
    }

}
