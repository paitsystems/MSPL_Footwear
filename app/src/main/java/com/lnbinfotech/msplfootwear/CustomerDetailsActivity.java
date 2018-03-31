package com.lnbinfotech.msplfootwear;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.lnbinfotech.msplfootwear.adapters.CustomerDetailListAdapter;
import com.lnbinfotech.msplfootwear.connectivity.ConnectivityTest;
import com.lnbinfotech.msplfootwear.constant.Constant;
import com.lnbinfotech.msplfootwear.db.DBHandler;
import com.lnbinfotech.msplfootwear.interfaces.ServerCallback;
import com.lnbinfotech.msplfootwear.log.WriteLog;
import com.lnbinfotech.msplfootwear.model.CustomerDetailClass;
import com.lnbinfotech.msplfootwear.model.UserClass;
import com.lnbinfotech.msplfootwear.volleyrequests.VolleyRequests;

import java.util.ArrayList;

public class CustomerDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView listView;
    private CustomerDetailListAdapter adapter;
    private DBHandler db;
    private Toast toast;
    private Constant constant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Constant.liveTestFlag==1) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        setContentView(R.layout.activity_customer_details);

        init();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        checkIsActive();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                UserClass custClass = (UserClass) listView.getAdapter().getItem(i);
                Intent intent = new Intent(getApplicationContext(),CustomerLoginActivity.class);
                intent.putExtra("cust",custClass);
                startActivity(intent);
                overridePendingTransition(R.anim.enter,R.anim.exit);
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case 0:
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

    private void init() {
        listView = (ListView) findViewById(R.id.listView);
        db = new DBHandler(getApplicationContext());
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        constant = new Constant(CustomerDetailsActivity.this);
    }

    private void showDia(int a) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CustomerDetailsActivity.this);
        builder.setCancelable(false);
        if (a == 0) {
            builder.setMessage("Do You Want To Exit App?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new Constant(CustomerDetailsActivity.this).doFinish();
                    toast.cancel();
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }else if (a == 1) {
            builder.setMessage("You Are Not An Active Customer?");
            builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    toast.cancel();
                    new Constant(CustomerDetailsActivity.this).doFinish();
                }
            });
        }else if (a == 2) {
            builder.setMessage("Please Try Again");
            builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    checkIsActive();
                }
            });
            builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    toast.cancel();
                    new Constant(CustomerDetailsActivity.this).doFinish();
                }
            });
        }else if (a == 3) {
            builder.setTitle("You Are Offline");
            builder.setMessage("Please Connect To Network?");
            builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    checkIsActive();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    new Constant(CustomerDetailsActivity.this).doFinish();

                }
            });
        }
        builder.create().show();
    }

    private void checkIsActive() {
        constant = new Constant(CustomerDetailsActivity.this);
        if (ConnectivityTest.getNetStat(getApplicationContext())) {
            int id = FirstActivity.pref.getInt(getString(R.string.pref_retailCustId), 0);
            String url = Constant.ipaddress + "/GetActiveStatus?id=" + id + "&type=C";
            Constant.showLog(url);
            writeLog("checkIsActive_" + url);
            constant.showPD();
            VolleyRequests requests = new VolleyRequests(CustomerDetailsActivity.this);
            requests.getActiveStatus(url, new ServerCallback() {
                @Override
                public void onSuccess(String result) {
                    constant.showPD();
                    if (result.equals("A")) {
                        loadData();
                    } else {
                        showDia(1);
                    }
                }

                @Override
                public void onFailure(String result) {
                    constant.showPD();
                    showDia(2);
                }
            });
        } else {
            showDia(3);
        }
    }

    private void loadData(){
        ArrayList<UserClass> list = db.getUserDetail();
        adapter = new CustomerDetailListAdapter(CustomerDetailsActivity.this,list);
        listView.setAdapter(adapter);
    }

    private void writeLog(String _data){
        new WriteLog().writeLog(getApplicationContext(),"CustomerDetailsActivity_"+_data);
    }

}
