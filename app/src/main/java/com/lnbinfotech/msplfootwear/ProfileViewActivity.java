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
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.lnbinfotech.msplfootwear.adapters.CheckAvailStockAdapter;
import com.lnbinfotech.msplfootwear.constant.Constant;
import com.lnbinfotech.msplfootwear.db.DBHandler;
import com.lnbinfotech.msplfootwear.interfaces.ServerCallback;
import com.lnbinfotech.msplfootwear.interfaces.ServerCallbackList;
import com.lnbinfotech.msplfootwear.log.WriteLog;
import com.lnbinfotech.msplfootwear.model.CheckAvailStockClass;
import com.lnbinfotech.msplfootwear.model.UserClass;
import com.lnbinfotech.msplfootwear.model.UserGetterSetterClass;
import com.lnbinfotech.msplfootwear.volleyrequests.VolleyRequests;

import java.util.List;

public class ProfileViewActivity extends AppCompatActivity implements View.OnClickListener {

    private DBHandler db;
    private EditText ed_mobno, ed_emailid, ed_cc, ed_cusname, ed_cgst, ed_panno;
    private AppCompatButton bt_save;
    private ImageView imgv_edit;
    private String mob_no = "", email_id = "", _cc = "";
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Constant.liveTestFlag == 1) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        setContentView(R.layout.activity_profile_view);

        init();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        getMobNo();

        imgv_edit.setOnClickListener(this);
        bt_save.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgv_edit:
                Intent i = new Intent(ProfileViewActivity.this, ProfileDataUpdateActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case R.id.bt_save:
                getvalue();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //showDia(0);
        db.close();
        new Constant(ProfileViewActivity.this).doFinish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //showDia(0);
                new Constant(ProfileViewActivity.this).doFinish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        // toast = Toast.makeText()
        FirstActivity.pref = getSharedPreferences(FirstActivity.PREF_NAME, MODE_PRIVATE);
        db = new DBHandler(this);
        ed_cusname = findViewById(R.id.ed_cusname);
        ed_cgst = findViewById(R.id.ed_cgst);
        ed_panno = findViewById(R.id.ed_pan_no);
        ed_mobno = findViewById(R.id.ed_mobno);
        ed_emailid = findViewById(R.id.ed_emailid);
        ed_cc = findViewById(R.id.ed_cc);
        bt_save = findViewById(R.id.bt_save);
        imgv_edit = findViewById(R.id.imgv_edit);
        toast = Toast.makeText(getApplicationContext(),"",Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
    }

    private void getMobNo() {
        Cursor cursor = db.getProfileData(FirstActivity.pref.getInt(getString(R.string.pref_retailCustId), 0));
        if (cursor.moveToFirst()) {
            do {
                mob_no = cursor.getString(cursor.getColumnIndex(DBHandler.UM_MobileNo));
            } while (cursor.moveToNext());
        }
        cursor.close();
        getUserInfo();
    }

    private void getUserInfo() {
        String imeino, imeino1, imeino2;
        Constant constant = new Constant(ProfileViewActivity.this);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            imeino = new Constant(getApplicationContext()).getIMEINo1();
            String[] arr = imeino.split("\\^");
            if (arr.length > 1) {
                imeino1 = arr[0];
                imeino2 = arr[1];
            } else {
                imeino1 = imeino;
                imeino2 = imeino;
            }
        } else {
            imeino = new Constant(getApplicationContext()).getIMEINo();
            imeino1 = imeino;
            imeino2 = imeino;
        }

        String url = Constant.ipaddress + "/GetUserDetailV6?mobileno=" + mob_no + "&IMEINo1=" + imeino1
                + "&IMEINo2=" + imeino2 + "&type=C";

        Constant.showLog(url);
        writeLog("getUserInfo_" + url);
        constant.showPD();
        VolleyRequests requests = new VolleyRequests(ProfileViewActivity.this);
        requests.getUserDetailProfile(url, new ServerCallbackList() {
            @Override
            public void onSuccess(Object result) {
                constant.showPD();
                List<UserClass> userList = (List<UserClass>) result;
                if(userList.size()!=0) {
                    for(UserClass user : userList) {
                        ed_cusname.setText(user.getPartyName());
                        ed_emailid.setText(user.getEmail());
                        ed_mobno.setText(user.getMobile());
                        ed_cgst.setText(user.getGSTNo());
                        ed_panno.setText(user.getPANno());
                    }
                } else {
                    toast.setText("Something Went Wrong...");
                    toast.show();
                }
            }
            @Override
            public void onFailure(Object result) {
                constant.showPD();
                new Constant();
                toast.setText("Please Try Again...");
                toast.show();

            }
        });
    }

    private void getProfileData() {
        Cursor cursor = new DBHandler(getApplicationContext()).getProfileData(FirstActivity.pref.getInt(getString(R.string.pref_retailCustId), 0));
        if (cursor.moveToFirst()) {
            do {
                ed_cusname.setText(cursor.getString(cursor.getColumnIndex(DBHandler.UM_PartyName)));
                ed_emailid.setText(cursor.getString(cursor.getColumnIndex(DBHandler.UM_Email)));
                ed_mobno.setText(cursor.getString(cursor.getColumnIndex(DBHandler.UM_MobileNo)));
                ed_cgst.setText(cursor.getString(cursor.getColumnIndex(DBHandler.UM_GSTNo)));
                ed_panno.setText(cursor.getString(cursor.getColumnIndex(DBHandler.UM_PANNo)));
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private void update_values() {
        mob_no = getIntent().getStringExtra("mobileno");
        ed_mobno.setText(mob_no);
        email_id = getIntent().getStringExtra("emailid");
        ed_emailid.setText(email_id);
        _cc = getIntent().getStringExtra("cc");
        ed_cc.setText(_cc);
    }

    private void getvalue() {
        UserGetterSetterClass user = new UserGetterSetterClass();
        user.setMob_no(ed_emailid.getText().toString());
        user.setEmail_id(ed_mobno.getText().toString());
        user.setCc(ed_cc.getText().toString());
        // db.addProfileDetail(user);
    }

    private void showDia(int a) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileViewActivity.this);
        builder.setCancelable(false);
        builder.setMessage(R.string.doyouwanttoexitfromapp);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                new Constant(ProfileViewActivity.this).doFinish();
                toast.cancel();
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
        new WriteLog().writeLog(getApplicationContext(), "ProfileViewActivity_" + _data);
    }

}
