package com.lnbinfotech.msplfootwear;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lnbinfotech.msplfootwear.adapters.TrackOrderDetailAdapter;
import com.lnbinfotech.msplfootwear.connectivity.ConnectivityTest;
import com.lnbinfotech.msplfootwear.constant.Constant;
import com.lnbinfotech.msplfootwear.db.DBHandler;
import com.lnbinfotech.msplfootwear.interfaces.ServerCallbackList;
import com.lnbinfotech.msplfootwear.log.WriteLog;
import com.lnbinfotech.msplfootwear.model.TrackOrderDetailClass;
import com.lnbinfotech.msplfootwear.model.TrackOrderMasterClass;
import com.lnbinfotech.msplfootwear.volleyrequests.VolleyRequests;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class TrackOrderDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private Toast toast;
    private TextView tv_date, tv_orderno, tv_orderqty, tv_orderamt, tv_orstatus;
    private TrackOrderMasterClass orderClass;
    private Constant constant;
    private ExpandableListView expandableListView;
    private HashMap<Integer, String> prodIDNameMap;
    private List<Integer> prodIdList;
    private DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Constant.liveTestFlag==1) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        setContentView(R.layout.activity_track_order_detail);
        init();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        getOrder();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onBackPressed() {
        //showDia(0);
        new Constant(TrackOrderDetailActivity.this).doFinish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //showDia(0);
                new Constant(TrackOrderDetailActivity.this).doFinish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        FirstActivity.pref = getSharedPreferences(FirstActivity.PREF_NAME,MODE_PRIVATE);
        constant = new Constant(TrackOrderDetailActivity.this);
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_orderno = (TextView) findViewById(R.id.tv_orderno);
        tv_orderqty = (TextView) findViewById(R.id.tv_orderqty);
        tv_orderamt = (TextView) findViewById(R.id.tv_orderamt);
        tv_orstatus = (TextView) findViewById(R.id.tv_orstatus);
        db = new DBHandler(getApplicationContext());
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
    }

    private void getOrder() {
        orderClass = (TrackOrderMasterClass) getIntent().getSerializableExtra("trackorderclass");
        tv_date.setText(orderClass.getPODate());
        tv_orderno.setText(orderClass.getPono());
        tv_orderqty.setText(orderClass.getLooseQty());
        tv_orderamt.setText(orderClass.getNetAmt());
        String stat = orderClass.getApprove();
        if (stat.equalsIgnoreCase("Y")) {
            tv_orstatus.setText("Approved");
            tv_orstatus.setTextColor(getResources().getColor(R.color.darkgreen));
        } else {
            tv_orstatus.setText("Not Approved");
            tv_orstatus.setTextColor(getResources().getColor(R.color.red));
        }
        if (ConnectivityTest.getNetStat(getApplicationContext())) {
            loadOrderDetails();
        } else {
            toast.setText("You Are Offline");
            toast.show();
        }
    }

    private void loadOrderDetails() {
        String url = Constant.ipaddress + "/GetTrackOrderDetail?mastId=" + orderClass.getAuto();
        Constant.showLog(url);
        writeLog("loadOrderDetails_" + url);
        constant.showPD();
        VolleyRequests requests = new VolleyRequests(TrackOrderDetailActivity.this);
        requests.loadDetailOrder(url, new ServerCallbackList() {
            @Override
            public void onSuccess(Object result) {
                constant.showPD();
                setAdapters(result);
            }
            @Override
            public void onFailure(Object result) {
                constant.showPD();
                showPopup(2);
            }
        });
    }

    private void setAdapters(Object result){
        prodIDNameMap = new HashMap<>();
        prodIdList = new ArrayList<>();
        HashMap<Integer, List<TrackOrderDetailClass>> map = (HashMap<Integer, List<TrackOrderDetailClass>>) result;
        if (map.size() != 0) {
            List<Integer> keys = new ArrayList<>(map.keySet());
            String str = "";
            for(Integer _int:keys){
                str = str+_int+",";
            }
            str = str.substring(0,str.length()-1);
            Cursor res = db.getProdName(str);
            if(res.moveToFirst()){
                do{
                    if(!prodIdList.contains(res.getInt(res.getColumnIndex(DBHandler.PM_ProductID)))) {
                        prodIdList.add(res.getInt(res.getColumnIndex(DBHandler.PM_ProductID)));
                    }
                    prodIDNameMap.put(res.getInt(res.getColumnIndex(DBHandler.PM_ProductID)),res.getString(res.getColumnIndex(DBHandler.PM_Finalprod)));
                }while (res.moveToNext());
            }
            res.close();
            TrackOrderDetailAdapter adapter = new TrackOrderDetailAdapter(prodIDNameMap,map,prodIdList,getApplicationContext());
            expandableListView.setAdapter(adapter);
        } else {
            showPopup(1);
        }
    }

    private void showPopup(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
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
                    loadOrderDetails();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    new Constant(TrackOrderDetailActivity.this).doFinish();
                }
            });
        }
        builder.create().show();
    }

    private void writeLog(String _data) {
        new WriteLog().writeLog(getApplicationContext(), "DetailTrackOrderActivity_" + _data);
    }
}
