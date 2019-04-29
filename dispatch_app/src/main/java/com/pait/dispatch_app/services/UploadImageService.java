package com.pait.dispatch_app.services;

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

import com.pait.dispatch_app.R;
import com.pait.dispatch_app.constant.Constant;
import com.pait.dispatch_app.log.WriteLog;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;

//Created by SNEHA on 10/25/2017.

public class UploadImageService extends IntentService {

    public UploadImageService() {
        super(UploadImageService.class.getName());
    }

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
        String NOTIFICATION_CHANNEL_ID = "com.pait.dispatch_app";
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
                .setContentTitle("Uploading Images")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try{
            Constant.showLog("Service started..");
            File f;
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                f = new File(Environment.getExternalStorageDirectory() + File.separator +
                        Constant.folder_name + File.separator + Constant.image_folder);
            } else {
                f = getApplicationContext().getFilesDir();
            }
            FTPClient client = new FTPClient();
            client.connect(Constant.ftp_adress, 21);
            client.login(Constant.ftp_username, Constant.ftp_password);
            client.setFileType(FTP.BINARY_FILE_TYPE);
            client.enterLocalPassiveMode();
            if (f != null) {
                if (f.exists()) {
                    Constant.showLog(f.getPath());
                    for (File file : f.listFiles()) {
                        if (file != null && !file.isDirectory()) {
                            FileInputStream ifile = new FileInputStream(file);
                            client.cwd(Constant.ftp_directory);
                            String str = file.getName();
                            String arr[] = str.split("_");
                            try {
                                String month = arr[6];
                                String day = arr[5];
                                String dpCenter = arr[1];
                                client.cwd(month + "/" + day + "/" + dpCenter);
                            } catch (Exception e){
                                if(!file.getName().equals("temp.jpg")) {
                                    e.printStackTrace();
                                    writeLog("onHandleIntent_" + e.getMessage());
                                }
                            }
                            if (client.storeFile(file.getName(), ifile)) {
                                file.delete();
                                Constant.showLog("Image deleted.."+file.getName());
                            } else {
                                writeLog("onHandleIntent_Error_While_Storing_File");
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
