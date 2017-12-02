package com.lnbinfotech.msplfootwear;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import android.view.Gravity;

import com.lnbinfotech.msplfootwear.adapters.CheckoutCustOrderAdapter;
import com.lnbinfotech.msplfootwear.connectivity.ConnectivityTest;
import com.lnbinfotech.msplfootwear.constant.Constant;
import com.lnbinfotech.msplfootwear.interfaces.ServerCallbackList;
import com.lnbinfotech.msplfootwear.log.WriteLog;
import com.lnbinfotech.msplfootwear.model.CheckoutCustOrderClass;
import com.lnbinfotech.msplfootwear.volleyrequests.VolleyRequests;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CheckoutCustOrderActivity extends AppCompatActivity implements View.OnClickListener {

    private Constant constant, constant1;
    private Toast toast;
    private ListView lv_check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_cust_order);

        init();

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
        new Constant(CheckoutCustOrderActivity.this).doFinish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //showDia(0);
                new Constant(CheckoutCustOrderActivity.this).doFinish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        constant = new Constant(CheckoutCustOrderActivity.this);
        constant1 = new Constant(getApplicationContext());
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        lv_check = (ListView) findViewById(R.id.lv_check);
    }

    private void showCheckoutOrderDetails() {
        if (ConnectivityTest.getNetStat(CheckoutCustOrderActivity.this)) {

            String data = "";
            String url = Constant.ipaddress + "/comparecheckoutCustOrder?data="+data;
            Constant.showLog(url);
            writeLog("showCheckoutOrderDetails" + url);
            constant.showPD();
            VolleyRequests requests = new VolleyRequests(CheckoutCustOrderActivity.this);
            requests.loadCheckoutOrder(url, new ServerCallbackList() {
                @Override
                public void onSuccess(Object result) {
                    constant.showPD();
                    List<CheckoutCustOrderClass> list = (List<CheckoutCustOrderClass>) result;
                    if (list.size() != 0 ) {
                        CheckoutCustOrderAdapter adapter = new CheckoutCustOrderAdapter(list, getApplicationContext());
                        lv_check.setAdapter(adapter);
                       // setTotal(list);
                        showPopup(1);
                    } else {
                       showPopup(3);
                    }
                   // setData();
                }

                @Override
                public void onFailure(Object result) {
                    constant.showPD();
                    showPopup(2);
                }
            });
        } else {
            toast.setText("You Are Offline");
            toast.show();
        }
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
                  showCheckoutOrderDetails();
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
        new WriteLog().writeLog(getApplicationContext(), "CheckoutCustOrderActivity_" + _data);
    }
}
