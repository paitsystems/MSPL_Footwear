package com.lnbinfotech.msplfootwear.services;

// Created by lnb on 8/16/2017.

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;

import com.lnbinfotech.msplfootwear.FirstActivity;
import com.lnbinfotech.msplfootwear.R;
import com.lnbinfotech.msplfootwear.constant.Constant;
import com.lnbinfotech.msplfootwear.log.WriteLog;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class DownloadImageService extends IntentService {

    public static final String BROADCAST = "imageDownloadedBroadcast";

    public DownloadImageService() {
        super(DownloadImageService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            Constant.showLog("Service Started");
            String imageName = intent.getExtras().getString("imageName");
            FTPClient ftp = new FTPClient();
            try {
                ftp.connect(Constant.ftp_adress1, 21);
            } catch (Exception e) {
                e.printStackTrace();
                writeLog("DownloadImageService_doInBackground_"+e.getMessage());
                ftp.connect(Constant.ftp_adress2, 21);
            }
            int reply = ftp.getReplyCode();
            if (FTPReply.isPositiveCompletion(reply)) {
                ftp.login(Constant.ftp_username, Constant.ftp_password);
                ftp.setFileType(FTP.BINARY_FILE_TYPE);
                ftp.enterLocalPassiveMode();
                if (ftp.changeWorkingDirectory(Constant.dir_Customer_Master)) {
                    File fname = new File(Environment.getExternalStorageDirectory() + File.separator + Constant.folder_name, imageName);
                    OutputStream outstream = new BufferedOutputStream(new FileOutputStream(fname));
                    ftp.retrieveFile(imageName, outstream);
                    outstream.close();
                    ftp.logout();
                    ftp.disconnect();
                    Intent intent1 = new Intent(BROADCAST);
                    sendBroadcast(intent1);
                    writeLog("DownloadImageService_onHandleIntent_broadcastSend");
                } else {
                    writeLog("DownloadImageService_onHandleIntent_changeWorkingDirectory");
                }
            } else {
                writeLog("DownloadImageService_onHandleIntent_isPositiveCompletion");
            }
        } catch (Exception e) {
            e.printStackTrace();
            writeLog("DownloadImageService_onHandleIntent_"+e.getMessage());
        }
    }

    private void writeLog(String _data){
        new WriteLog().writeLog(getApplicationContext(),_data);
    }

}

