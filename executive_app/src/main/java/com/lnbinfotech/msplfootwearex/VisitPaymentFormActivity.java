package com.lnbinfotech.msplfootwearex;

import android.content.DialogInterface;
import android.content.Intent;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;

import android.widget.Toast;

import com.lnbinfotech.msplfootwearex.adapters.ShowChqDetailAdapter;

import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.log.WriteLog;
import com.lnbinfotech.msplfootwearex.model.ChequeDetailsGetterSetter;
import com.lnbinfotech.msplfootwearex.model.VisitPaymentFormGetterSetter;


import java.util.ArrayList;

import java.util.List;


public class VisitPaymentFormActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText ed_amount, ed_cus_name;
    private RadioButton rdo_cash, rdo_cheque;
    private LinearLayout cheque_lay, add_more_lay, chqbtn_det_lay, list_lay;
    private ListView lv_show_chq_detail;
    private String auto_type;
    private AppCompatButton btn_save;
    public static List<ChequeDetailsGetterSetter> ls;
    //private ChequeDetailsGetterSetter cheque;
    public static VisitPaymentFormGetterSetter visit;
    private int total_amt = 0;
    private Toast toast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_payment_form);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.visitpayment);
        }
        init();
    }

    private void init() {
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);

        ed_amount = (EditText) findViewById(R.id.ed_amount);
        ed_cus_name = (EditText) findViewById(R.id.ed_cus_name);
        btn_save = (AppCompatButton) findViewById(R.id.btn_save);
        lv_show_chq_detail = (ListView) findViewById(R.id.lv_show_chq_detail);
        rdo_cash = (RadioButton) findViewById(R.id.rdo_cash);
        rdo_cheque = (RadioButton) findViewById(R.id.rdo_cheque);
        cheque_lay = (LinearLayout) findViewById(R.id.cheque_lay);
        chqbtn_det_lay = (LinearLayout) findViewById(R.id.chqbtn_det_lay);
        add_more_lay = (LinearLayout) findViewById(R.id.add_more_lay);
        list_lay = (LinearLayout) findViewById(R.id.list_lay);

        ed_cus_name.setOnClickListener(this);
        rdo_cash.setOnClickListener(this);
        rdo_cheque.setOnClickListener(this);
        chqbtn_det_lay.setOnClickListener(this);
        add_more_lay.setOnClickListener(this);
        btn_save.setOnClickListener(this);

        visit = new VisitPaymentFormGetterSetter();
        //cheque = new ChequeDetailsGetterSetter();
        ls = new ArrayList<>();
        //setAmount();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_save:
                //cheque_amount_missmatch();
                if (ed_cus_name.getText().toString().equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Please enter customer name", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                if (rdo_cheque.isChecked()) {
                    validation();
                }
                break;
            case R.id.add_more_lay:
                setAmount();
                cheque_button_validation();
                break;
            case R.id.chqbtn_det_lay:
                Intent intent = new Intent(VisitPaymentFormActivity.this, ChequeDetailsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                writeLog("goes to ChequeDetailsActivity");
                //finish();
                break;
            case R.id.ed_cus_name:
                Intent i = new Intent(VisitPaymentFormActivity.this, SelectAutoItemActivity.class);
                auto_type = "cus";
                i.putExtra("Auto_type", auto_type);
                startActivity(i);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                writeLog("goes to SelectAutoItemActivity");
                break;
            case R.id.rdo_cash:
                rdo_cash.setChecked(true);
                rdo_cheque.setChecked(false);
                cheque_lay.setVisibility(View.GONE);

                if (ls.size() != 0) {
                    showPopup(0);
                }
                break;
            case R.id.rdo_cheque:
                rdo_cash.setChecked(false);
                rdo_cheque.setChecked(true);
                cheque_lay.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onBackPressed() {
         showPopup(1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //new Constant(VisitPaymentFormActivity.this).doFinish();
                showPopup(1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        get_auto_cuslist();
        setAmount();
        show_chq_adapter();
        if (ls.size() != 0) {
            list_lay.setVisibility(View.VISIBLE);
            add_more_lay.setVisibility(View.VISIBLE);
            chqbtn_det_lay.setVisibility(View.GONE);
        } else {
            list_lay.setVisibility(View.GONE);
            add_more_lay.setVisibility(View.GONE);
            chqbtn_det_lay.setVisibility(View.VISIBLE);
        }
    }

    private void get_auto_cuslist() {
        //VisitPaymentFormGetterSetter getterSetter = new VisitPaymentFormGetterSetter();
        // get_cust = getIntent().getStringExtra("Customer_name");
        Constant.showLog("cus: " + visit.getCustomer_name());
        ed_cus_name.setText(visit.getCustomer_name());
    }

    private void setAmount() {
        String amt = ed_amount.getText().toString();
        visit.setAmount(amt);
        Constant.showLog("amount: " + visit.getAmount());
    }

    private void show_chq_adapter() {
        lv_show_chq_detail.setAdapter(null);
        ShowChqDetailAdapter adapter = new ShowChqDetailAdapter(this, ls);
        Constant.showLog("listchq: " + ls.size());
        lv_show_chq_detail.setAdapter(adapter);
    }

    private void cheque_button_validation() {
        if (ed_amount.getText().toString().equals("")) {
            Toast toast = Toast.makeText(getApplicationContext(), "Please enter amount", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            Intent intent = new Intent(VisitPaymentFormActivity.this, ChequeDetailsActivity.class);
            startActivity(intent);
            writeLog("goes to ChequeDetailsActivity");
            //finish();
        }
    }

    private void cheque_amount_missmatch() {
        String amt = ChequeDetailsActivity.chequeDetails.getChq_det_amt();
        total_amt = total_amt + Integer.parseInt(amt);
        Constant.showLog("total_amt:" + total_amt);
        String total = String.valueOf(total_amt);
        String totaled_amount = visit.getAmount();
        Constant.showLog("totaled_amount:" + totaled_amount);
        if (!total.equals(totaled_amount)) {
            toast.setText("Cheque amount missmatch,Please enter total amount");
            toast.show();
        } else {
            toast.setText("Data saved successfully");
            toast.show();
        }
    }

    private void validation() {
        if (ed_amount.getText().toString().equals("")) {
            Toast toast = Toast.makeText(getApplicationContext(), "Please enter amount", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else if (lv_show_chq_detail.getAdapter().getCount() == 0) {
            Constant.showLog("list_size:" + lv_show_chq_detail.getAdapter().getCount());
            //}else if(ls.size() == 0){
            Toast toast = Toast.makeText(getApplicationContext(), "Please enter cheque details", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            cheque_amount_missmatch();
        }

    }

    private void showPopup(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (id == 0) {
            builder.setMessage("Do you want to delete cheque amount?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ls.clear();
                    lv_show_chq_detail.setAdapter(null);
                    list_lay.setVisibility(View.GONE);
                    chqbtn_det_lay.setVisibility(View.VISIBLE);
                    add_more_lay.setVisibility(View.GONE);
                    //ChequeDetailsActivity.chequeDetails.setChq_det_amt(null);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
        } else if (id == 1) {
            builder.setMessage("Do you want to clear cheque details");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent in = new Intent(VisitPaymentFormActivity.this, OptionsActivity.class);
                    OptionsActivity.new_cus = null;
                    startActivity(in);
                    new Constant(VisitPaymentFormActivity.this).doFinish();
                    // finish();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    rdo_cheque.setChecked(true);
                    rdo_cash.setChecked(false);
                    dialogInterface.dismiss();
                }
            });
        }
        builder.create().show();
    }

    private void writeLog(String _data) {
        new WriteLog().writeLog(getApplicationContext(), "VisitPaymentFormActivity_" + _data);
    }


}
