package com.lnbinfotech.msplfootwearex;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.lnbinfotech.msplfootwearex.braodcasts.MySMSBroadcastReceiver;
import com.lnbinfotech.msplfootwearex.braodcasts.ReadSms;
import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.interfaces.ServerCallback;
import com.lnbinfotech.msplfootwearex.interfaces.SmsListener;
import com.lnbinfotech.msplfootwearex.log.WriteLog;
import com.lnbinfotech.msplfootwearex.model.CheckOtpClass;
import com.lnbinfotech.msplfootwearex.utility.AppSignatureHelper;
import com.lnbinfotech.msplfootwearex.volleyrequests.VolleyRequests;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class CheckOTPActivity extends AppCompatActivity implements View.OnClickListener,
             MySMSBroadcastReceiver.OTPReceiveListener {

    private EditText ed1,ed2,ed3,ed4,ed5,ed6;
    private AppCompatButton btn_verifyotp,btn_resendotp;
    private Toast toast;
    private CheckOtpClass otpClass;
    private Constant constant;
    private final Timer timer = new Timer();
    private int time = 0;
    private TextView tv_timecount,tv_text1, tv_otp;
    private CountDownTimer countDown;
    private String mobNo,imeiNo, imeino1, imeino2;
    private String response_value;
    //private ReadSms receiver;
    //private MySMSReceiver receiver;
    private MySMSBroadcastReceiver receiver;
    private SmsRetrieverClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Constant.liveTestFlag==1) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        setContentView(R.layout.activity_checkotp);
        init();
        otpClass = (CheckOtpClass) getIntent().getSerializableExtra("otp");
        response_value = otpClass.getOtp();
        tv_otp.setText(response_value);
        mobNo = otpClass.getMobileno();
        imeiNo = otpClass.getImeino();
        imeino1 = otpClass.getImeino1();
        imeino2 = otpClass.getImeino2();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setTitle(R.string.title_activity_login);
        }

        //autoOTP();
        //receiver = new ReadSms();

        if(Constant.showLogFlag==0) {
            ArrayList<String> appCodes = new ArrayList<>();
            AppSignatureHelper hash = new AppSignatureHelper(getApplicationContext());
            appCodes = hash.getAppSignatures();
            String yourhash = appCodes.get(0);
            Constant.showLog("yourhash-" + yourhash);
        }

        //client = SmsRetriever.getClient(this);
        receiver = new MySMSBroadcastReceiver();
        receiver.initOTPListener(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
        getApplicationContext().registerReceiver(receiver, filter);
        //startSMSListener();

        resendOTP();

        if (countDown == null) {
            tv_text1.setText("Your OTP will get within 5 min..");
            int minutes = 5 * 60 * 1000;
            startTimerCount(minutes);
        }

        /*IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        //filter.addAction(getPackageName() + "android.provider.Telephony.SMS_RECEIVED");
        receiver = new MySMSReceiver();

        receiver.bindListener(new SmsListener() {
            @Override
            public void onReceivedMessage(String message) {
                Constant.showLog("message:"+message);
                ed1.setText(message.substring(0,1));
                Constant.showLog("message:"+message.substring(0,1));
                ed2.setText(message.substring(1,2));
                Constant.showLog("message:"+message.substring(1,2));
                ed3.setText(message.substring(2,3));
                Constant.showLog("message:"+message.substring(2,3));
                ed4.setText(message.substring(3,4));
                Constant.showLog("message:"+message.substring(3,4));
                ed5.setText(message.substring(4,5));
                Constant.showLog("message:"+message.substring(4,5));
                ed6.setText(message.substring(5,6));
                Constant.showLog("message:"+message.substring(5,6));
                timer.cancel();
                countDown.cancel();
                tv_text1.setText("OTP get successfully");
                Constant.showLog("CheckOTPActivity_onReceivedMessage_Called");
            }
        });

        registerReceiver(receiver, filter);*/
        /*IntentFilter filter = new IntentFilter();
        filter.addAction(getPackageName() + "android.provider.Telephony.SMS_RECEIVED");

        receiver = new MySMSReceiver();
        registerReceiver(receiver, filter);

        receiver.bindListener(new SmsListener() {
            @Override
            public void onReceivedMessage(String message) {
                Constant.showLog("message:"+message);
                ed1.setText(message.substring(0,1));
                Constant.showLog("message:"+message.substring(0,1));
                ed2.setText(message.substring(1,2));
                Constant.showLog("message:"+message.substring(1,2));
                ed3.setText(message.substring(2,3));
                Constant.showLog("message:"+message.substring(2,3));
                ed4.setText(message.substring(3,4));
                Constant.showLog("message:"+message.substring(3,4));
                ed5.setText(message.substring(4,5));
                Constant.showLog("message:"+message.substring(4,5));
                ed6.setText(message.substring(5,6));
                Constant.showLog("message:"+message.substring(5,6));
                timer.cancel();
                countDown.cancel();
                tv_text1.setText("OTP get successfully");
                Constant.showLog("CheckOTPActivity_onReceivedMessage_Called");
            }
        });

        if(countDown == null) {
            tv_text1.setText("Your OTP will get within 5 min..");
            int minutes = 5 * 60 * 1000;
            startTimerCount(minutes);
        }*/
        //timerCount();

        btn_verifyotp.setOnClickListener(this);
        btn_resendotp.setOnClickListener(this);

        ed1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(ed1.getText().toString().length()==1){
                    ed2.requestFocus();
                }
            }
        });

        ed2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(ed2.getText().toString().length()==1){
                    ed3.requestFocus();
                }
            }
        });

        ed3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(ed3.getText().toString().length()==1){
                    ed4.requestFocus();
                }
            }
        });

        ed4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(ed4.getText().toString().length()==1){
                    ed5.requestFocus();
                }
            }
        });

        ed5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(ed5.getText().toString().length()==1){
                    ed6.requestFocus();
                }
            }
        });

        ed6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(ed6.getText().toString().length()==1){
                    ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(ed6.getWindowToken(),0);
                    verifyOTP();
                    btn_resendotp.setSupportBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(),R.color.lightgray));
                   // btn_resendotp.setBackgroundColor(getResources().getColor(R.color.lightgray));
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_verifyotp:
                verifyOTP();
                break;
            case R.id.btn_resendotp:
                Constant.showLog("resend btn click!!");
                btn_resendotp.setEnabled(false);
                btn_resendotp.setSupportBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(),R.color.lightgray));
                resendOTP();
                //autoOTP();
                tv_text1.setText("Your OTP will get within 5 min..");
                int minutes = 5 * 60 * 1000;
                startTimerCount(minutes);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        showDia(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                showDia(0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onOTPReceived(String message) {
        Constant.showLog("message:" + message);
        ed1.setText(message.substring(0, 1));
        ed2.setText(message.substring(1, 2));
        ed3.setText(message.substring(2, 3));
        ed4.setText(message.substring(3, 4));
        ed5.setText(message.substring(4, 5));
        ed6.setText(message.substring(5, 6));
        timer.cancel();
        countDown.cancel();
        tv_text1.setText("OTP get successfully");
        Constant.showLog("CheckOTPActivity_onReceivedMessage_Called");

    }

    @Override
    public void onOTPTimeOut() {
        btn_verifyotp.setEnabled(false);
        btn_resendotp.setEnabled(true);
    }

    private void startSMSListener() {
        Task<Void> task = client.startSmsRetriever();

        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Constant.showLog("onSuccess");
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Constant.showLog("onFailure");
            }
        });
    }

    private void verifyOTP(){
        if(ed1.getText().toString().length()==1 && ed2.getText().toString().length()==1 &&
                ed3.getText().toString().length()==1 && ed4.getText().toString().length()==1 &&
                ed5.getText().toString().length()==1 && ed6.getText().toString().length()==1) {
            String otp = ed1.getText().toString()+ed2.getText().toString()+
                    ed3.getText().toString()+ed4.getText().toString()+
                    ed5.getText().toString()+ed6.getText().toString();
            Constant.showLog(otp);
            Log.d("Log","response_value:"+response_value);
            writeLog("response_value:"+response_value);
            if(otp.equals(response_value)) {
                Constant.showLog("response_value:"+response_value);
                writeLog("OTP_Matched");
                showDia(1);
            }else{
                writeLog("Invalid_OTP");
                toast.setText(R.string.invalid_otp);
                toast.show();
            }
        }else{
            writeLog("Enter_OTP");
            toast.setText(R.string.pleaseenterotp);
            toast.show();
        }
    }

   /* public void onReceivedMessage(String message){
        try {
            ed1.setText(message);
            Constant.showLog("message:"+message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }*/

    private void timerCount(){
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
              runOnUiThread(new Runnable() {
                  @Override
                  public void run() {
                      time++;
                      Constant.showLog("Time"+time);
                      if(time == 180){
                          timer.cancel();
                          btn_resendotp.setEnabled(true);
                      }
                  }
              });
            }
        },0,1000);

    }

    private void startTimerCount(int noOfMinutes){
        countDown = new CountDownTimer(noOfMinutes,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
              long millis = millisUntilFinished;
                String ms = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                tv_timecount.setText(ms);
                //Constant.showLog("count:"+ms);
            }

            @Override
            public void onFinish() {
                tv_text1.setText("Time's up!!");
                response_value = "0";
                btn_resendotp.setEnabled(true);
               // btn_resendotp.setBackgroundColor(getResources().getColor(R.color.maroon));
                btn_resendotp.setSupportBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(),R.color.maroon));
            }
        }.start();
    }

    /*private void autoOTP(){
        ReadSms.bindListener(new SmsListener() {
            @Override
            public void onReceivedMessage(String message) {
                Constant.showLog("message:"+message);
                ed1.setText(message.substring(0,1));
                Constant.showLog("message:"+message.substring(0,1));
                ed2.setText(message.substring(1,2));
                Constant.showLog("message:"+message.substring(1,2));
                ed3.setText(message.substring(2,3));
                Constant.showLog("message:"+message.substring(2,3));
                ed4.setText(message.substring(3,4));
                Constant.showLog("message:"+message.substring(3,4));
                ed5.setText(message.substring(4,5));
                Constant.showLog("message:"+message.substring(4,5));
                ed6.setText(message.substring(5,6));
                Constant.showLog("message:"+message.substring(5,6));
                timer.cancel();
                countDown.cancel();
                tv_text1.setText("OTP get successfully");

            }
        });
    }*/

    private void resendOTP(){
        try {
            constant = new Constant(CheckOTPActivity.this);
            constant.showPD();
            String _mobNo = URLEncoder.encode(mobNo,"UTF-8");
            String _imeiNo = URLEncoder.encode(imeiNo,"UTF-8");
            String _imeiNo1 = URLEncoder.encode(imeino1,"UTF-8");
            String _imeiNo2 = URLEncoder.encode(imeino2,"UTF-8");

            //String url = Constant.ipaddress + "/GetOTPCode?mobileno="+_mobNo+"&IMEINo="+_imeiNo+"&type=E";
            String url = Constant.ipaddress + "/GetOTPCodeV6?mobileno="+_mobNo+"&IMEINo1="
                    +_imeiNo1+"&IMEINo2="+_imeiNo2+"&type=E";
            Constant.showLog(url);
            writeLog("requestOTP_" + url);
            VolleyRequests requests = new VolleyRequests(CheckOTPActivity.this);
            requests.getOTPCode(url, new ServerCallback() {
                @Override
                public void onSuccess(String response) {
                    constant.showPD();
                    if (!response.equals("0") && !response.equals("-1") && !response.equals("-2")) {
                        //On Success
                        doThis(response,mobNo,imeiNo);
                        writeLog("requestOTP_Success_" + response);
                    } else if (!response.equals("0") && response.equals("-1") && !response.equals("-2")) {
                        //Already Registered
                        showDia(3);
                        writeLog("requestOTP_Fail_" + response);
                    } else if (!response.equals("0") && !response.equals("-1") && response.equals("-2")) {
                        //Registered Mobile Number Not Found
                        showDia(2);
                        writeLog("requestOTP_Fail_" + response);
                    }
                }
                @Override
                public void onFailure(String result) {
                    constant.showPD();
                    writeLog("requestOTP_VolleyError_"+result);
                }
            });


        }catch (Exception e){
            e.printStackTrace();
            writeLog("requestOTP_catch_"+e.getMessage());
        }
    }

    private void doThis(String response, String _mobNo, String _imeino){
        String arr[] = response.split("-");
        if (arr.length > 1) {
            writeLog("requestOTP_Success_" + response);
            response =  arr[1];
            Constant.showLog("dothis:response"+response);
            response_value = response;
        } else {
            showDia(-1);
            writeLog("requestOTP_" + response);
        }
    }

    private void getUserInfo(){
        constant = new Constant(CheckOTPActivity.this);
        //String url = Constant.ipaddress+"/GetUserDetail?mobileno="+otpClass.getMobileno()+"&IMEINo="+otpClass.getImeino()+"&type=E";
        String url = Constant.ipaddress+"/GetUserDetailV6?mobileno="+otpClass.getMobileno()+"&IMEINo1="+otpClass.getImeino1()
                +"&IMEINo2="+otpClass.getImeino2()+"&type=E";

        Constant.showLog(url);
        writeLog("getUserInfo_" + url);
        constant.showPD();
        VolleyRequests requests = new VolleyRequests(CheckOTPActivity.this);
        requests.getUserDetail(url, new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                constant.showPD();
                doFinish();
            }
            @Override
            public void onFailure(String result) {
                constant.showPD();
                writeLog("getUserInfo_onFailure_"+result);
                showDia(-1);
            }
        });
    }

    @Override
    protected void onDestroy() {
        try {
            if(receiver!=null) {
                //receiver.bindListener(null);
                unregisterReceiver(receiver);
                receiver = null;
            }
        }catch (Exception e){
            e.printStackTrace();
            writeLog("onDestroy_"+e.getMessage());
        }
        super.onDestroy();
    }

    private void doFinish(){
        if(countDown!=null) {
            countDown.cancel();
        }
        SharedPreferences.Editor editor = FirstActivity.pref.edit();
        editor.putBoolean(getString(R.string.pref_isRegistered),true);
        editor.putBoolean(getString(R.string.pref_imeino2),true);
        editor.apply();
        finish();
        Intent intent = new Intent(getApplicationContext(), CustomerDetailsActivity.class);
        intent.putExtra("otp",otpClass);
        startActivity(intent);
        overridePendingTransition(R.anim.enter,R.anim.exit);
    }

    private void init() {
        ed1 = (EditText) findViewById(R.id.ed1);
        ed2 = (EditText) findViewById(R.id.ed2);
        ed3 = (EditText) findViewById(R.id.ed3);
        ed4 = (EditText) findViewById(R.id.ed4);
        ed5 = (EditText) findViewById(R.id.ed5);
        ed6 = (EditText) findViewById(R.id.ed6);

        tv_otp = (TextView) findViewById(R.id.tv_otp);
        tv_text1 = (TextView) findViewById(R.id.tv_text1);
        tv_timecount = (TextView) findViewById(R.id.tv_timecount);
        FirstActivity.pref = getSharedPreferences(FirstActivity.PREF_NAME,MODE_PRIVATE);
        btn_verifyotp = (AppCompatButton) findViewById(R.id.btn_verifyotp);
        btn_resendotp = (AppCompatButton) findViewById(R.id.btn_resendotp);
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        constant = new Constant(CheckOTPActivity.this);
    }

    private void showDia(int a) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CheckOTPActivity.this);
        builder.setCancelable(false);
        if (a == -1) {
            builder.setTitle(R.string.somethingwentwrong);
            builder.setMessage(R.string.pleasecontactyouradministrator);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new Constant();
                    dialog.dismiss();
                }
            });
        }else if (a == 0) {
            builder.setMessage(R.string.doyouwanttoexitfromapp);
            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    try {
                        if (receiver != null) {
                            //receiver.bindListener(null);
                            unregisterReceiver(receiver);
                            receiver = null;
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    if(countDown!=null) {
                        countDown.cancel();
                    }
                    new Constant(CheckOTPActivity.this).doFinish();
                    toast.cancel();
                }
            });
            builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }else if (a == 1) {
            builder.setMessage(R.string.registrationdonesuccessfully);
            builder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    getUserInfo();
                }
            });
        }else if (a == 2) {
            builder.setTitle(R.string.numbernotmatch);
            builder.setMessage(R.string.pleaseenterregisteredmobilenumber);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }else if (a == 3) {
            builder.setTitle(R.string.alreadyregistered);
            builder.setMessage(R.string.pleasecontactyouradministrator);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        builder.create().show();
    }

    private void writeLog(String _data){
        new WriteLog().writeLog(getApplicationContext(),"RegistrationActivity_"+_data);
    }

    public static class MySMSReceiver extends BroadcastReceiver{
        private boolean b ;
        private String text;
        private SmsListener smsListener;

        @Override
        public void onReceive(Context context, Intent intent) {
            final Bundle bundle = intent.getExtras();
            if(bundle != null){
                final Object[] pdusobj = (Object[]) bundle.get("pdus");
                assert pdusobj != null;
                for (int i = 0; i <= pdusobj.length-1; i++) {
                    if (smsListener != null) {
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
                    } else {
                        Constant.showLog("NULL");
                    }
                    //writeLog("ReadSMS_onReceive_Called");
                }
            }
        }

        public void bindListener(SmsListener listener) {
            smsListener = listener;
            Constant.showLog("ReadSMS_bindListener_Called");
        }
    }


}
