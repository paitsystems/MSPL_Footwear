package com.pait.cust;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Toast;

import com.pait.cust.connectivity.ConnectivityTest;
import com.pait.cust.constant.Constant;
import com.pait.cust.db.DBHandler;
import com.pait.cust.log.WriteLog;
import com.pait.cust.permission.GetPermission;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

        if(Constant.liveTestFlag==1) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        setContentView(R.layout.activity_first);

        overridePendingTransition(R.anim.enter,R.anim.exit);

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
        }else if(!permission.checkReceiveSMSPermission(getApplicationContext())){
            permission.requestReceiveSMSPermission(getApplicationContext(),FirstActivity.this);//9
        }else if(!permission.checkReadSMSPermission(getApplicationContext())){
            permission.requestReadSMSPermission(getApplicationContext(),FirstActivity.this);//10
        }else if(!permission.checkSendSMSPermission(getApplicationContext())){
            permission.requestSendSMSPermission(getApplicationContext(),FirstActivity.this);//11
        }else if(!permission.checkCoarseLocationPermission(getApplicationContext())){
            permission.requestCoarseLocationPermission(getApplicationContext(),FirstActivity.this);//6
        }else if(!permission.checkFineLocationPermission(getApplicationContext())){
            permission.requestFineLocationPermission(getApplicationContext(),FirstActivity.this);//5
        }else {
            if(ConnectivityTest.getNetStat(getApplicationContext())) {
                doThis();
            }else{
                showDia(1);
            }
        }
    }

    private void doThis(){
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            dbpath = pInfo.applicationInfo.dataDir+"/databases";
            Constant.showLog(dbpath);
            CopyDb();
        } catch (Exception e) {
            e.printStackTrace();
            writeLog("doThis_"+e.getMessage());
        }

        if (pref.contains(getString(R.string.pref_isRegistered))) {
            startActivity(new Intent(getApplicationContext(), CustomerDetailsActivity.class));
        } else {
            startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
        }

        /*if (!pref.contains(getString(R.string.pref_FTPLocation))) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putString(getString(R.string.pref_FTPLocation),"ftp.lnbinfotech.com");
            editor.putString(getString(R.string.pref_FTPUser),"supportftp@lnbinfotech.com");
            editor.putString(getString(R.string.pref_FTPPass),"support$456");
            editor.putString(getString(R.string.pref_FTPImgFolder),"Test");
            editor.apply();
        }*/
        DBHandler db = new DBHandler(getApplicationContext());
        db.close();
        overridePendingTransition(R.anim.enter,R.anim.exit);
        doFinish();
    }

    private void CopyDb() throws IOException {
        if(!checkDB()){
            //constant.showPD();
            InputStream is = getApplicationContext().getAssets().open(DBHandler.Database_Name);
            File file = new File(dbpath);
            if(!file.exists()) {
                if (file.mkdir()) {
                    Constant.showLog("Database Created");
                    writeLog("CopyDb_Database_Created");
                }
            }
            OutputStream os = new FileOutputStream(dbpath+"/"+DBHandler.Database_Name);
            byte[] buffer = new byte[2014];
            while (is.read(buffer)>0){
                os.write(buffer);
            }
            os.flush();
            os.close();
            is.close();
            DBHandler db = new DBHandler(getApplicationContext());
            db.deleteTable(DBHandler.Table_CustomerOrder);
            db.deleteTable(DBHandler.Table_Usermaster);
            //constant.showPD();
            if(!pref.contains(getString(R.string.pref_lastSync))){
                SharedPreferences.Editor editor = pref.edit();
                String str = getTime();
                Constant.showLog("Last Sync - "+str);
                editor.putString(getString(R.string.pref_lastSync),str);
                editor.apply();
            }
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
                checkpermmission();
                break;
            case 9:
                checkpermmission();
                break;
            case 10:
                checkpermmission();
                break;
            case 11:
                checkpermmission();
                break;
            case 6:
                checkpermmission();
                break;
            case 5:
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

    private String getTime() {
        String str = "";
        try{
            str = new SimpleDateFormat("dd/MMM/yyyy HH:mm", Locale.ENGLISH).format(new Date());
        }catch (Exception e){
            e.printStackTrace();
        }
        return str;
    }

    private void showDia(int a) {
        AlertDialog.Builder builder = new AlertDialog.Builder(FirstActivity.this);
        builder.setCancelable(false);
        if (a == 1) {
            builder.setTitle("You Are Offline");
            builder.setMessage("Please Connect To Network?");
            builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    checkpermmission();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    new Constant(FirstActivity.this).doFinish();

                }
            });
        }
        builder.create().show();
    }

    private void writeLog(String _data){
        new WriteLog().writeLog(getApplicationContext(),"FirstAcitivity_"+_data);
    }
}
