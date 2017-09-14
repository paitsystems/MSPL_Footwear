package com.lnbinfotech.msplfootwearex;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.Toast;

import com.lnbinfotech.msplfootwearex.connectivity.ConnectivityTest;
import com.lnbinfotech.msplfootwearex.log.WriteLog;
import com.lnbinfotech.msplfootwearex.permission.GetPermission;

// Created by lnb on 8/11/2016.

public class FirstActivity extends AppCompatActivity {

    public static SharedPreferences pref;
    public static String PREF_NAME = "Tickets";
    private GetPermission permission;
    public static Context context;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        pref = getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        permission = new GetPermission();
        context = getApplicationContext();
        toast = Toast.makeText(getApplicationContext(),"",Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
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
        if (pref.contains(getString(R.string.pref_isRegistered))) {
            startActivity(new Intent(getApplicationContext(), CustomerDetailsActivity.class));
        } else {
            startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
        }
        overridePendingTransition(R.anim.enter,R.anim.exit);
        doFinish();
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
