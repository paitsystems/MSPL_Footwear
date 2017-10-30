package com.lnbinfotech.msplfootwearex;


import android.database.Cursor;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.lnbinfotech.msplfootwearex.adapters.SetAutoItemAdapter;
import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.db.DBHandler;
import com.lnbinfotech.msplfootwearex.log.WriteLog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class SelectAutoItemActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText ed_cus_name, ed_branch, ed_bank;
    private List<String> cus_list, bank_list, branch_list;
    private LinearLayout cus_lay, bank_lay, branch_lay;
    private ListView lv_cus;//, lv_bank, lv_branch;
    private String get_type = "", select_item;
    private SetAutoItemAdapter adapter;
    private DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_auto_item);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.payment);
        }
        init();
    }

    private void init() {
        db = new DBHandler(SelectAutoItemActivity.this);
        ed_cus_name = (EditText) findViewById(R.id.ed_cus_name);
        ed_bank = (EditText) findViewById(R.id.ed_bank);
        ed_branch = (EditText) findViewById(R.id.ed_branch);

        cus_lay = (LinearLayout) findViewById(R.id.cus_lay);
        bank_lay = (LinearLayout) findViewById(R.id.bank_lay);
        branch_lay = (LinearLayout) findViewById(R.id.branch_lay);


        lv_cus = (ListView) findViewById(R.id.lv_cus);
        lv_cus.setTextFilterEnabled(true);

        // lv_bank = (ListView) findViewById(R.id.lv_bank);
        // lv_branch = (ListView) findViewById(R.id.lv_branch);
        cus_list = new ArrayList<>();
        bank_list = new ArrayList<>();
        branch_list = new ArrayList<>();

        /*cus_list.add(0, "sneha");
        cus_list.add(1, "pooja");
        cus_list.add(2, "kiran");
        cus_list.add(3, "aniket");
        cus_list.add(4, "poonam");
        cus_list.add(5, "neha");*/

        /*bank_list.add(0, "BOB");
        bank_list.add(1, "BOM");
        bank_list.add(2, "ICI");
        bank_list.add(3, "ICC");
        bank_list.add(4, "SBI");*/

        /*branch_list.add(0, "Pune");
        branch_list.add(1, "Mumbai");
        branch_list.add(2, "Naagpur");
        branch_list.add(3, "Nashik");
        branch_list.add(4, "Satara");*/


        get_type = getIntent().getStringExtra("Auto_type");
        get_auto_type();

        //adapter = new SetAutoItemAdapter(this, cus_list);


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

        ed_bank.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence.toString());
                lv_cus.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ed_branch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence.toString());
                lv_cus.setAdapter(adapter);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        lv_cus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                if (get_type.equals("cus")) {
                    // String select_item = cus_list.get(position);
                    String select_item = (String) parent.getItemAtPosition(position);
                    VisitPaymentFormActivity.visit.setCustomer_name(select_item);
                    ed_cus_name.setText(select_item);
                    Constant.showLog("selcteditem: " + select_item);
                    writeLog("setOnItemClickListener():list item selected:" + select_item);
                    //finish();
                    new Constant(SelectAutoItemActivity.this).doFinish();

                } else if (get_type.equals("bank")) {
                    //String select_item = bank_list.get(position);
                    String select_item = (String) parent.getItemAtPosition(position);
                    ChequeDetailsActivity.selectAuto.setChq_auto_bank(select_item);
                    ed_bank.setText(select_item);
                    Constant.showLog("selcted_item: " + select_item);
                    writeLog("setOnItemClickListener():list item selected:" + select_item);
                    //finish();
                    new Constant(SelectAutoItemActivity.this).doFinish();
                } else if (get_type.equals("branch")) {
                    //String select_item = branch_list.get(position);
                    String select_item = (String) parent.getItemAtPosition(position);
                    ChequeDetailsActivity.selectAuto.setChq_auto_branch(select_item);
                    ed_branch.setText(select_item);
                    Constant.showLog("selcted_item: " + select_item);
                    writeLog("setOnItemClickListener():list item selected:" + select_item);
                    //finish();
                    new Constant(SelectAutoItemActivity.this).doFinish();
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
    }

     private void getCustomerList(){
     // Cursor cursor = db.getCustomerName();

    }

    public void setCusList() {
        Set keys = AreawiseCustomerSelectionActivity.hashmap.keySet();
        Iterator itr = keys.iterator();
        String key,value="";
        while (itr.hasNext()){
            key = (String) itr.next();
            value = AreawiseCustomerSelectionActivity.hashmap.get(key);
            Constant.showLog(" value:"+ value);
            Cursor cur  = db.getCustomerName(value);
            if(cur.moveToFirst()){
                do{
                    cus_list.add(cur.getString(cur.getColumnIndex(DBHandler.CM_Name)));
                    Constant.showLog("cuslist:"+cus_list.size());
                }while (cur.moveToNext());
            }
            cur.close();
        }

        adapter = new SetAutoItemAdapter(this, cus_list);
        //lv_cus.setAdapter(new SetAutoItemAdapter(this, cus_list));
        lv_cus.setAdapter(adapter);
    }

    public void setBankList() {
        lv_cus.setAdapter(null);
        Cursor cursor = db.getBankName();
        if(cursor.moveToFirst()){
            do{
               bank_list.add(cursor.getString(cursor.getColumnIndex(DBHandler.Bank_Name)));
            }while (cursor.moveToNext());
        }
        cursor.close();
        adapter = new SetAutoItemAdapter(this, bank_list);
        // lv_bank.setAdapter(new SetAutoItemAdapter(this, bank_list));
        lv_cus.setAdapter(adapter);
    }

    public void setBranchList() {
        lv_cus.setAdapter(null);
        Cursor cursor = db.getBranchName();
        if(cursor.moveToFirst()){
            do{
                branch_list.add(cursor.getString(cursor.getColumnIndex(DBHandler.Branch_CBranch)));
            }while (cursor.moveToNext());
        }
        cursor.close();
        adapter = new SetAutoItemAdapter(this, branch_list);
        //lv_branch.setAdapter(new SetAutoItemAdapter(this, branch_list));
        lv_cus.setAdapter(adapter);
    }

    private void get_auto_type() {
        if (get_type.equals("cus")) {
            cus_lay.setVisibility(View.VISIBLE);
            bank_lay.setVisibility(View.GONE);
            branch_lay.setVisibility(View.GONE);
            setCusList();
        }
        if (get_type.equals("bank")) {
            cus_lay.setVisibility(View.GONE);
            bank_lay.setVisibility(View.VISIBLE);
            branch_lay.setVisibility(View.GONE);
            setBankList();
        }
        if (get_type.equals("branch")) {
            cus_lay.setVisibility(View.GONE);
            bank_lay.setVisibility(View.GONE);
            branch_lay.setVisibility(View.VISIBLE);
            setBranchList();
        }
    }

    private void writeLog(String _data) {
        new WriteLog().writeLog(getApplicationContext(), "SelectAutoItemActivity_" + _data);
    }
}
