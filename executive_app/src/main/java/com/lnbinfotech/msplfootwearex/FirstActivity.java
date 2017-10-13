package com.lnbinfotech.msplfootwearex;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.Toast;

import com.lnbinfotech.msplfootwearex.connectivity.ConnectivityTest;
import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.db.DBHandler;
import com.lnbinfotech.msplfootwearex.log.WriteLog;
import com.lnbinfotech.msplfootwearex.permission.GetPermission;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

// Created by lnb on 8/11/2016.

public class FirstActivity extends AppCompatActivity {

    public static SharedPreferences pref;
    public static String PREF_NAME = "Tickets";
    private GetPermission permission;
    public static Context context;
    private Toast toast;
    private String dbpath;
    private Constant constant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        pref = getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        permission = new GetPermission();
        context = getApplicationContext();
        toast = Toast.makeText(getApplicationContext(),"",Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        constant = new Constant(FirstActivity.this);
        checkpermmission();
    }

    private void checkpermmission(){
        if(!permission.checkCameraPermission(getApplicationContext())){
            permission.requestCameraPermission(getApplicationContext(),FirstActivity.this);//1
        }else if(!permission.checkReadExternalStoragePermission(getApplicationContext())){
            permission.requestReadExternalPermission(getApplicationContext(),FirstActivity.this);//2
        }else if(!permission.checkWriteExternalStoragePermission(getApplicationContext())){
            permission.requestWriteExternalPermission(getApplicationContext(),FirstActivity.this);//3
        }else if(!permission.checkReadPhoneStatePermission(getApplicationContext())){
            permission.requestReadPhoneStatPermission(getApplicationContext(),FirstActivity.this);//4
        }else if(!permission.checkRebootPermission(getApplicationContext())){
            permission.requestRebootPermission(getApplicationContext(),FirstActivity.this);//7
        }else {
            if(ConnectivityTest.getNetStat(getApplicationContext())) {
                doThis();
            }else{
                toast.setText(getString(R.string.you_are_offline));
                toast.show();
            }
        }
    }

    private void doThis(){
        //new DBHandler(getApplicationContext());
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            dbpath = pInfo.applicationInfo.dataDir+"/databases";
            Constant.showLog(dbpath);
            CopyDb();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (pref.contains(getString(R.string.pref_isRegistered))) {
            startActivity(new Intent(getApplicationContext(), CustomerDetailsActivity.class));
        } else {
            startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
        }
        if (!pref.contains(getString(R.string.pref_FTPLocation))) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putString(getString(R.string.pref_FTPLocation),"ftp.lnbinfotech.com");
            editor.putString(getString(R.string.pref_FTPUser),"supportftp@lnbinfotech.com");
            editor.putString(getString(R.string.pref_FTPPass),"support$456");
            editor.putString(getString(R.string.pref_FTPImgFolder),"Test");
            editor.apply();
           // editor.commit();
        }
        overridePendingTransition(R.anim.enter,R.anim.exit);
        doFinish();
    }

    private void CopyDb() throws IOException {
        if(!checkDB()){
            constant.showPD();
            InputStream is = getApplicationContext().getAssets().open(DBHandler.Database_Name);
            File file = new File(dbpath);
            if(!file.exists()){
                if(file.mkdir())
                    Constant.showLog("Database Created");
            }
            OutputStream os = new FileOutputStream(dbpath+"/"+DBHandler.Database_Name);
            byte[] buffer = new byte[2014];
            while (is.read(buffer)>0){
                os.write(buffer);
            }
            os.flush();
            os.close();
            is.close();
            constant.showPD();
        }
    }

    private boolean checkDB(){
        File file = getApplicationContext().getDatabasePath(DBHandler.Database_Name);
        return file.exists();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                checkpermmission();
                break;
            case 2:
                checkpermmission();
                break;
            case 3:
                checkpermmission();
                break;
            case 4:
                checkpermmission();
                break;
            case 7:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    writeLog("All_Permission_Granted");
                    doThis();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        doFinish();
    }

    private void doFinish(){
        finish();
        toast.cancel();
        overridePendingTransition(R.anim.enter,R.anim.exit);
    }

    private void writeLog(String _data){
        new WriteLog().writeLog(getApplicationContext(),"FirstAcitivity_"+_data);
    }
}
