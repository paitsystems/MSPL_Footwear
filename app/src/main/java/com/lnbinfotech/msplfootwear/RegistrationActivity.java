package com.lnbinfotech.msplfootwear;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.lnbinfotech.msplfootwear.constant.AppSingleton;
import com.lnbinfotech.msplfootwear.constant.Constant;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText ed_mobNo;
    private Button btn_otp;
    private Toast toast;

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
                    toast.setText("Invalid Mobile Number");
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
        String mobNo = ed_mobNo.getText().toString();
        String url = Constant.ipaddress+"/GetOTPCode?mobileno="+mobNo;

        StringRequest request = new StringRequest(url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }
        );

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(request,"OTP");

        //startActivity(new Intent(getApplicationContext(),CheckOTP.class));
        //overridePendingTransition(R.anim.enter,R.anim.exit);
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
    }

    private void showDia(int a) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
        builder.setCancelable(false);
        if (a == 0) {
            builder.setMessage("Do You Want To Exit App?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new Constant(RegistrationActivity.this).doFinish();
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        if (a == 1) {
            builder.setMessage("Are You Sure To Receive OTP On This Number ?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    requestOTP();
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        builder.create().show();
    }

}
