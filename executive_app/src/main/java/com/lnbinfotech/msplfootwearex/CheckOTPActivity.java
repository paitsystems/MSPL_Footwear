package com.lnbinfotech.msplfootwearex;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.interfaces.ServerCallback;
import com.lnbinfotech.msplfootwearex.log.WriteLog;
import com.lnbinfotech.msplfootwearex.model.CheckOtpClass;
import com.lnbinfotech.msplfootwearex.volleyrequests.VolleyRequests;

public class CheckOTPActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText ed1,ed2,ed3,ed4,ed5,ed6;
    private Button btn_verifyotp;
    private Toast toast;
    private CheckOtpClass otpClass;
    private Constant constant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkotp);

        init();

        otpClass = (CheckOtpClass) getIntent().getSerializableExtra("otp");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        btn_verifyotp.setOnClickListener(this);

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

    private void verifyOTP(){
        if(ed1.getText().toString().length()==1 && ed2.getText().toString().length()==1 &&
                ed3.getText().toString().length()==1 && ed4.getText().toString().length()==1 &&
                ed5.getText().toString().length()==1 && ed6.getText().toString().length()==1) {
            String otp = ed1.getText().toString()+ed2.getText().toString()+
                    ed3.getText().toString()+ed4.getText().toString()+
                    ed5.getText().toString()+ed6.getText().toString();
            Constant.showLog(otp);
            if(otp.equals(otpClass.getOtp())) {
                showDia(1);
            }else{
                toast.setText(R.string.invalid_otp);
                toast.show();
            }
        }else{
            toast.setText(R.string.pleaseenterotp);
            toast.show();
        }
    }

    private void getCustInfo(){
        String url = Constant.ipaddress+"/GetCustomerDetail?mobileno="+otpClass.getMobileno()+"&IMEINo="+otpClass.getImeino();
        Constant.showLog(url);
        writeLog("requestOTP_" + url);
        constant.showPD();
        VolleyRequests requests = new VolleyRequests(CheckOTPActivity.this);
        requests.getCustomerDetail(url, new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                constant.showPD();
                doFinish();
            }
            @Override
            public void onFailure(String result) {
                constant.showPD();
                showDia(-1);
            }
        });
    }

    private void doFinish(){
        SharedPreferences.Editor editor = FirstActivity.pref.edit();
        editor.putBoolean(getString(R.string.pref_isRegistered),true);
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
        btn_verifyotp = (Button) findViewById(R.id.btn_verifyotp);
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
                    dialog.dismiss();
                }
            });
        }else if (a == 0) {
            builder.setMessage(R.string.doyouwanttoexitfromapp);
            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new Constant(CheckOTPActivity.this).doFinish();
                    toast.cancel();
                    dialog.dismiss();
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
                    getCustInfo();
                }
            });
        }
        builder.create().show();
    }

    private void writeLog(String _data){
        new WriteLog().writeLog(getApplicationContext(),"RegistrationActivity_"+_data);
    }
}
