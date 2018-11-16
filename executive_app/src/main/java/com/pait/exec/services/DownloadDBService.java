package com.pait.exec.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.Environment;

import com.pait.exec.FirstActivity;
import com.pait.exec.R;
import com.pait.exec.constant.Constant;
import com.pait.exec.db.DBHandler;
import com.pait.exec.log.WriteLog;
import com.pait.exec.model.CustomerOrderClass;
import com.pait.exec.model.UserClass;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

//Created by Anup on 10/13/2018.

public class DownloadDBService extends IntentService {

    private ArrayList<UserClass> userList;
    private ArrayList<CustomerOrderClass> custList;

    public DownloadDBService() {
        super(DownloadImageService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        FTPClient ftp = null;
        try{
            FirstActivity.pref = getApplicationContext().getSharedPreferences(FirstActivity.PREF_NAME,MODE_PRIVATE);
            Constant.showLog("Service Started");
            ftp = new FTPClient();
            String imageName = DBHandler.Database_Name;
            ftp.connect(Constant.ftp_adress, 21);
            int reply = ftp.getReplyCode();
            if (FTPReply.isPositiveCompletion(reply)) {
                ftp.login(Constant.ftp_username, Constant.ftp_password);
                ftp.setFileType(FTP.BINARY_FILE_TYPE);
                ftp.enterLocalPassiveMode();
                if (ftp.changeWorkingDirectory(Constant.ftp_directory)) {
                    File fName = new File(Environment.getExternalStorageDirectory() + File.separator + Constant.folder_name, DBHandler.Database_Name);
                    OutputStream outStream;
                    outStream = new BufferedOutputStream(new FileOutputStream(fName));
                    ftp.retrieveFile(imageName, outStream);
                    outStream.close();
                    ftp.logout();
                    ftp.disconnect();
                    Constant.showLog("File Downloaded");
                    writeLog("onHandleIntent_File_Downloaded");
                    getData();
                    CopyDb();
                    /*Intent intent1 = new Intent(BROADCAST);
                    sendBroadcast(intent1);*/
                    writeLog("DownloadImageService_onHandleIntent_broadcastSend");
                }
            }
        } catch (Exception e) {
            /*Intent intent1 = new Intent(BROADCAST);
            sendBroadcast(intent1);*/
            e.printStackTrace();
            writeLog("onHandleIntent_"+e.getMessage());
        }finally {
            try {
                if (ftp != null) {
                    ftp.disconnect();
                }
            }catch (Exception e){
                e.printStackTrace();
                writeLog("finally_"+e.getMessage());
            }
        }
    }

    private void CopyDb() throws IOException {
        try {
            String currentDBPath = android.os.Environment.getExternalStorageDirectory() + File.separator + Constant.folder_name;
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String backupDBPath = pInfo.applicationInfo.dataDir + "/databases";
            File currentDB = new File(currentDBPath, DBHandler.Database_Name);
            File backupDB = new File(backupDBPath, DBHandler.Database_Name);
            Constant.showLog(currentDB.getAbsolutePath());
            Constant.showLog(backupDB.getAbsolutePath());
            FileChannel source = new FileInputStream(currentDB).getChannel();
            FileChannel destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            SharedPreferences.Editor editor = FirstActivity.pref.edit();
            String str = getTime();
            Constant.showLog("Last Sync - " + str);
            writeLog("CopyDb_Last Sync_" + str);
            editor.putString(getString(R.string.pref_lastSync), str);
            editor.apply();
            DBHandler db = new DBHandler(getApplicationContext());
            db.deleteTable(DBHandler.Table_CustomerOrder);
            db.deleteTable(DBHandler.Table_Usermaster);
            putData();
        } catch (Exception e) {
            e.printStackTrace();
            writeLog("CopyDb_"+e.getMessage());
        }
    }

    private void getData() {
        DBHandler db = new DBHandler(getApplicationContext());
        userList = db.getUserDetail();
        custList = db.getCustOrder();
        Constant.showLog("userList-"+userList.size()+"-custList-"+custList.size());
    }

    private void putData() {
        DBHandler db = new DBHandler(getApplicationContext());
        for(int i=0;i<userList.size();i++) {
            db.addUserDetail(userList.get(i));
        }
        for(int i=0;i<custList.size();i++) {
            db.addCustomerOrder(custList.get(i));
        }
        Constant.showLog("userList-"+userList.size()+"-custList-"+custList.size());
    }

    private String getTime() {
        String str = "";
        try{
            str = new SimpleDateFormat("dd/MMM/yyyy HH:mm", Locale.ENGLISH).format(new Date());
        }catch (Exception e){
            e.printStackTrace();
        }
        return str;
    }

    private void writeLog(String _data){
        new WriteLog().writeLog(getApplicationContext(),"DownloadDBService_"+_data);
    }

}


