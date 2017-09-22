package com.lnbinfotech.msplfootwearex;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lnbinfotech.msplfootwearex.model.ChequeDetailsGetterSetter;


public class ChequeDetailsActivity extends AppCompatActivity {
    EditText ed_chq_date,ed_chq_no,ed_chq_amt,ed_chq_ref;
    AppCompatButton btn_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheque_details);

        ed_chq_date = (EditText) findViewById(R.id.ed_chq_date);
        ed_chq_no = (EditText) findViewById(R.id.ed_chq_no);
        ed_chq_amt = (EditText) findViewById(R.id.ed_chq_amt);
        ed_chq_ref  = (EditText) findViewById(R.id.ed_chq_ref);

        btn_submit = (AppCompatButton) findViewById(R.id.btn_submt);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ed_chq_date.getText().toString().equals("")){
                    Toast toast = Toast.makeText(getApplicationContext(),"Please,enter cheque date",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }else if(ed_chq_no.getText().toString().equals("")){
                    Toast toast = Toast.makeText(getApplicationContext(),"Please,enter cheque number",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }else  if (ed_chq_amt.getText().toString().equals("")){
                    Toast toast = Toast.makeText(getApplicationContext(),"Please,enter cheque amount",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                } else if(ed_chq_ref.getText().toString().equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Please,enter cheque reference", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }  else {
                    get_data();
                    finish();
                    /*Intent i = new Intent(ChequeDetailsActivity.this, VisitPaymentFormActivity.class);
                    startActivity(i);*/
                }

            }
        });

    }

    public void get_data(){
        //ChequeDetailsGetterSetter chequeDetails = new ChequeDetailsGetterSetter();
        String date = ed_chq_date.getText().toString();
        VisitPaymentFormActivity.cheque.setChq_det_date(date);
        String number = ed_chq_no.getText().toString();
        VisitPaymentFormActivity.cheque.setChq_det_number(number);
        String amount = ed_chq_amt.getText().toString();
        VisitPaymentFormActivity.cheque.setChq_det_amt(amount);
        String ref = ed_chq_ref.getText().toString();
        VisitPaymentFormActivity.cheque.setChq_det_ref(ref);
    }

}
