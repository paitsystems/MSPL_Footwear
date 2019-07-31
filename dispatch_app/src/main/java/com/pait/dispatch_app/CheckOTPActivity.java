package com.pait.dispatch_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.pait.dispatch_app.broadcasts.MySMSBroadcastReceiver;
import com.pait.dispatch_app.constant.Constant;
import com.pait.dispatch_app.interfaces.ServerCallback;
import com.pait.dispatch_app.log.WriteLog;
import com.pait.dispatch_app.model.CheckOtpClass;
import com.pait.dispatch_app.utility.AppSignatureHelper;
import com.pait.dispatch_app.volleyrequests.VolleyRequests;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class CheckOTPActivity extends AppCompatActivity implements View.OnClickListener,
        MySMSBroadcastReceiver.OTPReceiveListener {

    private EditText ed1, ed2, ed3, ed4, ed5, ed6;
    private AppCompatButton btn_verifyotp, btn_resendotp;
    private Toast toast;
    private CheckOtpClass otpClass;
    private Constant constant;
    private final Timer timer = new Timer();
    private int time = 0;
    private TextView tv_timecount, tv_text1, tv_otp;
    private CountDownTimer countDown;
    private String mobNo, imeiNo, imeino1, imeino2;
    private String response_value;
    //private ReadSms receiver;
    //private MySMSReceiver receiver;
    private MySMSBroadcastReceiver receiver;
    private SmsRetrieverClient client;
    private EditText[] editTexts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Constant.liveTestFlag == 1) {
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

        AppSignatureHelper hash = new AppSignatureHelper(getApplicationContext());
        ArrayList<String> appCodes = hash.getAppSignatures();
        String yourhash = appCodes.get(0);
        Constant.showLog("yourhash-" + yourhash);
        writeLog("HashCode " + yourhash);

        client = SmsRetriever.getClient(this);
        receiver = new MySMSBroadcastReceiver();
        receiver.initOTPListener(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
        getApplicationContext().registerReceiver(receiver, filter);
        startSMSListener();

        resendOTP();

        if (countDown == null) {
            tv_text1.setText("Your OTP will get within 5 min..");
            int minutes = 5 * 60 * 1000;
            startTimerCount(minutes);
        }

        if (countDown == null) {
            tv_text1.setText("Your OTP will get within 5 min..");
            int minutes = 5 * 60 * 1000;
            startTimerCount(minutes);
        }

        btn_verifyotp.setOnClickListener(this);
        btn_resendotp.setOnClickListener(this);

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
                btn_resendotp.setSupportBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.lightgray));
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
    protected void onDestroy() {
        try {
            if (receiver != null) {
                //receiver.bindListener(null);
                unregisterReceiver(receiver);
                receiver = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            writeLog("onDestroy_" + e.getMessage());
        }
        super.onDestroy();
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

    private void verifyOTP() {
        if (ed1.getText().toString().length() == 1 && ed2.getText().toString().length() == 1 &&
                ed3.getText().toString().length() == 1 && ed4.getText().toString().length() == 1 &&
                ed5.getText().toString().length() == 1 && ed6.getText().toString().length() == 1) {
            String otp = ed1.getText().toString() + ed2.getText().toString() +
                    ed3.getText().toString() + ed4.getText().toString() +
                    ed5.getText().toString() + ed6.getText().toString();
            Constant.showLog(otp);
            Constant.showLog("response_value:" + response_value);
            writeLog("response_value:" + response_value);
            if (otp.equals(response_value)) {
                Constant.showLog("response_value:" + response_value);
                writeLog("OTP_Matched");
                showDia(1);
            } else {
                writeLog("Invalid_OTP");
                toast.setText(R.string.invalid_otp);
                toast.show();
            }
        } else {
            writeLog("Enter_OTP");
            toast.setText(R.string.pleaseenterotp);
            toast.show();
        }
    }

    private void timerCount() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        time++;
                        Constant.showLog("Time" + time);
                        if (time == 180) {
                            timer.cancel();
                            btn_resendotp.setEnabled(true);
                        }
                    }
                });
            }
        }, 0, 1000);

    }

    private void startTimerCount(int noOfMinutes) {
        countDown = new CountDownTimer(noOfMinutes, 1000) {
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
                btn_resendotp.setEnabled(true);
                response_value = "0";
                btn_resendotp.setSupportBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.maroon));
                //btn_resendotp.setBackgroundColor(getResources().getColor(R.color.maroon));
            }
        }.start();
    }

    private void resendOTP() {
        try {
            constant = new Constant(CheckOTPActivity.this);
            constant.showPD();
            String _mobNo = URLEncoder.encode(mobNo, "UTF-8");
            String _imeiNo = URLEncoder.encode(imeiNo, "UTF-8");
            String _imeiNo1 = URLEncoder.encode(imeino1, "UTF-8");
            String _imeiNo2 = URLEncoder.encode(imeino2, "UTF-8");

            //String url = Constant.ipaddress + "/GetOTPCode?mobileno="+_mobNo+"&IMEINo="+_imeiNo+"&type=E";
            String url = Constant.ipaddress + "/GetOTPCodeV6?mobileno=" + _mobNo + "&IMEINo1="
                    + _imeiNo1 + "&IMEINo2=" + _imeiNo2 + "&type=D";
            Constant.showLog(url);
            writeLog("requestOTP_" + url);
            VolleyRequests requests = new VolleyRequests(CheckOTPActivity.this);
            requests.getOTPCode(url, new ServerCallback() {
                @Override
                public void onSuccess(String response) {
                    constant.showPD();
                    if (!response.equals("0") && !response.equals("-1") && !response.equals("-2")) {
                        //On Success
                        doThis(response, mobNo, imeiNo);
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
                    showDia(4);
                    writeLog("requestOTP_VolleyError_" + result);
                }
            });
        } catch (Exception e) {
            constant.showPD();
            e.printStackTrace();
            writeLog("requestOTP_catch_" + e.getMessage());
        }
    }

    private void doThis(String response, String _mobNo, String _imeino) {
        String arr[] = response.split("-");
        if (arr.length > 1) {
            writeLog("requestOTP_Success_" + response);
            response = arr[1];
            Constant.showLog("dothis:response" + response);
            response_value = response;
        } else {
            showDia(-1);
            writeLog("requestOTP_" + response);
        }
    }

    private void getUserInfo() {
        constant = new Constant(CheckOTPActivity.this);
        //String url = Constant.ipaddress+"/GetUserDetail?mobileno="+otpClass.getMobileno()+"&IMEINo="+otpClass.getImeino()+"&type=E";
        String url = Constant.ipaddress + "/GetUserDetailV6?mobileno=" + otpClass.getMobileno() + "&IMEINo1=" + otpClass.getImeino1()
                + "&IMEINo2=" + otpClass.getImeino2() + "&type=E";

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
                writeLog("getUserInfo_onFailure_" + result);
                showDia(-1);
            }
        });
    }

    private void doFinish() {
        if (countDown != null) {
            countDown.cancel();
        }
        SharedPreferences.Editor editor = FirstActivity.pref.edit();
        editor.putBoolean(getString(R.string.pref_imeino2), true);
        editor.apply();
        finish();
        Intent intent = new Intent(getApplicationContext(), CustomerDetailsActivity.class);
        intent.putExtra("otp", otpClass);
        startActivity(intent);
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    private void init() {
        FirstActivity.pref = getSharedPreferences(FirstActivity.PREF_NAME, MODE_PRIVATE);
        ed1 = findViewById(R.id.ed1);
        ed2 = findViewById(R.id.ed2);
        ed3 = findViewById(R.id.ed3);
        ed4 = findViewById(R.id.ed4);
        ed5 = findViewById(R.id.ed5);
        ed6 = findViewById(R.id.ed6);
        tv_text1 = findViewById(R.id.tv_text1);
        tv_otp = findViewById(R.id.tv_otp);
        tv_timecount = findViewById(R.id.tv_timecount);

        btn_verifyotp = findViewById(R.id.btn_verifyotp);
        btn_resendotp = findViewById(R.id.btn_resendotp);
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        constant = new Constant(CheckOTPActivity.this);

        editTexts = new EditText[]{ed1, ed2, ed3, ed4, ed5, ed6};
        ed1.addTextChangedListener(new PinTextWatcher(0));
        ed2.addTextChangedListener(new PinTextWatcher(1));
        ed3.addTextChangedListener(new PinTextWatcher(2));
        ed4.addTextChangedListener(new PinTextWatcher(3));
        ed5.addTextChangedListener(new PinTextWatcher(4));
        ed6.addTextChangedListener(new PinTextWatcher(5));

        ed1.setOnKeyListener(new PinOnKeyListener(0));
        ed2.setOnKeyListener(new PinOnKeyListener(1));
        ed3.setOnKeyListener(new PinOnKeyListener(2));
        ed4.setOnKeyListener(new PinOnKeyListener(3));
        ed5.setOnKeyListener(new PinOnKeyListener(4));
        ed6.setOnKeyListener(new PinOnKeyListener(5));
    }


    public class PinTextWatcher implements TextWatcher {

        private int currentIndex;
        private boolean isFirst = false, isLast = false;
        private String newTypedString = "";

        PinTextWatcher(int currentIndex) {
            this.currentIndex = currentIndex;

            if (currentIndex == 0)
                this.isFirst = true;
            else if (currentIndex == editTexts.length - 1)
                this.isLast = true;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            newTypedString = s.subSequence(start, start + count).toString().trim();
        }

        @Override
        public void afterTextChanged(Editable s) {

            String text = newTypedString;

            /* Detect paste event and set first char */
            if (text.length() > 1)
                text = String.valueOf(text.charAt(0));

            editTexts[currentIndex].removeTextChangedListener(this);
            editTexts[currentIndex].setText(text);
            editTexts[currentIndex].setSelection(text.length());
            editTexts[currentIndex].addTextChangedListener(this);

            if (text.length() == 1)
                moveToNext();
            else if (text.length() == 0)
                moveToPrevious();
        }

        private void moveToNext() {
            if (!isLast)
                editTexts[currentIndex + 1].requestFocus();

            if (isAllEditTextsFilled() && isLast) { // isLast is optional
                editTexts[currentIndex].clearFocus();
                hideKeyboard();
                verifyOTP();
                btn_resendotp.setSupportBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.lightgray));

            }
        }

        private void moveToPrevious() {
            if (!isFirst)
                editTexts[currentIndex - 1].requestFocus();
        }

        private boolean isAllEditTextsFilled() {
            for (EditText editText : editTexts)
                if (editText.getText().toString().trim().length() == 0)
                    return false;
            return true;
        }

        private void hideKeyboard() {
            if (getCurrentFocus() != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        }

    }

    public class PinOnKeyListener implements View.OnKeyListener {

        private int currentIndex;

        PinOnKeyListener(int currentIndex) {
            this.currentIndex = currentIndex;
        }

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (editTexts[currentIndex].getText().toString().isEmpty() && currentIndex != 0)
                    editTexts[currentIndex - 1].requestFocus();
            }
            return false;
        }

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
                    dialog.dismiss();
                    finish();
                }
            });
        } else if (a == 0) {
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
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (countDown != null) {
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
        } else if (a == 1) {
            builder.setMessage(R.string.registrationdonesuccessfully);
            builder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    getUserInfo();
                }
            });
        } else if (a == 2) {
            builder.setTitle(R.string.numbernotmatch);
            builder.setMessage(R.string.pleaseenterregisteredmobilenumber);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
        } else if (a == 3) {
            builder.setTitle(R.string.alreadyregistered);
            builder.setMessage(R.string.pleasecontactyouradministrator);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
        } else if (a == 4) {
            builder.setTitle(R.string.somethingwentwrong);
            builder.setMessage("Try Again");
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    new Constant();
                    resendOTP();
                }
            });
        }
        builder.create().show();
    }

    private void writeLog(String _data) {
        new WriteLog().writeLog(getApplicationContext(), "CheckOTPActivity_" + _data);
    }

}
