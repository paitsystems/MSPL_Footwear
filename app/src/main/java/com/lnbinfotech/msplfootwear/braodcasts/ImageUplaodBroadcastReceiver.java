package com.lnbinfotech.msplfootwear.braodcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.util.Log;

import com.lnbinfotech.msplfootwear.connectivity.ConnectivityTest;
import com.lnbinfotech.msplfootwear.constant.Constant;
import com.lnbinfotech.msplfootwear.services.UploadImageService;

import java.io.File;

//Created by SNEHA on 10/25/2017.

public class ImageUplaodBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION) || action.equals("test")) {
                File f = null;
                if (ConnectivityTest.getNetStat(context)) {
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        f = new File(Environment.getExternalStorageDirectory() + File.separator +
                                Constant.folder_name + File.separator + Constant.image_folder);
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
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

