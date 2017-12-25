package com.lnbinfotech.msplfootwearex;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lnbinfotech.msplfootwearex.adapters.WarehousesDetailAdapter;
import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.db.DBHandler;
import com.lnbinfotech.msplfootwearex.interfaces.ServerCallbackList;
import com.lnbinfotech.msplfootwearex.log.WriteLog;
import com.lnbinfotech.msplfootwearex.model.CustOutstandingClass;
import com.lnbinfotech.msplfootwearex.model.WarehouseDetailsClass;
import com.lnbinfotech.msplfootwearex.volleyrequests.VolleyRequests;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DisplayCustOutstandingActivity extends AppCompatActivity implements View.OnClickListener {

    private Constant constant, constant1;
    private Toast toast;
    public static CustOutstandingClass outClass;
    private TextView tv_cl, tv_days, tv_odays, tv_pdc, tv_co, tv_bfp, tv_oda,
            tv_corder, tv_uo, tv_noa, tv_ol, tot_qty, tot_amt, tv_custname;
    private ListView lv_warehouse_detail;
    private DBHandler db;
    float total_qty = 0, total_amt = 0;
    private DecimalFormat dc;
    private String str = "";

    private LinearLayout lay_warehouse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_cust_outstanding);

        init();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if(DisplayCustOutstandingActivity.outClass==null) {
            loadOustandingdetail();
        }else{
            setData();
        }

        str = getIntent().getExtras().getString("val");
        if(str.equals("1")) {
            lay_warehouse.setVisibility(View.GONE);

        }else {
            lay_warehouse.setVisibility(View.VISIBLE);
            showWarehouseData();
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

        lay_warehouse = (LinearLayout) findViewById(R.id.lay_warehouse);
        tv_cl = (TextView) findViewById(R.id.tv_cl);
        tv_days = (TextView) findViewById(R.id.tv_days);
        tv_odays = (TextView) findViewById(R.id.tv_odays);
        tv_pdc = (TextView) findViewById(R.id.tv_pdc);
        tv_co = (TextView) findViewById(R.id.tv_co);
        tv_bfp = (TextView) findViewById(R.id.tv_bfp);
        tv_oda = (TextView) findViewById(R.id.tv_oda);
        tv_corder = (TextView) findViewById(R.id.tv_corder);
        tv_uo = (TextView) findViewById(R.id.tv_uo);
        tv_noa = (TextView) findViewById(R.id.tv_noa);
        tv_ol = (TextView) findViewById(R.id.tv_ol);
        tv_custname = (TextView) findViewById(R.id.tv_custname1);
        tv_custname.setText(FirstActivity.pref.getString(getString(R.string.pref_selcustname),""));
        lv_warehouse_detail = (ListView) findViewById(R.id.lv_warhouse_detail);
        db = new DBHandler(this);
        tot_qty = (TextView) findViewById(R.id.tot_qty);
        tot_amt = (TextView) findViewById(R.id.tot_amt);
        dc = new DecimalFormat();
        dc.setMaximumFractionDigits(2);
    }

    private void showWarehouseData() {
        Cursor cursor = db.getWarehouseData();
        List<WarehouseDetailsClass> wlist = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                WarehouseDetailsClass wclass = new WarehouseDetailsClass();
                wclass.setBranchid(cursor.getInt(cursor.getColumnIndex(DBHandler.CO_BranchId)));
                wclass.setWarehouse(cursor.getString(cursor.getColumnIndex(DBHandler.Company_Initial)));
                wclass.setQty(cursor.getFloat(cursor.getColumnIndex(DBHandler.CO_LooseQty)));
                wclass.setAmt(cursor.getFloat(cursor.getColumnIndex(DBHandler.CO_NetAmt)));
                wlist.add(wclass);
            } while (cursor.moveToNext());
            db.close();
            cursor.close();
            WarehousesDetailAdapter adapter = new WarehousesDetailAdapter(getApplicationContext(), wlist);
            lv_warehouse_detail.setAdapter(adapter);
            setTotal(wlist);
        }
    }

    private void setTotal(List<WarehouseDetailsClass> wrList) {
        total_amt = 0;
        total_qty = 0;
        for (WarehouseDetailsClass wdClass : wrList) {
            total_amt = total_amt + wdClass.getAmt();
            Constant.showLog("total_amt"+wdClass.getAmt());
            total_qty = total_qty + wdClass.getQty();
            Constant.showLog("total_qty"+wdClass.getQty());
        }

        tot_qty.setText(dc.format(total_qty));
        tot_amt.setText(dc.format(total_amt));
    }


    private void loadOustandingdetail(){
        int cust_id = FirstActivity.pref.getInt(getString(R.string.pref_selcustid),0);
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
                setData();
            }
            @Override
            public void onFailure(Object result) {
                constant.showPD();
                writeLog("loadOustandingdetail_onFailure_"+result);
                showPopup(2);
            }
        });
    }

    private void setData(){
        String currOrder =  FirstActivity.pref.getString("totalNetAmnt","0");
        tv_cl.setText(outClass.getCreditlimit());
        tv_days.setText(outClass.getCreditdays());
        tv_odays.setText(outClass.getOverDueDays());
        tv_co.setText(outClass.getCurrOutstnd());
        tv_corder.setText(currOrder);
        tv_pdc.setText(outClass.getPostDatedCheque());
        tv_bfp.setText(outClass.getBalForPayment());
        tv_oda.setText(outClass.getOverDueAmnt());
        tv_ol.setText(outClass.getOverLimit());
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
