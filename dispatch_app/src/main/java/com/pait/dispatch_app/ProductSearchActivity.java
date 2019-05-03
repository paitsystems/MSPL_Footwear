package com.pait.dispatch_app;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.pait.dispatch_app.adapters.ProductSearchAdapter;
import com.pait.dispatch_app.constant.Constant;
import com.pait.dispatch_app.db.DBHandler;
import com.pait.dispatch_app.log.WriteLog;
import com.pait.dispatch_app.model.DispatchMasterClass;
import com.pait.dispatch_app.model.EmployeeMasterClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductSearchActivity extends AppCompatActivity implements View.OnClickListener {

    private Toast toast;
    private EditText ed_search;
    private ListView listView;
    private DBHandler db;
    private List<DispatchMasterClass> prodList;
    private ProductSearchAdapter adapter;
    public static String partyName = null;
    private String from = "0", hoCode = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Constant.liveTestFlag == 1) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        setTheme(R.style.AppThemeAddToCard);
        setContentView(R.layout.activity_product_search);

        init();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        from = getIntent().getExtras().getString("from");
        hoCode = getIntent().getExtras().getString("hoCode");

        if (from.equals("1")) {
            setPartyName();
        } else if (from.equals("2")) {
            setPONo();
        } else if (from.equals("3")) {
            setDPBy();
        }

        ed_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = ed_search.getText().toString().toLowerCase(Locale.getDefault());
                if (adapter != null) {
                    adapter.filter(text);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(listView.getWindowToken(), 0);
                Intent returnIntent = new Intent();
                if (from.equals("1") || from.equals("2")) {
                    DispatchMasterClass dm = (DispatchMasterClass) listView.getItemAtPosition(i);
                    Constant.showLog(dm.getPartyName());
                    partyName = dm.getPartyName();
                    returnIntent.putExtra("result", dm);
                } else if (from.equals("3")){
                    DispatchMasterClass dm = (DispatchMasterClass) listView.getItemAtPosition(i);
                    EmployeeMasterClass em = new EmployeeMasterClass();
                    em.setEmp_Id(Integer.parseInt(dm.getEmp_Id()));
                    em.setName(dm.getEmp_Name());
                    returnIntent.putExtra("result", em);
                }
                setResult(Activity.RESULT_OK, returnIntent);
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
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
        new Constant(ProductSearchActivity.this).doFinish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //showDia(0);
                new Constant(ProductSearchActivity.this).doFinish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        FirstActivity.pref = getSharedPreferences(FirstActivity.PREF_NAME,MODE_PRIVATE);
        ed_search = findViewById(R.id.ed_search);
        listView = findViewById(R.id.listView);
        db = new DBHandler(getApplicationContext());
        prodList = new ArrayList<>();
    }

    private void setPartyName(){
        Cursor res = db.getPartyName();
        if(res.moveToFirst()){
            do{
                DispatchMasterClass dm = new DispatchMasterClass();
                dm.setPartyName(res.getString(res.getColumnIndex(DBHandler.DM_PartyName)));
                dm.setCustId(res.getString(res.getColumnIndex(DBHandler.DM_CustId)));
                prodList.add(dm);
            }while(res.moveToNext());
        }
        res.close();
        adapter = new ProductSearchAdapter(prodList, getApplicationContext(),from);
        listView.setAdapter(adapter);
    }

    private void setPONo(){
        Cursor res = db.getPONo(partyName);
        if(res.moveToFirst()){
            do{
                DispatchMasterClass dm = new DispatchMasterClass();
                dm.setCustId(res.getString(res.getColumnIndex(DBHandler.DM_CustId)));
                dm.setPartyName(res.getString(res.getColumnIndex(DBHandler.DM_PartyName)));
                dm.setPONO(res.getString(res.getColumnIndex(DBHandler.DM_PONO)));
                dm.setEmp_Name(res.getString(res.getColumnIndex(DBHandler.DM_Emp_Name)));
                dm.setEmp_Id(res.getString(res.getColumnIndex(DBHandler.DM_Emp_Id)));
                dm.setTotalQty(res.getString(res.getColumnIndex(DBHandler.DM_TotalQty)));
                dm.setTransporter(res.getString(res.getColumnIndex(DBHandler.DM_Transporter)));
                dm.setDcNo(res.getString(res.getColumnIndex(DBHandler.DM_DCNo)));
                dm.setDCdate(res.getString(res.getColumnIndex(DBHandler.DM_DCDate)));
                dm.setDPTotal(res.getString(res.getColumnIndex(DBHandler.DM_DPTotal)));
                dm.setPSImage(res.getString(res.getColumnIndex(DBHandler.DM_PSImage)));
                prodList.add(dm);
            }while(res.moveToNext());
        }
        res.close();
        adapter = new ProductSearchAdapter(prodList, getApplicationContext(),from);
        listView.setAdapter(adapter);
    }

    private void setDPBy(){
        Cursor res = db.getEmpName(Integer.parseInt(hoCode));
        if(res.moveToFirst()) {
            do {
                DispatchMasterClass dm = new DispatchMasterClass();
                dm.setEmp_Name(res.getString(res.getColumnIndex(DBHandler.EMP_Name)));
                dm.setEmp_Id(res.getString(res.getColumnIndex(DBHandler.EMP_EmpId)));
                prodList.add(dm);
            } while (res.moveToNext());
        }
        res.close();
        adapter = new ProductSearchAdapter(prodList, getApplicationContext(),from);
        listView.setAdapter(adapter);
    }

    private void showDia(int a) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProductSearchActivity.this);
        builder.setCancelable(false);
        if (a == 0) {
            builder.setMessage("Do You Want To Exit App?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new Constant(ProductSearchActivity.this).doFinish();
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
        new WriteLog().writeLog(getApplicationContext(), "ProductSearchActivity_" + _data);
    }
}
