package com.lnbinfotech.msplfootwear.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import com.lnbinfotech.msplfootwear.FirstActivity;
import com.lnbinfotech.msplfootwear.R;
import com.lnbinfotech.msplfootwear.constant.Constant;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by SNEHA on 10/25/2017.
 */
public class UploadImageService extends IntentService {
    public static final String BROADCAST = "imageUploadBroadcast";//android.net.conn.CONNECTIVITY_CHANGE

    public UploadImageService() {
        super(UploadImageService.class.getName());
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        try
        {
            Constant.showLog("Service started..");
            FirstActivity.pref =  getSharedPreferences(FirstActivity.PREF_NAME,MODE_PRIVATE);
            String ftpaddress = FirstActivity.pref.getString(getString(R.string.pref_FTPLocation), "");
            String ftpuser = FirstActivity.pref.getString(getString(R.string.pref_FTPUser), "");
            String ftppass = FirstActivity.pref.getString(getString(R.string.pref_FTPPass), "");
            String ftpfolder = FirstActivity.pref.getString(getString(R.string.pref_FTPImgFolder), "");

            String filename = intent.getStringExtra("filename");
            File f;
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                f = new File(Environment.getExternalStorageDirectory() + File.separator + Constant.folder_name + File.separator + filename);
            } else {
                f = new File(getFilesDir(), filename);
            }
            FTPClient client = new FTPClient();
            client.connect(ftpaddress, 21);
            client.login(ftpuser, ftppass);
            client.setFileType(FTP.BINARY_FILE_TYPE);
            client.enterLocalPassiveMode();
            if (f != null) {
                FileInputStream ifile = new FileInputStream(f);
                client.cwd(ftpfolder);
                //client.cwd("CLR");
                if (client.storeFile(filename, ifile)) {
                    f.delete();
                    Constant.showLog("Image deleted..");
                } else {
                    Log.d("Log", "Log");
                }
            }
            client.disconnect();
            Constant.showLog("disconnected..");
            // f.delete();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
