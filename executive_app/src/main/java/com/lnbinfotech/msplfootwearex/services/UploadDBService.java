package com.lnbinfotech.msplfootwearex.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageInfo;

import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.db.DBHandler;
import com.lnbinfotech.msplfootwearex.log.WriteLog;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;

//Created by Anup on 10/13/2018.

public class UploadDBService extends IntentService {

    public UploadDBService() {
        super(UploadDBService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        FTPClient client = null;
        try {
            Constant.showLog("Service started..");
            client = new FTPClient();
            try {
                client.connect(Constant.ftp_adress1, 21);
            } catch (Exception e) {
                e.printStackTrace();
                writeLog("UploadDBService_doInBackground_"+e.getMessage());
                client.connect(Constant.ftp_adress2, 21);
            }
            client.login(Constant.ftp_username, Constant.ftp_password);
            client.setFileType(FTP.BINARY_FILE_TYPE);
            client.enterLocalPassiveMode();
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            //String dbPath = pInfo.applicationInfo.dataDir + "/databases";
            String dbPath = android.os.Environment.getExternalStorageDirectory() + File.separator + Constant.folder_name;
            Constant.showLog(dbPath + "/SmartGST.zip");
            File f = new File(dbPath);
            for (File file : f.listFiles()) {
                if (file != null) {
                    Constant.showLog("File Name " + file.getName());
                    if (file.getName().equals("SmartGST.zip")) {
                        FileInputStream iFile = new FileInputStream(file);
                        client.cwd(Constant.dir_data_sync);
                        if (client.storeFile(file.getName(), iFile)) {
                            writeLog("onHandleIntent_File_Store_Successfully");
                            Constant.showLog("File Stored " + file.getName());
                        } else {
                            writeLog("onHandleIntent_Error_While_Storing_File");
                        }
                    }
                }
            }
            client.disconnect();
            Constant.showLog("disconnected..");
        } catch (Exception e) {
            e.printStackTrace();
            writeLog("onHandleIntent_"+e.getMessage());
        }finally {
            try {
                if (client != null) {
                    client.disconnect();
                }
            }catch (Exception e){
                e.printStackTrace();
                writeLog("finally_"+e.getMessage());
            }
        }
    }

    private void writeLog(String _data) {
        new WriteLog().writeLog(getApplicationContext(), "UploadDBService_" + _data);
    }

}
