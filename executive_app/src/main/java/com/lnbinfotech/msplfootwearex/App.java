package com.lnbinfotech.msplfootwearex;

import android.app.Application;
import android.content.Intent;

import com.lnbinfotech.msplfootwearex.connectivity.ConnectivityTest;
import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.services.LocationServicePRM;

//Created by Anup on 01-09-2018.

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Constant.showLog("App_onCreate()");
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if(level == TRIM_MEMORY_UI_HIDDEN){
            Constant.showLog("App_onTrimMemory()");
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
                if (ConnectivityTest.getNetStat(getApplicationContext())) {
                    Intent intent = new Intent(App.this, LocationServicePRM.class);
                    //startService(intent);
                }
            }
        }
    }
}
