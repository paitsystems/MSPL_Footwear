package com.lnbinfotech.msplfootwearex.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.model.ConnectivityTest;

import java.io.File;

//Created by SNEHA on 12-10-2017.

public class ImageUplaodBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals("android.net.conn.CONNECTIVITY_CHANGE") || action.equals("test")) {
            File f = null;
            if (ConnectivityTest.getNetStat(context)) {
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    f = new File(Environment.getExternalStorageDirectory() + File.separator + Constant.folder_name);
                } else {
                    f = context.getFilesDir();
                }
                if (f != null) {
                    if (f.exists()) {
                        Log.d("Path", f.getPath());
                        for (File file : f.listFiles()) {
                            Intent intent2 = new Intent(context, UploadImageService.class);
                            intent2.putExtra("filename", file.getName());
                            context.startService(intent2);
                        }
                    }
                }
            }
        }
    }
}