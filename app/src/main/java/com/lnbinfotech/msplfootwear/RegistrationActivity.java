package com.lnbinfotech.msplfootwear;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.lnbinfotech.msplfootwear.connectivity.ConnectivityTest;
import com.lnbinfotech.msplfootwear.constant.AppSingleton;
import com.lnbinfotech.msplfootwear.constant.Constant;
import com.lnbinfotech.msplfootwear.log.WriteLog;
import com.lnbinfotech.msplfootwear.model.CheckOtpClass;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText ed_mobNo;
    private Button btn_otp;
    private Toast toast;
    private Constant constant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        init();

        btn_otp.setOnClickListener(this);

        ed_mobNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(ed_mobNo.getText().toString().length()==10){
                    ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(ed_mobNo.getWindowToken(),0);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_otp:
                if(ed_mobNo.getText().toString().length()==10) {
                    showDia(1);
                }else{
                    toast.setText(getString(R.string.invalid_mob_no));
                    toast.show();
                }
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

    private void requestOTP(){
        constant.showPD();
        String mobNo = ed_mobNo.getText().toString();
        String url = Constant.ipaddress+"/GetOTPCode?mobileno="+mobNo;
        Constant.showLog(url);
        writeLog("requestOTP_"+url);
        StringRequest request = new StringRequest(url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Constant.showLog(response);
                    response = response.replace("\\","");
                    response = response.replace("\"","");
                    Constant.showLog(response);
                    constant.showPD();
                    if(!response.equals("0")) {
                        String arr[] = response.split("-");
                        if(arr.length>1) {
                            writeLog("requestOTP_Success_"+response);
                            CheckOtpClass otp = new CheckOtpClass();
                            otp.setCustId(arr[0]);
                            otp.setOtp(arr[1]);
                            otp.setMobileno(ed_mobNo.getText().toString());
                            otp.setImeino(constant.getIMEINo());
                            finish();
                            Intent intent = new Intent(getApplicationContext(), CheckOTPActivity.class);
                            intent.putExtra("otp",otp);
                            startActivity(intent);
                            overridePendingTransition(R.anim.enter, R.anim.exit);
                        }else{
                            showDia(3);
                            writeLog("requestOTP_"+response);
                        }
                    }else{
                        showDia(2);
                        writeLog("requestOTP_Fail_"+response);
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Constant.showLog(error.getMessage());
                    constant.showPD();
                    writeLog("requestOTP_VolleyError_"+error.getMessage());
                }
            }
        );
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(request,"OTP");
    }

    private void LoadImage(){
        String url = "https://androidtutorialpoint.com/api/lg_nexus_5x";
        ImageLoader imageLoader = AppSingleton.getInstance(getApplicationContext()).getImageLoader();
        imageLoader.get(url, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                if (response.getBitmap() != null) {
                    LayoutInflater li = LayoutInflater.from(RegistrationActivity.this);
                    View showDialogView = li.inflate(R.layout.test, null);
                    ImageView ig = (ImageView)showDialogView.findViewById(R.id.image_view_dialog);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RegistrationActivity.this);
                    alertDialogBuilder.setView(showDialogView);
                    alertDialogBuilder
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            })
                            .setCancelable(false)
                            .create();
                    ig.setImageBitmap(response.getBitmap());
                    alertDialogBuilder.show();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Constant.showLog(error.getMessage());
            }
        });
    }

    private void init() {
        ed_mobNo = (EditText) findViewById(R.id.ed_mobno);
        btn_otp = (Button) findViewById(R.id.btn_otp);
        toast = Toast.makeText(getApplicationContext(),"",Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        constant = new Constant(RegistrationActivity.this);
    }

    private void showDia(int a) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
        builder.setCancelable(false);
        if (a == 0) {
            builder.setMessage(R.string.doyouwanttoexitfromapp);
            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new Constant(RegistrationActivity.this).doFinish();
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        if (a == 1) {
            builder.setMessage(R.string.areyousuretoreceiveotponthisnumber);
            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(ConnectivityTest.getNetStat(getApplicationContext())) {
                        requestOTP();
                    }else{
                        toast.setText(getString(R.string.you_are_offline));
                        toast.show();
                    }
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton(R.string.edit, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
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
            builder.setTitle(R.string.somethingwentwrong);
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

}
