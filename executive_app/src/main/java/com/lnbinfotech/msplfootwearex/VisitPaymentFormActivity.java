package com.lnbinfotech.msplfootwearex;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.lnbinfotech.msplfootwearex.adapters.ShowChqDetailAdapter;
import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.log.WriteLog;
import com.lnbinfotech.msplfootwearex.model.ChequeDetailsGetterSetter;
import com.lnbinfotech.msplfootwearex.model.VisitPaymentFormGetterSetter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VisitPaymentFormActivity extends AppCompatActivity {

    private EditText ed_amount,ed_cus_name;
    private RadioButton rdo_cash,rdo_cheque;
    private LinearLayout cheque_lay,add_more_lay,chqbtn_det_lay,list_lay;
    private ListView lv_show_chq_detail;
    private String auto_type,total_amt = "";
    private AppCompatButton btn_save;
    static List<ChequeDetailsGetterSetter> ls;
    private ChequeDetailsGetterSetter cheque;
    static VisitPaymentFormGetterSetter visit;
   // private int total_amt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_payment_form);

        init();

    }

    void init(){
        ed_amount = (EditText) findViewById(R.id.ed_amount);
        ed_cus_name = (EditText) findViewById(R.id.ed_cus_name);
        btn_save = (AppCompatButton) findViewById(R.id.btn_save);
        lv_show_chq_detail = (ListView) findViewById(R.id.lv_show_chq_detail);
        rdo_cash = (RadioButton) findViewById(R.id.rdo_cash);
        rdo_cheque = (RadioButton) findViewById(R.id.rdo_cheque);
        cheque_lay = (LinearLayout) findViewById(R.id.cheque_lay);
        chqbtn_det_lay= (LinearLayout) findViewById(R.id.chqbtn_det_lay);
        add_more_lay = (LinearLayout) findViewById(R.id.add_more_lay);
        list_lay = (LinearLayout) findViewById(R.id.list_lay);



        ed_cus_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VisitPaymentFormActivity.this,SelectAutoItemActivity.class);
                auto_type = "cus";
                intent.putExtra("Auto_type",auto_type);
                startActivity(intent);
                writeLog("goes to SelectAutoItemActivity");
            }
        });

        rdo_cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rdo_cash.setChecked(true);
                rdo_cheque.setChecked(false);
                cheque_lay.setVisibility(View.GONE);
            }
        });

        rdo_cheque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rdo_cash.setChecked(false);
                rdo_cheque.setChecked(true);
                cheque_lay.setVisibility(View.VISIBLE);
            }
        });

        chqbtn_det_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAmount();
                cheque_button_validation();

            }
        });

        add_more_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(VisitPaymentFormActivity.this,ChequeDetailsActivity.class);
                startActivity(intent);
                writeLog("goes to ChequeDetailsActivity");
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // cheque_amount_missmatch();
                if(ed_cus_name.getText().toString().equals("")){
                    Toast toast  = Toast.makeText(getApplicationContext(),"Please enter customer name",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
                if(rdo_cheque.isChecked()){
                    validation();
                }

            }
        });

        visit = new VisitPaymentFormGetterSetter();
        cheque = new ChequeDetailsGetterSetter();
        ls = new ArrayList<>();
        setAmount();

    }

    @Override
    protected void onResume() {
        super.onResume();
        get_auto_cuslist();
        //if(!lv_show_chq_detail.getAdapter().equals(null)) {
         show_chq_adapter();
         if(ls.size() != 0){
             list_lay.setVisibility(View.VISIBLE);
             add_more_lay.setVisibility(View.VISIBLE);
             chqbtn_det_lay.setVisibility(View.GONE);
         }else {
             list_lay.setVisibility(View.GONE);
             add_more_lay.setVisibility(View.GONE);
             chqbtn_det_lay.setVisibility(View.VISIBLE);
         }
        //add_more_lay.setVisibility(View.VISIBLE);
       // }

    }


    private void get_auto_cuslist(){
       //VisitPaymentFormGetterSetter getterSetter = new VisitPaymentFormGetterSetter();
       // get_cust = getIntent().getStringExtra("Customer_name");
         Log.d("Log","cus: "+visit.getCustomer_name());
         ed_cus_name.setText(visit.getCustomer_name());
    }


    private void setAmount(){
        String amt =  ed_amount.getText().toString();
        visit.set_amount(amt);
    }

    private void show_chq_adapter(){
        //ls = new ArrayList<>();
        lv_show_chq_detail.setAdapter(null);

        //ls.add(cheque);
        //TODO create function in database getting all valuse of cheque and iterate cursor.and then set ls to adapter
        //for(int i = 0; i <= ls.size(); i++) {
            ShowChqDetailAdapter adapter = new ShowChqDetailAdapter(this, ls);
            Log.d("Log", "listchq: " + ls.size());
            lv_show_chq_detail.setAdapter(adapter);
        //}
       // add_more_lay.setVisibility(View.VISIBLE);
        /*Toast toast  = Toast.makeText(getApplicationContext(),"Cheque details added successfully",Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();*/
    }

    private void cheque_button_validation(){
        if(ed_amount.getText().toString().equals("")){
            Toast toast  = Toast.makeText(getApplicationContext(),"Please enter amount",Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }else {
            Intent intent = new Intent(VisitPaymentFormActivity.this,ChequeDetailsActivity.class);
            startActivity(intent);
            writeLog("goes to ChequeDetailsActivity");
        }
    }

    private void cheque_amount_missmatch(){
            total_amt = total_amt + ChequeDetailsActivity.chequeDetails.getChq_det_amt();
            Log.d("Log", "total_amt:" + total_amt);

            /*String totalAmt_str = String.valueOf(total_amt);
            Log.d("Log", "totalAmt_str:" + totalAmt_str);*/

            String totaled_amount = visit.getamount();
            Log.d("Log", "totaled_amount:" + totaled_amount);
           /* if(!totalAmt_str.equals(totaled_amount)){
                Toast toast  = Toast.makeText(getApplicationContext(),"Cheque amount missmatch,Please enter total amount",Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            }*/


    }

    private void validation(){
         if(ed_amount.getText().toString().equals("")){
            Toast toast  = Toast.makeText(getApplicationContext(),"Please enter amount",Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
         }else if(lv_show_chq_detail.getAdapter().getCount() == 0){
             Log.d("Log","list_sie:"+lv_show_chq_detail.getAdapter().getCount());
         //}else if(ls.size() == 0){
            Toast toast  = Toast.makeText(getApplicationContext(),"Please enter cheque details",Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }else {
             cheque_amount_missmatch();
         }

    }

    private void showPopup(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("Do you want exit?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

        builder.create().show();
    }

    private void writeLog(String _data){
        new WriteLog().writeLog(getApplicationContext(),"VisitPaymentFormActivity_" +_data);
    }

}
