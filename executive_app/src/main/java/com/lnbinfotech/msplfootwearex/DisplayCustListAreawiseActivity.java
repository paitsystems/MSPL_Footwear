package com.lnbinfotech.msplfootwearex;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.view.Gravity;

import com.lnbinfotech.msplfootwearex.adapters.DisplayAreawiseCustListAdapter;
import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.db.DBHandler;
import com.lnbinfotech.msplfootwearex.log.WriteLog;

import java.util.ArrayList;
import java.util.List;

public class DisplayCustListAreawiseActivity extends AppCompatActivity implements View.OnClickListener {

    private Constant constant, constant1;
    private Toast toast;
    private ListView lv_cus;
    private List<String> cus_list;
    private DBHandler db;
    private EditText ed_cus_name;
    private DisplayAreawiseCustListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_cust_list_areawise);

        init();
        setCusList();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("DisplayCustomerList");
        }

        ed_cus_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence.toString());
                lv_cus.setAdapter(adapter);
                //String str = ed_cus_name.getText().toString().toLowerCase(Locale.getDefault());
                //adapter.getFilter().filter(str);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        lv_cus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                // String select_item = cus_list.get(position);
                String select_item = (String) parent.getItemAtPosition(position);
                Constant.showLog("selctedCustomerName: " + select_item);
                writeLog("setOnItemClickListener():list item selected:" + select_item);
                //finish();
                int cust_id = db.getCustNameId(select_item);
                Constant.showLog("cust_id: " + cust_id);
                startActivity(new Intent(getApplicationContext(), CutsizeSetwiseOrderActivity.class));
                overridePendingTransition(R.anim.enter, R.anim.exit);
                finish();
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
        //showDia(0);
        new Constant(DisplayCustListAreawiseActivity.this).doFinish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //showDia(0);
                new Constant(DisplayCustListAreawiseActivity.this).doFinish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        db = new DBHandler(DisplayCustListAreawiseActivity.this);
        lv_cus = (ListView) findViewById(R.id.lv_cus);
        lv_cus.setTextFilterEnabled(true);
        constant = new Constant(DisplayCustListAreawiseActivity.this);
        constant1 = new Constant(getApplicationContext());
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        ed_cus_name = (EditText) findViewById(R.id.ed_cus_name);
        cus_list = new ArrayList<>();
    }

    public void setCusList() {

        int id = Integer.parseInt(FirstActivity.pref.getString("areaid",""));
        Constant.showLog("arrrid:"+id);
        //TODO PASS ID
        //Cursor cur = db.getCustNameAreawise(id);
        Cursor cur = db.getCustNameAreawise();
        if (cur.moveToFirst()) {
            do {
                cus_list.add(cur.getString(cur.getColumnIndex(DBHandler.CM_Name)));
                Constant.showLog("cuslist:" + cus_list.size());
            } while (cur.moveToNext());
        }
        cur.close();

         adapter = new DisplayAreawiseCustListAdapter(this, cus_list);
        lv_cus.setAdapter(adapter);
    }

    private void showDia(int a) {
        AlertDialog.Builder builder = new AlertDialog.Builder(DisplayCustListAreawiseActivity.this);
        builder.setCancelable(false);
        if (a == 0) {
            builder.setMessage("Do You Want To Exit App?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new Constant(DisplayCustListAreawiseActivity.this).doFinish();
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
        new WriteLog().writeLog(getApplicationContext(), "DisplayCustListActivity_" + _data);
    }
}
