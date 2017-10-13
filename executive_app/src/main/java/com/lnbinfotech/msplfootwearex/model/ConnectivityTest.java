package com.lnbinfotech.msplfootwearex.model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by SNEHA on 12-10-2017.
 */
public class ConnectivityTest {
    public static boolean getNetStat(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net_info = cm.getActiveNetworkInfo();
        if (net_info != null) {
            if (net_info.isConnected()) {
                return true;
            }
        }
        return false;
    }
}
