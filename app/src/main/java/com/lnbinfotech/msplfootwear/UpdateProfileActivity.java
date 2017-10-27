package com.lnbinfotech.msplfootwear;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lnbinfotech.msplfootwear.constant.Constant;
import com.lnbinfotech.msplfootwear.db.DBHandler;
import com.lnbinfotech.msplfootwear.log.WriteLog;
import com.lnbinfotech.msplfootwear.model.UserClass;
import com.lnbinfotech.msplfootwear.model.UserGetterSetterClass;

public class UpdateProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private DBHandler db;
    private EditText ed_mobno, ed_emailid, ed_cc, ed_cusname, ed_cgst, ed_panno;
    private AppCompatButton bt_save;
    private ImageView imgv_edit;
    private String mob_no = "", email_id = "", _cc = "";
    private Toast toast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update__profile);
        init();
        getUpdatedValue();
        // update_values();
        // tv_cusname.setText();
        imgv_edit.setOnClickListener(this);
        bt_save.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgv_edit:
                Intent i = new Intent(UpdateProfileActivity.this, Update_ProfileData_Activity.class);
                startActivity(i);
                break;
            case R.id.bt_save:
               // getvalue();
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

    void init() {
       // toast = Toast.makeText()
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

    private void getUpdatedValue() {
        Cursor cursor = db.getUpdateData(FirstActivity.pref.getInt(getString(R.string.pref_retailCustId),0));
        // UserClass  userClass = new UserClass();
        if (cursor.moveToFirst()) {
            do {
                ed_cusname.setText(cursor.getString(cursor.getColumnIndex(DBHandler.UM_Name)));
                ed_emailid.setText(cursor.getString(cursor.getColumnIndex(DBHandler.UM_Email)));
                ed_mobno.setText(cursor.getString(cursor.getColumnIndex(DBHandler.UM_MobileNo)));
                ed_cgst.setText(cursor.getString(cursor.getColumnIndex(DBHandler.UM_GSTNo)));
                ed_panno.setText(cursor.getString(cursor.getColumnIndex(DBHandler.UM_PANNo)));
                // cursor.getString(cursor.getColumnIndex("cc")) ;
            } while (cursor.moveToNext());
        }
        cursor.close();
    }


    void update_values() {
        mob_no = getIntent().getStringExtra("mobileno");
        ed_mobno.setText(mob_no);

        email_id = getIntent().getStringExtra("emailid");
        ed_emailid.setText(email_id);

        _cc = getIntent().getStringExtra("cc");
        ed_cc.setText(_cc);
    }

    void getvalue() {
        UserGetterSetterClass user = new UserGetterSetterClass();
        user.setMob_no(ed_emailid.getText().toString());
        user.setEmail_id(ed_mobno.getText().toString());
        user.setCc(ed_cc.getText().toString());
        // db.addProfileDetail(user);

    }

    private void showDia(int a) {
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProfileActivity.this);
        builder.setCancelable(false);

            builder.setMessage(R.string.doyouwanttoexitfromapp);
            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new Constant(UpdateProfileActivity.this).doFinish();
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
        builder.create().show();
    }

    private void writeLog(String _data) {
        new WriteLog().writeLog(getApplicationContext(), "UpdateProfileActivity_" + _data);
    }

}
