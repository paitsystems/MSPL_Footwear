package com.lnbinfotech.msplfootwear;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.lnbinfotech.msplfootwear.constant.Constant;

public class ProfileDataUpdateActivity extends AppCompatActivity {
    EditText ed_mobno,ed_emailid,ed_cc,ed_cusname,ed_cgst,ed_panno;
    AppCompatButton bt_update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Constant.liveTestFlag==1) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        setContentView(R.layout.activity_profile_data_update);
        init();
    }

    void init(){

        ed_cusname = (EditText) findViewById(R.id.ed_cusname);
        ed_cgst = (EditText) findViewById(R.id.ed_cgst);
        ed_panno = (EditText) findViewById(R.id.ed_pan_no);
        ed_mobno = (EditText) findViewById(R.id.ed_mobno);
        ed_emailid = (EditText) findViewById(R.id.ed_emailid);
        ed_cc = (EditText) findViewById(R.id.ed_cc);
        bt_update = (AppCompatButton) findViewById(R.id.bt_update);

        ed_mobno.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                ed_mobno.setBackgroundResource(R.drawable.bordercolor_change);
                ed_cc.setBackgroundResource(R.drawable.bordercolor_edittext_default);
                ed_emailid.setBackgroundResource(R.drawable.bordercolor_edittext_default);
            }
        });

        /*ed_mobno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ed_mobno.setBackgroundResource(R.drawable.bordercolor_change);
                ed_cc.setBackgroundResource(R.drawable.bordercolor_edittext_default);
                ed_emailid.setBackgroundResource(R.drawable.bordercolor_edittext_default);
            }
        });*/
        ed_emailid.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                ed_mobno.setBackgroundResource(R.drawable.bordercolor_edittext_default);
                ed_emailid.setBackgroundResource(R.drawable.bordercolor_change);
                ed_cc.setBackgroundResource(R.drawable.bordercolor_edittext_default);
            }
        });
        /*ed_emailid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ed_mobno.setBackgroundResource(R.drawable.bordercolor_edittext_default);
                ed_emailid.setBackgroundResource(R.drawable.bordercolor_change);
                ed_cc.setBackgroundResource(R.drawable.bordercolor_edittext_default);
            }
        });*/
        ed_cc.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                ed_mobno.setBackgroundResource(R.drawable.bordercolor_edittext_default);
                ed_emailid.setBackgroundResource(R.drawable.bordercolor_edittext_default);
                ed_cc.setBackgroundResource(R.drawable.bordercolor_change);
            }
        });
        /*ed_cc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ed_mobno.setBackgroundResource(R.drawable.bordercolor_edittext_default);
                ed_emailid.setBackgroundResource(R.drawable.bordercolor_edittext_default);
                ed_cc.setBackgroundResource(R.drawable.bordercolor_change);
            }
        });*/

        bt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ed_mobno.getText().toString().equals("") & ed_emailid.getText().toString().equals("")){
                    Toast toast = Toast.makeText(getApplicationContext(),"Please,enter mobile no and  email id..",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }else if(ed_mobno.getText().toString().equals("")){
                    Toast toast =  Toast.makeText(getApplicationContext(),"Please,enter mobile no..",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();

                }else if(ed_emailid.getText().toString().equals("")){
                    Toast toast = Toast.makeText(getApplicationContext(),"Please,enter email id..",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }else {

                    String mobile_no = ed_mobno.getText().toString();
                    String email = ed_emailid.getText().toString();
                    String cc = ed_cc.getText().toString();
                    Intent intent = new Intent(ProfileDataUpdateActivity.this, ProfileViewActivity.class);
                    intent.putExtra("mobileno", mobile_no);
                    intent.putExtra("emailid", email);
                    intent.putExtra("cc", cc);
                    startActivity(intent);
                    Toast toast =  Toast.makeText(getApplicationContext(),"Data updated successfully..",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();

                }

            }
        });

    }

    public  void show_popup(int id) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Data updated successfully..");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                show_popup(3);

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });

        builder.create().show();
    }
}
