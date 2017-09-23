package com.lnbinfotech.msplfootwear;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lnbinfotech.msplfootwear.db.DBHandler;
import com.lnbinfotech.msplfootwear.model.UserGetterSetterClass;

public class UpdateProfileActivity extends AppCompatActivity {
    DBHandler db;
    EditText ed_mobno,ed_emailid,ed_cc,ed_cusname,ed_cgst,ed_panno;
    AppCompatButton bt_save;
    ImageView imgv_edit;
    String mob_no ="",email_id = "",_cc="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update__profile);
        init();
        update_values();

        // tv_cusname.setText();

        imgv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(UpdateProfileActivity.this,Update_ProfileData_Activity.class);
                startActivity(i);
            }
        });

        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getvalue();

            }
        });


    }
    void init(){
        db = new DBHandler(this);
        ed_cusname = (EditText) findViewById(R.id.ed_cusname);
        ed_cgst = (EditText) findViewById(R.id.ed_cgst);
        ed_panno = (EditText) findViewById(R.id.ed_pan_no);
        ed_mobno = (EditText) findViewById(R.id.ed_mobno);
        ed_emailid = (EditText) findViewById(R.id.ed_emailid);
        ed_cc = (EditText) findViewById(R.id.ed_cc);
        bt_save = (AppCompatButton) findViewById(R.id.bt_save);
        imgv_edit = (ImageView) findViewById(R.id.imgv_edit);
    }

    void update_values(){
        mob_no = getIntent().getStringExtra("mobileno");
        ed_mobno.setText(mob_no);

        email_id = getIntent().getStringExtra("emailid");
        ed_emailid.setText(email_id);

        _cc = getIntent().getStringExtra("cc");
        ed_cc.setText(_cc);
    }
    void getvalue(){
        UserGetterSetterClass user = new UserGetterSetterClass();
        user.setMob_no( ed_emailid.getText().toString());
        user.setEmail_id( ed_mobno.getText().toString());
        user.setCc(ed_cc.getText().toString());
        // db.addProfileDetail(user);

    }


}
