package com.lnbinfotech.msplfootwear;

import android.content.ContentUris;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Gravity;

import com.lnbinfotech.msplfootwear.adapters.ViewCustomerOrderAdapter;
import com.lnbinfotech.msplfootwear.constant.Constant;
import com.lnbinfotech.msplfootwear.db.DBHandler;
import com.lnbinfotech.msplfootwear.log.WriteLog;
import com.lnbinfotech.msplfootwear.model.CustomerOrderClass;

import java.util.ArrayList;
import java.util.List;

public class ViewCustomerOrderActiviy extends AppCompatActivity implements View.OnClickListener {

    private Constant constant, constant1;
    private Toast toast;

    private TextView tv_totset, tv_totqty, tv_totamnt, tv_tot_gstamt, tv_tot_grossamt, tv_disc_per, tv_discamnt;
    private ListView lv_vOrder;
    private Button btn_proceed;

    private DBHandler db;
    private List<CustomerOrderClass> list;
    private String totalSet = "0", totalQty = "0",totalAmnt = "0", totalNetAmnt = "0",
            totalGrossAmnt = "0",totalDiscAmnt = "0", totalGSTAmnt = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_customer_order);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Your Order");
        }

        init();

        setData();

        lv_vOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                list.get(i);
                Constant.showLog("selected pos:"+i);
                AddToCartActivity.updateCustOrder = list.get(i);
                showDia(1);
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

    private void setData(){
        Cursor cursor = db.getViewOrderData();

        if(cursor.moveToFirst()){
            do{
                CustomerOrderClass order = new CustomerOrderClass();
                order.setAuto(cursor.getInt(cursor.getColumnIndex(DBHandler.CO_Auto)));
                order.setProductid(cursor.getInt(cursor.getColumnIndex(DBHandler.CO_Productid)));
                order.setSizeGroup(cursor.getString(cursor.getColumnIndex(DBHandler.CO_SizeGroup)));
                order.setColor(cursor.getString(cursor.getColumnIndex(DBHandler.CO_Color)));
                order.setHashCode(cursor.getString(cursor.getColumnIndex(DBHandler.CO_HashCode)));
                order.setLooseQty(cursor.getInt(cursor.getColumnIndex(DBHandler.CO_LooseQty)));
                order.setMrp(cursor.getString(cursor.getColumnIndex(DBHandler.CO_MRP)));
                order.setAmount(cursor.getString(cursor.getColumnIndex(DBHandler.CO_Amount)));
                order.setGstper(cursor.getString(cursor.getColumnIndex(DBHandler.CO_GSTPer)));
                order.setRate(cursor.getString(cursor.getColumnIndex(DBHandler.CO_Rate)));
                order.setQty(cursor.getInt(cursor.getColumnIndex(DBHandler.CO_Qty)));
                order.setActLooseQty(cursor.getInt(cursor.getColumnIndex(DBHandler.CO_ActLooseQty)));
                order.setLoosePackTyp(cursor.getString(cursor.getColumnIndex(DBHandler.CO_LoosePackTyp)));
                order.setPerPackQty(cursor.getInt(cursor.getColumnIndex(DBHandler.CO_PerPackQty)));
                order.setOrderType(cursor.getString(cursor.getColumnIndex(DBHandler.CO_OrderType)));
                list.add(order);
            }while (cursor.moveToNext());
        }
        cursor.close();
        ViewCustomerOrderAdapter adapter = new ViewCustomerOrderAdapter(list,getApplicationContext());
        lv_vOrder.setAdapter(adapter);
        totalCalculations();
    }

    @Override
    public void onBackPressed() {
        //showDia(0);
        new Constant(ViewCustomerOrderActiviy.this).doFinish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //showDia(0);
                new Constant(ViewCustomerOrderActiviy.this).doFinish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void totalCalculations(){
        totalSet = "0";
        totalQty = "0";
        totalAmnt = "0";
        totalNetAmnt = "0";
        totalGrossAmnt = "0";
        totalDiscAmnt = "0";
        totalGSTAmnt = "0";

        Cursor res = db.getCustOrderTotals();
        if(res.moveToFirst()){
            totalQty = res.getString(res.getColumnIndex(DBHandler.CO_LooseQty));
            totalSet = res.getString(res.getColumnIndex(DBHandler.CO_Auto));
            totalAmnt = res.getString(res.getColumnIndex(DBHandler.CO_Amount));
            totalNetAmnt = res.getString(res.getColumnIndex(DBHandler.CO_NetAmt));
            totalGrossAmnt = res.getString(res.getColumnIndex(DBHandler.CO_AmtAfterDisc));
            totalGSTAmnt = res.getString(res.getColumnIndex(DBHandler.CO_GSTAmt));
            totalDiscAmnt = res.getString(res.getColumnIndex(DBHandler.CO_DiscAmt));
        }
        res.close();

        tv_totqty.setText(totalQty);
        tv_totset.setText(totalSet);
        tv_totamnt.setText(totalNetAmnt);
        tv_tot_gstamt.setText(totalGSTAmnt);
        tv_tot_grossamt.setText(totalAmnt);
        tv_disc_per.setText(String.valueOf(OptionsActivity.custDisc));
        tv_discamnt.setText(totalDiscAmnt);
    }

    private void init() {
        constant = new Constant(ViewCustomerOrderActiviy.this);
        constant1 = new Constant(getApplicationContext());
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        lv_vOrder = (ListView) findViewById(R.id.lv_vOrder);
        db = new DBHandler(ViewCustomerOrderActiviy.this);
        list = new ArrayList<>();
        tv_totset = (TextView) findViewById(R.id.tv_tot_set);
        tv_totqty = (TextView) findViewById(R.id.tv_tot_qty);
        tv_totamnt = (TextView) findViewById(R.id.tv_amt);
        tv_tot_gstamt = (TextView) findViewById(R.id.tv_gst_amt);
        tv_tot_grossamt = (TextView) findViewById(R.id.tv_gross_amt);
        tv_disc_per = (TextView) findViewById(R.id.tv_disc_per);
        tv_discamnt = (TextView) findViewById(R.id.tv_disc_amt);
        btn_proceed = (Button) findViewById(R.id.btn_proceed);
    }

    private void showDia(int a) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewCustomerOrderActiviy.this);
        builder.setCancelable(false);
        if (a == 0) {
            builder.setMessage("Do You Want To Exit App?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new Constant(ViewCustomerOrderActiviy.this).doFinish();
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
            builder.setMessage("Do You Want To Update Order");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AddToCartActivity.activityToFrom = 2;
                    new Constant(ViewCustomerOrderActiviy.this).doFinish();
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        builder.create().show();
    }

    private void writeLog(String _data) {
        new WriteLog().writeLog(getApplicationContext(), "ViewCustomerOrderActiviy_" + _data);
    }
}
