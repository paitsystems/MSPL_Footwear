package com.lnbinfotech.msplfootwearex.braodcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.lnbinfotech.msplfootwearex.CheckOTPActivity;
import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.interfaces.SmsListener;

/**
 * Created by SNEHA on 10/14/2017.
 */
public class ReadSms extends BroadcastReceiver {
    boolean b ;
    String text;
    private static SmsListener smsListener;
    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        if(bundle != null){
            final Object[] pdusobj = (Object[]) bundle.get("pdus");
            for (int i = 0; i <= pdusobj.length-1; i++){
                SmsMessage current_msg = SmsMessage.createFromPdu((byte[]) pdusobj[i]);
                String mob_no = current_msg.getDisplayOriginatingAddress();
                String sender_no = mob_no;
                String message = current_msg.getDisplayMessageBody();
                text = message.replaceAll("[^0-9]","");
                Constant.showLog("text:"+text.substring(0,6));
                Constant.showLog("text:"+text.substring(0,1));
                Constant.showLog("text:"+text.substring(1,2));
                if (!b){
                  smsListener.onReceivedMessage(text);
                }
                Constant.showLog("ReadSMS_onReceive_Called");
            }
        }
    }

    public static void bindListener(SmsListener listener) {
        smsListener = listener;
        Constant.showLog("ReadSMS_bindListener_Called");
    }
}
