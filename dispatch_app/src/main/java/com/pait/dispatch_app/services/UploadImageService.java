package com.pait.dispatch_app.services;

import android.app.IntentService;
import android.app.Notification;
import android.content.Intent;
import android.os.Environment;

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
        startForeground(1,new Notification());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try{
            Constant.showLog("Service started..");
            //String filename = intent.getStringExtra("filename");
            File f;/*
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                f = new File(Environment.getExternalStorageDirectory() + File.separator +
                        Constant.folder_name + File.separator + Constant.image_folder + File.separator + filename);
            } else {
                f = new File(getFilesDir(), filename);
            }*/
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
        }
    }

    private void writeLog(String _data) {
        new WriteLog().writeLog(getApplicationContext(), "UploadImageService_" + _data);
    }

}
