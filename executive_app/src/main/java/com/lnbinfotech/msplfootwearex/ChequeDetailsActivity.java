package com.lnbinfotech.msplfootwearex;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.lnbinfotech.msplfootwearex.log.WriteLog;
import com.lnbinfotech.msplfootwearex.model.ChequeDetailsGetterSetter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class ChequeDetailsActivity extends AppCompatActivity {
   private EditText ed_branch,ed_bank,ed_chq_date,ed_chq_no,ed_chq_amt,ed_chq_ref;
    private AppCompatButton btn_submit;
    private String auto_type;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    private Date current_date = Calendar.getInstance().getTime();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheque_details);
        init();
    }

    private void init(){
        ed_bank = (EditText) findViewById(R.id.ed_bank);
        ed_branch = (EditText) findViewById(R.id.ed_branch);
        ed_chq_date = (EditText) findViewById(R.id.ed_chq_date);
        ed_chq_no = (EditText) findViewById(R.id.ed_chq_no);
        ed_chq_amt = (EditText) findViewById(R.id.ed_chq_amt);
        ed_chq_ref  = (EditText) findViewById(R.id.ed_chq_ref);

        btn_submit = (AppCompatButton) findViewById(R.id.btn_submt);

        ed_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //VisitPaymentFormGetterSetter getterSetter = new VisitPaymentFormGetterSetter();

                Intent intent = new Intent(ChequeDetailsActivity.this,SelectAutoItemActivity.class);
                auto_type = "bank";
                intent.putExtra("Auto_type",auto_type);
                startActivity(intent);
                writeLog("goes to SelectAutoItemActivity");
            }
        });

        ed_branch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChequeDetailsActivity.this,SelectAutoItemActivity.class);
                auto_type = "branch";
                intent.putExtra("Auto_type",auto_type);
                startActivity(intent);
                writeLog("goes to SelectAutoItemActivity");
            }
        });

        ed_chq_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(1);
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validations();
            }
        });

    }
    protected void onResume() {
        super.onResume();
        get_auto_branchlist();
        get_auto_banklist();

    }

    public void get_data(){
        ChequeDetailsGetterSetter chequeDetails = new ChequeDetailsGetterSetter();

        String date = ed_chq_date.getText().toString();
        //VisitPaymentFormActivity.cheque.setChq_det_date(date);
        chequeDetails.setChq_det_date(date);
        String number = ed_chq_no.getText().toString();
        //VisitPaymentFormActivity.cheque.setChq_det_number(number);
        chequeDetails.setChq_det_number(number);
        String amount = ed_chq_amt.getText().toString();
        //VisitPaymentFormActivity.cheque.setChq_det_amt(amount);
        chequeDetails.setChq_det_amt(amount);
        String ref = ed_chq_ref.getText().toString();
        //VisitPaymentFormActivity.cheque.setChq_det_ref(ref);
        chequeDetails.setChq_det_ref(ref);
        VisitPaymentFormActivity.ls.add(chequeDetails);
        finish();
    }

    private void get_auto_banklist(){
        // get_bank = getIntent().getStringExtra("Bank_name");
         ed_bank.setText(VisitPaymentFormActivity.visit.getCheque_bank());
        Log.d("Log","ed_bank: "+VisitPaymentFormActivity.visit.getCheque_bank());
        //ed_amount.setText(visit.getCheque_amount());
    }
    private void get_auto_branchlist(){
        // get_branch = getIntent().getStringExtra("Branch_name");
        ed_branch.setText(VisitPaymentFormActivity.visit.getCheque_branch());
        Log.d("Log","ed_branch: "+VisitPaymentFormActivity.visit.getCheque_branch());
        //ed_amount.setText(visit.getCheque_amount());

    }
    private void validations() {
        if (ed_bank.getText().toString().equals("")) {
            Toast toast = Toast.makeText(getApplicationContext(), "Please enter bank name", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else if (ed_branch.getText().toString().equals("")) {
            Toast toast = Toast.makeText(getApplicationContext(), "Please enter branch name", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else if (ed_chq_date.getText().toString().equals("")) {
            Toast toast = Toast.makeText(getApplicationContext(), "Please,enter cheque date", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else if (ed_chq_no.getText().toString().equals("")) {
            Toast toast = Toast.makeText(getApplicationContext(), "Please,enter cheque number", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else if (ed_chq_amt.getText().toString().equals("")) {
            Toast toast = Toast.makeText(getApplicationContext(), "Please,enter cheque amount", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else if (ed_chq_ref.getText().toString().equals("")) {
            Toast toast = Toast.makeText(getApplicationContext(), "Please,enter cheque reference", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            get_data();

        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        return new DatePickerDialog(this,chq_date,2017,6,22);
    }

    DatePickerDialog.OnDateSetListener chq_date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
              try {
                  Date select_date = sdf.parse(dayOfMonth + "/" + (monthOfYear +1) + "/" + year);
                  ed_chq_date.setText(sdf.format(select_date));
              }catch (Exception e){
                  e.printStackTrace();
              }
        }
    };

    private void writeLog(String _data){
        new WriteLog().writeLog(getApplicationContext(),"VisitPaymentFormActivity_" +_data);
    }

}
