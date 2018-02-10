package com.lnbinfotech.msplfootwearex;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Gravity;

import com.lnbinfotech.msplfootwearex.adapters.CustCurrenyAdapter;
import com.lnbinfotech.msplfootwearex.adapters.SaleExeCurrenyAdapter;
import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.db.DBHandler;
import com.lnbinfotech.msplfootwearex.interfaces.CurrencyInterface;
import com.lnbinfotech.msplfootwearex.interfaces.ServerCallback;
import com.lnbinfotech.msplfootwearex.log.WriteLog;
import com.lnbinfotech.msplfootwearex.model.CurrencyMasterClass;
import com.lnbinfotech.msplfootwearex.volleyrequests.VolleyRequests;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class CurrencyDetailsActivity extends AppCompatActivity implements View.OnClickListener,CurrencyInterface {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_detail);

        init();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cust_curr:
                cust_lay.setVisibility(View.VISIBLE);
                se_lay.setVisibility(View.GONE);
                loadCustCurrencyMaster();
                break;
            case R.id.btn_se_curr:
                se_lay.setVisibility(View.VISIBLE);
                cust_lay.setVisibility(View.GONE);
                loadSECurrencyMaster();
                break;
            case R.id.btn_submit:

                break;
        }
    }

    @Override
    public void onBackPressed() {
        //showDia(0);
        new Constant(CurrencyDetailsActivity.this).doFinish();
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
                new Constant(CurrencyDetailsActivity.this).doFinish();
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
        for(CurrencyMasterClass currClass : custCurrencyList){
            int curr = Integer.parseInt(currClass.getCurrency());
            int val = Integer.parseInt(currClass.getValue());
            curr = curr * val;
            custTotal = custTotal + curr;
            Constant.showLog("custCurrency "+custTotal);
        }
        tv_custTotal.setText(String.valueOf(custTotal));
        setTotal();
    }

    @Override
    public void seCurrency(String value) {
        tv_retTotal.setText("0");
        int retTotal = 0;
        for(CurrencyMasterClass currClass : seCurrencyList){
            int curr = Integer.parseInt(currClass.getCurrency());
            int val = Integer.parseInt(currClass.getValue());
            curr = curr * val;
            retTotal = retTotal + curr;
            Constant.showLog("seCurrency "+retTotal);
        }
        tv_retTotal.setText(String.valueOf(retTotal));
        setTotal();
    }

    private void setTotal(){
        int custTotal = stringToInt(tv_custTotal);
        int retTotal = stringToInt(tv_retTotal);
        int total = custTotal - retTotal;
        tv_total.setText(String.valueOf(total));
    }

    private int stringToInt(TextView view){
        return Integer.parseInt(view.getText().toString());
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
                    custCurrencyList.add(cur);
                } while (res.moveToNext());
            }
            res.close();
            custAdapter = new CustCurrenyAdapter(custCurrencyList, getApplicationContext());
            custAdapter.initInterface(CurrencyDetailsActivity.this);
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
                    seCurrencyList.add(cur);
                } while (res.moveToNext());
            }
            res.close();
            seAdapter = new SaleExeCurrenyAdapter(seCurrencyList, getApplicationContext());
            seAdapter.initInterface(CurrencyDetailsActivity.this);
            seList.setAdapter(seAdapter);
        }
    }

    private void init() {
        constant = new Constant(CurrencyDetailsActivity.this);
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

        tv_custTotal = (TextView) findViewById(R.id.tv_custTotal);
        tv_retTotal = (TextView) findViewById(R.id.tv_retTotal);
        tv_total = (TextView) findViewById(R.id.tv_total);

        btn_cust.setOnClickListener(this);
        btn_se.setOnClickListener(this);
        btn_submit.setOnClickListener(this);

    }

    private void showDia(int a) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CurrencyDetailsActivity.this);
        builder.setCancelable(false);
        if (a == 0) {
            builder.setMessage("Do You Want To Exit App?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new Constant(CurrencyDetailsActivity.this).doFinish();
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
                            new Constant(CurrencyDetailsActivity.this).doFinish();
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
                    dialog.dismiss();
                }
            });
        }else if (a == 2) {
            builder.setMessage("Do You Want Discard Changes?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    custCurrencyList.clear();
                    seCurrencyList.clear();
                    cust_lay.setVisibility(View.GONE);
                    se_lay.setVisibility(View.GONE);
                    tv_custTotal.setText("0");
                    tv_retTotal.setText("0");
                    tv_total.setText("0");
                    dialog.dismiss();
                }
            });
        }
        builder.create().show();
    }

    private void writeLog(String _data) {
        new WriteLog().writeLog(getApplicationContext(), "CurrencyDetailsActivity_" +
_data);
    }

}
