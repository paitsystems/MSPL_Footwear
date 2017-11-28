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
import android.widget.ListView;
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
    private ListView lv_vOrder;
    private DBHandler db;
    private List<CustomerOrderClass> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_customer_order);

        init();
        setData();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        lv_vOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                list.get(i);
                Constant.showLog("selected pos:"+i);
                Constant.showLog("selected pos:"+  list.get(i));
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
                order.setMrp(cursor.getDouble(cursor.getColumnIndex(DBHandler.CO_MRP)));
                order.setAmount(cursor.getDouble(cursor.getColumnIndex(DBHandler.CO_Amount)));
                order.setGstper(cursor.getDouble(cursor.getColumnIndex(DBHandler.CO_GSTPer)));
                order.setRate(cursor.getDouble(cursor.getColumnIndex(DBHandler.CO_Rate)));
                list.add(order);
            }while (cursor.moveToNext());
        }
        cursor.close();
        ViewCustomerOrderAdapter adapter = new ViewCustomerOrderAdapter(list,getApplicationContext());
        lv_vOrder.setAdapter(adapter);
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

    private void init() {
        constant = new Constant(ViewCustomerOrderActiviy.this);
        constant1 = new Constant(getApplicationContext());
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        lv_vOrder = (ListView) findViewById(R.id.lv_vOrder);
        db = new DBHandler(ViewCustomerOrderActiviy.this);
        list = new ArrayList<>();
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
        }
        builder.create().show();
    }

    private void writeLog(String _data) {
        new WriteLog().writeLog(getApplicationContext(), "ViewCustomerOrderActiviy_" + _data);
    }
}
