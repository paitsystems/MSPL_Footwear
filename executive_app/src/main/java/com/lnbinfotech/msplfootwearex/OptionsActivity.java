package com.lnbinfotech.msplfootwearex;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.model.NewCustomerEntryGetterSetter;

public class OptionsActivity extends AppCompatActivity implements View.OnClickListener {

    public static NewCustomerEntryGetterSetter new_cus;
    private Button btn_take_order, btn_visit, btn_report, btn_new_cust_entry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        init();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        btn_take_order.setOnClickListener(this);
        btn_visit.setOnClickListener(this);
        btn_report.setOnClickListener(this);
        btn_new_cust_entry.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_take_order:
                showDia(1);
                break;
            case R.id.btn_visit:
                startActivity(new Intent(getApplicationContext(),AreawiseCustomerSelectionActivity.class));
                overridePendingTransition(R.anim.enter,R.anim.exit);
                break;
            case R.id.btn_reports:
                break;
            case R.id.btn_new_cust_entry:
                startActivity(new Intent(getApplicationContext(),NewCustomerEntryActivity.class));
                overridePendingTransition(R.anim.enter,R.anim.exit);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        showDia(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                showDia(0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        new_cus = new NewCustomerEntryGetterSetter();
        btn_take_order = (Button) findViewById(R.id.btn_take_order);
        btn_visit = (Button) findViewById(R.id.btn_visit);
        btn_report = (Button) findViewById(R.id.btn_reports);
        btn_new_cust_entry = (Button) findViewById(R.id.btn_new_cust_entry);
    }

    private void showDia(int a) {
        AlertDialog.Builder builder = new AlertDialog.Builder(OptionsActivity.this);
        builder.setCancelable(false);
        if (a == 0) {
            builder.setMessage("Do You Want To Exit App?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new Constant(OptionsActivity.this).doFinish();
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }if (a == 1) {
            builder.setTitle("Take Order");
            builder.setMessage("How do you want to take order?");
            builder.setPositiveButton("Imagewise", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(getApplicationContext(),ImagewiseSetwiseOrderActivity.class));
                    overridePendingTransition(R.anim.enter,R.anim.exit);
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("Cutsize", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(getApplicationContext(),CutsizeSetwiseOrderActivity.class));
                    overridePendingTransition(R.anim.enter,R.anim.exit);
                    dialog.dismiss();
                }
            });
            builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        builder.create().show();
    }

}
