package com.lnbinfotech.msplfootwear.braodcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.lnbinfotech.msplfootwear.constant.Constant;
import com.lnbinfotech.msplfootwear.interfaces.SmsListener;

//Created by SNEHA on 10/16/2017.

public class ReadSms extends BroadcastReceiver {
    boolean b;
    String text;
    private static SmsListener smsListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        if (bundle != null) {
            final Object[] pdusobj = (Object[]) bundle.get("pdus");
            assert pdusobj != null;
            for (int i = 0; i <= pdusobj.length - 1; i++) {
                SmsMessage current_msg = SmsMessage.createFromPdu((byte[]) pdusobj[i]);

                String cmp_name = current_msg.getDisplayOriginatingAddress();
                Constant.showLog("mob_no" + cmp_name);

                String service_center = current_msg.getServiceCenterAddress();
                Constant.showLog("service_cebter" + service_center);

                //String sender_no = cmp_name;
                // if(cmp_name.equals("MD-LNBTCH") && service_center.equals("+919868191090")) {

                String message = current_msg.getDisplayMessageBody();
                text = message.replaceAll("[^0-9]", "");
                Constant.showLog("text:" + text.substring(0, 6));
                Constant.showLog("text:" + text.substring(0, 1));
                Constant.showLog("text:" + text.substring(1, 2));
                if (!b) {
                    smsListener.onReceivedMessage(text);
                }
                Constant.showLog("ReadSMS_onReceive_Called");
                //  }
            }
        }
    }

    public void bindListener(SmsListener listener) {
        smsListener = listener;
        Constant.showLog("ReadSMS_bindListener_Called");
    }
}

