package com.lnbinfotech.msplfootwearex;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.lnbinfotech.msplfootwearex.adapters.SetAutoItemAdapter;
import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.db.DBHandler;
import com.lnbinfotech.msplfootwearex.log.WriteLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

        if(Constant.liveTestFlag==1) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        setContentView(R.layout.activity_select_auto_item);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.payment);
        }
        init();
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                new Constant(SelectAutoItemActivity.this).doFinish();
                break;
        }
        return super.onOptionsItemSelected(item);
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

        cus_list = new ArrayList<>();
        bank_list = new ArrayList<>();
        branch_list = new ArrayList<>();

        get_type = getIntent().getStringExtra("Auto_type");
        get_auto_type();

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
                    ChequeDetailsActivityChanged.selectAuto.setChq_auto_bank(select_item);
                    ed_bank.setText(select_item);
                    Constant.showLog("selcted_item: " + select_item);
                    writeLog("setOnItemClickListener():list item selected:" + select_item);
                    //finish();
                    new Constant(SelectAutoItemActivity.this).doFinish();
                } else if (get_type.equals("branch")) {
                    //String select_item = branch_list.get(position);
                    String select_item = (String) parent.getItemAtPosition(position);
                    ChequeDetailsActivityChanged.selectAuto.setChq_auto_branch(select_item);
                    ed_branch.setText(select_item);
                    Constant.showLog("selcted_item: " + select_item);
                    writeLog("setOnItemClickListener():list item selected:" + select_item);
                    //finish();
                    new Constant(SelectAutoItemActivity.this).doFinish();
                }else if (get_type.equals("bank1")) {
                    //String select_item = bank_list.get(position);
                    String select_item = (String) parent.getItemAtPosition(position);
                    ChequeDetailsActivityChanged.selectAuto.setChq_auto_bank(select_item);
                    ed_bank.setText(select_item);
                    Constant.showLog("selcted_item: " + select_item);
                    writeLog("setOnItemClickListener():list item selected:" + select_item);
                    //finish();
                    new Constant(SelectAutoItemActivity.this).doFinish();
                } else if (get_type.equals("branch1")) {
                    //String select_item = branch_list.get(position);
                    String select_item = (String) parent.getItemAtPosition(position);
                    ChequeDetailsActivityChanged.selectAuto.setChq_auto_branch(select_item);
                    ed_branch.setText(select_item);
                    Constant.showLog("selcted_item: " + select_item);
                    writeLog("setOnItemClickListener():list item selected:" + select_item);
                    //finish();
                    new Constant(SelectAutoItemActivity.this).doFinish();
                }
            }
        });
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

    public void setOffBankList() {
        lv_cus.setAdapter(null);
        /*Cursor cursor = db.getBankName();
        if(cursor.moveToFirst()){
            do{
                bank_list.add(cursor.getString(cursor.getColumnIndex(DBHandler.Bank_Name)));
            }while (cursor.moveToNext());
        }
        cursor.close();*/
        int hocode = FirstActivity.pref.getInt(getString(R.string.pref_hocode),0);
        if(hocode==1) {
            bank_list.add("AXIS BANK LTD - 915020000840968");
            bank_list.add("Bank Of Maharashtra - 60198200608");
        }else if(hocode==12) {
            bank_list.add("AXIS BANK LTD");
            bank_list.add("Bank Of Maharashtra");
        }else if(hocode==13) {
            bank_list.add("AXIS BANK LTD");
            bank_list.add("Bank Of Maharashtra");
        }
        adapter = new SetAutoItemAdapter(this, bank_list);
        // lv_bank.setAdapter(new SetAutoItemAdapter(this, bank_list));
        lv_cus.setAdapter(adapter);
    }

    public void setOffBranchList() {
        lv_cus.setAdapter(null);
        Cursor cursor = db.getBranchName();
        if(cursor.moveToFirst()){
            do{
                branch_list.add(cursor.getString(cursor.getColumnIndex(DBHandler.Branch_CBranch)));
            }while (cursor.moveToNext());
        }
        cursor.close();
        adapter = new SetAutoItemAdapter(this, branch_list);
        lv_cus.setAdapter(adapter);
    }

    private void get_auto_type() {
        if (get_type.equals("cus")) {
            cus_lay.setVisibility(View.VISIBLE);
            bank_lay.setVisibility(View.GONE);
            branch_lay.setVisibility(View.GONE);
            setCusList();
        }else if (get_type.equals("bank")) {
            cus_lay.setVisibility(View.GONE);
            bank_lay.setVisibility(View.VISIBLE);
            branch_lay.setVisibility(View.GONE);
            setBankList();
        }else if (get_type.equals("branch")) {
            cus_lay.setVisibility(View.GONE);
            bank_lay.setVisibility(View.GONE);
            branch_lay.setVisibility(View.VISIBLE);
            setBranchList();
        }else if (get_type.equals("bank1")) {
            cus_lay.setVisibility(View.GONE);
            bank_lay.setVisibility(View.VISIBLE);
            branch_lay.setVisibility(View.GONE);
            setOffBankList();
        }else if (get_type.equals("branch1")) {
            cus_lay.setVisibility(View.GONE);
            bank_lay.setVisibility(View.GONE);
            branch_lay.setVisibility(View.VISIBLE);
            setOffBranchList();
        }
    }

    private void writeLog(String _data) {
        new WriteLog().writeLog(getApplicationContext(), "SelectAutoItemActivity_" + _data);
    }
}
