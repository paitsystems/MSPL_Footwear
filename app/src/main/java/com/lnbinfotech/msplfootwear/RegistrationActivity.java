package com.lnbinfotech.msplfootwear;

import android.content.DialogInterface;
import android.content.Intent;
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

import com.lnbinfotech.msplfootwear.connectivity.ConnectivityTest;
import com.lnbinfotech.msplfootwear.constant.Constant;
import com.lnbinfotech.msplfootwear.interfaces.ServerCallback;
import com.lnbinfotech.msplfootwear.log.WriteLog;
import com.lnbinfotech.msplfootwear.model.CheckOtpClass;
import com.lnbinfotech.msplfootwear.volleyrequests.VolleyRequests;

import java.net.URLEncoder;

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
        try {
            constant.showPD();
            final String mobNo = ed_mobNo.getText().toString();
            final String imeino = new Constant(getApplicationContext()).getIMEINo();
            String _mobNo = URLEncoder.encode(mobNo, "UTF-8");
            String _imeino = URLEncoder.encode(imeino, "UTF-8");
            String url = Constant.ipaddress + "/GetOTPCode?mobileno="+_mobNo+"&IMEINo="+_imeino+"&type=C";
            Constant.showLog(url);
            writeLog("requestOTP_" + url);

            VolleyRequests requests = new VolleyRequests(RegistrationActivity.this);
            requests.getOTPCode(url, new ServerCallback() {
                @Override
                public void onSuccess(String response) {
                    constant.showPD();
                    if (!response.equals("0") && !response.equals("-1") && !response.equals("-2")) {
                        //On Success
                        doThis(response,mobNo,imeino);
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
                    writeLog("requestOTP_VolleyError_");
                }
            });
        }catch (Exception e){
            showDia(-1);
            e.printStackTrace();
            writeLog("requestOTP_Exception_" + e.getMessage());
        }
    }

    private void doThis(String response, String mobNo, String imeino){
        String arr[] = response.split("-");
        if (arr.length > 1) {
            writeLog("requestOTP_Success_" + response);
            CheckOtpClass otp = new CheckOtpClass();
            otp.setCustId(arr[0]);
            otp.setOtp(arr[1]);
            otp.setMobileno(mobNo);
            otp.setImeino(imeino);
            finish();
            Intent intent = new Intent(getApplicationContext(), CheckOTPActivity.class);
            intent.putExtra("otp", otp);
            startActivity(intent);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        } else {
            showDia(-1);
            writeLog("requestOTP_" + response);
        }
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
                    new Constant(RegistrationActivity.this).doFinish();
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

/*
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

*/

}
