package com.pait.exec;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pait.exec.adapters.CustCurrenyAdapter;
import com.pait.exec.adapters.SaleExeCurrenyAdapter;
import com.pait.exec.constant.Constant;
import com.pait.exec.db.DBHandler;
import com.pait.exec.interfaces.CurrencyInterface;
import com.pait.exec.log.WriteLog;
import com.pait.exec.model.CurrencyMasterClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class CurrencyDetailsActivityChanged extends AppCompatActivity implements View.OnClickListener,CurrencyInterface {

    private Constant constant, constant1;
    private Toast toast;
    private ListView custList, seList;
    private ArrayList<CurrencyMasterClass> custCurrencyList, seCurrencyList;
    private CustCurrenyAdapter custAdapter;
    private SaleExeCurrenyAdapter seAdapter;
    private DBHandler db;
    private Button btn_cust, btn_se, btn_submit;
    private LinearLayout cust_lay, se_lay;
    private TextView tv_custTotal, tv_retTotal, tv_total;
    private HashMap<Integer,Integer> custCurrencyMap, seCurrencyMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_detail_changed);

        init();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        tv_total.setText(getIntent().getExtras().getString("total"));

        loadCustCurrencyMaster();
        loadSECurrencyMaster();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cust_curr:
                cust_lay.setVisibility(View.VISIBLE);
                se_lay.setVisibility(View.GONE);
                break;
            case R.id.btn_se_curr:
                se_lay.setVisibility(View.VISIBLE);
                cust_lay.setVisibility(View.GONE);
                break;
            case R.id.btn_submit:

                break;
        }
    }

    @Override
    public void onBackPressed() {
        //showDia(0);
        new Constant(CurrencyDetailsActivityChanged.this).doFinish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.currency_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //showDia(0);
                new Constant(CurrencyDetailsActivityChanged.this).doFinish();
                break;
            case R.id.curr_save:
                showDia(1);
                break;
            case R.id.curr_cancel:
                showDia(2);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void custCurrency(String value) {
        tv_custTotal.setText("0");
        int custTotal = 0;
        custCurrencyMap.clear();
        for(CurrencyMasterClass currClass : custCurrencyList){
            int curr = Integer.parseInt(currClass.getCurrency());
            int val = Integer.parseInt(currClass.getValue());
            int _curr = curr * val;
            custTotal = custTotal + _curr;
            Constant.showLog("custCurrency "+custTotal);
            custCurrencyMap.put(curr,val);
        }
        tv_custTotal.setText(String.valueOf(custTotal));
        setTotal();
    }

    @Override
    public void seCurrency(String value) {
        tv_retTotal.setText("0");
        int retTotal = 0;
        seCurrencyMap.clear();
        for(CurrencyMasterClass currClass : seCurrencyList){
            int curr = Integer.parseInt(currClass.getCurrency());
            int val = Integer.parseInt(currClass.getValue());
            int _curr = curr * val;
            retTotal = retTotal + _curr;
            Constant.showLog("seCurrency "+retTotal);
            seCurrencyMap.put(curr,val);
        }
        tv_retTotal.setText(String.valueOf(retTotal));
        setTotal();
    }

    private void init() {
        constant = new Constant(CurrencyDetailsActivityChanged.this);
        constant1 = new Constant(getApplicationContext());
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        custCurrencyList = new ArrayList<>();
        seCurrencyList = new ArrayList<>();
        custList = (ListView) findViewById(R.id.custlist);
        seList = (ListView) findViewById(R.id.selist);
        db = new DBHandler(getApplicationContext());
        btn_cust = (Button) findViewById(R.id.btn_cust_curr);
        btn_se = (Button) findViewById(R.id.btn_se_curr);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        cust_lay = (LinearLayout) findViewById(R.id.cust_lay);
        se_lay = (LinearLayout) findViewById(R.id.se_lay);
        FirstActivity.pref = getSharedPreferences(FirstActivity.PREF_NAME,MODE_PRIVATE);
        tv_custTotal = (TextView) findViewById(R.id.tv_custTotal);
        tv_retTotal = (TextView) findViewById(R.id.tv_retTotal);
        tv_total = (TextView) findViewById(R.id.tv_total);
        custCurrencyMap = new HashMap<>();
        seCurrencyMap = new HashMap<>();
        VisitPaymentFormActivity.custCurrencyStr = "";
        VisitPaymentFormActivity.seCurrencyStr = "";

        btn_cust.setOnClickListener(this);
        btn_se.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
    }

    private void showDia(int a) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CurrencyDetailsActivityChanged.this);
        builder.setCancelable(false);
        if (a == 0) {
            builder.setMessage("Do You Want To Exit App?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new Constant(CurrencyDetailsActivityChanged.this).doFinish();
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
            builder.setMessage("Do You Want Save Details?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(custCurrencyList.size()!=0 && seCurrencyList.size()!=0) {
                        if(!tv_total.getText().toString().equals("0")) {
                            if(tv_total.getText().toString().equals(getTotal())) {
                                ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(tv_total.getWindowToken(),0);
                                VisitPaymentFormActivity.total = Integer.parseInt(tv_total.getText().toString());
                                VisitPaymentFormActivity.isCurrencyDataSaved = 1;
                                Set<Integer> cset = custCurrencyMap.keySet();
                                for(int curr : cset) {
                                    int value = custCurrencyMap.get(curr);
                                    if(value!=0){
                                        VisitPaymentFormActivity.custCurrencyStr =
                                                VisitPaymentFormActivity.custCurrencyStr + curr+"-"+value+",";
                                    }
                                }
                                Set<Integer> sset = seCurrencyMap.keySet();
                                for(int curr : sset) {
                                    int value = seCurrencyMap.get(curr);
                                    if(value!=0){
                                        VisitPaymentFormActivity.seCurrencyStr =
                                                VisitPaymentFormActivity.seCurrencyStr + curr+"-"+value+",";
                                    }
                                }
                                Constant.showLog(VisitPaymentFormActivity.custCurrencyStr);
                                Constant.showLog(VisitPaymentFormActivity.seCurrencyStr);
                                new Constant(CurrencyDetailsActivityChanged.this).doFinish();
                            }else{
                                toast.setText("Paid-Return Does Not Match With Total");
                                toast.show();
                            }
                        }else{
                            toast.setText("Please Enter Values");
                            toast.show();
                        }
                    }else{
                        toast.setText("Please Enter Values");
                        toast.show();
                    }
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    VisitPaymentFormActivity.isCurrencyDataSaved = 0;
                    dialog.dismiss();
                }
            });
        }else if (a == 2) {
            builder.setMessage("Do You Want Discard Changes?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    VisitPaymentFormActivity.isCurrencyDataSaved = 0;
                    custCurrencyList.clear();
                    seCurrencyList.clear();
                    //cust_lay.setVisibility(View.GONE);
                    //se_lay.setVisibility(View.GONE);
                    tv_custTotal.setText("0");
                    tv_retTotal.setText("0");
                    //tv_total.setText("0");
                    dialog.dismiss();
                    VisitPaymentFormActivity.custCurrencyStr = "";
                    VisitPaymentFormActivity.seCurrencyStr = "";
                    loadCustCurrencyMaster();
                    loadSECurrencyMaster();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    VisitPaymentFormActivity.isCurrencyDataSaved = 0;
                    dialog.dismiss();
                }
            });
        }
        builder.create().show();
    }

    private void loadCustCurrencyMaster(){
        if(custCurrencyList.isEmpty()) {
            custList.setAdapter(null);
            Cursor res = db.getCurrenyMaster();
            if (res.moveToFirst()) {
                do {
                    CurrencyMasterClass cur = new CurrencyMasterClass();
                    cur.setCurrency(res.getString(res.getColumnIndex(DBHandler.Curr_Currency)));
                    cur.setValue("0");
                    cur.setTotal("0");
                    custCurrencyList.add(cur);
                } while (res.moveToNext());
            }
            res.close();
            custAdapter = new CustCurrenyAdapter(custCurrencyList, getApplicationContext());
            custAdapter.initInterface(CurrencyDetailsActivityChanged.this);
            custList.setAdapter(custAdapter);
        }
    }

    private void loadSECurrencyMaster(){
        if(seCurrencyList.isEmpty()) {
            seList.setAdapter(null);
            Cursor res = db.getCurrenyMaster();
            if (res.moveToFirst()) {
                do {
                    CurrencyMasterClass cur = new CurrencyMasterClass();
                    cur.setCurrency(res.getString(res.getColumnIndex(DBHandler.Curr_Currency)));
                    cur.setValue("0");
                    cur.setTotal("0");
                    seCurrencyList.add(cur);
                } while (res.moveToNext());
            }
            res.close();
            seAdapter = new SaleExeCurrenyAdapter(seCurrencyList, getApplicationContext());
            seAdapter.initInterface(CurrencyDetailsActivityChanged.this);
            seList.setAdapter(seAdapter);
        }
    }

    private void setTotal(){
        int paid = Integer.parseInt(tv_custTotal.getText().toString());
        int ret = Integer.parseInt(tv_retTotal.getText().toString());
        int tot = paid - ret;
        //tv_total.setText(String.valueOf(tot));
    }

    private String getTotal(){
        int paid = Integer.parseInt(tv_custTotal.getText().toString());
        int ret = Integer.parseInt(tv_retTotal.getText().toString());
        return String.valueOf(paid - ret);
    }

    private void writeLog(String _data) {
        new WriteLog().writeLog(getApplicationContext(), "CurrencyDetailsActivityChanged_" + _data);
    }

}
