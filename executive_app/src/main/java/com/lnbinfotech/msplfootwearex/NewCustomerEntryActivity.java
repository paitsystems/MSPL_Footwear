package com.lnbinfotech.msplfootwearex;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.log.WriteLog;

public class NewCustomerEntryActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText ed_cus_name, ed_mobile_no, ed_email_id, ed_address;
    private Button bt_next, bt_update, bt_cancel;
    private LinearLayout save_lay, update_lay;
    public static int flag = 1;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_customer_entry);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.newcustomerentry);
        }
        init();
    }

    private void init() {
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        //toast.show();

        ed_cus_name = (EditText) findViewById(R.id.ed_cus_name);
        ed_mobile_no = (EditText) findViewById(R.id.ed_mobile_no);
        ed_email_id = (EditText) findViewById(R.id.ed_emailid);
        ed_address = (EditText) findViewById(R.id.ed_address);
        bt_next = (Button) findViewById(R.id.btn_next);
        bt_update = (Button) findViewById(R.id.btn_update);
        bt_cancel = (Button) findViewById(R.id.btn_cancel);
        save_lay = (LinearLayout) findViewById(R.id.save_lay);
        update_lay = (LinearLayout) findViewById(R.id.update_lay);
        save_lay.setVisibility(View.VISIBLE);
        update_lay.setVisibility(View.GONE);

        if (flag == 0) {
            save_lay.setVisibility(View.GONE);
            update_lay.setVisibility(View.VISIBLE);
            set_value_newCusEntry();

            bt_update.setOnClickListener(this);
            bt_cancel.setOnClickListener(this);
        } else {
            save_lay.setVisibility(View.VISIBLE);
            update_lay.setVisibility(View.GONE);
        }
        bt_next.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                add_value();
                validation();
                writeLog("Next button of onclick():data saved and goes to DetailFormActivity ");
                break;
            case R.id.btn_update:
                add_value();
                Intent i = new Intent(NewCustomerEntryActivity.this, NewCustomerEntryDetailFormActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                writeLog("Update button of onclick():data updated and goes to DetailFormActivity ");
                finish();
                break;
            case R.id.btn_cancel:
                Intent intent = new Intent(NewCustomerEntryActivity.this, NewCustomerEntryDetailFormActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                writeLog("Cancel button of onclick():data canceled and goes to DetailFormActivity ");
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        showPopup();
        /*Intent intent = new Intent(NewCustomerEntryActivity.this, NewCustomerEntryDetailFormActivity.class);
        startActivity(intent);
        writeLog("onBackPressed():data canceled and goes to DetailFormActivity ");
        finish();*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
               // new Constant(NewCustomerEntryActivity.this).doFinish();
                showPopup();
                break;
        }
        return super.onOptionsItemSelected(item);
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

    private void set_value_newCusEntry() {
        Constant.showLog("cus_name: " + OptionsActivity.new_cus.getCust_name());
        ed_cus_name.setText(OptionsActivity.new_cus.getCust_name());
        ed_mobile_no.setText(OptionsActivity.new_cus.getMobile_no());
        ed_email_id.setText(OptionsActivity.new_cus.getEmail_id());
        ed_address.setText(OptionsActivity.new_cus.getAddress());
    }

    private void validation() {
        if (ed_cus_name.getText().toString().equals("") & ed_mobile_no.getText().toString().equals("")
                & ed_email_id.getText().toString().equals("") & ed_address.getText().toString().equals("")) {
            toast.setText("Please,fill all the fields");
            toast.show();
        } else if (ed_cus_name.getText().toString().equals("")) {
            toast.setText("Please,enter customer name ");
            toast.show();
        } else if (ed_mobile_no.getText().toString().equals("")) {
            toast.setText("Please,enter mobile number");
            toast.show();
        } else if (ed_email_id.getText().toString().equals("")) {
            toast.setText("Please,enter email id");
            toast.show();
        } else if (ed_address.getText().toString().equals("")) {
            toast.setText("Please,enter address");
            toast.show();
        } else {
            Intent i = new Intent(NewCustomerEntryActivity.this, AttachCustomerImage.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
            writeLog("validation(): data saved and goes to DetailFormActivity ");
            finish();
        }
    }

    private void showPopup() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to clear this data");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent in = new Intent(NewCustomerEntryActivity.this, OptionsActivity.class);
                OptionsActivity.new_cus = null;
                startActivity(in);
                new Constant(NewCustomerEntryActivity.this).doFinish();
                //finish();
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

    private void writeLog(String _data) {
        new WriteLog().writeLog(getApplicationContext(), "NewCustomerEntryActivity_" + _data);
    }


}
