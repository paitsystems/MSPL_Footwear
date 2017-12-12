package com.lnbinfotech.msplfootwear;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Gravity;

import com.lnbinfotech.msplfootwear.constant.Constant;
import com.lnbinfotech.msplfootwear.interfaces.ServerCallbackList;
import com.lnbinfotech.msplfootwear.log.WriteLog;
import com.lnbinfotech.msplfootwear.model.CustOutstandingClass;
import com.lnbinfotech.msplfootwear.model.GSTMasterClass;
import com.lnbinfotech.msplfootwear.volleyrequests.VolleyRequests;

public class DisplayCustOutstandingActivity extends AppCompatActivity implements View.OnClickListener {

    private Constant constant, constant1;
    private Toast toast;
    public static CustOutstandingClass outClass;
    private TextView tv_cl,tv_days,tv_pdc,tv_co,tv_bfp,tv_oda,tv_corder,tv_uo,tv_noa,tv_ol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_cust_outstanding);

        init();
        loadOustandingdetail();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }

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
        //showDia(0);
        new Constant(DisplayCustOutstandingActivity.this).doFinish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //showDia(0);
                new Constant(DisplayCustOutstandingActivity.this).doFinish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        constant = new Constant(DisplayCustOutstandingActivity.this);
        constant1 = new Constant(getApplicationContext());
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        outClass = new CustOutstandingClass();

        tv_cl = (TextView) findViewById(R.id.tv_cl);
        tv_days = (TextView) findViewById(R.id.tv_days);
        tv_pdc = (TextView) findViewById(R.id.tv_pdc);
        tv_co = (TextView) findViewById(R.id.tv_co);
        tv_bfp = (TextView) findViewById(R.id.tv_bfp);
        tv_oda = (TextView) findViewById(R.id.tv_oda);
        tv_corder = (TextView) findViewById(R.id.tv_corder);
        tv_uo = (TextView) findViewById(R.id.tv_uo);
        tv_noa = (TextView) findViewById(R.id.tv_noa);
        tv_ol = (TextView) findViewById(R.id.tv_ol);
    }

    private void loadOustandingdetail(){
        int cust_id = FirstActivity.pref.getInt(getString(R.string.pref_retailCustId),0);
        String url = Constant.ipaddress + "/GetCustOutstanding?Id=" +cust_id ;
        Constant.showLog(url);
        writeLog("loadOustandingdetail_" + url);
        constant.showPD();
        final VolleyRequests requests = new VolleyRequests(DisplayCustOutstandingActivity.this);
        requests.loadCustOutstanding(url, new ServerCallbackList() {
            @Override
            public void onSuccess(Object result) {
                constant.showPD();
              //  outClass = (CustOutstandingClass) result;
                String currOrder =  FirstActivity.pref.getString("totalNetAmnt","");
                tv_cl.setText(outClass.getCreditlimit());
                tv_days.setText(outClass.getCreditdays());
                tv_co.setText(outClass.getCurrOutstnd());
                tv_corder.setText(currOrder);
                tv_pdc.setText(outClass.getPostDatedCheque());
                tv_bfp.setText(outClass.getBalForPayment());
                tv_oda.setText(outClass.getOverDueAmnt());
                tv_ol.setText(outClass.getOverLimit());
            }
            @Override
            public void onFailure(Object result) {
                constant.showPD();
                showPopup(2);
            }
        });
    }

    private void showPopup(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (id == 0) {
            builder.setMessage("Do you want to exit?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
        } else if (id == 1) {
            builder.setMessage("Data Loaded Successfully");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        } else if (id == 2) {
            builder.setMessage("Error While Loading Data?");
            builder.setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    loadOustandingdetail();
                }
            });
        } else if (id == 3) {
            builder.setMessage("No Record Available");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }

        builder.create().show();
    }

    private void writeLog(String _data) {
        new WriteLog().writeLog(getApplicationContext(), "DisplayCustOutstandingActivity_" + _data);
    }
}
