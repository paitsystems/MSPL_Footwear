package com.lnbinfotech.msplfootwearex;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.view.Gravity;


import com.lnbinfotech.msplfootwearex.adapters.DisplayCustListAdapter;
import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.db.DBHandler;
import com.lnbinfotech.msplfootwearex.log.WriteLog;

import java.util.ArrayList;
import java.util.List;

public class DisplayCustListActivity extends AppCompatActivity implements View.OnClickListener {

    private Constant constant, constant1;
    private Toast toast;
    private ListView lv_cus;
    private List<String> cus_list;
    private DBHandler db;
    private EditText ed_cus_name;
    private DisplayCustListAdapter adapter;
    public static int custId = 0;
    private String select_item = "", from = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Constant.liveTestFlag==1) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        setContentView(R.layout.activity_display_cust_list);

        init();
        setCusList();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Take Order");
        }

        try{
            from = getIntent().getExtras().getString("from");
        }catch (Exception e){
            e.printStackTrace();
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
                select_item = (String) parent.getItemAtPosition(position);
                Constant.showLog("selctedCustomerName: " + select_item);
                writeLog("setOnItemClickListener():list item selected:" + select_item);
                custId = db.getCustNameId(select_item);
                int selCustId = FirstActivity.pref.getInt(getString(R.string.pref_selcustid), 0);
                if(selCustId!=0){
                    if(selCustId!=custId){
                        int count = db.getCustOrderDetail();
                        if(count!=0) {
                            showDia(1);
                        }else{
                            saveNCountinue();
                        }
                    }else{
                        saveNCountinue();
                    }
                }else{
                    saveNCountinue();
                }
                Constant.showLog("cust_id: " + custId+"-"+select_item);
            }
        });
    }

    private void saveNCountinue(){
        SharedPreferences.Editor editor = FirstActivity.pref.edit();
        editor.putInt(getString(R.string.pref_selcustid),custId);
        editor.putString(getString(R.string.pref_selcustname),select_item);
        editor.apply();
        finish();
        if(from.equals("order")) {
            startActivity(new Intent(getApplicationContext(), CutsizeSetwiseOrderActivity.class));
        }else{
            startActivity(new Intent(getApplicationContext(), TrackOrderMasterActivity.class));
        }
        overridePendingTransition(R.anim.enter, R.anim.exit);
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
        new Constant(DisplayCustListActivity.this).doFinish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //showDia(0);
                new Constant(DisplayCustListActivity.this).doFinish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        db = new DBHandler(DisplayCustListActivity.this);
        lv_cus = (ListView) findViewById(R.id.lv_cus);
        lv_cus.setTextFilterEnabled(true);
        constant = new Constant(DisplayCustListActivity.this);
        constant1 = new Constant(getApplicationContext());
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        ed_cus_name = (EditText) findViewById(R.id.ed_cus_name);
        cus_list = new ArrayList<>();
    }

    public void setCusList() {
        int hocode = FirstActivity.pref.getInt(getString(R.string.pref_hocode),0);
        Cursor cur = db.getCustName(hocode);
        if (cur.moveToFirst()) {
            do {
                cus_list.add(cur.getString(cur.getColumnIndex(DBHandler.CM_PartyName)));
            } while (cur.moveToNext());
        }
        cur.close();

        adapter = new DisplayCustListAdapter(this, cus_list);
        lv_cus.setAdapter(adapter);
       Constant.showLog("cus_list.size():"+cus_list.size());
    }

    private void showDia(int a) {
        AlertDialog.Builder builder = new AlertDialog.Builder(DisplayCustListActivity.this);
        builder.setCancelable(false);
        if (a == 0) {
            builder.setMessage("Do You Want To Exit App?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new Constant(DisplayCustListActivity.this).doFinish();
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
            String custName = FirstActivity.pref.getString(getString(R.string.pref_selcustname),"");
            builder.setMessage("There Is Already Pending Order For Customer - "+custName);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setNeutralButton("Clear Last Order", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences.Editor editor = FirstActivity.pref.edit();
                    editor.putInt(getString(R.string.pref_selcustid),0);
                    editor.putString(getString(R.string.pref_selcustname),"");
                    editor.putString("totalNetAmnt","0");
                    DisplayCustOutstandingActivity.outClass = null;
                    editor.apply();
                    db.deleteTable(DBHandler.Table_CustomerOrder);
                    dialog.dismiss();
                    showDia(2);
                }
            });
        }else if (a == 2) {
            builder.setMessage("Previous Order Cleared Successfully");
            builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    saveNCountinue();
                }
            });
            builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
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
