package com.lnbinfotech.msplfootwearex;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lnbinfotech.msplfootwearex.model.NewCustomerEntryGetterSetter;

public class NewCustomerEntryActivity extends AppCompatActivity {
    private EditText ed_cus_name,ed_mobile_no,ed_email_id,ed_address;
    private Button bt_next,bt_update,bt_cancel;
    private LinearLayout save_lay,update_lay;
    //static NewCustomerEntryGetterSetter OptionsActivity.new_cus;
    static int flag = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_customer_entry);
        init();


    }
    void init(){
        ed_cus_name = (EditText) findViewById(R.id.ed_cus_name);
        ed_mobile_no = (EditText) findViewById(R.id.ed_mobile_no);
        ed_email_id = (EditText) findViewById(R.id.ed_emailid);
        ed_address = (EditText) findViewById(R.id.ed_address);
        bt_next = (Button) findViewById(R.id.btn_next);
        bt_update = (Button) findViewById(R.id.btn_update);
        bt_cancel = (Button) findViewById(R.id.btn_cancel);
        save_lay = (LinearLayout) findViewById(R.id.save_lay);
        update_lay = (LinearLayout) findViewById(R.id.update_lay);

        if(flag == 0) {
            save_lay.setVisibility(View.GONE);
            update_lay.setVisibility(View.VISIBLE);
            set_value_newCusEntry();


            //save_lay.setVisibility(View.VISIBLE);
            //update_lay.setVisibility(View.GONE);


            bt_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    add_value();
                    Intent i = new Intent(NewCustomerEntryActivity.this, NewCustomerEntryDetailFormActivity.class);
                    startActivity(i);
                    finish();
                }

            });


        }

        //OptionsActivity.new_cus = new NewCustomerEntryGetterSetter();

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //set_value_newCusEntry();
                //add_value();
                Intent i = new Intent(NewCustomerEntryActivity.this, NewCustomerEntryDetailFormActivity.class);
                startActivity(i);
                finish();
            }
        });
        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag == 0) {
                    add_value();
                    Intent i = new Intent(NewCustomerEntryActivity.this,NewCustomerEntryDetailFormActivity.class);
                    startActivity(i);
                    finish();
                }else {
                    add_value();
                    validation();
                }
            }
        });
    }

    private void add_value() {
        String cus = ed_cus_name.getText().toString();
        OptionsActivity.new_cus.setCust_name(cus);
        String mobile = ed_mobile_no.getText().toString();
        OptionsActivity.new_cus.setMobile_no(mobile);
        String email = ed_email_id.getText().toString();
        OptionsActivity.new_cus.setEmail_id(email);
        String address = ed_address.getText().toString();
        OptionsActivity.new_cus.setAddress(address);
    }
    private void  set_value_newCusEntry(){
        Log.d("Log","cus_name: "+OptionsActivity.new_cus.getCust_name());
        ed_cus_name.setText(OptionsActivity.new_cus.getCust_name());
        ed_mobile_no.setText(OptionsActivity.new_cus.getMobile_no());
        ed_email_id.setText(OptionsActivity.new_cus.getEmail_id());
        ed_address.setText(OptionsActivity.new_cus.getAddress());
    }

    private void validation(){
        if (ed_cus_name.getText().toString().equals("") & ed_mobile_no.getText().equals("")
                & ed_email_id.getText().toString().equals("") & ed_address.getText().toString().equals("")) {
            Toast toast = Toast.makeText(getApplicationContext(), "Please,fill all the fields", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else if (ed_cus_name.getText().toString().equals("")) {
            Toast toast = Toast.makeText(getApplicationContext(), "Please,enter customer name", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else if (ed_mobile_no.getText().toString().equals("")) {
            Toast toast = Toast.makeText(getApplicationContext(), "Please,enter mobile number", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else if (ed_email_id.getText().toString().equals("")) {
            Toast toast = Toast.makeText(getApplicationContext(), "Please,enter email id", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else if (ed_address.getText().toString().equals("")) {
            Toast toast = Toast.makeText(getApplicationContext(), "Please,enter address", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            Intent i = new Intent(NewCustomerEntryActivity.this, AttachCustomerImage.class);
            startActivity(i);
            finish();
        }
    }



    public void showPopup() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to attach image?");

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
}
