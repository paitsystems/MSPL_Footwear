package com.pait.exec.braodcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.pait.exec.constant.Constant;
import com.pait.exec.interfaces.SmsListener;
import com.pait.exec.log.WriteLog;

// Created by SNEHA on 10/14/2017.

public class ReadSms extends BroadcastReceiver {
    private boolean b ;
    private String text;
    private static SmsListener smsListener;
    private Context context;

    @Override
    public void onReceive(Context _context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        this.context = _context;
        if(smsListener!=null) {
            if (bundle != null) {
                try {
                    final Object[] pdusobj = (Object[]) bundle.get("pdus");
                    for (int i = 0; i <= pdusobj.length - 1; i++) {
                        SmsMessage current_msg = SmsMessage.createFromPdu((byte[]) pdusobj[i]);
                        String mob_no = current_msg.getDisplayOriginatingAddress();
                        String sender_no = mob_no;
                        String message = current_msg.getDisplayMessageBody();
                        text = message.replaceAll("[^0-9]", "");
                        //if(text.length()>=6 && text!=null) {
                        Constant.showLog("text:" + text.substring(0, 6));
                        Constant.showLog("text:" + text.substring(0, 1));
                        Constant.showLog("text:" + text.substring(1, 2));
                        if (!b) {
                            smsListener.onReceivedMessage(text);
                        }
                        Constant.showLog("ReadSMS_onReceive_Called");
                        //}
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    writeLog("onReceive_"+e.getMessage());
                }
            }
        }
    }

    public void bindListener(SmsListener listener) {
        smsListener = listener;
        Constant.showLog("ReadSMS_bindListener_Called");
    }

    private void writeLog(String _data) {
        new WriteLog().writeLog(context, "ReadSms_" + _data);
    }
}
