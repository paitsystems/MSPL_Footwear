package com.lnbinfotech.msplfootwear;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lnbinfotech.msplfootwear.db.DBHandler;
import com.lnbinfotech.msplfootwear.model.UserGetterSetterClass;

public class UpdateProfileActivity extends AppCompatActivity {
    DBHandler db;
    TextView tv_cusname;
    EditText ed_mobno,ed_emailid,ed_cc;
    Button bt_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update__profile);
        init();

       // tv_cusname.setText();

        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              getvalue();
            }
        });


    }
    void init(){
        db = new DBHandler(this);
        tv_cusname = (TextView) findViewById(R.id.tv_cusname);
        ed_mobno = (EditText) findViewById(R.id.ed_mobno);
        ed_emailid = (EditText) findViewById(R.id.ed_emailid);
        ed_cc = (EditText) findViewById(R.id.ed_cc);
        bt_save = (Button) findViewById(R.id.bt_save);
    }
    void getvalue(){
        UserGetterSetterClass user = new UserGetterSetterClass();
        user.setMob_no( ed_emailid.getText().toString());
        user.setEmail_id( ed_mobno.getText().toString());
        user.setCc(ed_cc.getText().toString());
        db.addProfileDetail(user);


    }


}
