package com.lnbinfotech.msplfootwear;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lnbinfotech.msplfootwear.adapters.TrackOrderAdapter;
import com.lnbinfotech.msplfootwear.constant.Constant;
import com.lnbinfotech.msplfootwear.interfaces.ServerCallbackList;
import com.lnbinfotech.msplfootwear.log.WriteLog;
import com.lnbinfotech.msplfootwear.model.TrackOrderClass;
import com.lnbinfotech.msplfootwear.volleyrequests.VolleyRequests;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class DetailTrackOrderActivity extends AppCompatActivity implements View.OnClickListener {
    private SimpleDateFormat sdf;
    private Toast toast;
    private TextView tv_date, tv_orderno, tv_orderqty, tv_orderamt, tv_orstatus;
    private TrackOrderClass orderClass;
    private Constant constant;
    private ExpandableListView expandableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_track_order);
        init();
        getOrder();
    }

    private void init() {
        constant = new Constant(DetailTrackOrderActivity.this);
        sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_orderno = (TextView) findViewById(R.id.tv_orderno);
        tv_orderqty = (TextView) findViewById(R.id.tv_orderqty);
        tv_orderamt = (TextView) findViewById(R.id.tv_orderamt);
        tv_orstatus = (TextView) findViewById(R.id.tv_orstatus);

        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getOrder() {
        orderClass = (TrackOrderClass) getIntent().getSerializableExtra("trackorderclass");
        tv_date.setText(orderClass.getPODate());
        tv_orderno.setText(orderClass.getPono());
        tv_orderqty.setText(orderClass.getLooseQty());
        tv_orderamt.setText(orderClass.getNetAmt());
        tv_orstatus.setText(orderClass.getApprove());
    }

    private void loadOrderDetails() {
        String url = Constant.ipaddress + "/GetDetailTrackOrder?mastId=" + FirstActivity.pref.getInt(getString(R.string.pref_retailCustId), 0);
        Constant.showLog(url);
        writeLog("loadOrderDetails_" + url);
        constant.showPD();
        VolleyRequests requests = new VolleyRequests(DetailTrackOrderActivity.this);
        requests.loadDetailOrder(url, new ServerCallbackList() {

            @Override
            public void onSuccess(Object result) {
                constant.showPD();
                List<TrackOrderClass> list = (List<TrackOrderClass>) result;
                if (list.size() != 0) {

                    //expandableListView.setAdapter(adapter);
                } else {
                    showPopup(1);
                }

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
       if(id == 0) {
           builder.setMessage("Do you want to rxit?");
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
       }else if (id == 1) {
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
                   loadOrderDetails();
               }
           });
       }
        builder.create().show();
    }


    private void writeLog(String _data) {
        new WriteLog().writeLog(getApplicationContext(), "DetailTrackOrderActivity_" + _data);
    }
}
